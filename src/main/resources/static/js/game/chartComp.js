let chartSocket;

let holdingValue;

const graphScale = document.getElementById("graphScale");

const priceChartContainer = document.getElementById("priceChartContainer");

const priceChartWriter = document.getElementById("priceChartWriter");
const priceChartWriterCtx = priceChartWriter.getContext("2d");

const priceChartLayout = document.getElementById("priceChartLayout");
const priceChartLayoutCtx = priceChartLayout.getContext("2d");

const tradeVolumeChartContainer = document.getElementById("tradeVolumeChartContainer");

const tradeVolumeChartWriter = document.getElementById("tradeVolumeChartWriter");
const tradeVolumeChartWriterCtx = tradeVolumeChartWriter.getContext("2d");

const tradeVolumeChartLayout = document.getElementById("tradeVolumeChartLayout");
const tradeVolumeChartLayoutCtx = tradeVolumeChartLayout.getContext("2d");


const graphLayout = document.getElementById("graphContainer");

const graphDisplay = document.getElementById("graphDisplay");
const totalTradesLog = document.getElementById("totalTradesLog");

let autoScroll = true;
let onCode = false;

// 화면 크기 비율로 설정할 필요 있음
let mainWidth = window.innerWidth * 0.7;
let mainHeight = 400;

let priceChartHeight = mainHeight * 0.65;
let tradeVolumeChartHeight = mainHeight * 0.35;

const marginLeft = 0;
const marginTop = 0;

graphLayout.addEventListener("scroll", function (event) {
    if (onCode === false) {
        autoScroll = event.target.scrollLeft + event.target.clientWidth >= event.target.scrollWidth - 1;
    } else {
        onCode = false;
    }
});

window.addEventListener("resize", function (event) {
    mainWidth = window.innerWidth * 0.7;

    graphLayout.width = mainWidth;
    graphLayout.height = mainHeight + 40;

    graphLayout.style.width = mainWidth + "px";
    graphLayout.style.height = mainHeight + 40 + "px";


    graphDisplay.width = mainWidth;
    graphDisplay.height = graphLayout.height + 10;

    graphDisplay.style.width = mainWidth + "px";
    graphDisplay.style.height = graphLayout.height + 10 + "px";

    graphScale.height = graphLayout.height;

    graphScale.style.width = 40 + "px";
    graphScale.style.height = graphLayout.style.height + "px";


    priceChartLayout.width = mainWidth;
    priceChartLayout.height = priceChartHeight;
    priceChartWriter.width = mainWidth;
    priceChartWriter.height = priceChartHeight;
    priceChartContainer.width = mainWidth;
    priceChartContainer.height = priceChartHeight;

    tradeVolumeChartLayout.width = mainWidth;
    tradeVolumeChartLayout.height = tradeVolumeChartHeight;
    tradeVolumeChartWriter.width = mainWidth;
    tradeVolumeChartWriter.height = tradeVolumeChartHeight;
    tradeVolumeChartContainer.width = mainWidth;
    tradeVolumeChartContainer.height = tradeVolumeChartHeight;

    priceChartLayout.style.width = mainWidth + "px";
    priceChartLayout.style.height = priceChartHeight + "px";
    priceChartWriter.style.width = mainWidth + "px";
    priceChartWriter.style.height = priceChartHeight + "px";
    priceChartContainer.style.height = priceChartHeight + "px";
    priceChartContainer.style.width = mainWidth + "px";

    tradeVolumeChartLayout.style.width = mainWidth + "px";
    tradeVolumeChartLayout.style.height = tradeVolumeChartHeight + "px";
    tradeVolumeChartWriter.style.width = mainWidth + "px";
    tradeVolumeChartWriter.style.height = tradeVolumeChartHeight + "px";
    tradeVolumeChartContainer.style.width = mainWidth + "px";
    tradeVolumeChartContainer.style.height = tradeVolumeChartHeight + "px";


    drowGuideLine(guideLineLengthX, guideLineLengthY, priceChartLayoutCtx, priceChartHeight);
    drowGuideLine(guideLineLengthX, guideLineLengthY, tradeVolumeChartLayoutCtx, tradeVolumeChartHeight);
});

// 컴포넌트 크기 설정(html 크기 속성이랑 css 크기 속성이 다르게 먹어서 둘다 설정)


graphLayout.width = mainWidth;
graphLayout.height = mainHeight + 40;

graphLayout.style.width = mainWidth + "px";
graphLayout.style.height = mainHeight + 40 + "px";


graphDisplay.width = mainWidth;
graphDisplay.height = graphLayout.height + 10;

graphDisplay.style.width = mainWidth + "px";
graphDisplay.style.height = graphLayout.height + 10 + "px";

graphScale.height = graphLayout.height;

graphScale.style.width = 40 + "px";
graphScale.style.height = graphLayout.style.height + "px";


priceChartLayout.width = mainWidth;
priceChartLayout.height = priceChartHeight;
priceChartWriter.width = mainWidth;
priceChartWriter.height = priceChartHeight;
priceChartContainer.width = mainWidth;
priceChartContainer.height = priceChartHeight;

tradeVolumeChartLayout.width = mainWidth;
tradeVolumeChartLayout.height = tradeVolumeChartHeight;
tradeVolumeChartWriter.width = mainWidth;
tradeVolumeChartWriter.height = tradeVolumeChartHeight;
tradeVolumeChartContainer.width = mainWidth;
tradeVolumeChartContainer.height = tradeVolumeChartHeight;

priceChartLayout.style.width = mainWidth + "px";
priceChartLayout.style.height = priceChartHeight + "px";
priceChartWriter.style.width = mainWidth + "px";
priceChartWriter.style.height = priceChartHeight + "px";
priceChartContainer.style.height = priceChartHeight + "px";
priceChartContainer.style.width = mainWidth + "px";

tradeVolumeChartLayout.style.width = mainWidth + "px";
tradeVolumeChartLayout.style.height = tradeVolumeChartHeight + "px";
tradeVolumeChartWriter.style.width = mainWidth + "px";
tradeVolumeChartWriter.style.height = tradeVolumeChartHeight + "px";
tradeVolumeChartContainer.style.width = mainWidth + "px";
tradeVolumeChartContainer.style.height = tradeVolumeChartHeight + "px";


// 기본 봉 개수
let graphComp = 20;

let stickRefX = mainWidth;
let stickRefY = tradeVolumeChartHeight;

let candleRefX = mainWidth;
let candleRefY = priceChartHeight / 2;

const guideLineLengthY = 8;
let guideLineLengthX = graphComp / 3;

const valueStickWidth = 15;

// 그래프의 각 틱 시간정보
let relTime30Sec;
let relTime1Min;
let relTime3Min;
let relTime5Min;

// 5분 봉 캐시 변수
let priceChartValueData5Min = [0];
let priceChartValueData5MinZeroPoint = [0];
let priceChartValueData5MinMax = [0];
let priceChartValueData5MinMin = [0];

let valueData5Min = [0];
// 임의로 지정한 거래량 첫 막대의 그래프 기준값
let MaxStickValue5Min = 200000;

// 3분 봉 캐시 변수
let priceChartValueData3Min = [0];
let priceChartValueData3MinZeroPoint = [0];
let priceChartValueData3MinMax = [0];
let priceChartValueData3MinMin = [0];

let valueData3Min = [0];
let MaxStickValue3Min = MaxStickValue5Min * (3 / 5);

// 1분 봉 캐시 변수
let priceChartValueData1Min = [0];
let priceChartValueData1MinZeroPoint = [0];
let priceChartValueData1MinMax = [0];
let priceChartValueData1MinMin = [0];

let valueData1Min = [0];
let MaxStickValue1Min = MaxStickValue5Min / 5;

// 30초 봉 캐시 변수
let priceChartValueData30Sec = [0];
let priceChartValueData30SecZeroPoint = [0];
let priceChartValueData30SecMax = [0];
let priceChartValueData30SecMin = [0];


let valueData30Sec = [0];
let MaxStickValue30Sec = MaxStickValue5Min / 10;

// 1틱 차트 내부 데이터
let priceChartValueData1Tick = [0];

let valueData1Tick = [0];

// 화면에 표시될 데이터를 선택하기 위해 집계 데이터를 배열로 관리
let priceChartSelectData = [priceChartValueData5Min, priceChartValueData3Min, priceChartValueData1Min, priceChartValueData30Sec, priceChartValueData1Tick];
let priceChartSelectDataZeroPoint = [priceChartValueData5MinZeroPoint, priceChartValueData3MinZeroPoint, priceChartValueData1MinZeroPoint, priceChartValueData30SecZeroPoint];
let priceChartSelectDataMaxPoint = [priceChartValueData5MinMax, priceChartValueData3MinMax, priceChartValueData1MinMax, priceChartValueData30SecMax];
let priceChartSelectDataMinPoint = [priceChartValueData5MinMin, priceChartValueData3MinMin, priceChartValueData1MinMin, priceChartValueData30SecMin];

let selectData = [valueData5Min, valueData3Min, valueData1Min, valueData30Sec, valueData1Tick];
let selectDataMaxVolume = [MaxStickValue5Min, MaxStickValue3Min, MaxStickValue1Min, MaxStickValue30Sec];

// 화면에 표시될 데이터 선택 변수
let selectDataIndex = 3;

// 기준 시간 별로 milliseconds를 계산하여 캐시
const Min5 = 5 * 60 * 1000;
const Min3 = 3 * 60 * 1000;
const Min1 = 60 * 1000;
const Sec30 = 30 * 1000;

let oldPriceScale;
let oldVolumeScale;

// 그래프 상 고가, 저가 기준으로 상하 화면에 대한 그래프 비율 연산
let priceGap;
let highPointGap;
let lowPointGap;
let tickSize;
let viewRate;


onloadBody();
scrollSummary();

function scrollSummary() {
    onCode = true;
    graphLayout.scrollLeft = graphLayout.scrollWidth;
}

function onloadBody() {
    certificationToken()
        .then(data => {
            nickname.innerText = data.nickname;
            let tierImg;
            if (data.totalScore >= 0 && data.totalScore < 1500) {
                tierImg = '<img src="/images/tier/bronze.png" width="70" height="70" alt="브론즈 휘장"/>';
            } else if(data.totalScore >= 1500 && data.totalScore < 3500) {
                tierImg = '<img src="/images/tier/silver.png" width="70" height="70" alt="실버 휘장"/>';
            }else if(data.totalScore >= 3500 && data.totalScore < 5000) {
                tierImg = '<img src="/images/tier/gold.png" width="70" height="70" alt="골드 휘장"/>';
            }

            document.getElementById("totalScore").innerHTML = tierImg;
            cash.innerText = data.cash;
            chartSocketHandler();
            chatSocketHandler();
        });




    document.getElementById("stocksHolding").innerText = 0;


    drowGuideLine(guideLineLengthX, guideLineLengthY, priceChartLayoutCtx, priceChartHeight);
    drowGuideLine(guideLineLengthX, guideLineLengthY, tradeVolumeChartLayoutCtx, tradeVolumeChartHeight);
}

function drowGuideLine(widthLineLength, heightLineLength, context, height) {

    // canvas 내부의 외곽선 그리기

    // 1. 경로 시작 (이후 지정된 영역을 한 번에 그리는 느낌)
    context.beginPath();

    // 2. 외곽선 사각형
    context.rect(marginLeft, marginTop, mainWidth, height - marginTop * 2);

    // 3. 지정된 영역 채워 그리기
    context.strokeStyle = 'gray';
    context.lineWidth = 1;
    context.stroke();

    const widthLine = mainWidth / widthLineLength;
    const heightLine = height / heightLineLength;

    // 가로 가이드 라인
    for (let i = 1; i < heightLineLength; i++) {
        context.beginPath();
        context.moveTo(marginLeft,
            i * heightLine);
        context.lineTo(mainWidth,
            i * heightLine);

        context.strokeStyle = 'gray';
        context.lineWidth = 1;
        context.stroke();
    }

    // 세로 가이드라인
    for (let i = 1; i < widthLineLength; i++) {
        context.beginPath();
        context.moveTo(i * widthLine,
            marginTop);
        context.lineTo(i * widthLine,
            marginTop + height);

        context.strokeStyle = 'gray';
        context.lineWidth = 1;
        context.stroke();
    }
}

function drowCandle(RefX, RefY, width, value, highPoint, lowPoint, tickSize, viewRate) {

    RefX = Math.round(RefX);


    if (value === undefined) return;

    let valuePixels = Math.abs(value / tickSize * viewRate);
    let highPointPixels = highPoint / tickSize * viewRate;
    let lowPointPixels = lowPoint / tickSize * viewRate;

    // 데이터 갱신을 위해 타겟 봉 영역 지우기
    priceChartWriterCtx.clearRect(RefX - width, 0, width * 2, priceChartHeight);
    drowGuideLine(guideLineLengthX, guideLineLengthY, priceChartLayoutCtx, priceChartHeight);

    // 캔들 컴포넌트 그리기
    if (value > 0) {
        priceChartWriterCtx.fillStyle = 'blue';
        priceChartWriterCtx.strokeStyle = 'blue';
        priceChartWriterCtx.beginPath();
        priceChartWriterCtx.rect(RefX, RefY, width, valuePixels);
        priceChartWriterCtx.fill();
    } else if (value < 0) {
        priceChartWriterCtx.fillStyle = 'red';
        priceChartWriterCtx.strokeStyle = 'red';
        priceChartWriterCtx.beginPath();
        priceChartWriterCtx.rect(RefX, RefY - valuePixels, width, valuePixels);
        priceChartWriterCtx.fill();
    } else {

    }

    // 고점 라인 그리기
    priceChartWriterCtx.beginPath();
    priceChartWriterCtx.moveTo(RefX + valueStickWidth / 2, RefY);
    priceChartWriterCtx.lineTo(RefX + valueStickWidth / 2, RefY - highPointPixels);

    priceChartWriterCtx.stroke();

    // 저점 라인 그리기
    priceChartWriterCtx.beginPath();
    priceChartWriterCtx.moveTo(RefX + valueStickWidth / 2, RefY);
    priceChartWriterCtx.lineTo(RefX + valueStickWidth / 2, RefY + lowPointPixels);

    priceChartWriterCtx.stroke();

    //console.log("valuePixels: " + valuePixels + " highPointPixels: " + highPointPixels + " lowPointPixels: " + lowPointPixels);
    //console.log("stickRefX: " + RefX + ", stickRefY: " + RefY + ", width: " + width + ", value: " + value + ", highpoint: " + highPoint + ", lowpoint: " + lowPoint + ", tickSize: " + tickSize + ", viewRate: " + viewRate);
}

function drowStick(refX, refY, width, value) {

    refX = Math.round(refX);

    if (value === undefined) value = 0;

    tradeVolumeChartWriterCtx.clearRect(refX - width, 0, width * 2, tradeVolumeChartHeight);
    drowGuideLine(guideLineLengthX, guideLineLengthY, tradeVolumeChartLayoutCtx, tradeVolumeChartHeight);

    tradeVolumeChartWriterCtx.beginPath();
    tradeVolumeChartWriterCtx.rect(refX, refY, width, value);
    tradeVolumeChartWriterCtx.fillStyle = 'blue';
    tradeVolumeChartWriterCtx.fill();

    //console.log("stickRefX: " + RefX + ", stickRefY: " + RefY + ", width: " + width + ", value: " + value);
}

function shiftArrayRight(oldArray) {
    let newArray = new Array(oldArray.length).fill(0);
    for (let i = 0; i < oldArray.length; i++) {
        newArray[i + 1] = oldArray[i];
    }
    return newArray;
}

function getMax(intArray, length) {
    let max = intArray[0];
    for (let i; i < length; i++) {
        if (i > max) max = i;
    }
    return max;
}

function getMin(intArray, length) {
    let min = intArray[0];
    for (let i; i < length; i++) {
        if (i < min) min = i;
    }
    return min;
}

function expandGraph(index) {
    if (priceChartSelectData[selectDataIndex].length > graphComp && index === selectDataIndex) {
        mainWidth += mainWidth / graphComp;

        priceChartWriter.style.width = mainWidth + "px";
        priceChartLayout.style.width = mainWidth + "px";
        priceChartContainer.style.width = mainWidth + "px";

        priceChartWriter.width = mainWidth;
        priceChartLayout.width = mainWidth;
        priceChartContainer.width = mainWidth;

        tradeVolumeChartWriter.style.width = mainWidth + "px";
        tradeVolumeChartLayout.style.width = mainWidth + "px";
        tradeVolumeChartContainer.style.width = mainWidth + "px";

        tradeVolumeChartWriter.width = mainWidth;
        tradeVolumeChartLayout.width = mainWidth;
        tradeVolumeChartContainer.width = mainWidth;

        graphComp++;
        guideLineLengthX = graphComp / 3;
        if (autoScroll) {
            scrollSummary();
        }
    }
}

function getTickSize(price) {
    let unit;
    switch (true) {
        case (price < 1000):
            unit = 1;
            break;
        case (price < 5000):
            unit = 5;
            break;
        case (price < 10000):
            unit = 10;
            break;
        case (price < 50000):
            unit = 50;
            break;
        case (price < 100000):
            unit = 100;
            break;
        case (price < 500000):
            unit = 500;
            break;
        default:
            unit = 1000;
            break;
    }
    return unit;
}

function chartSocketHandler() {
    chartSocket = new WebSocket("/ws/chart");

    chartSocket.onopen = function (event) {
        chartSocket.send(localStorage.getItem("jwt-token"));
    };
    chartSocket.onmessage = function (event) {
        let stockDataArray = event.data.split("||");
        let DataHead = stockDataArray[0];
        let stockData;
        try {
            stockData = JSON.parse(stockDataArray[1]);
        } catch {
            stockData = stockDataArray[1];
        }

        if (DataHead === "trades") {
            if (relTime30Sec == null) {
                relTime30Sec = new Date(stockData.created_at);
                relTime1Min = relTime30Sec;
                relTime3Min = relTime30Sec;
                relTime5Min = relTime30Sec;
                for (let i = 0; i < priceChartSelectDataZeroPoint.length; i++) {
                    priceChartSelectDataZeroPoint[i][0] = stockData.stck_prpr;
                    priceChartSelectDataMaxPoint[i][0] = stockData.stck_prpr;
                    priceChartSelectDataMinPoint[i][0] = stockData.stck_prpr;
                }
            } else {
                let grapeTime = new Date(stockData.created_at);
                if (grapeTime - relTime5Min >= Min5) {
                    priceChartSelectData[0] = shiftArrayRight(priceChartSelectData[0]);
                    priceChartSelectDataMaxPoint[0] = shiftArrayRight(priceChartSelectDataMaxPoint[0]);
                    priceChartSelectDataMaxPoint[0][0] = stockData.stck_prpr;
                    priceChartSelectDataMinPoint[0] = shiftArrayRight(priceChartSelectDataMinPoint[0]);
                    priceChartSelectDataMinPoint[0][0] = stockData.stck_prpr;
                    priceChartSelectDataZeroPoint[0] = shiftArrayRight(priceChartSelectDataZeroPoint[0]);
                    priceChartSelectDataZeroPoint[0][0] = stockData.stck_prpr;
                    relTime5Min = grapeTime;
                    candleRefY = priceChartHeight / 2;

                    selectData[0] = shiftArrayRight(selectData[0]);
                    stickRefY = tradeVolumeChartHeight;
                    expandGraph(0, tradeVolumeChartWriter, tradeVolumeChartLayout, graphLayout);
                }
                if (grapeTime - relTime3Min >= Min3) {
                    priceChartSelectData[1] = shiftArrayRight(priceChartSelectData[1]);
                    priceChartSelectDataMaxPoint[1] = shiftArrayRight(priceChartSelectDataMaxPoint[1]);
                    priceChartSelectDataMaxPoint[1][0] = stockData.stck_prpr;
                    priceChartSelectDataMinPoint[1] = shiftArrayRight(priceChartSelectDataMinPoint[1]);
                    priceChartSelectDataMinPoint[1][0] = stockData.stck_prpr;
                    priceChartSelectDataZeroPoint[1] = shiftArrayRight(priceChartSelectDataZeroPoint[1]);
                    priceChartSelectDataZeroPoint[1][0] = stockData.stck_prpr;
                    relTime3Min = grapeTime;
                    candleRefY = priceChartHeight / 2;

                    selectData[1] = shiftArrayRight(selectData[1]);
                    stickRefY = tradeVolumeChartHeight;
                    expandGraph(1);
                }
                if (grapeTime - relTime1Min >= Min1) {
                    priceChartSelectData[2] = shiftArrayRight(priceChartSelectData[2]);
                    priceChartSelectDataMaxPoint[2] = shiftArrayRight(priceChartSelectDataMaxPoint[2]);
                    priceChartSelectDataMaxPoint[2][0] = stockData.stck_prpr;
                    priceChartSelectDataMinPoint[2] = shiftArrayRight(priceChartSelectDataMinPoint[2]);
                    priceChartSelectDataMinPoint[2][0] = stockData.stck_prpr;
                    priceChartSelectDataZeroPoint[2] = shiftArrayRight(priceChartSelectDataZeroPoint[2]);
                    priceChartSelectDataZeroPoint[2][0] = stockData.stck_prpr;
                    relTime1Min = grapeTime;
                    candleRefY = priceChartHeight / 2;

                    selectData[2] = shiftArrayRight(selectData[2]);
                    stickRefY = tradeVolumeChartHeight;
                    expandGraph(2);
                }
                if (grapeTime - relTime30Sec >= Sec30) {
                    priceChartSelectData[3] = shiftArrayRight(priceChartSelectData[3]);
                    priceChartSelectDataMaxPoint[3] = shiftArrayRight(priceChartSelectDataMaxPoint[3]);
                    priceChartSelectDataMaxPoint[3][0] = stockData.stck_prpr;
                    priceChartSelectDataMinPoint[3] = shiftArrayRight(priceChartSelectDataMinPoint[3]);
                    priceChartSelectDataMinPoint[3][0] = stockData.stck_prpr;
                    priceChartSelectDataZeroPoint[3] = shiftArrayRight(priceChartSelectDataZeroPoint[3]);
                    priceChartSelectDataZeroPoint[3][0] = stockData.stck_prpr;
                    relTime30Sec = grapeTime;
                    candleRefY = priceChartHeight / 2;

                    selectData[3] = shiftArrayRight(selectData[3]);
                    stickRefY = tradeVolumeChartHeight;
                    expandGraph(3);
                }
            }

            // 봉마다 고점, 저점 저장
            for (let i = 0; i < priceChartSelectDataMaxPoint.length; i++) {
                if (priceChartSelectDataMaxPoint[i][0] < stockData.stck_prpr) {
                    priceChartSelectDataMaxPoint[i][0] = stockData.stck_prpr;
                }
                if (priceChartSelectDataMinPoint[i][0] > stockData.stck_prpr) {
                    priceChartSelectDataMinPoint[i][0] = stockData.stck_prpr;
                }
            }

            // 종가 저장
            priceChartSelectData[0][0] = stockData.stck_prpr;
            priceChartSelectData[1][0] = stockData.stck_prpr;
            priceChartSelectData[2][0] = stockData.stck_prpr;
            priceChartSelectData[3][0] = stockData.stck_prpr;
            priceChartSelectData[4].push(stockData.stck_prpr);

            // 거래량 저장
            selectData[0][0] += stockData.cntg_vol;
            selectData[1][0] += stockData.cntg_vol;
            selectData[2][0] += stockData.cntg_vol;
            selectData[3][0] += stockData.cntg_vol;
            selectData[4].push(stockData.cntg_vol);

            // 그래프 그릴 x좌표 연산
            candleRefX = mainWidth - (mainWidth / graphComp);
            stickRefX = candleRefX;

            // 그래프 상 고가, 저가 기준으로 상하 화면에 대한 그래프 비율 연산
            priceGap = priceChartSelectDataZeroPoint[selectDataIndex][0] - stockData.stck_prpr;
            highPointGap = priceChartSelectDataMaxPoint[selectDataIndex][0] - priceChartSelectDataZeroPoint[selectDataIndex][0];
            lowPointGap = priceChartSelectDataZeroPoint[selectDataIndex][0] - priceChartSelectDataMinPoint[selectDataIndex][0];
            tickSize = getTickSize(stockData.stck_prpr);
            viewRate = priceChartHeight / 40;

            // 거래량 고점 갱신 시 그래프 표시 비율 연산
            if (selectData[0][0] > selectDataMaxVolume[0] && selectData[0][1] !== undefined) {
                selectDataMaxVolume[0] = selectData[0][0];
                if (selectDataIndex === 0) {
                    drowStick(stickRefX,
                        stickRefY - tradeVolumeChartHeight * 0.8,
                        valueStickWidth,
                        tradeVolumeChartHeight * 0.8);
                }
            }
            if (selectData[1][0] > selectDataMaxVolume[1] && selectData[1][0] !== undefined) {
                selectDataMaxVolume[1] = selectData[1][0];
                if (selectDataIndex === 1) {
                    drowStick(stickRefX,
                        stickRefY - tradeVolumeChartHeight * 0.8,
                        valueStickWidth,
                        tradeVolumeChartHeight * 0.8);
                }
            }
            if (selectData[2][0] > selectDataMaxVolume[2] && selectData[2][0] !== undefined) {
                selectDataMaxVolume[2] = selectData[2][0];
                if (selectDataIndex === 2) {
                    drowStick(stickRefX,
                        stickRefY - tradeVolumeChartHeight * 0.8,
                        valueStickWidth,
                        tradeVolumeChartHeight * 0.8);
                }
            }
            if (selectData[3][0] > selectDataMaxVolume[3] && selectData[3][0] !== undefined) {
                selectDataMaxVolume[3] = selectData[3][0];
                if (selectDataIndex === 3) {
                    drowStick(stickRefX,
                        stickRefY - tradeVolumeChartHeight * 0.8,
                        valueStickWidth,
                        tradeVolumeChartHeight * 0.8);
                }
            }

            // 최신 스틱 그래프 그리기
            drowStick(stickRefX,
                stickRefY - selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8,
                valueStickWidth,
                selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8);


            // 최신 캔들 그래프 그리기
            drowCandle(candleRefX, candleRefY, valueStickWidth, priceGap, highPointGap, lowPointGap, tickSize, viewRate);
            volumeScaleDrow(selectDataMaxVolume[selectDataIndex], stickRefY);
            // 과거 캔들 그래프 그리기
            let candleRefY2 = candleRefY;
            for (let i = 1; i < priceChartSelectData[selectDataIndex].length; i++) {
                candleRefY2 = candleRefY2 - viewRate * (priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i]) / tickSize;
                drowCandle(candleRefX - i * mainWidth / graphComp,
                    candleRefY2,
                    valueStickWidth,
                    priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i],
                    priceChartSelectDataMaxPoint[selectDataIndex][i] - priceChartSelectDataZeroPoint[selectDataIndex][i],
                    priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectDataMinPoint[selectDataIndex][i],
                    tickSize, viewRate);
            }

            // 과거 거래량 그래프 그리기
            for (let i = 1; i < selectData[selectDataIndex].length; i++) {
                drowStick(stickRefX - i * mainWidth / graphComp,
                    stickRefY - selectData[selectDataIndex][i] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8,
                    valueStickWidth,
                    selectData[selectDataIndex][i] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8);
            }

            stockPrice.innerText = stockData.stck_prpr;
            const num1 = document.getElementById("stocksHolding").innerText * stockData.stck_prpr;
            let returnRate = document.getElementById("returnRate");
            const rate = getRate(num1, holdingValue);
            if (!Number.isNaN(rate) && Number(holdingStocks.innerText) !== 0) {
                if (rate > 0) {
                    returnRate.style.color = "red";
                    returnRate.innerText = rate + "%";
                } else {
                    returnRate.style.color = "blue";
                    returnRate.innerText = rate + "%";
                }
            } else {
                returnRate.innerText = "";
            }

        } else if (DataHead === "quotes") {
            for (let i = 1; i <= 10; i++) {
                document.getElementById("buy" + i).innerText = stockData["askp_rsqn" + i];
                document.getElementById("buyPrice" + i).innerText = stockData["askp" + i];
                document.getElementById("buyAll").innerText = stockData.total_askp_rsqn_icdc;

                document.getElementById("sell" + i).innerText = stockData["bidp" + i];
                document.getElementById("sellPrice" + i).innerText = stockData["bidp_rsqn" + i];
                document.getElementById("sellAll").innerText = stockData.total_bidp_rsqn_icdc;
            }
        } else if (DataHead === "previewersTrades") {
            for (let value of stockData) {
                if (relTime30Sec == null) {
                    relTime30Sec = new Date(value.created_at);
                    relTime1Min = relTime30Sec;
                    relTime3Min = relTime30Sec;
                    relTime5Min = relTime30Sec;
                    for (let i = 0; i < priceChartSelectDataZeroPoint.length; i++) {
                        priceChartSelectDataZeroPoint[i][0] = value.stck_prpr;
                        priceChartSelectDataMaxPoint[i][0] = value.stck_prpr;
                        priceChartSelectDataMinPoint[i][0] = value.stck_prpr;
                    }
                } else {
                    let grapeTime = new Date(value.created_at);
                    if (grapeTime - relTime5Min >= Min5) {
                        priceChartSelectData[0] = shiftArrayRight(priceChartSelectData[0]);
                        priceChartSelectDataMaxPoint[0] = shiftArrayRight(priceChartSelectDataMaxPoint[0]);
                        priceChartSelectDataMaxPoint[0][0] = value.stck_prpr;
                        priceChartSelectDataMinPoint[0] = shiftArrayRight(priceChartSelectDataMinPoint[0]);
                        priceChartSelectDataMinPoint[0][0] = value.stck_prpr;
                        priceChartSelectDataZeroPoint[0] = shiftArrayRight(priceChartSelectDataZeroPoint[0]);
                        priceChartSelectDataZeroPoint[0][0] = value.stck_prpr;
                        relTime5Min = grapeTime;
                        candleRefY = priceChartHeight / 2;

                        selectData[0] = shiftArrayRight(selectData[0]);
                        stickRefY = tradeVolumeChartHeight;
                        expandGraph(0, tradeVolumeChartWriter, tradeVolumeChartLayout, graphLayout);
                    }
                    if (grapeTime - relTime3Min >= Min3) {
                        priceChartSelectData[1] = shiftArrayRight(priceChartSelectData[1]);
                        priceChartSelectDataMaxPoint[1] = shiftArrayRight(priceChartSelectDataMaxPoint[1]);
                        priceChartSelectDataMaxPoint[1][0] = value.stck_prpr;
                        priceChartSelectDataMinPoint[1] = shiftArrayRight(priceChartSelectDataMinPoint[1]);
                        priceChartSelectDataMinPoint[1][0] = value.stck_prpr;
                        priceChartSelectDataZeroPoint[1] = shiftArrayRight(priceChartSelectDataZeroPoint[1]);
                        priceChartSelectDataZeroPoint[1][0] = value.stck_prpr;
                        relTime3Min = grapeTime;
                        candleRefY = priceChartHeight / 2;

                        selectData[1] = shiftArrayRight(selectData[1]);
                        stickRefY = tradeVolumeChartHeight;
                        expandGraph(1);
                    }
                    if (grapeTime - relTime1Min >= Min1) {
                        priceChartSelectData[2] = shiftArrayRight(priceChartSelectData[2]);
                        priceChartSelectDataMaxPoint[2] = shiftArrayRight(priceChartSelectDataMaxPoint[2]);
                        priceChartSelectDataMaxPoint[2][0] = value.stck_prpr;
                        priceChartSelectDataMinPoint[2] = shiftArrayRight(priceChartSelectDataMinPoint[2]);
                        priceChartSelectDataMinPoint[2][0] = value.stck_prpr;
                        priceChartSelectDataZeroPoint[2] = shiftArrayRight(priceChartSelectDataZeroPoint[2]);
                        priceChartSelectDataZeroPoint[2][0] = value.stck_prpr;
                        relTime1Min = grapeTime;
                        candleRefY = priceChartHeight / 2;

                        selectData[2] = shiftArrayRight(selectData[2]);
                        stickRefY = tradeVolumeChartHeight;
                        expandGraph(2);
                    }
                    if (grapeTime - relTime30Sec >= Sec30) {
                        priceChartSelectData[3] = shiftArrayRight(priceChartSelectData[3]);
                        priceChartSelectDataMaxPoint[3] = shiftArrayRight(priceChartSelectDataMaxPoint[3]);
                        priceChartSelectDataMaxPoint[3][0] = value.stck_prpr;
                        priceChartSelectDataMinPoint[3] = shiftArrayRight(priceChartSelectDataMinPoint[3]);
                        priceChartSelectDataMinPoint[3][0] = value.stck_prpr;
                        priceChartSelectDataZeroPoint[3] = shiftArrayRight(priceChartSelectDataZeroPoint[3]);
                        priceChartSelectDataZeroPoint[3][0] = value.stck_prpr;
                        relTime30Sec = grapeTime;
                        candleRefY = priceChartHeight / 2;

                        selectData[3] = shiftArrayRight(selectData[3]);
                        stickRefY = tradeVolumeChartHeight;
                        expandGraph(3);
                    }

                    // 종가 저장
                    priceChartSelectData[0][0] = value.stck_prpr;
                    priceChartSelectData[1][0] = value.stck_prpr;
                    priceChartSelectData[2][0] = value.stck_prpr;
                    priceChartSelectData[3][0] = value.stck_prpr;
                    priceChartSelectData[4].push(value.stck_prpr);

                    // 거래량 저장
                    selectData[0][0] += value.cntg_vol;
                    selectData[1][0] += value.cntg_vol;
                    selectData[2][0] += value.cntg_vol;
                    selectData[3][0] += value.cntg_vol;
                    selectData[4].push(value.cntg_vol);

                    // 그래프 그릴 x좌표 연산
                    candleRefX = mainWidth - (mainWidth / graphComp);
                    stickRefX = candleRefX;

                    // 그래프 상 고가, 저가 기준으로 상하 화면에 대한 그래프 비율 연산
                    priceGap = priceChartSelectDataZeroPoint[selectDataIndex][0] - value.stck_prpr;
                    highPointGap = priceChartSelectDataMaxPoint[selectDataIndex][0] - priceChartSelectDataZeroPoint[selectDataIndex][0];
                    lowPointGap = priceChartSelectDataZeroPoint[selectDataIndex][0] - priceChartSelectDataMinPoint[selectDataIndex][0];
                    tickSize = getTickSize(value.stck_prpr);
                    viewRate = priceChartHeight / 40;

                    // 거래량 고점 갱신 시 그래프 표시 비율 연산
                    if (selectData[selectDataIndex][0] > selectDataMaxVolume[selectDataIndex] && selectData[selectDataIndex][1] !== undefined) {
                        selectDataMaxVolume[selectDataIndex] = selectData[selectDataIndex][0];
                        drowStick(stickRefX,
                            stickRefY - tradeVolumeChartHeight * 0.8,
                            valueStickWidth,
                            tradeVolumeChartHeight * 0.8);
                    } else {
                        // 최신 스틱 그래프 그리기
                        drowStick(stickRefX,
                            stickRefY - selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8,
                            valueStickWidth,
                            selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8);
                    }

                    // 최신 캔들 그래프 그리기
                    drowCandle(candleRefX, candleRefY, valueStickWidth, priceGap, highPointGap, lowPointGap, tickSize, viewRate);

                    // 과거 캔들 그래프 그리기
                    let candleRefY2 = candleRefY;
                    for (let i = 1; i < priceChartSelectData[selectDataIndex].length; i++) {
                        candleRefY2 = candleRefY2 - viewRate * (priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i]) / tickSize;
                        drowCandle(candleRefX - i * mainWidth / graphComp,
                            candleRefY2,
                            valueStickWidth,
                            priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i],
                            priceChartSelectDataMaxPoint[selectDataIndex][i] - priceChartSelectDataZeroPoint[selectDataIndex][i],
                            priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectDataMinPoint[selectDataIndex][i],
                            tickSize, viewRate);
                    }

                }
            }
        } else if (DataHead === "previewersQuotes") {
            let value = stockData[stockData.length - 1];
            for (let i = 1; i <= 10; i++) {
                document.getElementById("buy" + i).innerText = value["askp_rsqn" + i];
                document.getElementById("buyPrice" + i).innerText = value["askp" + i];
                const buy1Dif = document.getElementById("buyAll");
                buy1Dif.innerText = buy1Dif.innerText - value["askp_rsqn" + 1];
                document.getElementById("sell" + i).innerText = value["bidp" + i];
                document.getElementById("sellPrice" + i).innerText = value["bidp_rsqn" + i];
                const sell1Dif = document.getElementById("sellAll");
                sell1Dif.innerText = sell1Dif.innerText - value["bidp_rsqn" + 1];
            }
        } else if (DataHead === "numberOfParticipation") {
            document.getElementById("numberOfParticipation").innerText = stockData + "명";
        } else if (DataHead === "timeLeft") {
            showTimeLeft(stockData);
        } else if (DataHead === "playerStatus") {
            //console.log("입력 받은 플레이어스테이터스: " + JSON.stringify(stockData));

            //공지 띄우기
            const feedback = document.getElementById("orderFeedback");
            feedback.style.display = "block";
            setTimeout(() => {
                    feedback.style.display = "none";
            }, 3000);

            // 플레이어 스테이터스가 업데이트 됐을 때의 데이터를 처리해야 함
            document.getElementById("stocksHolding").innerText = stockData.stocksHolding;
            document.getElementById("cash").innerText = stockData.earnedCash.toLocaleString();
            updatePromiseTable(stockData);
            updateTradeLogTable(stockData);
            let sum = 0;
            stockData.orderDto.tradeLogs.forEach(row => {
                sum += row.price * row.quantity;
            });
            holdingValue = sum;
        } else if (DataHead === "isProsseced") {
            showGameEndDisplay();
        } else if (DataHead === "error") {
            //공지 띄우기
            const warning = document.getElementById("orderWarning");
            warning.innerHTML = "⚠️: 미체결 거래를 삭제 해주세요.";
            warning.style.display = "block";
            setTimeout(() => {
                warning.style.display = "none";
            }, 3000);
        } else {
            alert("에러 발생: 정제 되지 않은 데이터 수신!!", JSON.stringify(stockData));
        }
    };
    chartSocket.onerror = function (event) {
        //alert("에러 발생", event);
    };
    chartSocket.onclose = function (event) {
        //공지 띄우기
        const warning = document.getElementById("orderWarning");
        warning.innerHTML = "⚠️: 서버와 통신이 끊겼습니다.";
        warning.style.display = "block";
        setTimeout(() => {
            warning.style.display = "none";
        }, 3000);
    };
}

window.addEventListener("beforeunload", function (event) {
    chartSocket.close();
});

function updatePromiseTable(stockData) {

    if (!stockData.orderDto.orderTableDtos) {
        return;
    }

    const promiseTable = document.getElementById("promiseTable");

    const rows = promiseTable.querySelectorAll("tr");

    rows.forEach((row, index) => {
        if (index !== 0) {
            row.remove();
        }
    });

    stockData.orderDto.orderTableDtos.forEach(row => {
        const rowData = [];

        const id = row.id;
        const price = row.price;
        const quantity = row.quantity;
        const createdAtData = new Date(row.createdAt);
        const createdAt = createdAtData.getHours() + "시" +
            createdAtData.getMinutes() + "분"
            + createdAtData.getSeconds() + "초"

        rowData.push(id);
        rowData.push(price);
        rowData.push(quantity);
        rowData.push(createdAt);


        const tr = document.createElement('tr');

        for (let i = 0; i < rowData.length; i++) {
            const td = document.createElement('td');
            td.innerText = rowData[i];
            tr.appendChild(td);
        }
        const cancelButton = document.createElement("button");
        cancelButton.addEventListener("click", () => {
            requestCancelOrder(row.id);
        });
        cancelButton.className = "btn";
        cancelButton.innerText = "주문취소";
        const td = document.createElement('td');
        td.appendChild(cancelButton);
        tr.appendChild(td);


        promiseTable.appendChild(tr);
    });
}

function updateTradeLogTable(stockData) {

    if (!stockData.orderDto.tradeLogs) {
        return;
    }
    const tradeLogTable = document.getElementById("tradeLogTable");


    const rows = tradeLogTable.querySelectorAll("tr");

    rows.forEach((row, index) => {
        if (index !== 0) {
            row.remove();
        }
    });

    stockData.orderDto.tradeLogs.forEach(row => {
        const rowData = [];
        const id = row.id;
        const price = row.price;
        const quantity = row.quantity;
        const createdAtData = new Date(row.createdAt);
        const createdAt = createdAtData.getHours() + "시" +
            createdAtData.getMinutes() + "분"
            + createdAtData.getSeconds() + "초"

        rowData.push(row.id);
        rowData.push(row.price);
        rowData.push(row.quantity);
        rowData.push(createdAt);

        const tr = document.createElement('tr');

        for (let i = 0; i < 4; i++) {
            const td = document.createElement('td');
            td.innerText = rowData[i];
            tr.appendChild(td);
        }

        tradeLogTable.appendChild(tr);
    });
}

function getRate(number1, number2) {
    return (Math.round((number1 / number2 * 100 - 100) * 100) / 100)
}

function showTimeLeft(stockData) {
    const startedAt = new Date(stockData[0]);
    const serverNow = new Date(stockData[1]);
    const now = new Date();
    const timeDif = serverNow.getTime() - now.getTime();
    const endedAt = startedAt.getTime() + 1000 * 60 * 30;
    // 인터발 실행 시간 때문에 먼저 1회 실행후 로딩

    const timeLeft = Math.floor((endedAt - now.getTime() - timeDif) / 1000);
    document.getElementById("timeLeft").innerText = Math.floor(timeLeft / 60) + "분 " + timeLeft % 60 + "초"

    const interval = setInterval(() => {
        const now = new Date();
        if (now.getTime() + timeDif < endedAt) {
            const timeLeft = Math.floor((endedAt - now.getTime() - timeDif) / 1000);
            document.getElementById("timeLeft").innerText = Math.floor(timeLeft / 60) + "분 " + timeLeft % 60 + "초"
        } else {
            showLoadingDisplay();
            clearInterval(interval);
        }
    }, 1000); // 1초마다 체크
}

function refreshChart() {
    // 거래량 고점 갱신 시 그래프 표시 비율 연산
    if (selectData[selectDataIndex][0] > selectDataMaxVolume[selectDataIndex] && selectData[selectDataIndex][1] !== undefined) {
        selectDataMaxVolume[selectDataIndex] = selectData[selectDataIndex][0];
        drowStick(stickRefX,
            stickRefY - tradeVolumeChartHeight * 0.8,
            valueStickWidth,
            tradeVolumeChartHeight * 0.8);

    } else {
        // 최신 스틱 그래프 그리기
        drowStick(stickRefX,
            stickRefY - selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8,
            valueStickWidth,
            selectData[selectDataIndex][0] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8);
    }

    // 최신 캔들 그래프 그리기
    drowCandle(candleRefX, candleRefY, valueStickWidth, priceGap, highPointGap, lowPointGap, tickSize, viewRate);
    volumeScaleDrow(selectDataMaxVolume[selectDataIndex], stickRefY);
    // 과거 캔들 그래프 그리기
    let candleRefY2 = candleRefY;
    for (let i = 1; i < priceChartSelectData[selectDataIndex].length; i++) {
        candleRefY2 = candleRefY2 - viewRate * (priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i]) / tickSize;
        drowCandle(candleRefX - i * mainWidth / graphComp,
            candleRefY2,
            valueStickWidth,
            priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectData[selectDataIndex][i],
            priceChartSelectDataMaxPoint[selectDataIndex][i] - priceChartSelectDataZeroPoint[selectDataIndex][i],
            priceChartSelectDataZeroPoint[selectDataIndex][i] - priceChartSelectDataMinPoint[selectDataIndex][i],
            tickSize, viewRate);
    }

    // 과거 거래량 그래프 그리기
    for (let i = 1; i < selectData[selectDataIndex].length; i++) {
        drowStick(stickRefX - i * mainWidth / graphComp,
            stickRefY - selectData[selectDataIndex][i] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8,
            valueStickWidth,
            selectData[selectDataIndex][i] / selectDataMaxVolume[selectDataIndex] * tradeVolumeChartHeight * 0.8);
    }
}

function show30SecChart() {
    selectDataIndex = 3;
    priceChartWriterCtx.clearRect(0, 0, priceChartWriter.width, priceChartWriter.height);
    tradeVolumeChartWriterCtx.clearRect(0, 0, tradeVolumeChartWriter.width, tradeVolumeChartWriter.height);
    refreshChart();
}

function show1MinChart() {
    selectDataIndex = 2;
    priceChartWriterCtx.clearRect(0, 0, priceChartWriter.width, priceChartWriter.height);
    tradeVolumeChartWriterCtx.clearRect(0, 0, tradeVolumeChartWriter.width, tradeVolumeChartWriter.height);
    refreshChart();
}

function show3MinChart() {
    selectDataIndex = 1;
    priceChartWriterCtx.clearRect(0, 0, priceChartWriter.width, priceChartWriter.height);
    tradeVolumeChartWriterCtx.clearRect(0, 0, tradeVolumeChartWriter.width, tradeVolumeChartWriter.height);
    refreshChart();
}

function show5MinChart() {
    selectDataIndex = 0;
    priceChartWriterCtx.clearRect(0, 0, priceChartWriter.width, priceChartWriter.height);
    tradeVolumeChartWriterCtx.clearRect(0, 0, tradeVolumeChartWriter.width, tradeVolumeChartWriter.height);
    refreshChart();
}

function volumeScaleDrow(maxVolume, refY) {

    const digits = Math.floor(Math.log10(maxVolume)) + 1;
    const factor = Math.pow(10, digits - 1);
    let newVolumeScale = Math.ceil(maxVolume / factor) * factor;

    if (oldVolumeScale) {
        if (newVolumeScale == oldVolumeScale) {
            return;
        }
    }
    oldVolumeScale = newVolumeScale;


    const previouslyDiv = document.getElementById("volumeChild1");
    if (previouslyDiv) {
        previouslyDiv.remove();
    }

    const previouslyDiv2 = document.getElementById("volumeChild2");
    if (previouslyDiv2) {
        previouslyDiv2.remove();
    }


    const child = document.createElement("div");
    child.id = "volumeChild1";
    child.style.position = "absolute";
    child.style.right = "0px";
    child.style.width = "30px";
    child.style.height = "7px";
    child.style.display = "flex";
    child.style.justifyContent = "center";
    child.style.alignItems = "center";
    child.style.fontSize = 10 + "px";
    child.style.bottom = tradeVolumeChartHeight * 0.4 + 30 + "px";
    child.innerText = Math.round(newVolumeScale / 2);
    graphScale.appendChild(child);

    const child2 = document.createElement("div");
    child2.id = "volumeChild2";
    child2.style.position = "absolute";
    child2.style.right = "0px";
    child2.style.width = "30px";
    child2.style.height = "7px";
    child2.style.display = "flex";
    child2.style.justifyContent = "center";
    child2.style.alignItems = "center";
    child2.style.fontSize = 10 + "px";
    child2.style.bottom = tradeVolumeChartHeight * 0.8 + 40 + "px";
    child2.innerText = newVolumeScale;
    graphScale.appendChild(child2);
}