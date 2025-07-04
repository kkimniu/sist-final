package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.PlayerStatusDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ScoreUtil {

    @Value("${score.rank_points_per_player}")
    private long RANK_POINTS_PER_PLAYER;
    @Value("${score.rank_reward_rate}")
    private int REWARD_RATE = 2;
    @Value("${score.rank_max_score}")
    private int MAX_SCORE;

    public BigDecimal getReturnRate(long postCash, long earnedCash) {
        if(postCash == 0){
            throw new RuntimeException("자산 초기값이 0은 division by zero가 발생할 위험성이 있습니다!");
        }
        BigDecimal postCashAmount = new BigDecimal(postCash);
        BigDecimal earnedCashAmount = new BigDecimal(earnedCash);
        return earnedCashAmount.divide(postCashAmount, 2, RoundingMode.HALF_UP);
    }

    public Map<Long, PlayerStatusDto> evaluatePlayers(Map<Long, PlayerStatusDto> playerStatusDtos, int LastPrice) {

        // 세션에 적용될 토탈 스코어 선언
        long totalReward = playerStatusDtos.size() * RANK_POINTS_PER_PLAYER;

        // 남아 있는 미체결 거래가 있다면 모든 거래 종가로 처리
        AtomicLong totalTrade = new AtomicLong(0);
        playerStatusDtos.values().forEach(playerStatus -> {
            if(!playerStatus.getOrderDto().getOrderTableDtos().isEmpty()){
                playerStatus.getOrderDto().getOrderTableDtos().forEach(orderTableDto -> {
                    // 요청 금액 음수면 매도
                    if(orderTableDto.getPrice() < 0){
                        totalTrade.set(
                                totalTrade.get() +
                                orderTableDto.getQuantity() * LastPrice);
                    }else{
                        totalTrade.set(
                                totalTrade.get() -
                                        orderTableDto.getQuantity() * LastPrice);
                    }

                });

                playerStatus.setEarnedCash(
                        playerStatus.getEarnedCash() +
                        totalTrade.get());
            }
        });



        // 먼저 수익률 계산해서 저장
        playerStatusDtos.forEach((id, Dto) -> {
            Dto.setReturnRate(getReturnRate(Dto.getPostCash(),
                    Dto.getEarnedCash()
            ));
        });

        // 수익률 기반 내림차순 정렬
        Map<Long, PlayerStatusDto> playerSortedByRank = playerStatusDtos.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(
                        Comparator.comparing(PlayerStatusDto::getReturnRate).reversed()
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

    // 플레이어 점수 계산
        List<Double> weights = new ArrayList<>();
        int half = playerStatusDtos.size() / 2;
        double total = 0.0;

        for (int i = 1; i <= half; i++) {
            double weight = Math.pow(half - i + 1, REWARD_RATE);
            weights.add(weight);
            total += weight;
        }
        double[] normalizedScores = weights.stream()
                .mapToDouble(w -> w * totalReward / playerStatusDtos.size())
                .toArray();

        AtomicInteger midpoint = new AtomicInteger(Math.round(MAX_SCORE / 2));
        ArrayList<PlayerStatusDto> playerStausDtoList = new ArrayList<> (playerSortedByRank.values());

        IntStream.range(0, playerStausDtoList.size()/2).forEach(i -> {
            int score = playerStausDtoList.get(i).getEarnedScore() + (int) normalizedScores[i];
            playerStausDtoList.get(i).setEarnedScore(score);
        });

        IntStream.range(playerStausDtoList.size()/2, playerStausDtoList.size()).forEach(i -> {
            // 순위에 따라 가중치가 적용된 점수 계산
            int score = playerStausDtoList.get(i).getEarnedScore() - (int) normalizedScores[i];

            // 점수 한계 (0, 5000)에 가까워질 수록 점수 변동폭 조절
            playerStausDtoList.get(i).setEarnedScore(
                    (int) Math.round(
                            0.5 * (1 + Math.cos(Math.PI * Math.abs(score - midpoint.get()) / midpoint.get()))
                    )
            );
        });

        return playerSortedByRank;
    }
}
