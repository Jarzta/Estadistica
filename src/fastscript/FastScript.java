package fastscript;

import static fastscript.Estimable.*;
import static fastscript.TDF.cols;

/**
 *
 * @author pc
 */
public class FastScript {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//                             Ejemplo 1 (comentado)
//        String data = "0,5	0,5	10,5	0,8	2,3	0,7\n"
//                + "3,6	5,0	10,9	6,3	2,4	1,2\n"
//                + "3,8	5,1	13,1	7,4	10,6	10,5\n"
//                + "4,6	5,2	13,2	8,9	2,0	1,0\n"
//                + "4,9	10,3	13,3	10,2	3,5	10,8\n"
//                + "1,7	10,4	1,7	2,0	2,1	1,5\n"
//                + "13,3	11,5	11,6	11,8	11,6	11,4\n"
//                + "12,3	12,3	12,4	12,5	12,6	0,5";
//        System.out.println(data);
//        data = data.replaceAll("\n", "\t");
//        data = data.replaceAll(",", ".");

//        TDF tdf = new TDF(data, "\t", 2);
//        System.out.println(Arrays.deepToString(tdf.getClassesIntelvals()));
//        System.out.println(Arrays.toString(tdf.getMarcas()));
//        System.out.println(Arrays.toString(tdf.getData()));
//        System.out.println(tdf.toStringR(tdf.getClassesIntelvals()));
//        System.out.println("Marca de clase (Mi)");
//        System.out.println(tdf.toStringR(tdf.getMarks()));
//        System.out.println("Frecuencia absoluta simple");
//        System.out.println(tdf.toStringR(tdf.getFrecuenciaAbsSimple()));
//        System.out.println("Frecuencia relativa simple");
//        System.out.println(tdf.toStringR(tdf.getFrecuenciaRelSimple()));
//        System.out.println("Frecuencia relativa acumulada");
//        System.out.println(tdf.toStringR(tdf.getFrecuenciaRelAcumulada()));
//        System.out.println("Frecuencia absoluta acumulada");
//        System.out.println(tdf.toStringR(tdf.getFrecuenciaAbsAcumulada()));
//        System.out.println("--------------------------------------------");
//        tdf.showTable(2, false);
//        tdf.showTable(2, false, cols(1, 2, 0));
//        tdf.showTable(2, cols(1, 2, 0));
//        tdf.showTable(2);

//                          Ejemplo 2
        TDF tdf1 = new TDF(TDF.toData("1,3,5,7", ","), TDF.toDataInt("1,2,8", ","), 2, true);
        tdf1.showTable();
        tdf1.showTable(2, false);
        tdf1.showTable(2, false, cols(1, 2, 0));
        tdf1.showTable(2, cols(1, 2, 0));
        tdf1.showSummaryFull();

        // muestra la tabla y el resultado de todas la operaciones
//        tdf1.show();
        // muestra la tabla y el resultado de todas la operaciones con el paso a paso
//        tdf1.showAll();
    }

}
