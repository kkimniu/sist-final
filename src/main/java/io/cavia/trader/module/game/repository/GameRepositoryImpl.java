package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class GameRepositoryImpl implements GameRepository{

    private final GameMapper gameMapper;

    public void saveGame(Long stockId, LocalDateTime startedAt) {
        gameMapper.saveGame(stockId, startedAt);
    }

    public void saveGameParticipation(GameParticipation gameParticipation) {
        gameMapper.saveGameParticipation(gameParticipation);
    }

    public Member findMemberById(Long id) {
        return gameMapper.findMemberById(id);
    }

    public Long findLastGameId() {
        return gameMapper.findLastGameId();
    }

    public void updateCashAndTotalScoreById(Long id, int cash, int totalScore) {
        gameMapper.updateCashAndTotalScoreById(id, cash, totalScore);
    }



}
