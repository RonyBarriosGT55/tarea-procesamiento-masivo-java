import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorMalo {

    public static List<Cliente> cargarTodosLosClientes(String ruta) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta), 1024 * 1024)) {
            String linea;
            reader.readLine();

            while ((linea = reader.readLine()) != null) {
                Cliente cliente = convertirLineaACliente(linea, true);
                if (cliente != null) {
                    // MALA PRÁCTICA INTENCIONAL:
                    // Se cargan todos los clientes en memoria, incluyendo el JSON pesado.
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }

        return clientes;
    }

    public static Cliente convertirLineaACliente(String linea, boolean incluirJson) {
        // IMPORTANTE:
        // No usar linea.split(",", 8) para el modo optimizado.
        // split copia también el campo jsonData gigante aunque luego no se use.
        // Este parser localiza solo las primeras 7 comas y permite ignorar el JSON pesado.
        int c1 = linea.indexOf(',');
        int c2 = linea.indexOf(',', c1 + 1);
        int c3 = linea.indexOf(',', c2 + 1);
        int c4 = linea.indexOf(',', c3 + 1);
        int c5 = linea.indexOf(',', c4 + 1);
        int c6 = linea.indexOf(',', c5 + 1);
        int c7 = linea.indexOf(',', c6 + 1);

        if (c1 < 0 || c2 < 0 || c3 < 0 || c4 < 0 || c5 < 0 || c6 < 0 || c7 < 0) {
            return null;
        }

        try {
            int id = Integer.parseInt(linea.substring(0, c1));
            String nombre = linea.substring(c1 + 1, c2);
            double ingreso = Double.parseDouble(linea.substring(c2 + 1, c3));
            String segmento = normalizarTextoCorto(linea.substring(c3 + 1, c4));
            String region = normalizarTextoCorto(linea.substring(c4 + 1, c5));
            int score = Integer.parseInt(linea.substring(c5 + 1, c6));
            double deuda = Double.parseDouble(linea.substring(c6 + 1, c7));
            String jsonData = incluirJson ? linea.substring(c7 + 1) : "";

            return new Cliente(id, nombre, ingreso, segmento, region, score, deuda, jsonData);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String normalizarTextoCorto(String valor) {
        // Evita mantener millones de objetos String duplicados para segmento y región.
        // No cambia el valor lógico del dato; solo reutiliza literales conocidos.
        switch (valor) {
            case "PREPAGO": return "PREPAGO";
            case "POSTPAGO": return "POSTPAGO";
            case "RESIDENCIAL": return "RESIDENCIAL";
            case "PYME": return "PYME";
            case "CORPORATIVO": return "CORPORATIVO";
            case "NORTE": return "NORTE";
            case "SUR": return "SUR";
            case "ORIENTE": return "ORIENTE";
            case "OCCIDENTE": return "OCCIDENTE";
            case "CENTRO": return "CENTRO";
            default: return valor;
        }
    }
}
