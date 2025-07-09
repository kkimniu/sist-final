const orderPanel = document.getElementById("orderPanelLayout");
const panelLayout = document.getElementById("panelLayout");
const promisePanel = document.getElementById("promisePanel");
const tradeLogPanel = document.getElementById("tradeLogPanel");
const orderTabLayout = document.getElementById("tabLayout");
let orderOffsetX;
let orderOffsetY;
let orderPanelIsDragging = false;

const regex = /^(\s*|-?\d+(\.\d+)?\s*)$/;


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
    if(!regex.test(document.getElementById("sellOrderPrice").value)){
        showModal("가격에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("sellOrderPrice").value = "";
        return;
    }
    if(!regex.test(document.getElementById("sellOrderQuantity").value)){
        showModal("수량에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("sellOrderQuantity").value = "";
        return;
    }

    requestSellOrder()
        .then(data => {
            //console.log(data);
        });
}
function callBackRequestMarketSellOrder() {
    if(!regex.test(document.getElementById("sellOrderQuantity").value)){
        showModal("수량에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("sellOrderQuantity").value = "";
        return;
    }
    requestMarketSellOrder()
        .then(data => {
            //console.log(data);
        });
}

function callBackRequestBuyOrder() {

    if(!regex.test(document.getElementById("buyOrderPrice").value)){
        showModal("가격에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("buyOrderPrice").value = "";
        return;
    }
    if(!regex.test(document.getElementById("buyOrderQuantity").value)){
        showModal("수량에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("buyOrderQuantity").value = "";
        return;
    }

    requestBuyOrder()
        .then(data => {
            //console.log(data);
        });
}

function callBackRequestMarketBuyOrder() {
    if(!regex.test(document.getElementById("buyOrderQuantity").value)){
        showModal("수량에 숫자가 아닌 값이 입력되었습니다.");
        document.getElementById("buyOrderQuantity").value = "";
        return;
    }
    requestMarketBuyOrder()
        .then(data => {
            //console.log(data);
        });
}

const tabs = document.querySelectorAll('.tab');
tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        tabs.forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
    });
});

const buyPrice = document.getElementById("buyOrderPrice");
const buyQuantity = document.getElementById("buyOrderQuantity");
const sellQuantity = document.getElementById("sellOrderQuantity");
const sellPrice = document.getElementById("sellOrderPrice");

buyPrice.addEventListener("keypress", function (e) {
    const char = String.fromCharCode(e.which);
    if (!/[0-9]/.test(char)) {
        e.preventDefault();
    }
});

buyQuantity.addEventListener("keypress", function (e) {
    const char = String.fromCharCode(e.which);
    if (!/[0-9]/.test(char)) {
        e.preventDefault();
    }
});

sellPrice.addEventListener("keypress", function (e) {
    const char = String.fromCharCode(e.which);
    if (!/[0-9]/.test(char)) {
        e.preventDefault();
    }
});

sellQuantity.addEventListener("keypress", function (e) {
    const char = String.fromCharCode(e.which);
    if (!/[0-9]/.test(char)) {
        e.preventDefault();
    }
});

