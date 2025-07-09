function showGameEndDisplay() {

    requestEndedGameInfo()
        .then(data => {
            let returnRate = data.returnRate;
            if (returnRate === 0) {
                document.getElementById("gameEndDisplayReturnRate").style.color = "black";
            } else {
               returnRate = Math.round(((data.returnRate - 1) * 10000)) / 100;
                if (returnRate > 0) {
                    document.getElementById("gameEndDisplayReturnRate").style.color = "red";
                } else if (returnRate < 0) {
                    document.getElementById("gameEndDisplayReturnRate").style.color = "blue";
                }
            }
            document.getElementById("gameEndDisplayGameRank").textContent = "게임랭크: " + data.gameRank;
            document.getElementById("gameEndDisplayReturnRate").textContent = "수익률: " + returnRate + "%";
            document.getElementById("gameEndDisplayEarnedScore").textContent = "획득점수: " + data.earnedScore;
            document.getElementById("gameEndDisplayEarnedCash").textContent = "획득재화: " + data.earnedCash;
        });

// div 표시
    document.getElementById("gameProgressDisplay").style.display = "none";
    document.getElementById("loadingDisplay").style.display = "none";
    document.getElementById("gameEndDisplay").style.display = "block";
}

function showLoadingDisplay() {
    document.getElementById("gameProgressDisplay").style.display = "none";
    document.getElementById("gameEndDisplay").style.display = "none";
    document.getElementById("loadingDisplay").style.display = "block";
}