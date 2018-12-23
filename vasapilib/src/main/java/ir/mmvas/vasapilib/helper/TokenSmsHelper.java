package ir.mmvas.vasapilib.helper;


import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

import java.lang.ref.WeakReference;


public abstract class TokenSmsHelper implements TokenSmsReceiverCallback {
    private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private final WeakReference<Context> contextRef;

    private boolean isWaiting = false;
    private TokenSmsReceiver smsReceiver;
    private String originator;
    private Handler handler;
    private TimerRunnable timer;


    public TokenSmsHelper(Context context, String vasShortCode, Handler handler) {
        this.contextRef = new WeakReference<>(context);
        this.smsReceiver = new TokenSmsReceiver(vasShortCode, this);
        this.handler = handler;
        this.originator = vasShortCode;
        this.timer = new TimerRunnable(this, handler);
    }

    public void startTracking() {
        Context context = contextRef.get();
        if(context != null) {
            try {
                context.registerReceiver(smsReceiver, new IntentFilter(SMS_ACTION));
                isWaiting = true;
                timer.start();
                onStartTracking();
            } catch (Exception e){}
        }
    }

    public void stopTracking() {
        isWaiting = false;
        Context context = contextRef.get();
        if(context != null) {
            try {
                context.unregisterReceiver(smsReceiver);
            } catch (Exception e){}
        }
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    @Override
    public void onTokenReceived(String token) {
        timer.kill();
        onSuccess(token);
    }

    private static class TimerRunnable implements Runnable {
        private static final long DELAY = 60 * 1000;
        private final WeakReference<TokenSmsHelper> helperRef;
        private boolean kill = false;
        private Handler handler;
        public TimerRunnable(TokenSmsHelper helper, Handler handler) {
            this.helperRef = new WeakReference<>(helper);
            this.kill = false;
            this.handler = handler;
        }

        @Override
        public void run() {
            if(kill) return;
            TokenSmsHelper helper = helperRef.get();
            if(helper != null){
                helper.onFailure();
            }
        }

        public void kill(){
            this.kill = true;
        }

        public void start(){
            if(handler != null){
                this.kill = false;
                handler.postDelayed(this, DELAY);
            }
        }

    }

    public abstract void onStartTracking();
    public abstract void onSuccess(String token);
    public abstract void onFailure();
}
