package io.cavia.trader.module.auth.dto;

import io.cavia.trader.common.validation.ValidAuthKey;
import io.cavia.trader.common.validation.ValidEmail;
import io.cavia.trader.common.validation.ValidNickname;
import io.cavia.trader.common.validation.ValidPassword;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class SignupRequestDto {

    @AssertTrue(message = "서비스 이용약관에 동의해야 합니다.")
    private boolean termsOfServiceAgreed;

    @AssertTrue(message = "개인정보 처리방침에 동의해야 합니다.")
    private boolean privacyPolicyAgreed;

    @ValidEmail
    private String email;

    @ValidAuthKey
    private String authKey;

    @ValidNickname
    private String nickname;

    @ValidPassword
    private String password;

}
