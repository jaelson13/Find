package find.com.find.Util;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jaelson on 02/11/2017.
 */

public class NotificacaoIdService extends FirebaseInstanceIdService {


    private static final String TAG = "ServicoIdFirebase";
    public static final  String token = FirebaseInstanceId.getInstance().getToken();


    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String tokenAtualizado = FirebaseInstanceId.getInstance().getToken();
        // token = tokenAtualizado;
        //Displaying token on logcat
        Log.d(TAG, "Token atualizado: " + tokenAtualizado);

    }

}
