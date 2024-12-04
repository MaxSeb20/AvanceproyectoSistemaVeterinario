import java.util.LinkedList;
import java.util.Queue;

public class GestionCitas {
    private Queue<Cita> colaCitas;

    public GestionCitas() {
        this.colaCitas = new LinkedList<>();
    }

    public void agendarCita(Cita cita) {
        colaCitas.add(cita);
    }

    public void cancelarCita() {
        if (!colaCitas.isEmpty()) {
            colaCitas.poll();
        }
    }

    public Queue<Cita> verCitasPendientes() {
        return colaCitas;
    }
}

