package io.cavia.trader.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 아래의 조건이 포함된 커스텀 애노테이션입니다.
 * <p>
 * {@code @NotBlank(message = "이메일을 입력해주세요")}<br>
 * {@code @Email(message = "유효한 이메일 형식이 아닙니다")}<br>
 * {@code @Size(max = 254, message = "이메일은 254자를 초과할 수 없습니다")}
 * <p>
 * 필드에 사용가능합니다.
 *
 * @author Kim Beomhee
 */
// 1. 적용할 제약 조건 어노테이션들을 모두 붙여줍니다.
@NotBlank(message = "이메일을 입력해주세요")
@Email(message = "유효한 이메일 형식이 아닙니다")
@Size(max = 254, message = "이메일은 254자를 초과할 수 없습니다")

// 2. 이 어노테이션이 어디에 적용될지 지정합니다. (필드에 적용)
@Target({ElementType.FIELD})

// 3. 런타임까지 어노테이션 정보를 유지하도록 설정합니다.
@Retention(RetentionPolicy.RUNTIME)

// 4. 이 어노테이션이 제약 조건임을 알립니다.
//    (여러 제약 조건을 조합했으므로, validatedBy는 비워둡니다)
@Constraint(validatedBy = {})

// 5. 어노테이션 인터페이스를 정의합니다.
public @interface ValidEmail {

    // 아래 3개는 제약 조건 어노테이션의 표준 속성입니다. (규칙이므로 그대로 작성)
    // 커스텀 제약을 validatedBy에 추가했을 때 아래 메시지가 나옴
    String message() default "유효하지 않은 이메일 주소입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}