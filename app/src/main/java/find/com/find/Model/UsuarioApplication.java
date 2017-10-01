package find.com.find.Model;

import android.app.Application;

/**
 * Created by Jaelson on 16/09/2017.
 */

public class UsuarioApplication extends Application {
    private static Usuario usuario;
    private static UsuarioApplication instacia = null;
    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioApplication.usuario = usuario;
    }

    public static synchronized UsuarioApplication getInstacia() {
        if(instacia == null){
            instacia = new UsuarioApplication();
        }
        return instacia;
    }
}
