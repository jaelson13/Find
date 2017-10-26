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
    final RatingBar nota;

    //card
    final EditText card_estabelecimento;
    final EditText card_endereco;
    final EditText card_numero;
    final EditText card_descricao;
    final Spinner card_spnCategorias;
    final TextView btnAlterar;
    final Button card_btnAlterDados,card_btnfechar;
    final CardView card_alterarDados;
    //final ImageView imagemnova;


    public Mapeamentos_RecycleViewHolder(View itemView) {
        super(itemView);
        estabelecimento = (TextView) itemView.findViewById(R.id.local_txtestabelecimento);
        endereco = (TextView) itemView.findViewById(R.id.local_txtendereco);
        descricao = (TextView) itemView.findViewById(R.id.local_txtdescricao);
        imagem = (ImageView) itemView.findViewById(R.id.local_imagem);
        nota = (RatingBar) itemView.findViewById(R.id.local_rtnota);
        btnAlterar = (TextView) itemView.findViewById(R.id.local_btnAlterarDados);

        card_estabelecimento = (EditText) itemView.findViewById(R.id.card_edtEstabelecimento);
        card_endereco = (EditText) itemView.findViewById(R.id.card_edtEndereco);
        card_numero = (EditText) itemView.findViewById(R.id.card_edtNumero);
        card_descricao = (EditText) itemView.findViewById(R.id.card_edtDescricao);
        card_spnCategorias = (Spinner) itemView.findViewById(R.id.card_spnCategorias);
        card_btnAlterDados = (Button) itemView.findViewById(R.id.card_btnAlterar);
        card_alterarDados = (CardView) itemView.findViewById(R.id.card_alterarDados);
        card_btnfechar = (Button) itemView.findViewById(R.id.card_fechar);


    }
}
