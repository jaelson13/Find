package find.com.find.Services;

import java.util.List;

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

    @Multipart
    @POST("user/add")
    Call<Usuario> salvarUsuarioImagem(@Query("nome") String nome
            , @Query("email") String email, @Query("senha") String senha, @Query("sexo") String sexo, @Part MultipartBody.Part imagem);


    //Atualização de um Usuario
    @Headers("Content-type:application/json")
    @PUT("user/atualizar")
    Call<Usuario> atualizarUsuario(@Body Usuario usuario);

    @Multipart
    @POST("user/atualizarImg")
    Call<ResponseBody> atualizarUsuarioImagem(@Query("idUsuario") int idUsuario, @Query("email") String email, @Part MultipartBody.Part imagem);


    //Desativar um Usuario
    @Headers("Content-type:application/json")
    @PUT("user/desativar")
    Call<Void> desativarUsuario(@Query("idUsuario") int idUsuario);

    //Fazer login
    @Headers("Content-type:application/json")
    @POST("user/login")
    Call<Usuario> fazerLogin(@Query("email") String email, @Query("senha") String senha);

    //Upload Imagem
    //@Headers("Content-type:application/json")
    @Multipart
    @POST("img/add")
    Call<ResponseBody> upImage(@Part MultipartBody.Part file);

    //Recuperar Senha
    @Headers("Content-type:application/json")
    @POST("mail/enviar")
    Call<Void> recuperarSenha(@Query("email") String email);

    //MAPEAMENTO
    //Inserção de um mapeamento
    @Multipart
    @POST("map/add")
    Call<Mapeamento> salvarMapeamento(@Query("nomeLocal") String nomeLocal, @Query("endereco") String endereco,
                                      @Query("descricao") String descricao, @Query("numeroLocal") String numeroLocal,
                                      @Query("categoria") String categoria,
                                      @Query("data") String data, @Query("latitude") double latitude,
                                      @Query("longitude") double longetude, @Query("idUsuario") int idUsuario, @Part MultipartBody.Part imagem);

    //Listar todos os mapeamentos
    @Headers("Content-type:application/json")
    @GET("map/localizar")
    Call<List<Mapeamento>> getMapeamentos();

    //Listar todos os mapeamentos
    @Headers("Content-type:application/json")
    @GET("map/todos")
    Call<List<Mapeamento>> getAllMapeamentos();

    //Listar todos os mapeamentos
    @Headers("Content-type:application/json")
    @GET("map/buscaid/{idUsuario}")
    Call<List<Mapeamento>> getMapeamentosUser(@Path("idUsuario") int idUsuario);

    //Pega dados do mapeamento pelo id
    @FormUrlEncoded
    @GET("")
    Call<Integer> getMapeamentoPorId(@Field("idMapeamento") int idMapeamento);


    //Atualização de um mapeamento
    @Headers("Content-type:application/json")
    @POST("map/atualizar")
    Call<Mapeamento> atualizarMapeamento(@Body Mapeamento mapeamento);

    //Deletar um mapeamento
    @FormUrlEncoded
    @DELETE
    Call<Integer> deletarMapeamento(@Field("idMapeamento") int idMapeamento);


    //FEEDBACK

    //Listar todos os FeedBack
    @Headers("content-type:application/json")
    @POST("feedback/listar")
    Call<List<Feedback>> getFeedBacks(@Query("idMapeamento") int idMapeamento);

    //Pega dados do FeedBack pelo id
    @FormUrlEncoded
    @GET("")
    Call<Integer> getFeedBackPorId(@Field("idFeedBack") int idFeedBack);

    //Inserção de um FeedBack
    @Headers("content-type:application/json")
    @POST("feedback/add")
    Call<Feedback> salvarFeedBack(@Body Feedback feedback);

    //Atualização de um FeedBack
    @POST("")
    Call<Feedback> atualizarFeedBack(@Body Feedback feedback);

    //Deletar um FeedBack
    @FormUrlEncoded
    @DELETE
    Call<Integer> deletarFeedBack(@Field("idFeedBack") int idFeedBack);


}
