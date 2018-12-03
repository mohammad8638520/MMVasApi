package ir.mmvas.vasapilib.retrofit.toaster;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ir.mmvas.vasapilib.R;

public class Toaster {

    public static final int INFO = 4;
    public static final int ERROR = 3;
    public static final int WARNING = 2;
    public static final int DEFAULT = 5;
    public static final int SUCCESS = 1;


    public static void show(Context context, String message, int type){
        if(message == null) return;
        Toast toast = simpleToast(context, message, type);
//        int yOffset =  context.getResources().getDisplayMetrics().heightPixels / 5;
//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.show();
    }

    public static Toast make(Context context, String message, int type){
        return simpleToast(context, message, type);
    }
    public static void show(Context context, String message){
        show(context, message, DEFAULT);
    }

    private static Toast simpleToast(Context context, String text, int type){
        Toast toast = new Toast(context);
        TextView tv = (TextView) LayoutInflater.from(context).
                inflate(R.layout.toast_simple_layout, null, false);

        tv.setText(text);
        if(type > 0 ){
            tv.setBackgroundResource(bgDrawableId(type));
            tv.setTextColor(type == DEFAULT ? Color.BLACK : Color.WHITE);
        }

        tv.setPadding(100, 35, 100, 35);

        toast.setView(tv);
        int yOffset = context.getResources().getDisplayMetrics().heightPixels / 6;
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    private static int bgDrawableId(int type){
        int id = 0;
        switch (type){
            case SUCCESS :
                id = R.drawable.toast_simple_bg_success;
                break;
            case INFO :
                id = R.drawable.toast_simple_bg_info;
                break;
            case WARNING :
                id = R.drawable.toast_simple_bg_warning;
                break;
            case ERROR :
                id = R.drawable.toast_simple_bg_error;
                break;
            case DEFAULT :
            default:
                id = R.drawable.toast_simple_bg;
                break;
        }
        return id;
    }

    public static void showPressBackAgainToExitToast(Context context) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_simple_layout, null, false);
        toast.setView(view);
        int yOffset = context.getResources().getDisplayMetrics().heightPixels / 10;
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
