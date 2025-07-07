package io.cavia.trader.module.auth.aop;

import io.cavia.trader.module.auth.service.RecaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RecaptchaAspect {

    private final RecaptchaService recaptchaService;

    public RecaptchaAspect(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Around("@annotation(RequiresRecaptcha)")
    public Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String recaptchaToken = request.getHeader("X-Recaptcha-Token");

        boolean isVerified = recaptchaService.verifyRecaptcha(recaptchaToken);

        if (!isVerified) {
            // 검증 실패 시 예외를 발생시켜 메서드 실행을 중단
            throw new RuntimeException("AOP: reCAPTCHA verification is failed");
        }

        // 검증 성공 시, 원래 메서드를 실행
        return joinPoint.proceed();
    }
}