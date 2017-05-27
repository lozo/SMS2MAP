package si.gasilec.sms.gasilec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        //http://androidexample.com/Incomming_SMS_Broadcast_Receiver_-_Android_Example/index.php?view=article_discription&aid=62
        //https://www.javacodegeeks.com/2012/11/java-regular-expression-tutorial-with-examples.html
        //https://developer.android.com/reference/java/util/regex/Pattern.html
        // Retrieves a map of extended data from the intent.
        //https://github.com/ruckus/SMSButler-Android/tree/master/app/src/main/java/com/codycaughlan/smsbutler
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.i("SmsReceiver", "Začetek");
                for (int ji = 0; ji < pdusObj.length; ji++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[ji]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                   //POŽAR NA OBJEKTU, Slovenski trg 1  --Sending: ReCO, RIC:464646, IL: 4564564
                    Pattern pattern  = Pattern.compile("^(.+?),(.+?)--",Pattern.CASE_INSENSITIVE);
                    Matcher matcher  = pattern.matcher(message);


                    if(matcher.find()){
                        Log.i("SmsReceiver", "Najden zadetek :" + matcher.group(0));

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+matcher.group(2)+", Slovenija"));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
                        String mapsPackageName = "com.google.android.apps.maps";
                        i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
                        i.setPackage(mapsPackageName);
                        context.startActivity(i);
                    } else {
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", Ni zadetkov: ", duration);
                        toast.show();
                        //Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                        //Uri.parse("http://maps.google.com/maps?daddr=" + message);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
                        //context.startActivity(intent2);
                    }

                    Log.i("SmsReceiver", "Konec");


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }



}
