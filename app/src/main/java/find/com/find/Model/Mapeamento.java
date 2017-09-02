package find.com.find.Model;

import java.util.Date;

/**
 * Created by Jaelson on 01/09/2017.
 */

public class Mapeamento {
    private int idMapeamento;
    private String nomeLocal;
    private String numeroLocal;
    private String descricao;
    private String categoria;
    private Date data;
    private String urlImagem;
    private int latitude;
    private int longitude;

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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}
