package com.jiangKlijna.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * MathUtil
 * Author: jiangKlijna
 */
public class MathUtil {
    private MathUtil() {
    }

    //角度->弧度
    public static double angle2Radian(double angle) {
        return angle / 180 * Math.PI;
    }

    //弧度->角度
    public static double radian2Angle(double radian) {
        return radian / Math.PI * 180;
    }

    //返回科学计算后的乘法结果
    public static double multiply(double val1, double val2) {
        if (val1 == 0 || val2 == 0) return 0;
        return new BigDecimal(val1).multiply(new BigDecimal(val2)).doubleValue();
    }

    //返回科学计算后的除法结果
    public static double divide(double fz, double fm) {
        if (fz == 0 || fm == 0) return 0;
        return new BigDecimal(fz).divide(new BigDecimal(fm)).doubleValue();
    }

    //返回科学计算后的减法结果
    public static double subtract(double val1, double val2) {
        if (val2 == 0) return val1;
        return new BigDecimal(val1).subtract(new BigDecimal(val2)).doubleValue();
    }

    //返回科学计算后的加法结果
    public static double add(double val1, double val2) {
        return new BigDecimal(val1).add(new BigDecimal(val2)).doubleValue();
    }
}
