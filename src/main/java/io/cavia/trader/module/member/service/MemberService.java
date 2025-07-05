package io.cavia.trader.module.member.service;

import io.cavia.trader.module.member.dto.UserRankingDto;
import io.cavia.trader.module.member.entity.GameParticipation;
import io.cavia.trader.module.member.entity.Member;

import java.util.List;

public interface MemberService {

    /**
     * 특정 회원의 모든 게임 참여 기록을 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원의 게임 참여 기록 리스트
     */
    List<GameParticipation> getGameParticipationByMemberId(Long memberId);

    List<GameParticipation> getGameParticipationByMemberIdWithPaging(Long memberId, int limit, int offset);

    int getCountByMemberId(Long memberId);

    /**
     * 회원 ID로 특정 회원 정보를 조회합니다.
     *
     * @param id 조회할 회원의 ID
     * @return 조회된 Member 엔티티
     * @throws io.cavia.trader.common.exception.ApiException 회원을 찾을 수 없을 경우
     */
    Member getMemberById(Long id);

    /**
     * 이메일로 특정 회원 정보를 조회합니다.
     *
     * @param email 조회할 회원의 이메일
     * @return 조회된 Member 엔티티
     * @throws io.cavia.trader.common.exception.ApiException 회원을 찾을 수 없을 경우
     */
    Member getMemberByEmail(String email);

    /**
     * 특정 회원의 닉네임을 변경합니다.
     *
     * @param id       변경할 회원의 ID
     * @param nickname 새로운 닉네임
     * @throws io.cavia.trader.common.exception.ApiException 닉네임이 이미 존재할 경우
     */
    void changeNickname(Long id, String nickname);

    /**
     * 현재 사용자의 비밀번호가 일치하는지 검증합니다. (UI/UX 검증용)
     *
     * @param id       검증할 회원의 ID
     * @param password 검증할 현재 비밀번호
     * @throws io.cavia.trader.common.exception.ApiException 비밀번호가 일치하지 않을 경우
     */
    void validatePassword(Long id, String password);

    /**
     * 로그인된 사용자가 현재 비밀번호를 인증한 후, 새 비밀번호로 변경합니다.
     *
     * @param id              변경할 회원의 ID
     * @param currentPassword 확인을 위한 현재 비밀번호
     * @param newPassword     변경할 새 비밀번호
     * @throws io.cavia.trader.common.exception.ApiException 현재 비밀번호가 일치하지 않을 경우
     */
    void processPasswordChangeRequest(Long id, String currentPassword, String newPassword);

    /**
     * 이메일 인증 등 다른 방법으로 본인 인증을 마친 사용자의 비밀번호를 재설정합니다.
     * (추가적인 비밀번호 검증 과정 없기 때문에 주의)
     *
     * @param id          변경할 회원의 ID
     * @param newPassword 재설정할 새 비밀번호
     */
    void changePassword(Long id, String newPassword);

    /**
     * 특정 회원의 보유 자산을 초기화합니다.
     *
     * @param id 초기화할 회원의 ID
     */
    void resetCash(Long id);

    /**
     * 특정 회원의 탈퇴를 처리합니다.
     *
     * @param id       탈퇴할 회원의 ID
     * @param password 본인 확인을 위한 현재 비밀번호
     */
    void withdrawMember(Long id, String password);

    /**
     * 회원가입 시, 사용하려는 이메일이 이미 존재하는지 검사합니다.
     *
     * @param email 중복 검사할 이메일
     * @throws io.cavia.trader.common.exception.ApiException 이메일이 이미 존재할 경우
     */
    void validateDuplicateEmail(String email);

    /**
     * 회원가입 시, 사용하려는 닉네임이 이미 존재하는지 검사합니다.
     *
     * @param nickname 중복 검사할 닉네임
     * @throws io.cavia.trader.common.exception.ApiException 닉네임이 이미 존재할 경우
     */
    void validateDuplicateNickname(String nickname);

    /**
     * 새로운 회원 정보를 DB에 저장합니다.
     *
     * @param member 저장할 Member 엔티티
     */
    void createMember(Member member);

    /**
     * 보유 자산 순으로 전체 회원의 랭킹을 조회합니다.
     *
     * @param limit  조회할 개수
     * @param offset 조회를 시작할 위치
     * @return 랭킹 DTO 리스트
     */
    List<UserRankingDto> findAllOrderByCash(Long limit, Long offset);

    /**
     * 누적 점수 순으로 전체 회원의 랭킹을 조회합니다.
     *
     * @param limit  조회할 개수
     * @param offset 조회를 시작할 위치
     * @return 랭킹 DTO 리스트
     */
    List<UserRankingDto> findAllOrderByTotalScore(Long limit, Long offset);

}
