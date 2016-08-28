package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "co.juandavidpoveda.pruebachuck.database");

        //Frase
        Entity frase = schema.addEntity("Frase");
        Property fraseId = frase.addIdProperty().autoincrement().getProperty();
        frase.setTableName("frase");
        frase.addStringProperty("icon_url");
        frase.addStringProperty("url");
        frase.addStringProperty("value");

        frase.implementsSerializable();

        new DaoGenerator().generateAll(schema, args[0]);
    }
}


