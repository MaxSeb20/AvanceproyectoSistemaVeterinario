public class Mascota {
    private String nombre;
    private String especie;

    public Mascota(String nombre, String especie) {
        this.nombre = nombre;
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
