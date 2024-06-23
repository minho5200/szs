package com.szs.prj.util;

import com.szs.prj.entity.CreditCardDeductionMonth;
import com.szs.prj.entity.Pension;
import com.szs.prj.entity.UserTaxationInfo;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class TaxCalculatorUtil {
    //최대 세율
    private static final double TOP_RATE = 0.45;
    // 최대 세율시 부과하는 기본 세액
    private static final double TOP_BASE_TAX = 384060000;

    //계산 편의를 위한 클래스
    private static class TaxBracket {
        @Getter
        private final long upperLimit;
        @Getter
        private final double baseTax;
        @Getter
        private final double rate;

        public TaxBracket(long upperLimit, double baseTax, double rate) {
            this.upperLimit = upperLimit;
            this.baseTax = baseTax;
            this.rate = rate;
        }
    }

    // 구간별 세금
    private static final List<TaxBracket> TAX_BRACKETS = List.of(
            new TaxBracket(14000000, 0, 0.06),
            new TaxBracket(50000000, 840000, 0.15),
            new TaxBracket(88000000, 6240000, 0.24),
            new TaxBracket(150000000, 15360000, 0.35),
            new TaxBracket(300000000, 37060000, 0.38),
            new TaxBracket(500000000, 94060000, 0.4),
            new TaxBracket(1000000000, 174060000, 0.42)
    );

    // 산출세액 계산기
    public long calculateTaxAmount(long baseTax) {
        for (TaxBracket bracket : TAX_BRACKETS) {
            if (baseTax <= bracket.getUpperLimit()) {
                return roundHalfUp(bracket.getBaseTax() + (baseTax - getPreviousLimit(bracket)) * bracket.getRate());
            }
        }
        return roundHalfUp(TOP_BASE_TAX + (baseTax - 1000000000) * TOP_RATE);
    }

    // 이전 구간
    private long getPreviousLimit(TaxBracket currentBracket) {
        int index = TAX_BRACKETS.indexOf(currentBracket);
        return index == 0 ? 0 : TAX_BRACKETS.get(index - 1).getUpperLimit();
    }

    //소수점 계산기
    private long roundHalfUp(double value) {
        long result = Math.round(value);

        return result;
    }

    // 캐스팅
    private double convertingValue(String value) {
        double doubleValue = 0;
        try {
            DecimalFormat decimalFormat = new DecimalFormat();
            Number number = decimalFormat.parse(value);
            doubleValue = number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doubleValue;
    }

    // 최종 계산식
    public long processTax(UserTaxationInfo userTaxationInfo, List<Pension> pensionList, List<CreditCardDeductionMonth> creditCardDeductionMonthList) {
        double totalDeductionAmmount = 0;
        for (Pension pension : pensionList) {
            totalDeductionAmmount += convertingValue(pension.getDeductionAmount());
        }

        for (CreditCardDeductionMonth creditCardDeductionMonth : creditCardDeductionMonthList){
            totalDeductionAmmount += convertingValue(creditCardDeductionMonth.getAmount());
        }

        //과표
        long baseTax = roundHalfUp(userTaxationInfo.getTotalIncome().doubleValue() - totalDeductionAmmount);

        // 산출세액
        long tax = calculateTaxAmount(baseTax);

        // 결정세액
        long result = roundHalfUp(tax - convertingValue(userTaxationInfo.getTaxDeduction()));

        return result;
    }
}