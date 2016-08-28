package co.juandavidpoveda.pruebachuck.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fraseDaoConfig;

    private final FraseDao fraseDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        fraseDaoConfig = daoConfigMap.get(FraseDao.class).clone();
        fraseDaoConfig.initIdentityScope(type);

        fraseDao = new FraseDao(fraseDaoConfig, this);

        registerDao(Frase.class, fraseDao);
    }
    
    public void clear() {
        fraseDaoConfig.getIdentityScope().clear();
    }

    public FraseDao getFraseDao() {
        return fraseDao;
    }

}
