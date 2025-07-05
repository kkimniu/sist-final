package io.cavia.trader.module.game.service;

import io.cavia.trader.module.game.dto.PlayerStatusDto;
import io.cavia.trader.module.game.dto.TradeLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j(topic = "ScoreUtil")
public class ScoreUtil {

    @Value("${stock.default-fee-rate}")
    private BigDecimal DEFAULT_FEE_RATE;
    @Value("${score.rank_points_per_player}")
    private long RANK_POINTS_PER_PLAYER;
    @Value("${score.rank_reward_rate}")
    private int REWARD_RATE;
    @Value("${score.rank_max_score}")
    private int MAX_SCORE;

    public BigDecimal getReturnRate(long postCash, long earnedCash) {
        if (postCash == 0) {
            throw new RuntimeException("자산 초기값이 0은 division by zero가 발생할 위험성이 있습니다!");
        }
        BigDecimal postCashAmount = new BigDecimal(postCash);
        BigDecimal earnedCashAmount = new BigDecimal(earnedCash);
        return earnedCashAmount.divide(postCashAmount, 3, RoundingMode.HALF_UP);
    }

    public Map<Long, PlayerStatusDto> evaluatePlayers(Map<Long, PlayerStatusDto> playerStatusDtos, int LastPrice) {

        // 세션에 적용될 토탈 스코어 선언
        long totalReward = playerStatusDtos.size() * RANK_POINTS_PER_PLAYER;

        // 남아 있는 미체결 거래가 있다면 모든 거래 종가로 처리
        AtomicLong totalTrade = new AtomicLong(0);
        for (PlayerStatusDto playerStatus : playerStatusDtos.values()) {
            if (playerStatus.getOrderDto().getTradeLogs().isEmpty()) {
                playerStatus.setReturnRate(new BigDecimal(1));
                log.info("거래를 하지 않은 유저는 계산을 위해 수익률을 1로 세팅합니다. id: {}", playerStatus.getMemberId());
                continue;
            }

            long tradeVolume = 0;
            for (TradeLog log : playerStatus.getOrderDto().getTradeLogs()) {
                if (log.getPrice() > 0) {
                    tradeVolume += log.getQuantity();
                } else if (log.getPrice() < 0) {
                    tradeVolume -= log.getQuantity();
                }
            }

            if (tradeVolume > 0) {
                long tradeValue = tradeVolume * LastPrice;
                BigDecimal tradeValueAmount = new BigDecimal(tradeValue);
                BigDecimal feeValue = tradeValueAmount.multiply(DEFAULT_FEE_RATE);
                tradeValue = tradeValueAmount.subtract(feeValue).setScale(0, RoundingMode.HALF_UP).intValue();

                playerStatus.setEarnedCash(
                        playerStatus.getEarnedCash() + tradeValue
                );

                playerStatus.getOrderDto().getTradeLogs().add(
                        TradeLog.builder()
                                .Id(String.format("%06d", playerStatus.getIdCreator().getAndIncrement() % 1000000))
                                .price(-Math.abs(LastPrice))
                                .quantity((int) tradeVolume)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
                log.info("매도 하지 않은 주식을 처리합니다. 남아있던 매수 종목: {}", tradeVolume);
            }
            // 이번 게임에서 투자한 금액 대비 수익률 계산
            long buyValue = 0;
            long sellValue = 0;
            for (TradeLog log : playerStatus.getOrderDto().getTradeLogs()) {
                if (log.getPrice() > 0) {
                    buyValue += Math.abs(log.getQuantity() * log.getPrice());
                } else if (log.getPrice() < 0) {
                    sellValue += Math.abs(log.getQuantity() * log.getPrice());
                }
            }

            BigDecimal tradeValueAmount = new BigDecimal(sellValue);
            BigDecimal feeValue = tradeValueAmount.multiply(DEFAULT_FEE_RATE);
            sellValue = tradeValueAmount.subtract(feeValue).setScale(0, RoundingMode.HALF_UP).intValue();

            // 수익률 계산해서 저장
            playerStatus.setReturnRate(getReturnRate(buyValue, sellValue));
            log.info("세션 종료 중 수익률 계산 결과 id: {}, returnRate: {} ", playerStatus.getMemberId(), playerStatus.getReturnRate());
        }

        if (playerStatusDtos.size() == 1) {
            log.info("유저가 한 명인 세션에서는 점수를 무효처리하고 수익률과 손익금액만 저장합니다");
            return playerStatusDtos;
        }

        // 수익률 기반 내림차순 정렬
        List<PlayerStatusDto> playerSortedByRank = playerStatusDtos.values().stream()
                .sorted(Comparator.comparing(PlayerStatusDto::getReturnRate).reversed())
                .collect(Collectors.toList());

        // 순위 계산
        AtomicInteger gameRank = new AtomicInteger(1);
        int cnt = 0;
        for (int i = 0; i < playerSortedByRank.size(); i++) {
            if (i != 0) {
                if (playerSortedByRank.get(i - 1).getReturnRate().equals(playerSortedByRank.get(i).getReturnRate())) {
                    gameRank.set(gameRank.get() - 1);
                    cnt++;
                } else {
                    gameRank.set(gameRank.get() + cnt);
                    cnt = 0;
                }
            }
            playerSortedByRank.get(i).setGameRank(gameRank.get());
            gameRank.set(gameRank.get() + 1);
        }
        // 플레이어 점수 계산
        List<Double> weights = new ArrayList<>();
        int half = playerStatusDtos.size() / 2;
        double total = 0.0;

        for (int i = 1; i <= half; i++) {
            double weight = Math.pow(half - i + 1, REWARD_RATE);
            weights.add(weight);
            total += weight;
        }

        int maxRank = playerSortedByRank.get(playerSortedByRank.size() - 1).getGameRank();

        if (maxRank < 2) {
            log.info("유저의 최대 순위가 2보다 작으므로 무승부 처리하여 점수를 계산 하지 않습니다.");
            return playerStatusDtos;
        }

        Deque<Integer> normalizedScores = weights.stream()
                .map(w -> (int) Math.round(w * totalReward / maxRank))
                .collect(Collectors.toCollection(ArrayDeque::new));

        Deque<Integer> normalizedScores2 = new ArrayDeque<Integer>(normalizedScores);

        IntStream.range(1, maxRank / 2 + 1).forEach(i -> {
            playerSortedByRank.forEach(p -> {
                if (p.getGameRank() == i) {
                    log.info("점수 계산 전 통합 점수 : {}, {}", p.getPostScore(), p.getMemberNickname());
                    int returnScore = normalizedScores.peekFirst();
                    // 등수에 따라 할당된 점수만큼 플레이어에게 점수 부여
                    int score = returnScore;

                    log.info("점수 한계 조절 전 점수 : {}", score);
                    // 점수 한계 (0, 5000)에 가까워질 수록 점수 변동폭 조절
                    double scoreValue = score + p.getPostScore();
                    double midpoint = MAX_SCORE / 2.0;
                    double normalized = Math.abs(scoreValue - midpoint) / midpoint; // 0 ~ 1
                    double factor = Math.cos(normalized * Math.PI); // cos(0) = 1, cos(π) = -1
                    factor = (factor + 1) / 2.0; // 1 → 0 범위로 변환
                    score *= factor;
                    log.info("점수 한계 조절 후 점수 : {}", score);

                    p.setEarnedScore(score + p.getPostScore());
                    log.info("점수 계산 후 통합 점수 : {}, {}", p.getEarnedScore(), p.getMemberNickname());
                }
                if (p.getGameRank() == i + maxRank / 2) {

                    int j = i + maxRank / 2;
                    log.info("점수 계산 전 통합 점수 : {}, {}", p.getPostScore(), p.getMemberNickname());
                    int returnScore = normalizedScores2.peekLast();
                    log.info("점수 한계 조절 전 점수 : {}", returnScore);

                    double scoreValue = returnScore + p.getPostScore();
                    double midpoint = MAX_SCORE / 2.0;
                    double normalized = Math.abs(scoreValue - midpoint) / midpoint; // 0 ~ 1
                    double factor = Math.cos(normalized * Math.PI); // cos(0) = 1, cos(π) = -1
                    factor = (factor + 1) / 2.0; // 1 → 0 범위로 변환
                    returnScore *= factor;
                    log.info("점수 한계 조절 후 점수 : {}", returnScore);

                    p.setEarnedScore(p.getPostScore() - returnScore);
                    log.info("점수 계산 후 통합 점수 : {}, {}", p.getEarnedScore(), p.getMemberNickname());
                }
            });
        });

        return playerStatusDtos;
    }
}
