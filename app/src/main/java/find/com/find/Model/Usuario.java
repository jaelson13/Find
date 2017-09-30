package find.com.find.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaelson on 31/08/2017.
 */

public class Usuario {

    @SerializedName("idUsuario")
    private Integer idUsuario;
    @SerializedName("nome")
    private String nome;
    @SerializedName("email")
    private String email;
    @SerializedName("senha")
    private String senha;
    @SerializedName("urlImgPerfil")
    private String urlImgPerfil;
    @SerializedName("sexo")
    private String sexo;

    public Usuario() {
    }

    public String getUrlImgPerfil() {
        return urlImgPerfil;
    }

    public void setUrlImgPerfil(String urlImgPerfil) {
        this.urlImgPerfil = urlImgPerfil;
    }

    public String getSexo() { return sexo; }

    public void setSexo(String sexo) { this.sexo = sexo; }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
