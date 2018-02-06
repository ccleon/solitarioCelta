package es.upm.miw.SolitarioCelta;


public class Resultado {
    private String jugador;
    private String fecha;
    private int puntuacion;

    private String separador = MainActivity.separador;

    public Resultado (String jugador, int puntuacion, String fecha){
        this.jugador=jugador;
        this.puntuacion=puntuacion;
        this.fecha=fecha;
    }

    public Resultado (String res){
        String [] porPartes = res.split(separador);
        this.jugador = porPartes[0];
        this.puntuacion = Integer.parseInt(porPartes[2]);
        this.fecha = porPartes[4];
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String toString (){
        return this.jugador + separador + this.puntuacion + separador + this.fecha;
    }
}
