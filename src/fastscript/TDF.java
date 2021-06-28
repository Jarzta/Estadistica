package fastscript;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import static fastscript.Estimable.*;
import java.util.function.Supplier;

/**
 *
 * @author pc
 */
public class TDF {

    private Double[] data;
    private Integer n;
    private Double[] uniqueSet;

    private Double[][] clases;
    private Double[] marcasClase;
    private Integer[] frecuenciaAbsSimple;
    private Integer[] frecuenciaAbsAcumulada;
    private Double[] frecuenciaRelSimple;
    private Double[] frecuenciaRelAcumulada;
    private int presición;

//    public TDF(String str, String separator, int precision) {
//        this(false, str, separator, precision);
//    }
    public TDF(String str, String separator, int presición) {
        this.data = toData(str, separator);
        init(presición, data.length, data, () -> {
            return calculateClassesIntelvals();
        }, () -> {
            return countForIntervals();
        });
    }

    public TDF(Double[] ci, Integer[] fi, int precision) {
        this(ci, fi, precision, false);
    }

    public TDF(Double[] ci, Integer[] fi, int presición, boolean unique) {
        init(presición, sum(fi), new Double[0], () -> {
            return resolveClasses(ci, unique);
        }, () -> {
            return fi;
        });
    }

    private void init(int presición, int n, Double[] data, Supplier<Double[][]> ci, Supplier<Integer[]> fi) {
        this.presición = presición;
        this.data = data;
        this.n = n;
        this.clases = ci.get();
        this.marcasClase = calculateMarks();
        this.uniqueSet = calculateUniqueSet();
        this.frecuenciaAbsSimple = fi.get();
        this.frecuenciaAbsAcumulada = calculateFi();
        this.frecuenciaRelSimple = calculatefri();
        this.frecuenciaRelAcumulada = calculateFri();
        this.names = "Ci, Mi, fi, fri, Fi, Fri".replaceAll(" ", "").split(",");
        this.columns = new Object[][]{toStringMark().split("\n"), marcasClase, frecuenciaAbsSimple, frecuenciaRelSimple, frecuenciaAbsAcumulada, frecuenciaRelAcumulada};
        calculateEstimable();
    }

    public static final Double[] toData(String str, String separator) {
        String[] data = str.split(separator);
        Double[] datadouble = new Double[data.length];
        for (int i = 0; i < data.length; i++) {
            String string = data[i];
            datadouble[i] = new Double(string);
        }
        Arrays.sort(datadouble);
        return datadouble;
    }

    public static final Integer[] toDataInt(String str, String separator) {
        String[] data = str.split(separator);
        Integer[] dataIntegers = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            String string = data[i];
            dataIntegers[i] = new Integer(string);
        }
        Arrays.sort(dataIntegers);
        return dataIntegers;
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
        double amplitud = Math.round(f);
        double limI = getMin(), limS = limI + amplitud;
        double max = getMax();
        System.out.println(max);
        for (int i = 0; i < rows; i++) {
            intervals[i] = new Double[]{limI, limS};
            limI = limS;
            limS += amplitud;
        }
        return intervals;
    }

    public static final Double[][] resolveClasses(Double[] classes, boolean unique) {
        Double[][] setClass;
        if (unique) {
            // 1,3,5,7
            setClass = new Double[classes.length - 1][];
            setClass[0] = new Double[]{classes[0], classes[1]};
            for (int i = 2; i < classes.length; i++) {
                setClass[i - 1] = new Double[]{classes[i - 1], classes[i]};
            }
//            System.out.println("resol: "+Arrays.deepToString(setClass));
            return setClass;
        }
        int len = classes.length / 2;
        setClass = new Double[len][];
        int j = 1;
        for (int i = 0; i < len; i++) {
            setClass[i] = new Double[]{classes[i], classes[j]};
            j++;
        }
        return setClass;
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

    public static Integer count(double d, Double[] data) {
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
            dou[i] = frecuenciaAbsSimple[i] / new Double(n);
        }
        return dou;
    }

    public final Double[] calculateFri() {
        Double[] dou = new Double[frecuenciaAbsAcumulada.length];
        for (int i = 0; i < dou.length; i++) {
            dou[i] = frecuenciaAbsAcumulada[i] / new Double(n);
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

    public static Integer[] countAll(Double[] data, Double[] uniqueSet) {
        Integer[] counts = new Integer[uniqueSet.length];
        for (int i = 0; i < uniqueSet.length; i++) {
            Double double1 = uniqueSet[i];
            counts[i] = count(double1, data);
        }
        return counts;
    }

    public static Integer sum(Integer[] fi) {
        Integer sum = 0;
        for (Integer num : fi) {
            sum += num;
        }
        return sum;
    }

    public void showSummary() {
        System.out.println("Promedio: " + Estimable.getNumber(avg, presición));
        System.out.println("Varianza: " + Estimable.getNumber(s2, presición));
        System.out.println("Desviación Estándar: " + Estimable.getNumber(s, presición));
        System.out.println("Asimetría de Fisher: " + Estimable.getNumber(caf, presición));
        System.out.println("Apuntamiento de Curtosis: " + Estimable.getNumber(ac, presición));
//        System.out.println(Arrays.toString(uniqueSet));
//        System.out.println(Arrays.toString(countAll(data, uniqueSet)));
    }

    public void showSummaryFull() {
        Estimable.FULL = true;
        System.out.println("---------------------------------PROMEDIO (Media Aritmetica)---------------------------------");
        avg = promedioTDF(marcasClase, frecuenciaAbsSimple, n);
        System.out.println("Promedio: " + Estimable.getNumber(avg, presición));

        System.out.println("---------------------------------VARIANZA---------------------------------");
        s2 = varianzaTDF(marcasClase, frecuenciaAbsSimple, avg, n, presición);
        System.out.println("Varianza: " + Estimable.getNumber(s2, presición));

        System.out.println("---------------------------------DESVIACIÓN ESTÁNDAR---------------------------------");
        s = desviasiónEstándar(s2);
        System.out.println("Desviación Estándar: " + Estimable.getNumber(s, presición));

        System.out.println("---------------------------------ASIMETRÍA DE FISHER---------------------------------");
        caf = coeficienteDeFisher(marcasClase, avg, frecuenciaAbsSimple, n, presición);
        System.out.println("Asimetría de Fisher: " + Estimable.getNumber(caf, presición));

        System.out.println("---------------------------------APUNTAMIENTO DE CURTOSIS---------------------------------");
        ac = apuntamientoDeCurtosis(marcasClase, avg, frecuenciaAbsSimple, n, presición);
        System.out.println("Apuntamiento de Curtosis: " + Estimable.getNumber(ac, presición));
        Estimable.FULL = false;
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

        if (!horizontal) {
            for (int i = 0; i < columns.length; i++) {
                string += names[i] + separador;
                Object[] row = getRow(i, columns, horizontal);
                string += toString(row, separador, presición, lineEnd);
            }
        } else {
            if (names[0].equals(this.names[0])) {
                names[0] = names[0] + "\t";
            } else if (!names[names.length - 1].equals(this.names[0])) {
                for (int i = 0; i < names.length; i++) {
                    if (names[i].equals(this.names[0])) {
                        names[i] = names[i] + "\t";
                    }
                }
            }
            for (int i = 0; i < frecuenciaAbsSimple.length; i++) {
                Object[] row = getRow(i, columns, horizontal);
                string += toString(row, separador, presición, lineEnd);
            }
            for (String name : names) {
                header += name + separador;
            }
            header += lineEnd;
        }
        System.out.println(header + string);
    }

    public void showTable(int presición, boolean horizontal, Integer[] columns) {
        if (columns == null) {
            columns = getRange(0, horizontal ? this.columns.length : this.frecuenciaAbsSimple.length);
        } else if (columns.length == 0) {
            columns = getRange(0, horizontal ? this.columns.length : this.frecuenciaAbsSimple.length);
        }
        showTable(getSlicing(columns), getSlicing(columns, names), presición, horizontal);
    }

    public void showTable(int presición, boolean horizontal) {
        showTable(presición, horizontal, null);
    }

    public void showTable(Integer[] columns) {
        showTable(presición, true, columns);
    }

    public void showTable() {
        showTable(presición, true, null);
    }

    public void showTable(int presición) {
        showTable(presición, true, null);
    }

    public void showTable(int presición, Integer[] columns) {
        showTable(presición, true, columns);
    }

    public void show() {
        showTable();
        showSummary();
    }

    public void showAll() {
        showTable();
        showSummaryFull();
    }

    public static Integer[] cols(Integer... cols) {
        return cols;
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
//            System.out.println("objs: "+Arrays.toString(row));
        for (Object obj : row) {
            if (obj instanceof Double) {
//                System.out.println("oj: "+obj);
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
//        System.out.println("row: "+row);
//        System.out.println("columns.length: "+columns.length);
        Object[] tuple = new Object[columns.length];
        if (horizontal) {
//            System.out.println("---------------------- NEW ROW "+row);
            for (int i = 0; i < columns.length; i++) {
                tuple[i] = columns[i][row];
//                System.out.println("tuple[i]: "+tuple[i]);
            }
        } else {
            for (int i = 0; i < columns.length; i++) {
                tuple[i] = columns[row][i];
//                System.out.println("tuple[i]: "+tuple[i]);
            }
            System.arraycopy(columns[row], 0, tuple, 0, columns.length);
        }
//        System.out.println(Arrays.toString(tuple));
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
        String limI, limS;
        for (Double[] i : clases) {
            limI = Estimable.getNumber(i[0], presición) + "";
            limS = Estimable.getNumber(i[1], presición) + "";
            str += "[" + limI + " - " + limS + ")\n";
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

    private Double avg, s2, s, ac, caf;

    public Double mediaArimetica() {
        return avg;
    }

    public Double coeficienteDeAsimetriaDeFisher() {
        return caf;
    }

    public Double coeficienteDeApuntamientoDeCurtosis() {
        return ac;
    }

    public Double varianza() {
        return s2;
    }

    public Double desviaciónEstándar() {
        return s;
    }

    public final void calculateEstimable() {
        avg = promedioTDF(marcasClase, frecuenciaAbsSimple, n);
        s2 = varianzaTDF(marcasClase, frecuenciaAbsSimple, avg, n, presición);
        s = desviasiónEstándar(s2);
        caf = coeficienteDeFisher(marcasClase, avg, frecuenciaAbsSimple, n, presición);
        ac = apuntamientoDeCurtosis(marcasClase, avg, frecuenciaAbsSimple, n, presición);
    }

}
