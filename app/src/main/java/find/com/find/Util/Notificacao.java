package find.com.find.Util;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import find.com.find.Activies.Principal_Activity;
import find.com.find.R;

/**
 * Created by Jaelson on 01/11/2017.
 */

public class Notificacao extends FirebaseMessagingService{
    String type="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

           if (remoteMessage.getData().size() > 0) {
               type = "json";
               enviarNotificacao(remoteMessage.getData().toString());
           }
           if (remoteMessage.getNotification() != null) {
               type = "message";
               enviarNotificacao(remoteMessage.getNotification().getBody());
           }

    }

    private void enviarNotificacao(String msg){
    String id = "",message="",title="";
        if(type.equals("json")){
            try {
                JSONObject jsonObject = new JSONObject(msg);
                id = jsonObject.getString("id");
                message = jsonObject.getString("message");
                title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type.equals("message")){
            message = msg;
        }

        Intent intent = new Intent(getApplicationContext(),Principal_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"Default");
        notificationBuilder.setContentTitle("Seu Mapeamento");
        notificationBuilder.setContentText(message);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(sound);
        notificationBuilder.setSmallIcon(R.drawable.ic_logo);
        notificationBuilder.setAutoCancel(true);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
