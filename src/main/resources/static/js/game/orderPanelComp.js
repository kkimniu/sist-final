function showSellPanel(){
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    document.getElementById("sellPanel").style.display = "block";
}

function showBuyPanel(){
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "block";
}

function showPromisePanel(){
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "block";
}

function showTradeLogPanel() {
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "block";
}

function callBackRequestSellOrder() {
    requestSellOrder()
        .then(data => {
            console.log(data);
        });
}
function callBackRequestMarketSellOrder() {
    requestMarketSellOrder()
        .then(data => {
            console.log(data);
        });
}

function callBackRequestBuyOrder() {
    requestBuyOrder()
        .then(data => {
            console.log(data);
        });
}

function callBackRequestMarketBuyOrder() {
    requestMarketBuyOrder()
        .then(data => {
            console.log(data);
        });
}

