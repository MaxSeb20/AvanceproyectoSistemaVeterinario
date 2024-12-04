public class Cita {
    private Cliente cliente;
    private Mascota mascota;
    private String fecha;
    private String hora;
    private String motivo;

    public Cita(Cliente cliente, Mascota mascota, String fecha, String hora, String motivo) {
        this.cliente = cliente;
        this.mascota = mascota;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMotivo() {
        return motivo;
    }
}
