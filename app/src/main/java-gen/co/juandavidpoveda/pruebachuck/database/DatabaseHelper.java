package co.juandavidpoveda.pruebachuck.database;

/**
 * Created by JUAN DAVID on 28/08/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import co.juandavidpoveda.pruebachuck.pojo.FraseChuck;

public class DatabaseHelper extends DaoMaster.OpenHelper {

    private final String TAG = DatabaseHelper.class.getSimpleName();
    private FraseDao fraseDao;

    private SQLiteDatabase db;
    private DaoSession daoSession;

    public DatabaseHelper(Context context) {
        super(context, "pruebachuck.db", null);

        db = this.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        fraseDao = daoSession.getFraseDao();
    }

    //Cerrar la DB

    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
    }

    public FraseDao getFraseDao() {
        return fraseDao;
    }

    public void setFraseDao(FraseDao fraseDao) {
        this.fraseDao = fraseDao;
    }


    public long insertFrase(FraseChuck fraseChuck){

        Frase fraseDb = new Frase();

        fraseDb.setUrl(fraseChuck.getUrl());
        fraseDb.setIcon_url(fraseChuck.getIcon_url());
        fraseDb.setValue(fraseChuck.getValue());

        daoSession.insertOrReplace(fraseDb);

        return fraseDb.getId();

    }

    public void deleteFraseById(long id){
        fraseDao.queryBuilder().where(FraseDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public List<Frase> findAllFrases(){
        List<Frase> frases = fraseDao.loadAll();
        return frases;
    }

    public Frase findFraseById(long id){

        Frase fr = null;

        for(Frase f : findAllFrases()){

            if(f.getId() == id){
                fr = f;
                break;
            }

        }

        return fr;
    }

    public void deleteAllFrases(){
        fraseDao.deleteAll();
    }

}