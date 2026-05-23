public class CampaniaUtil {

    public static String determinarCampania(Cliente cliente) {
        String nivelIngreso;
        if (cliente.getIngreso() >= 25000) {
            nivelIngreso = "INGRESO_ALTO";
        } else if (cliente.getIngreso() >= 15000) {
            nivelIngreso = "INGRESO_MEDIO";
        } else if (cliente.getIngreso() >= 10000) {
            nivelIngreso = "INGRESO_BAJO";
        } else {
            nivelIngreso = "NO_APLICA";
        }

        String nivelScore;
        if (cliente.getScore() >= 800) {
            nivelScore = "SCORE_EXCELENTE";
        } else if (cliente.getScore() >= 600) {
            nivelScore = "SCORE_BUENO";
        } else if (cliente.getScore() >= 400) {
            nivelScore = "SCORE_REGULAR";
        } else {
            nivelScore = "SCORE_RIESGO";
        }

        String nivelDeuda;
        if (cliente.getDeuda() >= 7000) {
            nivelDeuda = "DEUDA_ALTA";
        } else if (cliente.getDeuda() >= 3000) {
            nivelDeuda = "DEUDA_MEDIA";
        } else {
            nivelDeuda = "DEUDA_BAJA";
        }

        return cliente.getSegmento() + "_" +
                cliente.getRegion() + "_" +
                nivelIngreso + "_" +
                nivelScore + "_" +
                nivelDeuda;
    }
}
