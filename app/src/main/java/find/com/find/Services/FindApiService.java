package find.com.find.Services;

import find.com.find.Model.Feedback;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.Token;
import find.com.find.Model.Usuario;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jaelson on 02/09/2017.
 */

public interface FindApiService {


    @Headers("Content-type:application/json")
    @GET("auth")
    Call<Token> pegarToken();

    //USUARIO

    //Pega dados do Usuario pelo id
    @FormUrlEncoded
    @GET("")
    Call<Integer> getUsuarioPorId(@Field("idUsuario") int idUsuario);

    //Inserção de um Usuario
    //FindWebService/webresources/com.find.usuario/create
    @Headers("Content-type:application/json")
    @POST("user/add")
    Call<Usuario> salvarUsuario(@Body Usuario usuario);

    //Atualização de um Usuario
    @Headers("Content-type:application/json")
    @PUT("user/atualizar")
    Call<Usuario> atualizarUsuario(@Header("X-Token") String token,@Body Usuario usuario);

    //Desativar um Usuario
    @FormUrlEncoded
    @PUT
    Call<Boolean> desativarUsuario(@Path("idUsuario") int idUsuario);

    //Fazer login
    @Headers("Content-type:application/json")
    @POST("user/login")
    Call<Usuario> fazerLogin(@Query("email") String email, @Query("senha") String senha);

    //Upload Imagem
    //@Headers("Content-type:application/json")
    @Multipart
    @POST("img/add")
    Call<ResponseBody> upImage(@Part MultipartBody.Part file);


    //MAPEAMENTO

    //Listar todos os mapeamentos
    @FormUrlEncoded
    @GET("")
    Call<Mapeamento> getMapeamentos(@Body Mapeamento mapeamento);

    //Pega dados do mapeamento pelo id
    @FormUrlEncoded
    @GET("")
    Call<Integer> getMapeamentoPorId(@Field("idMapeamento") int idMapeamento);

    //Inserção de um mapeamento
    @FormUrlEncoded
    @POST("")
    Call<Mapeamento> salvarMapeamento(@Body Mapeamento mapeamento);

    //Atualização de um mapeamento
    @FormUrlEncoded
    @POST("")
    Call<Mapeamento> atualizarMapeamento(@Body Mapeamento mapeamento);

    //Deletar um mapeamento
    @FormUrlEncoded
    @DELETE
    Call<Integer> deletarMapeamento(@Field("idMapeamento") int idMapeamento);



    //FEEDBACK

    //Listar todos os FeedBack
    @FormUrlEncoded
    @GET("")
    Call<Feedback> getFeedBack(@Body Feedback feedback);

    //Pega dados do FeedBack pelo id
    @FormUrlEncoded
    @GET("")
    Call<Integer> getFeedBackPorId(@Field("idFeedBack") int idFeedBack);

    //Inserção de um FeedBack
    @POST("")
    Call<Feedback> salvarFeedBack(@Body Feedback feedback);

    //Atualização de um FeedBack
    @POST("")
    Call<Feedback> atualizarFeedBack(@Body Feedback feedback);

    //Deletar um FeedBack
    @FormUrlEncoded
    @DELETE
    Call<Integer> deletarFeedBack(@Field("idFeedBack") int idFeedBack);


}
