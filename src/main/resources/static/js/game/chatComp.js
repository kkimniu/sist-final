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

let userInput = false;
let chatSocket;
let chatAutoScroll = true;

chatSend.addEventListener("click", function (event) {
    chatSocket.send(
        JSON.stringify(
            {
                "type": "chat",
                "Authorization": localStorage.getItem("jwt-token"),
                "message": chatInput.value,
            })
    )
    chatInput.value = "";
});

function chatSocketHandler() {
    chatSocket = new WebSocket("/ws/chat");

    chatSocket.onopen = function (event) {
        chatSocket.send(JSON.stringify(
            {
                "type": "validate",
                "Authorization": localStorage.getItem("jwt-token")
            }));
        console.log("채팅 연결 성공!");
    };

    chatSocket.onmessage = function (event) {
        let msgJson = JSON.parse(event.data);
        chatLog.innerText = chatLog.innerText + "\n" + msgJson.memberNickname + ": " + msgJson.msg + "\n";
        if (chatAutoScroll) {
            chatLayout.scrollTop = chatLayout.scrollHeight;
        }
        chatInputContainer.style.bottom = "10px";
    }
    chatSocket.onclose = function (event) {
        console.log("채팅 연결 종료!");
    };
    chatSocket.onerror = function (event) {
        alert("채팅 연결 오류!");
    };
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
       if (document.activeElement === chatInput) {
           chatSend.click();
       } else {
           chatInput.focus();
       }
    }
});
