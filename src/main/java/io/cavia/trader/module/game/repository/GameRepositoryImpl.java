package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class GameRepositoryImpl implements GameRepository {

    private final GameMapper gameMapper;

    @Override
    public void saveGame(Long stockId, LocalDateTime startedAt) {
        gameMapper.saveGame(stockId, startedAt);
    }

    @Override
    public void saveGameParticipation(GameParticipation gameParticipation) {
        gameMapper.saveGameParticipation(gameParticipation);
    }

    @Override
    public Member findMemberById(Long id) {
        return gameMapper.findMemberById(id);
    }

    @Override
    public Long findLastGameId() {
        return gameMapper.findLastGameId();
    }

    @Override
    public GameParticipation findLastGameParticipationByMemberId(Long memberId) {
        return gameMapper.findLastGameParticipationByMemberId(memberId);
    }

    @Override
    public void updateCashAndTotalScoreById(Long id, long cash, int totalScore) {
        gameMapper.updateCashAndTotalScoreById(id, cash, totalScore);
    }


}
