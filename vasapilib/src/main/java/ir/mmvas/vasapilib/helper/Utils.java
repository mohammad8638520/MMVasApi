package ir.mmvas.vasapilib.helper;

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

}
