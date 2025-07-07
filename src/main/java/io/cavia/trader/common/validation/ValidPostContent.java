package io.cavia.trader.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 아래의 조건이 포함된 커스텀 애노테이션입니다.
 * <p>
 * {@code @NotBlank(message = "내용을 입력해주세요")}<br>
 * {@code @Size(max = 2000, message = "내용은 2000자 이하로 입력해주세요")}
 * <p>
 * 필드에 사용가능합니다.
 *
 * @author KimBeomhee
 */
@NotBlank(message = "내용을 입력해주세요")
@Size(max = 2000, message = "내용은 2000자 이하로 입력해주세요")

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidPostContent {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}