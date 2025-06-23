package io.cavia.trader.module.member.controller;

import io.cavia.trader.module.member.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
@SessionAttributes("signupForm") //signupForm 이라는 객체가 모델에 들어갈 때 세션에도 복제함
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    //  요청이 들어오면 제일 먼저 실행되는 메서드. 빈 signupForm 객체가 모델에 있음을 보장함. 만약 다른 메서드 파라미터에
//  @ModelAttribute 로 signupForm을 먼저 세션에서 찾으면 이 메서드는 실행되지 않음.
    @ModelAttribute("signupForm")
    public SignupForm createSignupForm() {
        return new SignupForm();
    }

    @GetMapping("/terms")
    public String showTermsForm() {
        //TODO: 약관 파일을 불러와 넣어줘야함
        return "member/signup/terms";
    }

    @PostMapping("/terms")
    public String processTerms(@ModelAttribute("signupForm")
                               @Validated(SignupForm.ValidationGroups.TermsGroup.class) SignupForm signupForm,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/signup/terms";
        }
        //TODO: 약관 동의를 검증하고, 동의한 날짜와 약관 버전을 임시 저장해놔야 함.
        return "redirect:/signup/email";
    }

    @GetMapping("/email")
    public String showEmailForm() {
        return "member/signup/email";
    }

    @PostMapping("/email")
    public String processEmail(@ModelAttribute("signupForm")
                               @Validated(SignupForm.ValidationGroups.EmailGroup.class) SignupForm signupForm,
                               BindingResult bindingResult) {
        System.out.println("signupForm = " + signupForm);
        if (bindingResult.hasErrors()) {
            return "member/signup/email";
        }
        try {
            signupService.validateDuplicateEmail(signupForm.getEmail());
            //TODO: 이메일 전송 몇초 걸리고, 걸리는 동안 뷰 전환이 없어서, 발송 버튼을 계속 누를 수 있고 이메일도 계속 감.
            signupService.sendVerificationEmail(signupForm.getEmail());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "runtimeError", e.getMessage());
            return "member/signup/email";
        }
        return "redirect:/signup/verify";
    }

    @GetMapping("/verify")
    public String showAuthKeyForm() {
        return "member/signup/verify";
    }

    @PostMapping("/verify")
    public String processAuthKey(@ModelAttribute("signupForm")
                                 @Validated(SignupForm.ValidationGroups.AuthKeyGroup.class) SignupForm signupForm,
                                 BindingResult bindingResult) {
        System.out.println("signupForm = " + signupForm);
        if (bindingResult.hasErrors()) {
            return "member/signup/verify";
        }
        try {
            signupService.verifyAuthKey(signupForm.getEmail(), signupForm.getAuthKey());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("authKey", "runtimeError", e.getMessage());
            return "member/signup/verify";
        }
        return "redirect:/signup/nickname";
    }

    @GetMapping("/nickname")
    public String showNicknameForm() {
        return "member/signup/nickname";
    }

    @PostMapping("/nickname")
    public String processNickname(@ModelAttribute("signupForm")
                                  @Validated(SignupForm.ValidationGroups.NicknameGroup.class) SignupForm signupForm,
                                  BindingResult bindingResult) {
        System.out.println("signupForm = " + signupForm);
        if (bindingResult.hasErrors()) {
            return "member/signup/nickname";
        }
        try {
            signupService.validateDuplicateNickname(signupForm.getNickname());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("nickname", "runtimeError", e.getMessage());
            return "member/signup/nickname";
        }
        return "redirect:/signup/password";
    }

    @GetMapping("/password")
    public String showPasswordForm() {
        return "member/signup/password";
    }

    @PostMapping("/password")
    public String processPassword(@ModelAttribute("signupForm")
                                  @Validated(SignupForm.ValidationGroups.PasswordGroup.class) SignupForm signupForm,
                                  BindingResult bindingResult) {
        System.out.println("signupForm = " + signupForm);

        if (signupForm.getPassword() != null && signupForm.getPasswordConfirm() != null
                && !signupForm.getPassword().equals(signupForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "비밀번호가 일치하지 않습니다");
        }
        if (bindingResult.hasErrors()) {
            return "member/signup/password";
        }
        try {
            System.out.println(signupForm);
            signupService.join(signupForm);
        } catch (RuntimeException e) {
            bindingResult.rejectValue("passwordConfirm", "runtimeError", e.getMessage());
        }
        return "redirect:/signup/welcome";
    }

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "member/signup/welcome";
    }
}
