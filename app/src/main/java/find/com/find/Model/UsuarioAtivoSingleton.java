package find.com.find.Model;

/**
 * Created by Jaelson on 16/09/2017.
 */

public class UsuarioAtivoSingleton {
    private static Usuario usuario;
    private static UsuarioAtivoSingleton instacia = null;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioAtivoSingleton.usuario = usuario;
    }

    public static synchronized UsuarioAtivoSingleton getInstacia() {
        if(instacia == null){
            instacia = new UsuarioAtivoSingleton();
        }
        return instacia;
    }
}
