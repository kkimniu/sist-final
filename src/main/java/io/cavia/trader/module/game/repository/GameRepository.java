package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.game.entity.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface GameRepository {

    void saveGame(int stockId, LocalDateTime startedAt);
    void saveGameParticipation(int memberId,
                               int gameId,
                               BigDecimal returnRate,
                               int gameRank,
                               int earnedScore,
                               int postScore,
                               Long earnedCash,
                               Long postCash,
                               LocalDateTime enteredAt
    );
    Member findMemberById(int id);
    void updateCashAndTotalScoreById(int id, int cash, int totalScore);
}
