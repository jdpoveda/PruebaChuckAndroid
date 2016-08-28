package co.juandavidpoveda.pruebachuck;

/**
 * Created by JUAN DAVID on 28/08/2016.
 */
import android.app.Application;

import co.juandavidpoveda.pruebachuck.database.DatabaseHelper;

public class AppClass extends Application {

    private DatabaseHelper dbh;

    private final String TAG = AppClass.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();//
        dbh = new DatabaseHelper(this);
    }

    public DatabaseHelper getDbh() {
        return dbh;
    }

}