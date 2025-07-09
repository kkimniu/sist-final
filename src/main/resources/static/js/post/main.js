document.addEventListener('DOMContentLoaded', function () {
    const postContainer = document.getElementById('post-container');
    const postForm = document.getElementById('post-form');
    const postContent = document.getElementById('post-content');

    // --- 무한 스크롤을 위한 상태 변수 ---
    let offset = 0; // 다음 페이지를 요청하기 위한 시작점
    const limit = 15; // 한 번에 가져올 게시물 수
    let isLoading = false; // 현재 데이터를 로딩 중인지 여부 (중복 요청 방지)

    // (A) 로그인 상태 확인 (기존 코드)
    function checkLoginStatus() {
        const token = localStorage.getItem('jwt-token');
        if (token) {
            postForm.style.display = 'block';
        }
    }

    // 게시물 목록을 가져오는 함수 (수정됨)
    async function fetchPosts() {
        if (isLoading) return; // 로딩 중이면 함수 종료
        isLoading = true; // 로딩 시작

        try {
            const response = await fetch(`/api/posts?limit=${limit}&offset=${offset}`);
            const result = await response.json();
            const posts = result.data;

            if (posts.length > 0) {
                posts.forEach(post => {
                    const postElement = createPostElement(post);
                    postContainer.appendChild(postElement); // 새 게시물을 아래에 추가
                });
                // 다음 요청을 위해 offset 값을 업데이트
                offset += posts.length;
            } else {
                // 더 이상 불러올 게시물이 없으면 스크롤 이벤트 리스너를 제거할 수도 있습니다.
                //console.log("더 이상 게시물이 없습니다.");
            }

        } catch (error) {
            console.error('Error fetching posts:', error);
        } finally {
            isLoading = false; // 로딩 완료
        }
    }

    // 게시물 HTML 요소를 생성하는 함수 (기존 코드)
    function createPostElement(post) {
        const postElement = document.createElement('div');
        // post-item 클래스 추가
        postElement.classList.add('post-item');
        postElement.innerHTML = `
            <p>${post.content}</p>
            <small>작성자 ID: ${post.nickname} | 작성일: ${new Date(post.createdAt).toLocaleString()} </small>
        `;
        return postElement;
    }

    // 새 글 작성 (기존 코드)
    postForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const content = postContent.value;
        const token = localStorage.getItem('jwt-token');
        if (!content.trim() || !token) {
            alert('내용을 입력하거나 로그인 상태를 확인해주세요.');
            return;
        }

        try {
            const response = await fetch('/api/posts', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
                body: JSON.stringify({ content: content })
            });
            if (response.status === 201) {
                const result = await response.json();
                const newPost = result.data;
                const newPostElement = createPostElement(newPost);
                postContainer.prepend(newPostElement);
                offset++;
                postContent.value = '';
            } else {
                alert('글 작성에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error creating post:', error);
        }
    });

    // --- (B) 스크롤 이벤트 리스너 추가 ---
    window.addEventListener('scroll', () => {
        // 사용자가 페이지 끝까지 스크롤했는지 확인하고, 로딩 중이 아닐 때만 fetchPosts 호출
        // window.innerHeight: 브라우저 창의 높이
        // window.scrollY: 수직 스크롤 위치
        // document.body.offsetHeight: 전체 문서의 높이
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 100 && !isLoading) {
            fetchPosts();
        }
    });

    // --- 페이지 로드 시 초기 실행 ---
    checkLoginStatus();
    fetchPosts(); // 첫 페이지 게시물 로드
});