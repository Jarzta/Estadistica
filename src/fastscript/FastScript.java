package fastscript;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 *
 * @author pc
 */
public class FastScript {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String data = "0,5	0,5	10,5	0,8	2,3	0,7\n"
                + "3,6	5,0	10,9	6,3	2,4	1,2\n"
                + "3,8	5,1	13,1	7,4	10,6	10,5\n"
                + "4,6	5,2	13,2	8,9	2,0	1,0\n"
                + "4,9	10,3	13,3	10,2	3,5	10,8\n"
                + "1,7	10,4	1,7	2,0	2,1	1,5\n"
                + "13,3	11,5	11,6	11,8	11,6	11,4\n"
                + "12,3	12,3	12,4	12,5	12,6	0,5";
//        System.out.println(data);
        data = data.replaceAll("\n", "\t");
        data = data.replaceAll(",", ".");
//        data = data.replaceAll(";", "\n");

//        TDF tdf = new TDF(data, "\t", 2);
        TDF tdf = new TDF(true, data, "\t", 2);

//        tdf.setRoundable(true);
//        System.out.println(Arrays.deepToString(tdf.getClassesIntelvals()));
//        System.out.println(Arrays.toString(tdf.getMarcas()));
//        System.out.println(Arrays.toString(tdf.getData()));
        System.out.println(tdf.toStringR(tdf.getClassesIntelvals()));
        System.out.println("Marca de clase (Mi)");
        System.out.println(tdf.toStringR(tdf.getMarks()));
        System.out.println("Frecuencia absoluta simple");
        System.out.println(tdf.toStringR(tdf.getFrecuenciaAbsSimple()));
        System.out.println("Frecuencia relativa simple");
        System.out.println(tdf.toStringR(tdf.getFrecuenciaRelSimple()));
        System.out.println("Frecuencia relativa acumulada");
        System.out.println(tdf.toStringR(tdf.getFrecuenciaRelAcumulada()));
        System.out.println("Frecuencia absoluta acumulada");
        System.out.println(tdf.toStringR(tdf.getFrecuenciaAbsAcumulada()));
        System.out.println("--------------------------------------------");
//        tdf.showTable(2, false);
        tdf.showTable(2);
        tdf.showTable(2, 1, 2, 0, 3);

//        tdf.showSummary2();
//        Double[] mediaAritmetica = {23.49, 25.47, 27.45, 29.43, 31.41, 33.39, 35.37};
//        Integer[] fi = {
//            2,
//            2,
//            6,
//            17,
//            14,
//            8,
//            1
//        };
//
//        mediaAritmetica = new Double[]{
//            24.0,
//            28.0,
//            32.0,
//            36.0,
//            40.0,
//            44.0,
//            48.0
//        };
//        fi = new Integer[]{
//            2,
//            1,
//            8,
//            12,
//            10,
//            6,
//            1
//        };
//        int n = 40;
//        int precision = 4;
//        Double avg = promedioTDF(mediaAritmetica, fi, n);
//        avg = getNumber(avg, precision);
//        System.out.println(":)))))--------: " + avg);
//        varianzaTDF(mediaAritmetica, fi, avg, n, precision);
    }

    public static Double promedioTDF(Double[] mediaAritmetica, Integer[] fi, int n) {
        Double mixfi, promedio, sum = 0.0;
        String result = "", f = "";
        for (int i = 0; i < mediaAritmetica.length; i++) {
            mixfi = mediaAritmetica[i] * fi[i];
            result += "(" + mediaAritmetica[i] + "*" + fi[i] + ") + ";
            f += mixfi + " + ";
            sum += mixfi;
        }
        promedio = sum / n;
        System.out.println("sum(mi * fi)\n" + result);
        System.out.println(f);
        System.out.println(sum);
        System.out.println("Promedio: " + promedio);
        return promedio;
    }

    public static void varianzaTDF(Double[] mediaAritmetica, Integer[] fi, Double promedio, int n, int precision) {
        Double mixfi, sum = 0.0;
        String result = "", f = "";
        for (int i = 0; i < mediaAritmetica.length; i++) {
            mixfi = getNumber(Math.pow(mediaAritmetica[i] - promedio, 2) * fi[i], precision);
            result += "(" + mediaAritmetica[i] + " - " + promedio + ")^2" + "*" + fi[i] + "+ ";
            f += mixfi + " + ";
            sum += mixfi;
        }
        System.out.println("sum[(mi - sum(mi*fi))^2 * fi]\n" + result);
        System.out.println(f);
        System.out.println(sum);
        System.out.println("Varianza: " + (sum / n));
    }

    public static double mean(double li, double ls) {
        return (li + ls) * 0.5;
    }

    public static double getNumber(double num, int precision) {
        BigDecimal bigd = new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP);
        return bigd.doubleValue();
    }
}
