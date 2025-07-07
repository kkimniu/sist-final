package io.cavia.trader.module.auth.aop;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.common.exception.ErrorCode;
import io.cavia.trader.module.auth.service.RecaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * {@code @RequiresRecaptcha} 어노테이션이 붙은 메서드에 대해 Google reCAPTCHA v2 토큰 검증을 수행하는 AOP Aspect.
 * <p>
 * 이 Aspect는 대상 메서드가 실행되기 전에 HTTP 요청 헤더에서 reCAPTCHA 토큰을 추출하여
 * {@link RecaptchaService}에 검증을 위임합니다. 검증에 실패하면 {@link ApiException}을 발생시켜 메서드 실행을 중단합니다.
 *
 * @author KimBeomhee
 */
@Aspect
@Component
public class RecaptchaAspect {

    private final RecaptchaService recaptchaService;

    public RecaptchaAspect(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    /**
     * {@code @RequiresRecaptcha} 어노테이션이 적용된 메서드를 가로채 reCAPTCHA 검증 로직을 실행합니다.
     * <p>
     * HTTP 요청의 'X-Recaptcha-Token' 헤더에서 토큰을 추출하여 유효성을 검사합니다.
     * 검증에 성공하면 원본 메서드를 실행하고, 실패하면 예외를 발생시킵니다.
     *
     * @param joinPoint 프록시된 원본 메서드에 대한 정보를 담고 있으며, 원본 메서드를 실행하는 기능을 제공합니다.
     * @return 원본 메서드의 실행 결과.
     * @throws ApiException reCAPTCHA 토큰이 유효하지 않거나 검증에 실패한 경우 발생합니다.
     * @throws Throwable    원본 메서드 실행 중 발생할 수 있는 모든 예외.
     */
    @Around("@annotation(RequiresRecaptcha)")
    public Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String recaptchaToken = request.getHeader("X-Recaptcha-Token");

        boolean isVerified = recaptchaService.verifyRecaptcha(recaptchaToken);

        if (!isVerified) {
            throw new ApiException(ErrorCode.INVALID_RECAPTCHA_TOKEN);
        }
        // 검증 성공 시, 원래 메서드를 실행
        return joinPoint.proceed();
    }
}