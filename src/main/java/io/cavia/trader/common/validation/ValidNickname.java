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
 * {@code @NotBlank(message = "닉네임을 입력해주세요")}<br>
 * {@code @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,16}$"}
 * <p>
 * 필드에 사용가능합니다.
 *
 * @author Kim Beomhee
 */
@NotBlank(message = "닉네임을 입력해주세요")
@Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,16}$")

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidNickname {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}