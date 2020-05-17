package gr.eurobank.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        return sdf.format(new Date());
    }
}
