package co.juandavidpoveda.pruebachuck;

/**
 * Created by JUAN DAVID on 28/08/2016.
 */
import android.app.Application;

import co.juandavidpoveda.pruebachuck.database.DatabaseHelper;

public class AppClass extends Application {

    private DatabaseHelper dbh;
    private long idFraseNueva;
    private boolean noMostrarFraseNueva;

    private final String TAG = AppClass.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();//
        dbh = new DatabaseHelper(this);
    }

    public DatabaseHelper getDbh() {
        return dbh;
    }

    public long getIdFraseNueva() {
        return idFraseNueva;
    }

    public void setIdFraseNueva(long idFraseNueva) {
        this.idFraseNueva = idFraseNueva;
    }

    public boolean isNoMostrarFraseNueva() {
        return noMostrarFraseNueva;
    }

    public void setNoMostrarFraseNueva(boolean noMostrarFraseNueva) {
        this.noMostrarFraseNueva = noMostrarFraseNueva;
    }
}