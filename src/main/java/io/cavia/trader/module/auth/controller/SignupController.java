package io.cavia.trader.module.auth.controller;

import io.cavia.trader.common.exception.ApiException;
import io.cavia.trader.module.auth.dto.SignupDto;
import io.cavia.trader.module.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/signup")
@SessionAttributes("signupDto") //signupForm 이라는 객체가 모델에 들어갈 때 세션에도 복제함
@RequiredArgsConstructor
public class SignupController {

    private final AuthService authService;

    //  요청이 들어오면 제일 먼저 실행되는 메서드. 빈 signupForm 객체가 모델에 있음을 보장함. 만약 다른 메서드 파라미터에
//  @ModelAttribute 로 signupForm을 먼저 세션에서 찾으면 이 메서드는 실행되지 않음.
    @ModelAttribute("signupDto")
    public SignupDto createSignupDto() {
        return new SignupDto();
    }

    @GetMapping
    public String moveToSignupProcess() {
        return "redirect:/signup/terms";
    }

    @GetMapping("/terms")
    public String showTermsForm() {

        //TODO: 약관 파일을 불러와 넣어줘야함
        return "members/signup/terms";
    }

    @PostMapping("/terms")
    public String processTerms(@ModelAttribute("signupDto")
                               @Validated(SignupDto.ValidationGroups.TermsGroup.class) SignupDto signupDto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/signup/terms";
        }
        //TODO: 약관 동의를 검증하고, 동의한 날짜와 약관 버전을 임시 저장해놔야 함.
        return "redirect:/signup/email";
    }

    @GetMapping("/email")
    public String showEmailForm() {
        return "members/signup/email";
    }

    @PostMapping("/email")
    public String processEmail(@ModelAttribute("signupDto")
                               @Validated(SignupDto.ValidationGroups.EmailGroup.class) SignupDto signupDto,
                               BindingResult bindingResult) {
        System.out.println("signupDto = " + signupDto);
        if (bindingResult.hasErrors()) {
            return "members/signup/email";
        }
        try {
            //TODO: 이메일 전송 몇초 걸리고, 걸리는 동안 뷰 전환이 없어서, 발송 버튼을 계속 누를 수 있고 이메일도 계속 감.
            authService.sendVerificationEmail(signupDto.getEmail());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "runtimeError", e.getMessage());
            return "members/signup/email";
        }
        return "redirect:/signup/verify";
    }

    @GetMapping("/verify")
    public String showAuthKeyForm() {
        return "members/signup/verify";
    }

    @PostMapping("/verify")
    public String processAuthKey(@ModelAttribute("signupDto")
                                 @Validated(SignupDto.ValidationGroups.AuthKeyGroup.class) SignupDto signupDto,
                                 BindingResult bindingResult) {
        System.out.println("signupDto = " + signupDto);
        if (bindingResult.hasErrors()) {
            return "members/signup/verify";
        }
        try {
            authService.verifyCodeForSignup(signupDto.getEmail(), signupDto.getAuthKey());
        } catch (ApiException e) {
            bindingResult.rejectValue("authKey", "runtimeError", e.getErrorCode().getMessage());
            return "members/signup/verify";
        }
        return "redirect:/signup/nickname";
    }

    @GetMapping("/nickname")
    public String showNicknameForm() {
        return "members/signup/nickname";
    }

    @PostMapping("/nickname")
    public String processNickname(@ModelAttribute("signupDto")
                                  @Validated(SignupDto.ValidationGroups.NicknameGroup.class) SignupDto signupDto,
                                  BindingResult bindingResult) {
        System.out.println("signupDto = " + signupDto);
        if (bindingResult.hasErrors()) {
            return "members/signup/nickname";
        }
        try {
            authService.validateDuplicateNickname(signupDto.getNickname());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("nickname", "runtimeError", e.getMessage());
            return "members/signup/nickname";
        }
        return "redirect:/signup/password";
    }

    @GetMapping("/password")
    public String showPasswordForm() {
        return "members/signup/password";
    }

    /**
     * 입력한 비밀번호를 검증하고, 전체 폼 클래스를 다시 검증함. 검증 통과하면 회원가입 시도
     *
     * @param signupDto     세션에 있는 폼 클래스, 사용자가 입력한 정보들이 세션에 저장되어 있음
     * @param bindingResult 폼 클래스 검증 결과
     * @return 다음 페이지 뷰
     */
    @PostMapping("/password")
    public String processPassword(@ModelAttribute("signupDto")
                                  @Validated(SignupDto.ValidationGroups.SignupGroup.class) SignupDto signupDto,
                                  BindingResult bindingResult) {

        if (signupDto.getPassword() != null && signupDto.getPasswordConfirm() != null
                && !signupDto.getPassword().equals(signupDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "비밀번호가 일치하지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "members/signup/password";
        }
        try {
            System.out.println("회원가입 시도시 signupForm = " + signupDto);
            authService.join(signupDto);
        } catch (RuntimeException e) {
            bindingResult.rejectValue("passwordConfirm", "runtimeError", e.getMessage());
            e.printStackTrace();
            return "members/signup/password";
        }
        return "redirect:/signup/welcome";
    }

    @GetMapping("/welcome")
    public String showWelcomePage(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "members/signup/welcome";
    }
}
