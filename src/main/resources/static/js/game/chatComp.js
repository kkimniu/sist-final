// 채팅 관련 컴포넌트 캐시
const chatLayout = document.getElementById("chatLayout");
const chatLog = document.getElementById("chatLog");
const chatInput = document.getElementById("chatInput");
const chatSend = document.getElementById("chatSend");
const nickname = document.getElementById("nickname");
const cash = document.getElementById("cash");
const stockPrice = document.getElementById("stockPrice");
const holdingStocks = document.getElementById("stocksHolding");
const chatInputContainer = document.getElementById("chatInputContainer");
const emojiGuides = document.querySelectorAll(".emojiGuide");
let emojis = Array();
for (let emojiGuide of emojiGuides) {
    const emoji = document.createElement("img");
    emoji.id = emojiGuide.id.replace("Guide", "");
    emoji.src = emojiGuide.src;
    emoji.classList.add("emoji");
    emojis.push(emoji);
}


let userInput = false;
let chatSocket;
let chatAutoScroll = true;

chatSend.addEventListener("click", function (event) {
    if (chatInput.innerHTML === "") {
        //공지 띄우기
        const warning = document.getElementById("orderWarning");
        warning.innerHTML = "⚠️: 채팅창에 공백이 입력되었습니다.";
        warning.style.display = "block";
        setTimeout(() => {
            warning.style.display = "none";
        }, 3000);
        return;
    }
    // 내가 지정한 이모티콘 이외의 HTML은 제거
    for (let emojiGuide of emojiGuides){
        chatInput.innerHTML = chatInput.innerHTML.replaceAll(emojiGuide.outerHTML, "|"+emojiGuide.id.replace("Guide", ""));
    }
    const howManyEmoji = chatInput.innerText.split("|");

    if (chatInput.innerText.length > 100 || howManyEmoji.length-1 > 5) {
        const warning = document.getElementById("orderWarning");
        warning.innerHTML = "⚠️: 한 번에 너무 많은 채팅을 보내고 있습니다.<br>글자 제한: 100자 이모티콘 제한: 5개";
        warning.style.display = "block";
        setTimeout(() => {
            warning.style.display = "none";
        }, 3000);
        chatInput.innerHTML = "";
        return;
    }
    chatInput.innerHTML = stripHTML(chatInput.innerHTML);
        chatSocket.send(
            JSON.stringify(
                {
                    "type": "chat",
                    "Authorization": localStorage.getItem("jwt-token"),
                    "message": chatInput.innerText,
                })
        )
    chatInput.innerHTML = "";
});

function chatSocketHandler() {
    chatSocket = new WebSocket("/ws/chat");

    chatSocket.onopen = function (event) {
        chatSocket.send(JSON.stringify(
            {
                "type": "validate",
                "Authorization": localStorage.getItem("jwt-token")
            }));
    };

    chatSocket.onmessage = function (event) {
        let msgJson = JSON.parse(event.data);
    // 지정된 예약어는 Dom에 올라가있는 이미지 객체로 대체
        //chatLog.innerHTML = chatLog.innerHTML + "<br>" + msgJson.memberNickname + ": " + msgJson.msg;
        renderMessageWithEmojis(chatLog.innerHTML + "<br>" + msgJson.memberNickname + ": " + msgJson.msg);
        if (chatAutoScroll) {
            chatLayout.scrollTop = chatLayout.scrollHeight;
        }
        chatInputContainer.style.bottom = "10px";
    }
    chatSocket.onclose = function (event) {
        //console.log("채팅 연결 종료!");
    };
    chatSocket.onerror = function (event) {
        alert("채팅 연결 오류!");
    };
}
function showEmojiContainer() {
    document.getElementById("emojiContainer").style.display = "block";
}
function hideEmojiContainer() {
    document.getElementById("emojiContainer").style.display = "none";
}
function insertEmoji(img) {
    chatInput.innerHTML = chatInput.innerHTML + img.outerHTML;
    chatInput.focus();
}
chatInput.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
    }
});
function stripHTML(inputHTML) {
    const temp = document.createElement("div");
    temp.innerHTML = inputHTML;
    return temp.textContent || "";  // 평문만 반환
}

chatLayout.addEventListener("mousedown", function (event) {
    userInput = true;
});
chatLayout.addEventListener("mouseup", function (event) {
    if (chatLayout.scrollTop + chatLayout.clientHeight >= chatLayout.scrollHeight - 10) {
        chatAutoScroll = true;
    } else {
        chatAutoScroll = false;
    }
    userInput = false;
});

chatLayout.addEventListener("wheel", function (event) {
    userInput = true;
    clearTimeout(window.userInputTimer);
    window.userInputTimer = setTimeout(() => {
        if (chatLayout.scrollTop + chatLayout.clientHeight >= chatLayout.scrollHeight - 10) {
            chatAutoScroll = true;
        } else {
            chatAutoScroll = false;
        }
        userInput = false;
    }, 300); // 0.3초 안에 추가 입력 없으면 false
});

document.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        event.preventDefault();
       if (document.activeElement === chatInput) {
           chatSend.click();
       } else {
           chatInput.focus();
       }
    }
});

function renderMessageWithEmojis(message) {
    // 메시지를 공백/이모지 토큰 기준으로 나눔
    const tokens = message.split(/(\|[a-zA-Z0-9_]+)/);

    tokens.forEach(token => {
        if (token.startsWith("|")) {
            // 이모지 아이디 매칭
            const emojiId = token.slice(1);
            const match = [...emojis].find(e => e.id.replace("Guide") === emojiId);

            if (match) {
                // dom 객체 복사 후 이미지 삽입
                const emojiNode = match.cloneNode(true);
                chatLog.appendChild(emojiNode);
            } else {
                // 해당 이모지가 없으면 그냥 텍스트 삽입
                chatLog.innerHTML = chatLog.innerHTML + token;
                //appendChild(document.createTextNode(token));
            }
        } else {
            // 일반 텍스트 처리
            chatLog.innerHTML = chatLog.innerHTML + token;
            //chatLog.appendChild(document.createTextNode(token));
        }
    });
}

