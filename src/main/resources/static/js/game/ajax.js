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