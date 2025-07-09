// 폼의 'submit' 이벤트를 감지합니다.
document.getElementById('login-form').addEventListener('submit', async function(event) {
    // form의 기본 제출 동작(페이지 새로고침)을 막습니다.
    event.preventDefault();

    // 각 DOM 요소를 변수에 할당합니다.
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessageDiv = document.getElementById('error-message');

    // 이전 에러 메시지가 남아있을 수 있으므로 초기화합니다.
    errorMessageDiv.textContent = '';

    // 비동기 로직을 try...catch로 감쌉니다.
    try {
        // 1. fetch로 API를 호출하고 응답을 기다립니다.
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });

        // 2. 응답의 성공 여부를 헤더(상태 코드)로 먼저 확인합니다.
        if (!response.ok) {
            // 실패 시, 응답 본문을 파싱하여 에러 메시지를 얻습니다.
            const errorBody = await response.json();
            throw new Error(errorBody.message || "로그인에 실패했습니다.");
        }

        // --- 성공 시 로직 ---

        // 3. 응답 헤더에서 토큰을 추출합니다.
        const bearerToken = response.headers.get('Authorization');

        if (bearerToken && bearerToken.startsWith('Bearer ')) {
            const token = bearerToken.split(' ')[1];
            // 4. 토큰을 로컬 스토리지에 저장합니다.
            localStorage.setItem('jwt-token', token);
            // 5. 메인 페이지로 이동합니다.
            window.location.href = '/';
        } else {
            // 토큰이 없는 예외적인 경우
            throw new Error("서버로부터 토큰을 받지 못했습니다.");
        }

    } catch (error) {
        // 6. fetch 실패 또는 위에서 throw한 에러를 여기서 모두 처리합니다.
        console.error('로그인 처리 중 에러 발생:', error);
        errorMessageDiv.textContent = error.message;
    }
});