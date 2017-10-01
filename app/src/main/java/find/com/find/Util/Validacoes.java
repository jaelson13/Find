package find.com.find.Util;

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
}
