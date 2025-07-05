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

// 1단계: 인증메일 발송 버튼 클릭 이벤트
formStep1.addEventListener('submit', (e) => {
    e.preventDefault();
    step1Message.textContent = '';
    userEmail = emailInput.value;

    if (!userEmail) {
        step1Message.textContent = '이메일을 입력해주세요.';
        return;
    }

    // --- 1. 화면 전환을 먼저 즉시 실행 (Optimistic Update) ---
    step1Div.style.display = 'none';
    displayEmail.textContent = userEmail;
    step2Div.style.display = 'block';

    // --- 2. API 요청은 백그라운드에서 조용히 실행 ---
    fetch('/api/auth/verification/send-code', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: userEmail })
    })
    .then(response => {
        // 응답이 성공(2xx)이 아니면 에러를 발생시켜 .catch()로 넘깁니다.
        if (!response.ok) {
            // 서버가 보낸 에러 메시지를 얻기 위해 response.json()을 시도합니다.
            return response.json().then(body => {
                // 실제 에러 객체를 만들어서 구체적인 에러 메시지를 전달합니다.
                throw new Error(body.message || '알 수 없는 오류로 이메일 발송에 실패했습니다.');
            });
        }
        // 성공 시에는 아무것도 할 필요가 없습니다. 사용자는 이미 다음 단계에 있습니다.
        console.log('이메일 발송 요청이 서버에 성공적으로 접수되었습니다.');
    })
    .catch(error => {
        // 여기서 실패 처리
        // 이 코드는 10초 후에 실행될 수 있습니다.
        console.error('Error:', error);

        // 사용자에게 실패를 알립니다.
        alert(`오류: ${error.message}\n이메일 주소를 확인하고 다시 시도해주세요.`);

        // 사용자를 다시 1단계로 돌려보냅니다.
        step2Div.style.display = 'none';
        step1Div.style.display = 'block';
        step1Message.textContent = `오류: ${error.message}`;
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
        return response.json();
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