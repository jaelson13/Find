package find.com.find.Recycles;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import find.com.find.R;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class Mapeamentos_RecycleViewHolder extends RecyclerView.ViewHolder{
    final TextView estabelecimento;
    final TextView descricao;
    final TextView endereco;
    final ImageView imagem;
    final TextView notaAvaliacoes;
    final RatingBar nota;

    public Mapeamentos_RecycleViewHolder(View itemView) {
        super(itemView);

        estabelecimento = (TextView) itemView.findViewById(R.id.local_txtestabelecimento);
        endereco = (TextView) itemView.findViewById(R.id.local_txtendereco);
        descricao = (TextView) itemView.findViewById(R.id.local_txtdescricao);
        imagem = (ImageView) itemView.findViewById(R.id.local_imagem);
        nota = (RatingBar) itemView.findViewById(R.id.local_rtnota);
        notaAvaliacoes = (TextView) itemView.findViewById(R.id.local_avaliacoes);
    }
}
