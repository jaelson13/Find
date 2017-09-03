package find.com.find.Services;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jaelson on 02/09/2017.
 */

public class FindApiAdapter {
    //URL Base do endpoint. Deve terminar com /
    public static final String BASE_URL = "";
    private static FindApiAdapter findService;

    public static FindApiAdapter createService(){

        //Instancia do interceptador das requisições
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS);

        httpClient.addInterceptor(loggingInterceptor);

        if(findService==null) {
            //Instancia do Retrofit
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(httpClient.build()).build();
            findService = retrofit.create(FindApiAdapter.class);
        }

        return findService;


    }
}
