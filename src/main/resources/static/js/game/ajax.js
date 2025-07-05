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
        throw new Error("토큰 인증 실패!");
    }
}

async function getStocksHolding() {
    const stocksHolding = await fetch("/api/game/stocks-holding", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        }
    });
    if (stocksHolding.status === 200) {
        return stocksHolding.json();
    } else {
        throw new Error("보유 종목 요청 실패!");
    }
}

async function requestSellOrder() {
    let responseSellOrder = await fetch("/api/game/sell", {
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
    if (responseSellOrder.status === 200) {
        return responseSellOrder;
    } else {
        const errorData = await responseSellOrder.text();
        throw new Error("매도 요청 실패!" + errorData);
    }
}

async function requestBuyOrder() {
    let responseBuyOrder = await fetch("/api/game/buy", {
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
    if (responseBuyOrder.status === 200) {
        return responseBuyOrder;
    } else {
        const errorData = await responseBuyOrder.text();
        throw new Error("매수 요청 실패!" + errorData);
    }
}

async function requestCancelOrder(orderId) {
    let responseBuyOrder = await fetch("/api/game/cancel", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "orderId": orderId
        })
    });
    if (responseBuyOrder.status === 200) {
        return responseBuyOrder;
    } else {
        const errorData = await responseBuyOrder.text();
        throw new Error("주문 취소 요청 실패!" + errorData);
    }
}

async function requestMarketSellOrder() {
    let responseSellOrder = await fetch("/api/game/market-sell", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "quantity": document.getElementById("sellOrderQuantity").value
        })
    });
    if (responseSellOrder.status === 200) {
        return responseSellOrder;
    } else {
        const errorData = await responseSellOrder.text();
        throw new Error("시장가 매도 요청 실패!" + errorData);
    }
}

async function requestMarketBuyOrder() {
    let responseBuyOrder = await fetch("/api/game/market-buy", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        },
        body: JSON.stringify({
            "quantity": document.getElementById("buyOrderQuantity").value
        })
    });
    if (responseBuyOrder.status === 200) {
        return responseBuyOrder;
    } else {
        const errorData = await responseBuyOrder.text();
        throw new Error("시장가 매수 요청 실패!" + errorData);
    }
}

async function requestEndedGameInfo() {
    let responseBuyOrder = await fetch("/api/game/last-game-participation", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt-token")
        }
    });
    if (responseBuyOrder.status === 200) {
        return responseBuyOrder.json();
    } else {
        const errorData = await responseBuyOrder.text();
        throw new Error("게임 히스토리 조회 실패!" + errorData);
    }
}