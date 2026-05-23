import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultadoProcesamiento {

    private Map<String, Integer> conteoCampanias;
    private long tiempoMs;
    private long totalClientes;

    public ResultadoProcesamiento(Map<String, Integer> conteoCampanias, long tiempoMs, long totalClientes) {
        this.conteoCampanias = conteoCampanias;
        this.tiempoMs = tiempoMs;
        this.totalClientes = totalClientes;
    }

    public static ResultadoProcesamiento desdeListas(List<String> tiposCampania, List<List<Cliente>> grupos, long tiempoMs, long totalClientes) {
        Map<String, Integer> conteo = new LinkedHashMap<>();

        for (int i = 0; i < tiposCampania.size(); i++) {
            conteo.put(tiposCampania.get(i), grupos.get(i).size());
        }

        return new ResultadoProcesamiento(conteo, tiempoMs, totalClientes);
    }

    public static ResultadoProcesamiento desdeHashMapListas(Map<String, List<Cliente>> campanias, long tiempoMs, long totalClientes) {
        Map<String, Integer> conteo = new LinkedHashMap<>();

        for (Map.Entry<String, List<Cliente>> entry : campanias.entrySet()) {
            conteo.put(entry.getKey(), entry.getValue().size());
        }

        return new ResultadoProcesamiento(conteo, tiempoMs, totalClientes);
    }

    public void imprimirResumen(String titulo, int limiteFilas) {
        System.out.println("\n" + titulo);
        System.out.println("Clientes procesados: " + totalClientes);
        System.out.println("Campañas diferentes generadas: " + conteoCampanias.size());
        System.out.println("Tiempo de procesamiento: " + tiempoMs + " ms");

        System.out.println("\nTop de campañas con más registros:");
        List<Map.Entry<String, Integer>> registros = new ArrayList<>(conteoCampanias.entrySet());
        registros.sort(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)));

        int contador = 0;
        for (Map.Entry<String, Integer> entry : registros) {
            if (contador >= limiteFilas) {
                break;
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
            contador++;
        }
    }

    public int getCantidadCampanias() {
        return conteoCampanias.size();
    }

    public long getTiempoMs() {
        return tiempoMs;
    }

    public long getTotalClientes() {
        return totalClientes;
    }
}
