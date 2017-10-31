package find.com.find.Recycles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Feedback;
import find.com.find.Model.Mapeamento;
import find.com.find.R;
import find.com.find.Util.Validacoes;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class Feedback_ListAdapter extends RecyclerView.Adapter {
    private List<Feedback> feedbacks;
    private Context context;

    public Feedback_ListAdapter(List<Feedback> feedbacks, Context context) {
        this.feedbacks = feedbacks;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_feedbacks, parent, false);

        Feedback_RecycleViewHolder holder = new Feedback_RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Feedback_RecycleViewHolder lista = (Feedback_RecycleViewHolder) holder;

        Feedback feedback = feedbacks.get(position);
        String nome = "Por " + feedback.getUsuario();
        lista.nome.setText(nome);
        lista.nota.setRating(feedback.getNota());
        LayerDrawable stars = (LayerDrawable) lista.nota.getProgressDrawable();
        if (lista.nota.getRating() < 2) {
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else if (lista.nota.getRating() > 1 && lista.nota.getRating() < 4) {
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        } else {
            stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }
        lista.comentario.setText(feedback.getComentario());

    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

}
