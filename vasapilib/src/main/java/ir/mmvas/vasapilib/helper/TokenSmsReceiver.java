package ir.mmvas.vasapilib.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class TokenSmsReceiver extends BroadcastReceiver {
    private final WeakReference<TokenSmsReceiverCallback> callbackRef;

    private static final int OTP_CODE_LENGTH = 4;
    private String TAG = TokenSmsReceiver.class.getSimpleName();
    private String vasShortCode = "";

    public TokenSmsReceiver(String shortCode, TokenSmsReceiverCallback callback){
        this.callbackRef = new WeakReference<>(callback);
        this.vasShortCode = TextUtils.isEmpty(shortCode) ? "" : shortCode;
        if(!TextUtils.isEmpty(shortCode) && !shortCode.startsWith("98")) {
            this.vasShortCode = "98" + shortCode;
        }
    }

    public void onReceive(Context context, Intent intent) {
        Map<String, String> map = retrieveMessages(intent);
        if(!map.isEmpty() && map.containsKey(vasShortCode)) {
            String sms = map.get(vasShortCode);
            if(sms != null && sms.length() >= OTP_CODE_LENGTH) {
                handleTokenMessage(sms);
            }
        }
    }

    private void handleTokenMessage(String message){
        message = message.replace('\n', ' ').replace(':', ' ');
        String[] parts = message.split(" ");
        if(parts.length == 0){
            return;
        }
        for(String p : parts){
            if(Utils.isInteger(p.trim()) && p.trim().length() == OTP_CODE_LENGTH){
                TokenSmsReceiverCallback callback = callbackRef.get();
                if(callback != null) {
                    callback.onTokenReceived(p.trim());
                }
                return;
            }
        }
    }

    public String getSmsFromIntent(Intent intent){
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";
        if(myBundle == null) return null;

        Object [] pdus = (Object[]) myBundle.get("pdus");
        if(pdus == null) return null;

        messages = new SmsMessage[pdus.length];
        for (int i = 0; i < messages.length; i++)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = myBundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
            }
            else {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            strMessage += "SMS From: " + messages[i].getOriginatingAddress();
            strMessage += " : ";
            strMessage += messages[i].getMessageBody();
            strMessage += "\n";
        }

        Log.e("SMS", strMessage);
        return strMessage;
    }

    public Map<String, String> retrieveMessages(Intent intent) {
        Map<String, String> msg = null;
        SmsMessage[] msgs;
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey("pdus")) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            if (pdus != null) {
                int nbrOfpdus = pdus.length;
                msg = new HashMap<>(nbrOfpdus);
                msgs = new SmsMessage[nbrOfpdus];

                // There can be multiple SMS from multiple senders, there can be a maximum of nbrOfpdus different senders
                // However, send long SMS of same sender in one message
                for (int i = 0; i < nbrOfpdus; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    }
                    else {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }

                    String originatinAddress = msgs[i].getOriginatingAddress();
                    if (!msg.containsKey(originatinAddress)) {
                        msg.put(msgs[i].getOriginatingAddress(), msgs[i].getDisplayMessageBody());
                    } else {
                        String previousparts = msg.get(originatinAddress);
                        String msgString = previousparts + msgs[i].getMessageBody();
                        msg.put(originatinAddress, msgString);
                    }
                }
            }
        }

        return msg;
    }
}

