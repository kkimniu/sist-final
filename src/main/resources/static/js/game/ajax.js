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
    }else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    } else if (responseOrder.status === 422) {
        showModal("주식보유수량이 부족합니다.");
    } else {
        showModal("주식보유수량이 부족합니다.");
    }
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
    }else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    } else if (responseOrder.status === 422) {
        showModal("잔금이 부족합니다.");
    } else if (responseOrder.status === 429) {
        showModal("더 이상 주문을 할 수 없습니다. 미체결 거래를 취소해주세요.");
    } else {
        showModal("잔금이 부족합니다.");
    }
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
    }else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    } else if (responseOrder.status === 422) {
        showModal("잘못된 주문번호가 입력되었습니다.");
    } else {
        showModal("잘못된 주문번호가 입력되었습니다.")
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
    }else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    } else if (responseOrder.status === 422) {
        showModal("주식보유수량이 부족합니다.");
    } else if (responseOrder.status === 429) {
        showModal("더 이상 주문을 할 수 없습니다. 미체결 거래를 취소해주세요.");
    } else {
        showModal("주식보유수량이 부족합니다.");
    }
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
    }else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    } else if (responseOrder.status === 422){
        showModal("잔금이 부족합니다.");
    } else {
        showModal("잔금이 부족합니다.");
    }
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
    } else if (responseOrder.status === 403) {
        const errorData = await responseOrder.text();
        throw new Error("조회 권한이 없는 유저 입니다!" + errorData);
    }else {
        const errorData = await responseOrder.text();
        throw new Error("게임 히스토리 조회 실패!" + errorData);
    }
}