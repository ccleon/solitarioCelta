package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MejoresPuntuaciones extends Activity {

    private TextView textView;
    private String ficheroPuntuaciones = MainActivity.ficheroPuntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_puntuaciones);

        textView = (TextView) findViewById(R.id.mejorespuntuaciones);
        guardarPuntuacion(textView);
    }

    /**Reseteo puntuaciones*/
    public void resetearPuntuaciones(View v) {
        try {
            FileOutputStream fos = openFileOutput(ficheroPuntuaciones, Context.MODE_PRIVATE);
            fos.close();
            Toast.makeText(this, "Puntuaciones reseteadas", Toast.LENGTH_SHORT).show();
            guardarPuntuacion(v);
        } catch (Exception e) {
            Log.e("Puntuaciones", "Error al resetear puntuaciones");
        }
    }

    /**Muestro puntuacion*/
    public void guardarPuntuacion(View v) {
        ArrayList<Resultado> resultados = new ArrayList<Resultado>();
        BufferedReader fin;
        textView.setText("");

        try {
            fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroPuntuaciones)));
            String res = fin.readLine();

            if (res == null){
                textView.setText("No hay puntuaciones guardadas");
            }else{

            while (res != null) {
                resultados.add(new Resultado(res));
                res = fin.readLine();
            }

            Collections.sort(resultados, new Comparator<Resultado>(){
                @Override
                public int compare(Resultado o1, Resultado o2) {
                    return o1.getPuntuacion() < o2.getPuntuacion() ? -1 : (o1.getPuntuacion() == o2.getPuntuacion()) ? 0 : 1;
                }
            });

            for (int i =0 ; i < resultados.size(); i++){
               textView.append(resultados.get(i).toString() + "\n");
            }
            fin.close();
            }
        } catch (Exception e) {
            Log.e("Ficheros", "Error al mostrar las puntuaciones");
        }
    }

    public void ordenarJugador(View v) {
        ArrayList<Resultado> resultados = new ArrayList<Resultado>();
        BufferedReader fin;
        textView.setText("");

        try {
            fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroPuntuaciones)));
            String res = fin.readLine();

            if (res == null){
                textView.setText("No hay puntuaciones guardadas");
            }else{

                while (res != null) {
                    resultados.add(new Resultado(res));
                    res = fin.readLine();
                }

                Collections.sort(resultados, new Comparator<Resultado>(){
                    @Override
                    public int compare(Resultado o1, Resultado o2) {
                        return o1.getJugador().compareTo(o2.getJugador());
                    }
                });

                for (int i =0 ; i < resultados.size(); i++){
                    textView.append(resultados.get(i).toString() + "\n");
                }
                fin.close();
            }
        } catch (Exception e) {
            Log.e("Ficheros", "Error al mostrar las puntuaciones");
        }
    }
}
