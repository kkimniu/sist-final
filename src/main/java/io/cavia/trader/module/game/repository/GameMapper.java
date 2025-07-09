package io.cavia.trader.module.game.repository;

import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface GameMapper {

    void saveGame(@Param("stockId") Long stockId, @Param("startedAt") LocalDateTime startedAt);
    void saveGameParticipation(@Param("gameParticipation") GameParticipation gameParticipation);
    Member findMemberById(Long id);
    Long findLastGameId();
    GameParticipation findLastGameParticipationByMemberId(Long memberId);
    void updateCashAndTotalScoreById(@Param("id") long id,
                                     @Param("cash") long cash,
                                     @Param("totalScore") int totalScore
    );
}
