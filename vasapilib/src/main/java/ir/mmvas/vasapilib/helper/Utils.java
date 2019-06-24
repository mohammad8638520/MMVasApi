package ir.mmvas.vasapilib.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


public class Utils {

    public static String intArrayValuesToString(int[] arr) {
        if(arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean empty = true;
        for (int val : arr) {
            if (!empty) {
                sb.append(',');
            }
            sb.append(String.valueOf(val));
            empty = false;
        }

        return sb.toString();
    }

    public static int[] stringToIntArray(String s) {
        if(TextUtils.isEmpty(s)) {
            return new int[0];
        }
        try {
            String[] arr = s.split(",");
            int[] result = new int[arr.length];
            for(int i = 0; i < arr.length; i++) {
                result[i] = Integer.valueOf(arr[i]);
            }
            return result;
        } catch (Exception e) {
            return new int[0];
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static String getAppVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }


}
