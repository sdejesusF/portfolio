package sdejesus.portfolio;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sdejesus on 7/3/16.
 */

public class Utility {
    public static String getLocalJSON(Context ctx){
        try {
            InputStream is = ctx.getAssets().open("JSON/data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
