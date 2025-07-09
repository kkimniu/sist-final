// 전역 회원가입 데이터 객체
const signupData = {
    termsOfServiceAgreed: false,
    privacyPolicyAgreed: false,
    email: '',
    authKey: '',
    nickname: '',
    password: ''
};
let recaptchaToken;

window.onload = function() {
    // 약관 내용을 불러오는 함수 호출
    loadTerms();
};

// 약관 내용을 서버에서 불러와서 화면에 표시하는 함수
async function loadTerms() {
    try {
        // Promise.all로 두 개의 파일을 동시에 요청
        const [termsRes, policyRes] = await Promise.all([
            fetch('/api/auth/terms/terms-of-service'),
            fetch('/api/auth/terms/privacy-policy')
        ]);

        // 각 응답을 텍스트로 변환
        const termsText = await termsRes.json();
        const policyText = await policyRes.json();

        if(!termsRes){
            throw new Error(termsText.data || "terms-of-service 요청 중 실패 응답")
        }
        if(!policyRes){
            throw new Error(policyText.data || "privacy-policy 요청 중 실패 응답")
        }
        document.getElementById('terms-content').innerHTML = marked.parse(termsText.data);
        document.getElementById('privacy-content').innerHTML = marked.parse(policyText.data);

    } catch (error) {
        console.error("약관을 불러오는 중 에러 발생:", error);
        showMessage('terms-error', "약관을 불러오지 못했습니다.", true);
    }
}

// 페이지 전환을 위한 함수
function showPage(pageId) {
    // 1. 모든 페이지 div를 찾아서 숨깁니다.
    document.querySelectorAll('.page').forEach(page => {
        page.style.display = 'none';
    });
    // 2. 요청된 ID를 가진 페이지만 찾아서 보여줍니다.
    document.getElementById(pageId).style.display = 'block';
}

// 에러 또는 성공 메시지를 표시하는 함수
function showMessage(elementId, message, isError = true) {
    const el = document.getElementById(elementId);
    if (el) {
        el.innerText = message;
        // isError가 true이면 빨간색, false이면 녹색으로 설정
        el.style.color = isError ? 'red' : 'green';
    }
}

// reCAPTCHA 성공 시 실행될 콜백 함수
function onRecaptchaSuccess(token) {
    //console.log("reCAPTCHA 성공, 토큰:", token);
    recaptchaToken = token;
    showMessage('email-error', ''); // 성공했으니 에러 메시지는 지웁니다.
}

// reCAPTCHA 토큰 만료 시 실행될 콜백 함수
function onRecaptchaExpired() {
    console.log("reCAPTCHA 토큰 만료");
    recaptchaToken = null; // 토큰을 비웁니다.
}

// 1. 약관 동의
document.getElementById('btn-terms-next').addEventListener('click', () => {
    signupData.termsOfServiceAgreed = document.getElementById('termsOfServiceAgreed').checked;
    signupData.privacyPolicyAgreed = document.getElementById('privacyPolicyAgreed').checked;
    if (!signupData.termsOfServiceAgreed || !signupData.privacyPolicyAgreed) {
        showMessage('terms-error', '모든 필수 약관에 동의해야 합니다.');
        return;
    }
    showPage('page-email');
});
// 2. 이메일 발송 버튼 클릭 이벤트 핸들러 수정
document.getElementById('btn-email-next').addEventListener('click', () => {
    signupData.email = document.getElementById('email').value;
    if (!signupData.email) {
        showMessage('email-error', '이메일을 입력해주세요.');
        return;
    }

    // 이제 grecaptcha.getResponse() 대신 전역 변수를 확인합니다.
    if (!recaptchaToken) {
        showMessage('email-error', '"로봇이 아닙니다"를 체크해주세요.');
        return;
    }

    showPage('page-verify');
    document.getElementById('verify-email-display').innerText = signupData.email;
    showMessage('verify-error', '인증 메일을 발송 중입니다. 잠시만 기다려주세요...', false);
    sendCodeInBackground();
});

// 인증 코드 발송 함수 수정
async function sendCodeInBackground() {
    try {
        const res = await fetch('/api/auth/verification/send-code', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
             'X-Recaptcha-Token': recaptchaToken
             },
            body: JSON.stringify({
                email: signupData.email
            })
        });
        if (!res.ok) {
            const errorResponse = await res.json();
            throw new Error(errorResponse.message);
        }
        showMessage('verify-error', '인증 코드가 발송되었습니다. 이메일을 확인해주세요.', false);
        document.getElementById('authKey').focus();
    } catch (error) {
        showMessage('verify-error', error.message, true);
    } finally{
        // 서버 통신 후 reCAPTCHA를 리셋하고 토큰을 비웁니다.
        grecaptcha.reset();
        recaptchaToken = null;
    }
}

// 3. 인증 코드 검증 및 페이지 이동
document.getElementById('btn-verify-next').addEventListener('click', async () => {
    signupData.authKey = document.getElementById('authKey').value;
    try {
        const res = await fetch('/api/auth/signup/verify-code', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: signupData.email, authKey: signupData.authKey })
        });
        if (!res.ok) {
            const errorResponse = await res.json();
            throw new Error(errorResponse.message);
        }
        showPage('page-nickname');
    } catch(error) {
        showMessage('verify-error', error.message);
    }
});

// 4. 닉네임 검증 및 페이지 이동
document.getElementById('btn-nickname-next').addEventListener('click', async () => {
    signupData.nickname = document.getElementById('nickname').value;
    try {
        const res = await fetch('/api/auth/validate-nickname', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nickname: signupData.nickname })
        });
        if (!res.ok) {
            const errorResponse = await res.json();
            throw new Error(errorResponse.message);
        }
        showPage('page-password');
    } catch(error) {
        showMessage('nickname-error', error.message);
    }
});

// 5. 최종 회원가입
document.getElementById('btn-signup-submit').addEventListener('click', async () => {
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('passwordConfirm').value;
    if (password !== passwordConfirm) {
        showMessage('password-error', '비밀번호가 일치하지 않습니다.');
        return;
    }
    signupData.password = password;
    try {
        const res = await fetch('/api/auth/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(signupData)
        });
        if (!res.ok) {
            const errorResponse = await res.json();
            throw new Error(errorResponse.message);
        }
        showPage('page-welcome');
    } catch(error) {
        showMessage('password-error', error.message);
    }
});

// '돌아가기' 링크 공통 로직
document.querySelectorAll('.prev-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        showPage(e.target.dataset.page);
    });
});