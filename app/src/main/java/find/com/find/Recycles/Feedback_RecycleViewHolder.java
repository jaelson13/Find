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

public class Feedback_RecycleViewHolder extends RecyclerView.ViewHolder{
    final TextView nome;
    final RatingBar nota;
    final TextView comentario;


    public Feedback_RecycleViewHolder(View itemView) {
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.recycle_txtNome);
        comentario = (TextView) itemView.findViewById(R.id.recycle_txtComentario);;
        nota = (RatingBar) itemView.findViewById(R.id.recycle_rtNota);
    }
}
