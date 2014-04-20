package io.divide.client.android.security;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import io.divide.client.BackendUser;
import io.divide.client.data.ServerResponse;

/**
* Created by williamwebb on 3/20/14.
*/
public class UserUtils {
    private static final String ANONYMOUS_KEY = "anonymous_key";

    public static ServerResponse<BackendUser> getAnonymousUser(Context context){
        String id = UserUtils.getDeviceIdUnique(context);
        ServerResponse<BackendUser> response;

        response = BackendUser.signIn(id, id);
        if(!response.getStatus().isSuccess()){
            response = BackendUser.signUp(id, id, id);
            if(response.getStatus().isSuccess()){
                BackendUser user = response.get();
                user.put(ANONYMOUS_KEY,true);
                user.saveASync();
            }
        }
        return response;
    }

    public static String getDeviceIdUnique(Context context)
    {
        try {
            String a = getDeviceIdTm(context);
            String b = getDeviceIdAndroid(context);
            String c = getDeviceIdPseudo();

            if (a!=null && a.length()>0 && a.replace("0", "").length()>0)
                return a;
            else if (b!=null && b.length()>0 && b.equals("9774d56d682e549c")==false)
                return b;
            else if (c!=null && c.length()>0)
                return c;
            else
                return "";
        }
        catch(Exception ex)
        {
            return "";
        }
    }

    private static String getDeviceIdTm(Context context)
    {
        TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    private static String getDeviceIdAndroid(Context context)
    {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getDeviceIdPseudo()
    {
        String tstr="";
        if ( Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
            tstr+= Build.SERIAL;
            tstr += "::" + (Build.PRODUCT.length() % 10) + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10);
        }
        if(tstr.equals(""))tstr = "blah";

        return tstr;
    }
}
