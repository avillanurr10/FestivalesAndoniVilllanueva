import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Esta clase guarda una agenda con los festivales programados
 * en una serie de meses
 *
 * La agenda guarda los festivales en una colección map.
 * La clave del map es el mes (un enumerado festivales.modelo.Mes)
 * Cada mes tiene asociados en una colección ArrayList
 * los festivales de ese mes
 *
 * Solo aparecen los meses que incluyen algún festival
 *
 * Las claves se recuperan en orden alfabético
 *
 */
public class AgendaFestivales {
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private TreeMap<Mes, ArrayList<Festival>> agenda;

    /**
     * Constructor de la clase AgendaFestivales.
     * Inicializa la agenda como un TreeMap vacío.
     */
    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }

    /**
     * Añade un nuevo festival a la agenda.
     * Si la clave (el mes en el que se celebra el festival)
     * no existe en la agenda, se crea una nueva entrada
     * con dicha clave y la colección formada por ese único festival.
     * Si la clave (el mes) ya existe, se añade el nuevo festival
     * a la lista de festivales que ya existe para ese mes,
     * insertándolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso se usa el método de ayuda obtenerPosicionDeInsercion().
     *
     * @param festival El festival a añadir a la agenda.
     */
    public void addFestival(Festival festival) {
        Mes mes = festival.getMes();
        ArrayList<Festival> festivales = agenda.getOrDefault(mes, new ArrayList<>());
        int index = obtenerPosicionDeInsercion(festivales, festival);
        festivales.add(index, festival);
        agenda.put(mes, festivales);
    }

    /**
     * Determina la posición en la que debería ir el nuevo festival
     * de forma que la lista de festivales quede ordenada por nombre.
     *
     * @param festivales La lista de festivales ya existente para un mes.
     * @param festival El festival a insertar.
     * @return La posición donde insertar el festival en la lista.
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
        int index = 0;
        for (Festival f : festivales) {
            if (f.getNombre().compareToIgnoreCase(festival.getNombre()) > 0) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Devuelve una representación textual de la agenda de festivales.
     * La representación incluye los festivales agrupados por mes.
     *
     * @return Una cadena de texto que representa la agenda de festivales.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Mes, ArrayList<Festival>> entry : agenda.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            for (Festival festival : entry.getValue()) {
                sb.append(festival).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Devuelve la cantidad de festivales que hay en un mes dado.
     *
     * @param mes El mes a considerar.
     * @return La cantidad de festivales que hay en ese mes.
     * Si el mes no existe, devuelve -1.
     */
    public int festivalesEnMes(Mes mes) {
        ArrayList<Festival> festivales = agenda.get(mes);
        return festivales != null ? festivales.size() : -1;
    }

    /**
     * Agrupa todos los festivales de la agenda por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colección
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo.
     *
     * @return Un mapa que asocia estilos con conjuntos de nombres de festivales.
     */
    public Map<Estilo, TreeSet<String>> festivalesPorEstilo() {
        Map<Estilo, TreeSet<String>> festivalesPorEstilo = new TreeMap<>();
        for (ArrayList<Festival> festivales : agenda.values()) {
            for (Festival festival : festivales) {
                for (Estilo estilo : festival.getEstilos()) {
                    festivalesPorEstilo.computeIfAbsent(estilo, k -> new TreeSet<>()).add(festival.getNombre());
                }
            }
        }
        return festivalesPorEstilo;
    }
}
