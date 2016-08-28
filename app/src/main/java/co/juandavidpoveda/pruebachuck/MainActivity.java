package co.juandavidpoveda.pruebachuck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

import co.juandavidpoveda.pruebachuck.database.DatabaseHelper;
import co.juandavidpoveda.pruebachuck.pojo.FraseChuck;
import co.juandavidpoveda.pruebachuck.util.Utilidades;


public class MainActivity extends ActionBarActivity {

    private DatabaseHelper dh;
    private FraseChuck fraseChuck;
    private AppClass appClass;
    private TextView cargaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appClass = (AppClass) getApplication();
        dh = appClass.getDbh();

        cargaDatos = (TextView) findViewById(R.id.cargaDatosTextView);
        new NuevaFrase().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NuevaFrase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            System.out.println("******Get Frase");
            cargaDatos.setText("Obteniendo una Nueva Frase...");

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Utilidades.isConnected(MainActivity.this)) {
                try {

                    Gson gson = new Gson();

                    Type listType = new TypeToken<FraseChuck>() {
                    }.getType();


                    URL url = new URL(getString(R.string.frase_chuck_url));
                    URLConnection urlConnection = url.openConnection();
                    InputStream json = urlConnection.getInputStream();

                    if (json != null) {
                        System.out.println("*****json != null!");
                        Reader reader = new InputStreamReader(json);

                        fraseChuck = gson.fromJson(reader, listType); //Si el 200

                        if(fraseChuck != null) {
                            dh.insertFrase(fraseChuck);
                            System.out.println("Se insertó una nueva frase en la BD!");
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("NO HAY CONEXION");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("*****Post Execute!");

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if(fraseChuck != null) {
                        cargaDatos.setText("La frase es: " + fraseChuck.getValue());
                    } else {
                        cargaDatos.setText("Error cargando la frase :(");
                    }
                }
            }, 1);
        }


    }
}
