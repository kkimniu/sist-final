package io.cavia.trader.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 아래의 조건이 포함된 커스텀 애노테이션입니다.
 * <p>
 * {@code @NotBlank(message = "인증키를 입력해주세요")}<br>
 * {@code @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자입니다")}
 * <p>
 * 필드에 사용가능합니다.
 *
 * @author Kim Beomhee
 */
@NotBlank(message = "인증키를 입력해주세요")
@Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자입니다")

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidAuthKey {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}