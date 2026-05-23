import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcesadorOptimizado {

    public static List<Cliente> cargarClientesSinJsonPesado(String ruta) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta), 1024 * 1024)) {
            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {
                // Se carga el cliente, pero se descarta jsonData porque no participa en la campaña.
                Cliente cliente = ProcesadorMalo.convertirLineaACliente(linea, false);
                if (cliente != null) {
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo optimizado: " + e.getMessage());
        }

        return clientes;
    }

    public static ResultadoProcesamiento procesarEnMemoriaConHashMap(List<Cliente> clientes) {
        HashMap<String, List<Cliente>> campanias = new HashMap<>();
        long inicio = System.currentTimeMillis();

        for (Cliente cliente : clientes) {
            String tipo = CampaniaUtil.determinarCampania(cliente);

            // OPTIMIZACIÓN PRINCIPAL DE LA TAREA:
            // HashMap evita recorrer linealmente todas las campañas por cada cliente.
            campanias.computeIfAbsent(tipo, clave -> new ArrayList<>()).add(cliente);
        }

        long fin = System.currentTimeMillis();
        ResultadoProcesamiento resultado = ResultadoProcesamiento.desdeHashMapListas(campanias, fin - inicio, clientes.size());
        resultado.imprimirResumen("Resumen de campañas - HashMap en memoria", 20);
        return resultado;
    }

    public static ResultadoProcesamiento procesarDesdeArchivoStreaming(String ruta) {
        HashMap<String, Integer> campanias = new HashMap<>();
        long totalClientes = 0;
        long inicio = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta), 1024 * 1024)) {
            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {
                // Modo más eficiente en memoria:
                // lee línea por línea, ignora jsonData y solo acumula conteos por campaña.
                Cliente cliente = ProcesadorMalo.convertirLineaACliente(linea, false);
                if (cliente == null) {
                    continue;
                }

                String tipo = CampaniaUtil.determinarCampania(cliente);
                campanias.merge(tipo, 1, Integer::sum);
                totalClientes++;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }

        long fin = System.currentTimeMillis();
        ResultadoProcesamiento resultado = new ResultadoProcesamiento(campanias, fin - inicio, totalClientes);
        resultado.imprimirResumen("Resumen de campañas - streaming optimizado", 20);
        return resultado;
    }
}
