import java.util.List;

public class Main {

  
    private static final String NOMBRE_ESTUDIANTE = "RONY BARRIOS";
    private static final String CARNE = "9941 99 127";

    private static final String ARCHIVO = "clientes.csv";
    private static final int CANTIDAD_CLIENTES_DEFAULT = 2_000_000;

    public static void main(String[] args) {
        int cantidadClientes = obtenerCantidadClientes(args);
        boolean generarArchivo = debeGenerarArchivo(args);

        System.out.println("=================================================");
        System.out.println("TAREA - PROCESAMIENTO MASIVO OPTIMIZADO EN JAVA");
        System.out.println("=================================================");
        System.out.println("Estudiante: " + "RONY BARRIOS");
        System.out.println("Carné: " + "9941 99 127");
        System.out.println("Cantidad de clientes: " + cantidadClientes);

        System.out.println("\nMemoria antes de generar archivo:");
        mostrarMemoria();

        if (generarArchivo) {
            System.out.println("\nGenerando archivo CSV...");
            long inicioGeneracion = System.currentTimeMillis();
            GeneradorClientes.generarArchivo(ARCHIVO, cantidadClientes);
            long finGeneracion = System.currentTimeMillis();
            System.out.println("Tiempo de generación del archivo: " + (finGeneracion - inicioGeneracion) + " ms");
            mostrarMemoria();
        } else {
            System.out.println("\nSe usará el archivo existente: " + ARCHIVO);
            System.out.println("No se regenera para medir únicamente carga/procesamiento.");
            mostrarMemoria();
        }

        ResultadoProcesamiento resultadoIneficiente = ejecutarVersionBaseSinDetenerPrograma();

        System.out.println("\n================ VERSION OPTIMIZADA CON HASHMAP ================");
        System.out.println("Cargando clientes sin conservar jsonData pesado...");
        long inicioCargaOptimizada = System.currentTimeMillis();
        List<Cliente> clientesOptimizados = ProcesadorOptimizado.cargarClientesSinJsonPesado(ARCHIVO);
        long finCargaOptimizada = System.currentTimeMillis();
        System.out.println("Clientes cargados para procesamiento optimizado: " + clientesOptimizados.size());
        System.out.println("Tiempo de carga optimizada: " + (finCargaOptimizada - inicioCargaOptimizada) + " ms");
        mostrarMemoria();

        ResultadoProcesamiento resultadoHashMap = ProcesadorOptimizado.procesarEnMemoriaConHashMap(clientesOptimizados);
        mostrarMemoria();

        clientesOptimizados = null;
        solicitarLimpiezaMemoria();

        System.out.println("\n================ VERSION OPTIMIZADA STREAMING ================");
        ResultadoProcesamiento resultadoStreaming = ProcesadorOptimizado.procesarDesdeArchivoStreaming(ARCHIVO);
        mostrarMemoria();

        imprimirComparacion(resultadoIneficiente, resultadoHashMap, resultadoStreaming);
        System.out.println("\nFin del programa.");
    }

    private static ResultadoProcesamiento ejecutarVersionBaseSinDetenerPrograma() {
        System.out.println("\n================ VERSION BASE / INEFICIENTE ================");
        System.out.println("Cargando TODOS los clientes en memoria, incluyendo jsonData pesado...");

        try {
            long inicioCarga = System.currentTimeMillis();
            List<Cliente> clientes = ProcesadorMalo.cargarTodosLosClientes(ARCHIVO);
            long finCarga = System.currentTimeMillis();

            System.out.println("Clientes cargados en memoria: " + clientes.size());
            System.out.println("Tiempo de carga de clientes: " + (finCarga - inicioCarga) + " ms");
            mostrarMemoria();

            ResultadoProcesamiento resultado = ProcesadorIneficiente.procesar(clientes);
            mostrarMemoria();

            clientes = null;
            solicitarLimpiezaMemoria();
            return resultado;
        } catch (OutOfMemoryError error) {
            System.out.println("La versión base falló por falta de memoria: " + error.getClass().getSimpleName());
            System.out.println("Esto evidencia el problema de diseño: cargar 2,000,000 clientes con jsonData pesado en una List.");
            solicitarLimpiezaMemoria();
            mostrarMemoria();
            return null;
        }
    }


    private static boolean debeGenerarArchivo(String[] args) {
        for (String arg : args) {
            if ("--no-generar".equalsIgnoreCase(arg) || "no-generar".equalsIgnoreCase(arg)) {
                return false;
            }
        }
        return true;
    }

    private static int obtenerCantidadClientes(String[] args) {
        if (args.length == 0) {
            return CANTIDAD_CLIENTES_DEFAULT;
        }

        try {
            int cantidad = Integer.parseInt(args[0]);
            if (cantidad <= 0) {
                return CANTIDAD_CLIENTES_DEFAULT;
            }
            return cantidad;
        } catch (NumberFormatException e) {
            return CANTIDAD_CLIENTES_DEFAULT;
        }
    }

    private static void imprimirComparacion(ResultadoProcesamiento ineficiente,
                                            ResultadoProcesamiento hashMap,
                                            ResultadoProcesamiento streaming) {
        System.out.println("\n================ COMPARACION FINAL ================");
        System.out.println("Clientes procesados por versión optimizada: " + streaming.getTotalClientes());
        System.out.println("Campañas generadas: " + streaming.getCantidadCampanias());

        if (ineficiente == null) {
            System.out.println("Tiempo procesamiento ineficiente: NO COMPLETADO por OutOfMemoryError");
        } else {
            System.out.println("Tiempo procesamiento ineficiente: " + ineficiente.getTiempoMs() + " ms");
        }

        System.out.println("Tiempo procesamiento HashMap en memoria: " + hashMap.getTiempoMs() + " ms");
        System.out.println("Tiempo procesamiento streaming: " + streaming.getTiempoMs() + " ms");

        if (ineficiente != null && hashMap.getTiempoMs() > 0) {
            double mejoraBusqueda = (double) ineficiente.getTiempoMs() / hashMap.getTiempoMs();
            System.out.printf("Mejora aproximada HashMap vs ineficiente: %.2f veces%n", mejoraBusqueda);
        }

        System.out.println("Conclusión técnica: la versión optimizada sí procesa el volumen completo sin depender de cargar el JSON pesado en memoria.");
    }

    private static void solicitarLimpiezaMemoria() {
        System.gc();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void mostrarMemoria() {
        Runtime runtime = Runtime.getRuntime();
        long memoriaUsada = runtime.totalMemory() - runtime.freeMemory();
        long memoriaTotal = runtime.totalMemory();
        long memoriaMaxima = runtime.maxMemory();

        System.out.println("Memoria JVM:");
        System.out.println("Usada : " + (memoriaUsada / 1024 / 1024) + " MB");
        System.out.println("Total : " + (memoriaTotal / 1024 / 1024) + " MB");
        System.out.println("Máxima: " + (memoriaMaxima / 1024 / 1024) + " MB");
    }
}
