package fastscript;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author pc
 */
public class TDF {

    private Double[] data;
    private Double[] uniqueSet;

    private Double[][] clases;
    private Double[] marcasClase;
    private Integer[] frecuenciaAbsSimple;
    private Integer[] frecuenciaAbsAcumulada;
    private Double[] frecuenciaRelSimple;
    private Double[] frecuenciaRelAcumulada;
    private boolean roundable;
    private int precision;

    public TDF(String str, String separator, int precision) {
        this(false, str, separator, precision);
    }

    public TDF(boolean roundable, String str, String separator, int precision) {
        this.precision = precision;
        this.roundable = roundable;
        this.data = toData(str, separator);
        this.clases = calculateClassesIntelvals();
        this.marcasClase = calculateMarks();
        this.uniqueSet = calculateUniqueSet();
        this.frecuenciaAbsSimple = countForIntervals();
        this.frecuenciaAbsAcumulada = calculateFi();
        this.frecuenciaRelSimple = calculatefri();
        this.frecuenciaRelAcumulada = calculateFri();
        this.names = "Ci, Mi, fi, fri, Fi, Fri".replaceAll(" ", "").split(",");
//        this.columns = new Object[][]{clases, marcasClase, frecuenciaAbsSimple, frecuenciaRelSimple, frecuenciaAbsAcumulada, frecuenciaRelAcumulada};
        this.columns = new Object[][]{toStringMark().split("\n"), marcasClase, frecuenciaAbsSimple, frecuenciaRelSimple, frecuenciaAbsAcumulada, frecuenciaRelAcumulada};
    }

    public final Double[] toData(String str, String separator) {
        String[] data = str.split(separator);
        Double[] datadouble = new Double[data.length];
        for (int i = 0; i < data.length; i++) {
            String string = data[i];
            datadouble[i] = new Double(string);
//            BigDecimal bigd = new BigDecimal(datadouble[i]).setScale(precision, RoundingMode.HALF_UP);
//            datadouble[i] = bigd.doubleValue();
        }
        Arrays.sort(datadouble);
        return datadouble;
    }

    public final Double[] calculateUniqueSet() {
        ArrayList<Double> arr = new ArrayList<>();
        for (double dou : data) {
            if (!arr.contains(dou)) {
                arr.add(dou);
            }
        }
        return arr.toArray(new Double[arr.size()]);
    }

    public final Double[][] calculateClassesIntelvals() {
        int rows = getClasesCount();
        Float f = new Float(getAmplitud());
        Double[][] intervals = new Double[rows][];
        double amplitud;
        if (isRoundable()) {
            amplitud = Math.round(f);
        } else {
//            amplitud = f;
            BigDecimal bigd = new BigDecimal(f);
            bigd.setScale(precision, RoundingMode.HALF_UP);
            amplitud = bigd.doubleValue();
        }

        double limI = getMin(), limS = limI + amplitud;
        double max = getMax();
        System.out.println(max);
        for (int i = 0; i < rows; i++) {
//            limI = getNumber(limI);
//            limS = getNumber(limS);
            intervals[i] = new Double[]{limI, limS};
//            System.out.println(Arrays.toString(intervals[i]));
            limI = limS;
            limS += amplitud;
        }
        return intervals;
    }

    public final Double[] calculateMarks() {
        Double[] marks = new Double[clases.length];
        for (int i = 0; i < clases.length; i++) {
            Double[] clas = clases[i];
            marks[i] = (clas[0] + clas[1]) / 2;
        }
        return marks;
    }

    public double getMax() {
        return data[data.length - 1];
    }
// 439

    public double getMin() {
        return data[0];
    }

    public double getRange() {
        return getMax() - getMin();
    }

    public double getClasesNumber() {
        return 1 + 3.33 * Math.log10(data.length);
    }

    public int getClasesCount() {
        return (int) Math.round(getClasesNumber());
    }

    public Double getAmplitud() {
        return getRange() / getClasesNumber();
    }

    public Double[][] getClassesIntelvals() {
        return clases;
    }

    public Double[] getMarks() {
        return marcasClase;
    }

    public void setRoundable(boolean r) {
        roundable = r;
    }

    public boolean isRoundable() {
        return roundable;
    }

    public double getNumber(double num) {
        BigDecimal bigd = new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP);
        return bigd.doubleValue();
    }

    public int count(double d) {
        int count = 0;
        for (double dou : data) {
            if (dou == d) {
                count++;
            }
        }
        return count;
    }

    public final Double[] calculatefri() {
        Double[] dou = new Double[frecuenciaAbsSimple.length];
        for (int i = 0; i < dou.length; i++) {
            dou[i] = frecuenciaAbsSimple[i] / new Double(data.length);
        }
        return dou;
    }

    public final Double[] calculateFri() {
        Double[] dou = new Double[frecuenciaAbsAcumulada.length];
        for (int i = 0; i < dou.length; i++) {
            dou[i] = frecuenciaAbsAcumulada[i] / new Double(data.length);
        }
        return dou;
    }

    public final Integer[] calculateFi() {
        Integer[] dou = new Integer[frecuenciaAbsSimple.length];
        int aux = 0;
        for (int i = 0; i < dou.length; i++) {
            dou[i] = frecuenciaAbsSimple[i] + aux;
            aux = dou[i];
        }
        return dou;
    }

    public final Integer[] countForIntervals() {
        Integer[] counts = new Integer[clases.length];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = countForInterval(clases[i]);
        }
        return counts;
    }

    public int countForInterval(Double[] interval) {
        int count = 0;
        for (Double dt : data) {
            if (dt >= interval[0] && dt < interval[1]) {
                count++;
            }
        }
        return count;
    }

    public Integer[] countAll() {
        Integer[] counts = new Integer[uniqueSet.length];
        for (int i = 0; i < uniqueSet.length; i++) {
            Double double1 = uniqueSet[i];
            counts[i] = count(double1);
        }
        return counts;
    }

    public void showSummary() {
        System.out.println(Arrays.toString(uniqueSet));
        System.out.println(Arrays.toString(countAll()));
    }

    public void showSummary2() {
        System.out.println(Arrays.deepToString(clases));
        System.out.println(Arrays.toString(countForIntervals()));
    }

    public void showTable(Object[][] columns, int presición) {
        String string = "";
        for (int i = 0; i < columns.length; i++) {
            Object[] row = getRow(i, columns, true);
            string += toString(row, "\t", presición, "\n");
        }
        System.out.println(string);
    }

    private String[] names;

    public void showTable(Object[][] columns, String[] names, int presición, boolean horizontal) {
        String string = "", separador = "\t", lineEnd = "\n", 
                header = "";
        
        if(names[0].equals(this.names[0])) {
            names[0]=names[0]+"\t";
        }else if(!names[names.length-1].equals(this.names[0])){
            for(int i = 0; i<names.length; i++){
                if(names[i].equals(this.names[0])){
                    names[i]= names[i]+"\t";
                }
            }
        }
            

        for (int i = 0; i < columns.length; i++) {
            if (!horizontal) {
                string += names[i] + separador;
            }

            Object[] row = getRow(i, columns, horizontal);
            string += toString(row, separador, presición, lineEnd);
        }
        if (horizontal) {
            for (String name : names) {
                header += name + separador;
            }
            header += lineEnd;
        }
        System.out.println(header+string);
    }

    public void showTable(int presición, boolean horizontal, Integer... columns) {
        if (columns == null) {
            columns = getRange(0, this.columns.length);
        } else if (columns.length == 0) {
            columns = getRange(0, this.columns.length);
        }
        showTable(getSlicing(columns), getSlicing(columns, names), presición, horizontal);
    }

    public void showTable(int presición, Integer... columns) {
        showTable(presición, true, columns);
    }

    public void resolvePresición(Object[] row, int pres) {
        for (Object obj : row) {
            if (obj instanceof Double) {
                new BigDecimal(new Double(obj + "")).setScale(pres, RoundingMode.HALF_UP);
            }
        }
    }

    public String toString(Object[] row, String separator, String... end) {
        String str = "";
        for (Object obj : row) {
            str += obj + separator;
        }
        return str += end[0] == null ? "" : end[0];
    }

    public String toString(Object[] row, String separator, int presición, String... end) {
        String str = "";
        for (Object obj : row) {
            if (obj instanceof Double) {
                obj = new BigDecimal(new Double(obj + "")).setScale(presición, RoundingMode.HALF_UP);
            }
            str += obj + separator;
        }
        return str += end[0] == null ? "" : end[0];
    }

    private Integer[] numColumns;
    private Object[][] columns;
//    = {clases, marcasClase, frecuenciaAbsSimple, frecuenciaRelSimple, frecuenciaAbsAcumulada, frecuenciaRelAcumulada};

    public Object[] getRow(int row, Object[][] columns) {
        Object[] tuple = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) {
            tuple[i] = columns[i][row];
        }
        return tuple;
    }

    public Object[] getRow(int row, Object[][] columns, boolean horizontal) {
        Object[] tuple = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) {
            if (horizontal) {
                tuple[i] = columns[i][row];
            } else {
                tuple[i] = columns[row][i];
            }
        }
        return tuple;
    }

    public Integer[] not(Integer... nums) {
        if (null == nums) {
            return getRange(0, columns.length);
        }
        ArrayList<Integer> not = new ArrayList<>(Arrays.asList(nums));
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            if (!not.contains(i)) {
                arr.add(i);
            }
        }
        not(null);
        return arr.toArray(new Integer[arr.size()]);
    }

    public Integer[] getRange(int s, int e) {
        Integer[] arr = new Integer[e - s];
        int c = 0;
        for (int i = s; i < e; i++) {
            arr[c] = i;
            c++;
        }
        return arr;
    }

    public Object[][] getSlicing(Integer[] numColumns) {
        int c = 0;
        Object[][] shot = new Object[numColumns.length][];
        for (Integer i : numColumns) {
            shot[c] = columns[i];
            c++;
        }
        return shot;
    }
    
    public String[] getSlicing(Integer[] numColumns, String[] header) {
        int c = 0;
        String[] shot = new String[numColumns.length];
        for (Integer i : numColumns) {
            shot[c] = header[i];
            c++;
        }
        return shot;
    }

    public static final int TABLE = 1;

//    public void showFormat(int format, int precisión) {
//        // TABLE, NONE, ...
//        switch (format) {
//            case TABLE:
//                showTable();
//                break;
//        }
//    }
    public Double[] getData() {
        return data;
    }

    public Double[] getUniqueSet() {
        return uniqueSet;
    }

    public Integer[] getFrecuenciaAbsSimple() {
        return frecuenciaAbsSimple;
    }

    public Integer[] getFrecuenciaAbsAcumulada() {
        return frecuenciaAbsAcumulada;
    }

    public Double[] getFrecuenciaRelSimple() {
        return frecuenciaRelSimple;
    }

    public Double[] getFrecuenciaRelAcumulada() {
        return frecuenciaRelAcumulada;
    }

    public String toStringMark() {
        String str = "";
        for (Double[] i : clases) {
            str += "[" + i[0] + " - " + i[1] + ")\n";
        }
        return str;
    }

    private String toStringR(String r) {
        r = r.replaceAll(" ", "");
        if (r.startsWith("[[")) {
            r = r.substring(1, r.length() - 2) + ")\n";
            r = r.replaceAll("],", ")\n");
            r = r.replaceAll(",", " - ");
        } else {
            r = r.substring(1, r.length() - 1);
            r = r.replaceAll(",", "\n");
        }
        return r;
    }

    public String toStringR(Object[] str) {
        return toStringR(Arrays.deepToString(str));
    }

}
