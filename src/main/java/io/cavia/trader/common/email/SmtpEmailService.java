package io.cavia.trader.common.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${email.from.address}")
    private String fromAddress;
    @Value("${email.from.personal}")
    private String fromPersonal;

    private void sendEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // MimeMessageHelper를 사용하면 MimeMessage를 더 쉽게 조작할 수 있습니다.
            // true는 멀티파트 메시지를 사용하겠다는 의미입니다 (HTML 이메일에 필요).
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(fromAddress, fromPersonal);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true); // true는 이메일 본문을 HTML로 해석하겠다는 의미입니다.
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("메일 발신자의 인코딩 문제가 있습니다");
        }
    }

    /**
     * 사용자의 이메일 인증을 위해 인증키가 포함된 이메일을 발송합니다.
     *
     * @param to      메일 받을 주소
     * @param authKey 사용자에게 전달할 이메일 인증을 위한 인증키
     */
    @Override
    public void sendAuthEmail(String to, String authKey) {
        Context context = new Context();
        context.setVariable("username", to);
        context.setVariable("authKey", authKey);
        // 템플릿을 사용하여 HTML 본문 생성
        String htmlBody = templateEngine.process("email/auth-email", context);

        sendEmail(to, "[Trader.id] 이메일 인증", htmlBody);
    }
}
