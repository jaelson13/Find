package find.com.find.Model;

/**
 * Created by Jaelson on 01/09/2017.
 */

public class Feedback {

    private int idFeedback;
    private String comentario;
    private float nota;
    private int idUsuario;
    private int idMapeamento;

    public int getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdMapeamento() {
        return idMapeamento;
    }

    public void setIdMapeamento(int idMapeamento) {
        this.idMapeamento = idMapeamento;
    }
}

