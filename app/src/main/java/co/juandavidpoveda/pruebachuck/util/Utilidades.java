package co.juandavidpoveda.pruebachuck.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JUAN DAVID on 28/08/2016.
 */
public class Utilidades {

    public static boolean isConnected(Context con) {

        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //Tiene WiFi
        if (info != null && info.isConnected()) {
            System.out.println("WIFI");
            return true;
        } else {
            info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //Tiene datos
            if (info != null && info.isConnected()) {
                System.out.println("MOVIL");
                return true;
            }
        }

        return false;
    }

}
