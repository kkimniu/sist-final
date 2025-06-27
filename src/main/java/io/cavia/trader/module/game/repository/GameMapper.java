package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.game.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper
public interface GameMapper {

    void saveGame(@Param("stockId") int stockId, @Param("startedAt")LocalDateTime startedAt);
    void saveGameParticipation(@Param("memberId") int memberId,
                               @Param("gameId") int gameId,
                               @Param("returnRate") BigDecimal returnRate,
                               @Param("gameRank") int gameRank,
                               @Param("earnedScore") int earnedScore,
                               @Param("postScore") int postScore,
                               @Param("earnedCash") long earnedCash,
                               @Param("postCash") long postCash,
                               @Param("enteredAt") LocalDateTime enteredAt
    );
    Member findMemberById(long id);
    void updateCashAndTotalScoreById(@Param("id") long id,
                                     @Param("cash") int cash,
                                     @Param("totalScore") int totalScore
    );
}
