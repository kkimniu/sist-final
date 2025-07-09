
    // 호가창 스타일 지정
    for (let i = 1; i <= 10; i++) {
    let buy = document.getElementById("buy" + i);
    buy.style.color = "blue";

    let buyPrice = document.getElementById("buyPrice" + i);
    buyPrice.style.backgroundColor = "skyblue";
    buyPrice.style.color = "blue";

    let buyAll = document.getElementById("buyAll");
    buyAll.style.color = "blue";

    let sell = document.getElementById("sell" + i);
    sell.style.backgroundColor = "pink";
    sell.style.color = "red";

    let sellPrice = document.getElementById("sellPrice" + i);
    sellPrice.style.color = "red";

    let sellAll = document.getElementById("sellAll");
    sellAll.style.color = "red";
}

    // 호가창 컴포넌트 캐싱
    const concludeVolume = document.getElementById("concludeVolume");
    const concludePrice = document.getElementById("concludePrice");

    // 호가창 크기 지정
    const orderBookLayout = document.getElementById("orderBookLayout");
    const orderBook = document.getElementById("orderBookTable");