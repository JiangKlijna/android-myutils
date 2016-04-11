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
    public static double multiply(double val1, double val2, int mc, double... more) {
        if (val1 == 0 || val2 == 0) return 0;
        BigDecimal bg1 = new BigDecimal(val1);
        BigDecimal bg2 = new BigDecimal(val2);
        BigDecimal result = new BigDecimal(0);

        if (more.length > 0) {
            for (int i = 0, len = more.length; i < len; i++) {
                if (i < len - 1) {
                    result = result.multiply(new BigDecimal(more[i]));
                } else {
                    result = result.multiply(new BigDecimal(more[i]), new MathContext(mc, RoundingMode.HALF_UP));
                }
            }
        } else {
            result = bg1.multiply(bg2, new MathContext(mc, RoundingMode.HALF_UP));
        }

        return result.doubleValue();
    }

    //返回科学计算后的除法结果
    public static double divide(double fz, double fm, int mc, double... more) {
        if (fz == 0 || fm == 0) return 0;
        BigDecimal bg1 = new BigDecimal(fz);
        BigDecimal bg2 = new BigDecimal(fm);
        BigDecimal result = new BigDecimal(0);

        if (more.length > 0) {
            for (int i = 0, len = more.length; i < len; i++) {
                if (i < len - 1) {
                    result = result.divide(new BigDecimal(more[i]));
                } else {
                    result = result.divide(new BigDecimal(more[i]), new MathContext(mc, RoundingMode.HALF_UP));
                }
            }
        } else {
            result = bg1.divide(bg2, new MathContext(mc, RoundingMode.HALF_UP));
        }

        return result.doubleValue();
    }

    //返回科学计算后的减法结果
    public static double subtract(double val1, double val2, int mc, double... more) {
        if (val2 == 0) return val1;
        BigDecimal bg1 = new BigDecimal(val1);
        BigDecimal bg2 = new BigDecimal(val2);
        BigDecimal result = new BigDecimal(0);

        if (more.length > 0) {
            for (int i = 0, len = more.length; i < len; i++) {
                if (i < len - 1) {
                    result = result.subtract(new BigDecimal(more[i]));
                } else {
                    result = result.subtract(new BigDecimal(more[i]), new MathContext(mc, RoundingMode.HALF_UP));
                }
            }
        } else {
            result = bg1.subtract(bg2, new MathContext(mc, RoundingMode.HALF_UP));
        }

        return result.doubleValue();
    }

    //返回科学计算后的加法结果
    public static double add(double val1, double val2, int mc, double... more) {
        BigDecimal bg1 = new BigDecimal(val1);
        BigDecimal bg2 = new BigDecimal(val2);
        BigDecimal result = new BigDecimal(0);

        if (more.length > 0) {
            for (int i = 0, len = more.length; i < len; i++) {
                if (i < len - 1) {
                    result = result.add(new BigDecimal(more[i]));
                } else {
                    result = result.add(new BigDecimal(more[i]), new MathContext(mc, RoundingMode.HALF_UP));
                }
            }
        } else {
            result = bg1.add(bg2, new MathContext(mc, RoundingMode.HALF_UP));
        }

        return result.doubleValue();
    }
}
