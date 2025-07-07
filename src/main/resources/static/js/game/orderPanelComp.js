const sellTab = document.getElementById("sellTab");
const buyTab = document.getElementById("buyTab");
const promiseTab = document.getElementById("promiseTab");
const tradeLogTab = document.getElementById("tradeLogTab");
const panelLayout = document.getElementById("panelLayout");

const promisePanel = document.getElementById("promisePanel");
const tradeLogPanel = document.getElementById("tradeLogPanel");


function showSellPanel(){

    panelLayout.classList.remove('component-boxRed');
    panelLayout.classList.add('component-box');

    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    document.getElementById("sellPanel").style.display = "block";
}

function showBuyPanel(){

    panelLayout.classList.remove('component-box');
    panelLayout.classList.add('component-boxRed');

    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "block";
}

function showPromisePanel(){

    panelLayout.classList.remove('component-boxRed');
    panelLayout.classList.add('component-box');
    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("tradeLogPanel").style.display = "none";
    promisePanel.style.display = "block";
    promisePanel.style.top = "10px";
}

function showTradeLogPanel() {

    panelLayout.classList.remove('component-boxRed');
    panelLayout.classList.add('component-box');

    document.getElementById("sellPanel").style.display = "none";
    document.getElementById("buyPanel").style.display = "none";
    document.getElementById("promisePanel").style.display = "none";
    tradeLogPanel.style.display = "block";
    tradeLogPanel.style.top = "10px";
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

const tabs = document.querySelectorAll('.tab');
tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        tabs.forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
    });
});

