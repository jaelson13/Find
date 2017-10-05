package find.com.find.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaelson on 04/10/2017.
 */

public class Token {
    @SerializedName("auth-jwt")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
