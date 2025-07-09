
// 각 단계의 요소들을 미리 찾아둡니다.
const step1Div = document.getElementById('step1-verify-current');
const step2Div = document.getElementById('step2-set-new');

const formStep1 = document.getElementById('form-step1');
const formStep2 = document.getElementById('form-step2');

const currentPasswordInput = document.getElementById('current-password-input');
const newPasswordInput = document.getElementById('new-password-input');
const confirmPasswordInput = document.getElementById('confirm-password-input');

const step1Message = document.getElementById('step1-message');
const step2Message = document.getElementById('step2-message');

// 1단계에서 성공적으로 검증된 '현재 비밀번호'를 저장할 변수
let verifiedCurrentPassword = '';

/**
 * 1단계: 현재 비밀번호 검증
 */
formStep1.addEventListener('submit', async (e) => {
    e.preventDefault();
    step1Message.textContent = '';
    const currentPassword = currentPasswordInput.value;

    if (!currentPassword) {
        step1Message.textContent = '현재 비밀번호를 입력해주세요.';
        return;
    }

    try {
        // 서버의 @RequestBody String password는 JSON 문자열을 기대합니다.
        // 따라서 문자열 자체를 JSON.stringify로 감싸서 보냅니다.
        const response = await fetch('/api/members/me/password/verify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('jwt-token')}`
            },
            body: JSON.stringify({
                currentPassword : currentPassword
            })
        });

        if (response.ok) {
            // 검증 성공 시, 나중에 사용하기 위해 현재 비밀번호를 저장
            verifiedCurrentPassword = currentPassword;

            // 화면 전환
            alert('비밀번호가 확인되었습니다. 새 비밀번호를 입력해주세요.');
            step1Div.style.display = 'none';
            step2Div.style.display = 'block';
        } else {
            // GlobalExceptionHandler가 보낸 ErrorResponse를 처리
            const errorData = await response.json();
            step1Message.textContent = errorData.message || '비밀번호가 일치하지 않습니다.';
        }
    } catch (error) {
        step1Message.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
        console.error('Error:', error);
    }
});


/**
 * 2단계: 새 비밀번호로 변경
 */
formStep2.addEventListener('submit', async (e) => {
    e.preventDefault();
    step2Message.textContent = '';

    const newPassword = newPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    if (!newPassword || !confirmPassword) {
        step2Message.textContent = '새 비밀번호를 모두 입력해주세요.';
        return;
    }
    if (newPassword !== confirmPassword) {
        step2Message.textContent = '새 비밀번호가 일치하지 않습니다.';
        return;
    }

    // 서버의 PasswordChangeRequestDto 형식에 맞춰 요청 본문을 구성
    const requestBody = {
        currentPassword: verifiedCurrentPassword, // 1단계에서 저장해둔 값 사용
        newPassword: newPassword
    };

    try {
        const response = await fetch('/api/members/me/password', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('jwt-token')}`
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            alert('비밀번호가 성공적으로 변경되었습니다.');
            // 성공 시 마이페이지 메인 등으로 이동
            window.location.href = '/members/me';
        } else {
            const errorData = await response.json();
            step2Message.textContent = errorData.message || '비밀번호 변경에 실패했습니다.';
        }
    } catch (error) {
        step2Message.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
        console.error('Error:', error);
    }
});