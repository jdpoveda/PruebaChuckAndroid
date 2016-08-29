package co.juandavidpoveda.pruebachuck.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

import co.juandavidpoveda.pruebachuck.AppClass;
import co.juandavidpoveda.pruebachuck.R;
import co.juandavidpoveda.pruebachuck.database.DatabaseHelper;
import co.juandavidpoveda.pruebachuck.database.Frase;
import co.juandavidpoveda.pruebachuck.pojo.FraseChuck;
import co.juandavidpoveda.pruebachuck.sensors.ShakeDetector;
import co.juandavidpoveda.pruebachuck.sensors.ShakeService;
import co.juandavidpoveda.pruebachuck.util.Utilidades;


public class MainActivity extends ActionBarActivity {

    private DatabaseHelper dh;
    private Frase fraseActual;
    private AppClass appClass;
    private TextView cargaDatos;
    private ImageView imagenChuck;
    private ProgressBar loadingImg;
    private FloatingActionButton listaFAB;
    private Button nuevaFrase;
    private SensorManager sensorMan;
    private Sensor sensor;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appClass = (AppClass) getApplication();
        dh = appClass.getDbh();

        cargaDatos = (TextView) findViewById(R.id.cargaDatosTextView);
        imagenChuck = (ImageView) findViewById(R.id.imagenChuck);
        loadingImg = (ProgressBar) findViewById(R.id.progressBarLoadingImg);
        listaFAB = (FloatingActionButton) findViewById(R.id.listaFab);
        nuevaFrase = (Button) findViewById(R.id.nuevaFraseButton);

        nuevaFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NuevaFrase().execute();
            }
        });

        listaFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        if(!appClass.isNoMostrarFraseNueva()) {
            new NuevaFrase().execute();
            appClass.setNoMostrarFraseNueva(true);
        } else {
            fraseActual = dh.findFraseById(appClass.getIdFraseNueva());
            mostrarInfoFrase();
        }

        Intent intent = new Intent(MainActivity.this, ShakeService.class);
        startService(intent);

        // ShakeDetector initialization
        sensorMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorMan
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                new NuevaFrase().execute();
            }
        });

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

    private void mostrarInfoFrase(){

        if(fraseActual != null) {
            cargaDatos.setText(fraseActual.getValue());
            Picasso.with(MainActivity.this)
                    .load(fraseActual.getIcon_url())
                    .into(imagenChuck, new Callback() {
                        @Override
                        public void onSuccess() {
                            loadingImg.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub

                        }
            });
            imagenChuck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(fraseActual.getUrl()));
                    startActivity(intent);
                }
            });
        } else {
            cargaDatos.setText("Ha ocurrido un error cargando la frase :(");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        sensorMan.registerListener(shakeDetector, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        sensorMan.unregisterListener(shakeDetector);
        super.onPause();
    }

    //Llamado al API de Chuck para traer una nueva frase
    private class NuevaFrase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            System.out.println("******Get Frase");

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

                        FraseChuck fraseChuck = gson.fromJson(reader, listType); //Si el 200

                        if(fraseChuck != null) {
                            appClass.setIdFraseNueva(dh.insertFrase(fraseChuck));
                            System.out.println("Se insertó una nueva frase en la BD! id: " + appClass.getIdFraseNueva());
                            fraseActual = dh.findFraseById(appClass.getIdFraseNueva());
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
                    mostrarInfoFrase();
                }
            }, 1);
        }


    }
}
