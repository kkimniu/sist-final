package io.cavia.trader.module.auth.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 어노테이션이 붙은 메서드는 실행 전에 reCAPTCHA 검증을 거쳐야 함을 나타냅니다.
 *
 * @author KimBeomhee
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRecaptcha {
}