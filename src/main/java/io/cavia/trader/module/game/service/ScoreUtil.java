package io.cavia.trader.module.game.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ScoreUtil {

    public BigDecimal getReturnRate(long postCash, long earnedCash) {
        if(postCash == 0){
            throw new RuntimeException("자산 초기값이 0은 division by zero가 발생할 위험성이 있습니다!");
        }
        BigDecimal postCashAmount = new BigDecimal(postCash);
        BigDecimal earnedCashAmount = new BigDecimal(earnedCash);
        return earnedCashAmount.divide(postCashAmount, 2, RoundingMode.HALF_UP);
    }

    public
}
