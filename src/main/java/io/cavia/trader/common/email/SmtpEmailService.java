package io.cavia.trader.common.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SmtpEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private void sendEmail(String from, String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // MimeMessageHelper를 사용하면 MimeMessage를 더 쉽게 조작할 수 있습니다.
            // true는 멀티파트 메시지를 사용하겠다는 의미입니다 (HTML 이메일에 필요).
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true); // true는 이메일 본문을 HTML로 해석하겠다는 의미입니다.
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    public void sendAuthEmail(String to, String authKey) {
        String subject = "[Trader.io] 회원가입 이메일 인증";
        String from = "no-reply@trader.io";

        Context context = new Context();
        context.setVariable("username", to);
        context.setVariable("authKey", authKey);

        // 템플릿을 사용하여 HTML 본문 생성
        String htmlBody = templateEngine.process("email/auth-email", context);

        sendEmail(from, to, subject, htmlBody);
    }
}
