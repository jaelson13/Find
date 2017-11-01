package find.com.find.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Jaelson on 01/09/2017.
 */

public class Mapeamento {
    @SerializedName("idMapeamento")
    private int idMapeamento;
    @SerializedName("nomeLocal")
    private String nomeLocal;
    @SerializedName("endereco")
    private String endereco;
    @SerializedName("numeroLocal")
    private String numeroLocal;
    @SerializedName("descricao")
    private String descricao;
    @SerializedName("categoria")
    private String categoria;
    @SerializedName("data")
    private String data;
    @SerializedName("urlImagem")
    private String urlImagem;
    @SerializedName("nota")
    private float nota;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("idUsuario")
    private int idUsuario;
    @SerializedName("status")
    private boolean status;


    public int getIdMapeamento() {
        return idMapeamento;
    }

    public void setIdMapeamento(int idMapeamento) {
        this.idMapeamento = idMapeamento;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumeroLocal() {
        return numeroLocal;
    }

    public void setNumeroLocal(String numeroLocal) {
        this.numeroLocal = numeroLocal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {

        this.data = data;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
