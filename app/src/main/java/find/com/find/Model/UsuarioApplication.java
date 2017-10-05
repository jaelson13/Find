package find.com.find.Model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 16/09/2017.
 */

public class UsuarioApplication extends Application {
    private static Usuario usuario;
    private static Token token;
    private static UsuarioApplication instacia = null;

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        UsuarioApplication.token = token;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioApplication.usuario = usuario;
    }

    public static synchronized UsuarioApplication getInstacia() {
        if (instacia == null) {
            instacia = new UsuarioApplication();
        }
        return instacia;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getInstacia();

        FindApiService service = FindApiAdapter.createService(FindApiService.class,"root","toor");
        Call<Token> call = service.pegarToken();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.code() == 200){
                    UsuarioApplication.setToken(response.body());
                    Log.i("key",UsuarioApplication.getToken().getToken());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("Erro", t.getMessage());
            }
        });


    }
}
