// 전역 회원가입 데이터 객체
const signupData = {
    termsOfServiceAgreed: false,
    privacyPolicyAgreed: false,
    email: '',
    authKey: '',
    nickname: '',
    password: ''
};

// 페이지 전환 함수
function showPage(pageId) { /* ... */ }
// 메시지 표시 함수
function showMessage(elementId, message, isError = true) { /* ... */ }

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

// 2. 이메일 발송
document.getElementById('btn-email-next').addEventListener('click', () => {
    signupData.email = document.getElementById('email').value;
    if (!signupData.email) {
        showMessage('email-error', '이메일을 입력해주세요.');
        return;
    }
    showPage('page-verify');
    document.getElementById('verify-email-display').innerText = signupData.email;
    showMessage('verify-error', '인증 메일을 발송 중입니다. 잠시만 기다려주세요...', false);
    sendCodeInBackground();
});

async function sendCodeInBackground() {
    try {
        const res = await fetch('/api/auth/send-verification-email', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: signupData.email })
        });
        if (!res.ok) {
            const errorResponse = await res.json();
            throw new Error(errorResponse.message);
        }
        showMessage('verify-error', '인증 코드가 발송되었습니다. 이메일을 확인해주세요.', false);
        document.getElementById('authKey').focus();
    } catch (error) {
        showMessage('verify-error', error.message, true);
    }
}

// 3. 인증 코드 검증 및 페이지 이동
document.getElementById('btn-verify-next').addEventListener('click', async () => {
    signupData.authKey = document.getElementById('authKey').value;
    showMessage('verify-error', '인증을 확인 중입니다...', false);
    try {
        const res = await fetch('/api/auth/verify-code', {
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
    showMessage('nickname-error', '중복을 확인 중입니다...', false);
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