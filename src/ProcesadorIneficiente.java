import java.util.ArrayList;
import java.util.List;

public class ProcesadorIneficiente {

    public static ResultadoProcesamiento procesar(List<Cliente> clientes) {
        List<String> tiposCampania = new ArrayList<>();
        List<List<Cliente>> grupos = new ArrayList<>();

        long inicio = System.currentTimeMillis();

        for (Cliente cliente : clientes) {
            String tipo = CampaniaUtil.determinarCampania(cliente);
            int index = -1;

            // BÚSQUEDA LINEAL INEFICIENTE:
            // Por cada cliente se recorre la lista de campañas hasta encontrar coincidencia.
            for (int i = 0; i < tiposCampania.size(); i++) {
                if (tiposCampania.get(i).equals(tipo)) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                tiposCampania.add(tipo);
                List<Cliente> nuevaLista = new ArrayList<>();
                nuevaLista.add(cliente);
                grupos.add(nuevaLista);
            } else {
                grupos.get(index).add(cliente);
            }
        }

        long fin = System.currentTimeMillis();
        ResultadoProcesamiento resultado = ResultadoProcesamiento.desdeListas(tiposCampania, grupos, fin - inicio, clientes.size());
        resultado.imprimirResumen("Resumen de campañas - versión ineficiente", 20);
        return resultado;
    }
}
