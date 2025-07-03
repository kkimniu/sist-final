function showSellPanel(){
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogTable").style.display = "none";
    document.getElementById("sellPanel").style.display = "block";
}

function showBuyPanel(){
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogTable").style.display = "none";
    document.getElementById("buyPanel").style.display = "block";
}

function showPromisePanel(){
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("tradeLogTable").style.display = "none";
    document.getElementById("promisePanel").style.display = "block";
}

function showTradeLogPanel() {
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogTable").style.display = "block";
}
