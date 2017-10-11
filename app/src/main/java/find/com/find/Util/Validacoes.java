package find.com.find.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jaelson on 01/10/2017.
 */

public class Validacoes {
    public static boolean validarEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Uri getImageUri(Context contexto, Bitmap imagem) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(contexto.getContentResolver(), imagem, "Imagem", null);
        return Uri.parse(path);
    }

    public static String getPath(Context contexo, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loading = new CursorLoader(contexo, uri, projection, null, null, null);
        Cursor cursor = loading.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
