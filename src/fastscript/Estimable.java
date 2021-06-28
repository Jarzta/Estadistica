package fastscript;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author pc
 */
public class Estimable {

    public static boolean FULL = false;
    public static int PRESICIÓN = 2;

    public static Double promedioTDF(Double[] Ci, Integer[] fi, int n) {
        Double Cixfi, promedio, sum = 0.0;
        String result = "", f = "", value;
        int lastIndex = Ci.length - 1;
        for (int i = 0; i < Ci.length - 1; i++) {
            Cixfi = Ci[i] * fi[i];
            value = getNumber(Ci[i], PRESICIÓN) + "";
            result += "(" + value + "*" + fi[i] + ") + ";
            f += Cixfi + " + ";
            sum += Cixfi;
        }

        Cixfi = Ci[lastIndex] * fi[lastIndex];
        value = getNumber(Ci[lastIndex], PRESICIÓN) + "";
        result += "(" + value + "*" + fi[lastIndex] + ")";
        f += Cixfi + "";
        sum += Cixfi;

        promedio = sum / n;

        if (FULL) {
            System.out.println(result);
            System.out.println(f);
            System.out.println(sum);
        }
        return promedio;
    }

    public static Double varianzaTDF(Double[] mediaAritmetica, Integer[] fi, Double promedio, int n, int precision) {
        return coeficienteTDF(mediaAritmetica, promedio, 2, fi, n, precision);
    }

    public static Double desviasiónEstándar(Double s2) {
        Double s = Math.sqrt(s2);
        return s;
    }

    public static Double coeficienteTDF(Double[] Ci, Double promedio, int exponente, Integer[] fi, int n, int presición, Double... aditional) {
        Double mixfi, sum = 0.0;
        String result = "", f = "", value, avgStr, aditionalStr = "*";
        avgStr = getNumber(promedio, presición) + "";
        int lastIndex = Ci.length - 1;
        for (int i = 0; i < Ci.length - 1; i++) {
            mixfi = Math.pow(Ci[i] - promedio, exponente) * fi[i];
            value = getNumber(Ci[i], presición) + "";
            result += "(" + value + " - " + avgStr + ")^" + exponente + "*" + fi[i] + "+ ";
            f += getNumber(mixfi, presición) + " + ";
            sum += mixfi;
        }
        mixfi = Math.pow(Ci[lastIndex] - promedio, exponente) * fi[lastIndex];
        value = getNumber(Ci[lastIndex], presición) + "";
        result += "(" + value + " - " + avgStr + ")^" + exponente + "*" + fi[lastIndex];
        f += getNumber(mixfi, presición) + "";
        sum += mixfi;

        if (FULL) {
            System.out.println(result);
            System.out.println(f);
            aditionalStr = aditional.length == 0?"/" + n:"/(" + n+"*"+getNumber(aditional[0], presición)+")";
            System.out.println(getNumber(sum, presición) + aditionalStr);
        }
        if (aditional.length == 0) {
            aditional = new Double[]{1.0};
        }
        return sum / (n * aditional[0]);
    }

    public static Double coeficienteDeFisher(Double[] Ci, Double promedio, Integer[] fi, int n, int presición) {
        boolean aux = FULL;
        FULL = false;
        Double s2 = varianzaTDF(Ci, fi, promedio, n, presición), s, s3;
        s = desviasiónEstándar(s2);
        s3 = Math.pow(s, 3);
        FULL = aux;
        return coeficienteTDF(Ci, promedio, 3, fi, n, presición, s3);
    }

    public static Double apuntamientoDeCurtosis(Double[] Ci, Double promedio, Integer[] fi, int n, int presición) {
        boolean aux = FULL;
        FULL = false;
        Double s2 = varianzaTDF(Ci, fi, promedio, n, presición), s, s3;
        s = desviasiónEstándar(s2);
        s3 = Math.pow(s, 4);
        FULL = aux;
        return coeficienteTDF(Ci, promedio, 4, fi, n, presición, s3);
    }

    public static double mean(double li, double ls) {
        return (li + ls) * 0.5;
    }

    public static double getNumber(double num, int precision) {
        BigDecimal bigd = new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP);
        return bigd.doubleValue();
    }
}
