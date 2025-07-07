async function certificationToken() {
    const response = await fetch("/api/game/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: ""
    });
    if (response.status === 200) {
        return response.json();
    } else {
        alert("로그인이 필요한 페이지 입니다.");
        location.href = "/login";
    }
}

async function requestSellOrder() {
    let responseOrder = await fetch("/api/game/sell", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "price": document.getElementById("sellOrderPrice").value,
            "quantity": document.getElementById("sellOrderQuantity").value
        })
    });
    if (responseOrder.status === 200) {
        return responseOrder;
    } else if (responseOrder.status === 400) {
        showModal("입력 값을 확인해주세요.");
    } else if (responseOrder.status === 404) {
        alert("게임에 참여하지 않은 유저 입니다, 메인 페이지로 이동합니다.");
        location.href = "/";
    } else if (responseOrder.status === 422) {
        showModal("주식보유수량이 부족합니다.");
    } else if (responseOrder.status === 429) {
        showModal("더 이상 주문 할 수 없습니다. \n미체결 거래를 취소해주세요.");
    } else {
        alert("주문 실패");
    }

    document.getElementById("sellOrderPrice").value = "";
    document.getElementById("sellOrderQuantity").value = "";
}

async function requestBuyOrder() {
    let responseOrder = await fetch("/api/game/buy", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "price": document.getElementById("buyOrderPrice").value,
            "quantity": document.getElementById("buyOrderQuantity").value
        })
    });
    if (responseOrder.status === 200) {
        return responseOrder;
    } else if (responseOrder.status === 400) {
        showModal("입력 값을 확인해주세요.");
    } else if (responseOrder.status === 403) {
        alert("게임에 참여하지 않은 유저 입니다, 메인 페이지로 이동합니다.");
        location.href = "/";
    } else if (responseOrder.status === 422) {
        showModal("자산이 부족합니다.");
    } else if (responseOrder.status === 429) {
        showModal("더 이상 주문 할 수 없습니다. \n미체결 거래를 취소해주세요.");
    } else {
        alert("주문 실패");
    }
    document.getElementById("buyOrderPrice").value = "";
    document.getElementById("buyOrderQuantity").value = "";
}

async function requestCancelOrder(orderId) {
    let responseOrder = await fetch("/api/game/cancel", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "orderId": orderId
        })
    });
    if (responseOrder.status === 200) {
        return responseOrder;
    } else if (responseOrder.status === 400) {
        showModal("입력 값을 확인해주세요.");
    } else if (responseOrder.status === 403) {
        alert("게임에 참여하지 않은 유저 입니다, 메인 페이지로 이동합니다.");
        location.href = "/";
    } else if (responseOrder.status === 404) {
        showModal("이미 처리 중 입니다.");
    } else {
        showModal("주문 실패")
    }
}

async function requestMarketSellOrder() {
    let responseOrder = await fetch("/api/game/market-sell", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "quantity": document.getElementById("sellOrderQuantity").value
        })
    });
    if (responseOrder.status === 200) {
        return responseOrder;
    } else if (responseOrder.status === 400) {
        showModal("입력 값을 확인해주세요.");
    } else if (responseOrder.status === 403) {
        alert("게임에 참여하지 않은 유저 입니다, 메인 페이지로 이동합니다.");
        location.href = "/";
    } else if (responseOrder.status === 422) {
        showModal("주식보유수량이 부족합니다.");
    } else {
        showModal("주문 실패");
    }
    document.getElementById("sellOrderQuantity").value = "";
}

async function requestMarketBuyOrder() {
    let responseOrder = await fetch("/api/game/market-buy", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "quantity": document.getElementById("buyOrderQuantity").value
        })
    });
    if (responseOrder.status === 200) {
        return responseOrder;
    } else if (responseOrder.status === 400) {
        showModal("입력 값을 확인해주세요.");
    } else if (responseOrder.status === 403) {
        alert("게임에 참여하지 않은 유저 입니다, 메인 페이지로 이동합니다.");
        location.href = "/";
    } else if (responseOrder.status === 422) {
        showModal("잔금이 부족합니다.");
    } else {
        showModal("주문 실패");
    }
    document.getElementById("buyOrderQuantity").value = "";
}

async function requestEndedGameInfo() {
    let responseOrder = await fetch("/api/game/last-game-participation", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        }
    });
    if (responseOrder.status === 200) {
        return responseOrder.json();
    } else if (responseOrder.status === 404) {
        alert("당신의 게임 참가 이력을 찾지 못했습니다.");
    } else {
        alert("게임 결과 조회 실패");
    }
}