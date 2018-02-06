package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity {

	JuegoCelta juego;
    private final String GRID_KEY = "GRID_KEY";
    private static final String LOG_TAG = "CORI";
    protected static String ficheroPuntuaciones= "ficheroPuntuaciones.txt";
    protected static String partidaGuardada= "partidaGuardada.txt";
    private SharedPreferences preferencias;
    protected static String separador = "\t \t | \t \t";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        mostrarTablero();
    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);

        mostrarTablero();
        if (juego.juegoTerminado()) {
            preferencias = PreferenceManager.getDefaultSharedPreferences(this);

            String jugador = preferencias.getString(
                    getString(R.string.nombreJugador),
                    getString(R.string.nombreJugador_default));

            int puntuacion = juego.numeroFichas();

            Long tsLong =  System.currentTimeMillis()/1000;
            String fecha =  getDate(tsLong);



            Resultado resultado = new Resultado(jugador, puntuacion, fecha);

            try{
                String res = resultado.toString() + "\n";
                FileOutputStream fos = openFileOutput(ficheroPuntuaciones, Context.MODE_APPEND);
                fos.write(res.getBytes());
                fos.close();
            }catch (Exception e) {
                Log.e(LOG_TAG, "Error al escribir su puntuación");
            }

            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }
    }

    public  String getDate(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
    /**Guardo la partida actual*/
    public void guardarPartida () {
        try {
            String guardarPartida = juego.serializaTablero();
            FileOutputStream fos = openFileOutput(partidaGuardada, Context.MODE_PRIVATE);
            fos.write(guardarPartida.getBytes());
            fos.close();
            Toast.makeText(this, "Partida guardada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error al guardar la partida");
        }
    }
    /**Recupero la partida guardada*/
    public void recuperarPartida() {
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(partidaGuardada)));
            String partida = fin.readLine();
            juego.deserializaTablero(partida);
            mostrarTablero();
            Toast.makeText(this, "Partida restaurada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error al restaurar la partida");
        }
    }
    /**HASTA AQUÍ LO MÍO*/

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + Integer.toString(i) + Integer.toString(j);
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = (RadioButton) findViewById(idBoton);
                    button.setChecked(juego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    /**
     * Guarda el estado del tablero (serializado)
     * @param outState Bundle para almacenar el estado del juego
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GRID_KEY, juego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    /**
     * Recupera el estado del juego
     * @param savedInstanceState Bundle con el estado del juego almacenador
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        juego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;
            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;
            case R.id.opcReiniciarPartida:
                new ReiniciarPartidaFragment().show(getFragmentManager(), "ALERT DIALOG");
                return true;
            case R.id.opcMejoresResultados:
                startActivity(new Intent(this, MejoresPuntuaciones.class));
                return true;
            case R.id.opcGuardarPartida:
                guardarPartida();
                return true;
            case R.id.opcRecuperarPartida:
                recuperarPartida();
                return true;
            default:
                Toast.makeText(this, getString(R.string.txtSinImplementar), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
