package find.com.find.Recycles;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import find.com.find.R;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class Locais_RecycleViewHolder extends RecyclerView.ViewHolder{
    final TextView estabelecimento;
    final TextView endereco;
    final CircleImageView imagem;
    final TextView distancia;
    final RatingBar nota;
    final TextView btnVerMapa;

    public Locais_RecycleViewHolder(View itemView) {
        super(itemView);
        estabelecimento = (TextView) itemView.findViewById(R.id.local_txtestabelecimento);
        endereco = (TextView) itemView.findViewById(R.id.local_txtendereco);
        imagem = (CircleImageView) itemView.findViewById(R.id.local_imagem);
        distancia = (TextView) itemView.findViewById(R.id.local_txtdistancia);
        nota = (RatingBar) itemView.findViewById(R.id.local_rtNota);
        btnVerMapa = (TextView) itemView.findViewById(R.id.local_btnVerMapa);
    }
}
