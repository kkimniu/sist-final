// reset-password-promise.js

// DOM 요소 가져오기는 동일합니다.
const step1Div = document.getElementById('step1-email');
const step2Div = document.getElementById('step2-verification');
const step3Div = document.getElementById('step3-password');
// ... (이전 코드와 동일한 나머지 요소들)
const formStep1 = document.getElementById('form-step1');
const formStep2 = document.getElementById('form-step2');
const formStep3 = document.getElementById('form-step3');
const emailInput = document.getElementById('email-input');
const authKeyInput = document.getElementById('auth-key-input');
const newPasswordInput = document.getElementById('new-password-input');
const confirmPasswordInput = document.getElementById('confirm-password-input');
const displayEmail = document.getElementById('display-email');
const step1Message = document.getElementById('step1-message');
const step2Message = document.getElementById('step2-message');
const step3Message = document.getElementById('step3-message');

// 단계별 데이터를 저장할 변수
let userEmail = '';
let userAuthKey = '';

// --- 추가: reCAPTCHA 토큰을 저장할 전역 변수 ---
let recaptchaToken;

// --- 추가: reCAPTCHA 성공 시 실행될 콜백 함수 ---
function onRecaptchaSuccess(token) {
    //console.log("reCAPTCHA 성공, 토큰 저장");
    recaptchaToken = token;
    step1Message.textContent = ''; // 성공 시 에러 메시지 제거
}

// --- 추가: reCAPTCHA 토큰 만료 시 실행될 콜백 함수 ---
function onRecaptchaExpired() {
    console.log("reCAPTCHA 토큰 만료");
    recaptchaToken = null;
}

// --- 1단계: 인증메일 발송 버튼 클릭 이벤트 (수정) ---
formStep1.addEventListener('submit', (e) => {
    e.preventDefault();
    step1Message.textContent = '';
    userEmail = emailInput.value;

    if (!userEmail) {
        step1Message.textContent = '이메일을 입력해주세요.';
        return;
    }

    // --- 추가: reCAPTCHA 토큰이 있는지 확인 ---
    if (!recaptchaToken) {
        step1Message.textContent = '"로봇이 아닙니다"를 체크해주세요.';
        return;
    }

    // --- 1. 화면 전환을 먼저 즉시 실행 (Optimistic Update) ---
    step1Div.style.display = 'none';
    displayEmail.textContent = userEmail;
    step2Div.style.display = 'block';

    // --- 2. API 요청은 백그라운드에서 조용히 실행 ---
    fetch('/api/auth/verification/send-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Recaptcha-Token': recaptchaToken // --- 수정: 헤더에 토큰 추가 ---
        },
        body: JSON.stringify({ email: userEmail })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(body => {
                throw new Error(body.message || '알 수 없는 오류로 이메일 발송에 실패했습니다.');
            });
        }
        //console.log('이메일 발송 요청이 서버에 성공적으로 접수되었습니다.');
    })
    .catch(error => {
        console.error('Error:', error);
        alert(`오류: ${error.message}\n이메일 주소를 확인하고 다시 시도해주세요.`);
        step2Div.style.display = 'none';
        step1Div.style.display = 'block';
        step1Message.textContent = `오류: ${error.message}`;
    })
    .finally(() => {
        // --- 추가: 성공/실패 여부와 관계없이 reCAPTCHA를 리셋 ---
        grecaptcha.reset();
        recaptchaToken = null;
    });
});

// 2단계: 인증 확인 버튼 클릭 이벤트
formStep2.addEventListener('submit', (e) => {
    e.preventDefault();
    step2Message.textContent = '';
    userAuthKey = authKeyInput.value;

    if (!userAuthKey) {
        step2Message.textContent = '인증 코드를 입력해주세요.';
        return;
    }

    fetch('/api/auth/verification/verify-code', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: userEmail, authKey: userAuthKey })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(body => {
                throw new Error(body.message || '인증에 오류가 발생했습니다.');
            });
        }
        step2Div.style.display = 'none';
        step3Div.style.display = 'block';
        return;
    })
    .catch(error => {
        step2Message.textContent = error.message || '서버와 통신 중 오류가 발생했습니다.';
        console.error('Error:', error);
    });
});


// 3단계: 비밀번호 변경 버튼 클릭 이벤트
formStep3.addEventListener('submit', (e) => {
    e.preventDefault();
    step3Message.textContent = '';
    const newPassword = newPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    if (!newPassword || !confirmPassword) {
        step3Message.textContent = '비밀번호를 모두 입력해주세요.';
        return;
    }
    if (newPassword !== confirmPassword) {
        step3Message.textContent = '새 비밀번호가 일치하지 않습니다.';
        return;
    }

    const requestBody = {
        email: userEmail,
        authKey: userAuthKey,
        password: newPassword
    };

    fetch('/api/auth/password-reset', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                throw new Error(errorData.message || '비밀번호 변경에 실패했습니다.');
            });
        }
        return response.text();
    })
    .then(result => {
        alert('비밀번호가 성공적으로 변경되었습니다! 로그인 페이지로 이동합니다.');
        window.location.href = '/login';
    })
    .catch(error => {
        step3Message.textContent = error.message || '서버와 통신 중 오류가 발생했습니다.';
        console.error('Error:', error);
    });
});