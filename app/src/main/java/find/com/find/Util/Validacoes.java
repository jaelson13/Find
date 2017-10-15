package find.com.find.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import find.com.find.Model.UsuarioApplication;

/**
 * Created by Jaelson on 01/10/2017.
 */

public class Validacoes {
    //VALIDAR EMAIL
    public static boolean validarEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //RETORNAR O PATH DA URI
    public static Uri getImageUri(Context contexto, Bitmap imagem) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(contexto.getContentResolver(), imagem, "Imagem", null);
        return Uri.parse(path);
    }

    //PEGAR O CAMINHO REAL DA IMAGEM
    public static String getPath(Context contexo, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loading = new CursorLoader(contexo, uri, projection, null, null, null);
        Cursor cursor = loading.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //CARREGAR A IMAGEM DO SERVIDOR
    public static void carregarImagemUser(Context context, ImageView imageView){
        Picasso.with(context).load(UsuarioApplication.getUsuario().getUrlImgPerfil()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
    }
}
