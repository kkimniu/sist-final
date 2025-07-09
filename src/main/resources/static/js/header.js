  // 페이지의 DOM 콘텐츠가 모두 로드되면 checkLoginStatus 함수를 즉시 실행합니다.
    document.addEventListener("DOMContentLoaded", function() {
        checkLoginStatus();
    });

    // 서버 API를 호출하여 로그인 상태를 확인하고 헤더를 업데이트하는 함수
    function checkLoginStatus() {
        const token = localStorage.getItem('jwt-token');
        const anonymousHeader = document.getElementById('header-anonymous');
        const authenticatedHeader = document.getElementById('header-authenticated');

        // 로컬 스토리지에 토큰이 없으면, 바로 로그아웃 상태로 UI를 설정하고 함수를 종료합니다.
        if (!token) {
            anonymousHeader.style.display = 'block';
            authenticatedHeader.style.display = 'none';
            return;
        }

        // 토큰이 존재하면, 서버에 '내 정보 조회' API를 호출하여 토큰의 유효성을 검증합니다.
        fetch('/api/members/me', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            // 서버 응답을 처리합니다.
            if (!response.ok) { // 401, 403, 500 등 에러 응답일 경우
                return response.json().then(body => {
                throw new Error(body.message || "api 응답이 실패했습니다.");
                });
            }
            // 성공적인 응답(200 OK)이면, 응답 본문을 JSON으로 파싱합니다.
            return response.json();
        })
        .then(body => {
            const userInfo = body.data;
            // API 호출 성공 시: 로그인 상태 UI를 표시합니다.
            // 서버로부터 받은 최신 닉네임 정보를 사용합니다.
            document.getElementById('header-nickname').textContent = userInfo.nickname;

            anonymousHeader.style.display = 'none';
            authenticatedHeader.style.display = 'flex';
        })
        .catch(error => {
            // API 호출 실패 시 (네트워크 에러, 401 등): 로그아웃 상태로 처리합니다.
            console.error('로그인 상태 확인 실패:', error.message);

            // 유효하지 않은 토큰은 즉시 삭제합니다.
            localStorage.removeItem('jwt-token');

            anonymousHeader.style.display = 'block';
            authenticatedHeader.style.display = 'none';
        });
    }
