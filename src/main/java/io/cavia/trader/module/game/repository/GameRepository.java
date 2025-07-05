package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;

import java.time.LocalDateTime;

public interface GameRepository {
    void saveGame(Long stockId, LocalDateTime startedAt);
    void saveGameParticipation(GameParticipation gameParticipation);
    Member findMemberById(Long id);
    public Long findLastGameId();
    public GameParticipation findLastGameParticipationByMemberId(Long memberId);
    public void updateCashAndTotalScoreById(Long id, long cash, int totalScore);
}
