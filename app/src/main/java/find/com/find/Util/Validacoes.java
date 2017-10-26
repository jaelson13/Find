package find.com.find.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import find.com.find.Model.UsuarioApplication;

/**
 * Created by Jaelson on 01/10/2017.
 */

public class Validacoes {

    public static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiRmluZDEyMzQ1NiJ9.SJA_e7w0fqkKVASobxYsaWdF8xHWUgM7qR-XmHYkgTw";

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
    public static void carregarImagemUser(Context context, ImageView imageView) {
        Glide.with(context).load(UsuarioApplication.getUsuario().getUrlImgPerfil()).into(imageView);
    }

    //CARREGAR A IMAGEM DO SERVIDOR
    public static void carregarImagemMap(Context context,String urlMap, ImageView imageView) {
        Glide.with(context).load(urlMap).into(imageView);
    }

    //PEGAR A DATA ATUAL
    public static String getDataAtual() {
        Calendar calander = Calendar.getInstance();
        int dia = calander.get(Calendar.DAY_OF_MONTH);
        int mes = calander.get(Calendar.MONTH) + 1;
        int ano = calander.get(Calendar.YEAR);

        return dia + "/" + mes + "/" + ano;
    }

    public static String convertSha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02X",0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
