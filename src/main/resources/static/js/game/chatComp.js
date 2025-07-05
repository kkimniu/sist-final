// 채팅 관련 컴포넌트 캐시
const chatLayout = document.getElementById("chatLayout");
const chatLog = document.getElementById("chatLog");
const chatInput = document.getElementById("chatInput");
const chatSend = document.getElementById("chatSend");
const nickname = document.getElementById("nickname");
const cash = document.getElementById("cash");
const stockPrice = document.getElementById("stockPrice");
const holdingStocks = document.getElementById("stocksHolding");
const userJoinedCount = document.getElementById("userJoinedCount");
const remainingTime = document.getElementById("remainingTime");
let chatSocket;

chatSend.addEventListener("click", function (event) {
    console.log("채팅 보내기 클릭: " + chatInput.value);
    chatSocket.send(
        JSON.stringify(
            {
                "type": "chat",
                "Authorization": localStorage.getItem("jwt-token"),
                "message": chatInput.value,
            })
    )
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
        chatLog.innerText = chatLog.innerText + "\n" + msgJson.memberNickname + ": " + msgJson.msg;
    }
    chatSocket.onclose = function (event) {
        console.log("채팅 연결 종료!");
    };
    chatSocket.onerror = function (event) {
        alert("채팅 연결 오류!");
    };
}
