package find.com.find.Recycles;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;
import find.com.find.R;
import find.com.find.Util.Validacoes;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class Locais_ListAdapter extends RecyclerView.Adapter {
    private List<Mapeamento> mapeamentos;
    private Context context;
    private Activity activity;

    public Locais_ListAdapter(List<Mapeamento> mapeamentos, Context context, Activity activity) {
        this.mapeamentos = mapeamentos;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_locais, parent, false);

        Locais_RecycleViewHolder holder = new Locais_RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Locais_RecycleViewHolder lista = (Locais_RecycleViewHolder) holder;
        organizar();
        final Mapeamento mapeamento = mapeamentos.get(position);
        lista.estabelecimento.setText(mapeamento.getNomeLocal());
        lista.endereco.setText(mapeamento.getEndereco()+", "+mapeamento.getNumeroLocal());
        Validacoes.carregarImagemMap(context, mapeamento.getUrlImagem(), lista.imagem);
        Location local = new Location("local");
        local.setLatitude(mapeamento.getLatitude());
        local.setLongitude(mapeamento.getLongitude());
        float distancia = Principal_Activity.localizacao.distanceTo(local) / 1000;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (distancia < 1) {
            lista.distancia.setText(decimalFormat.format(distancia) + " m");
        } else {
            lista.distancia.setText(decimalFormat.format(distancia) + " Km");

        }

        lista.nota.setRating(mapeamento.getNota());

        LayerDrawable stars = (LayerDrawable) lista.nota.getProgressDrawable();
        if (lista.nota.getRating() < 2) {
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else if (lista.nota.getRating() > 1 && lista.nota.getRating() < 4) {
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        } else {
            stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }

        lista.btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng local = new LatLng(mapeamentos.get(position).getLatitude(),mapeamentos.get(position).getLongitude());
                activity.getFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                activity.onBackPressed();
                Principal_Activity.moverCamera(local);
        }
        });

    }

    @Override
    public int getItemCount() {
        return mapeamentos.size();
    }

    private float distancia(Mapeamento mapeamento) {
        Location local = new Location("local");
        local.setLatitude(mapeamento.getLatitude());
        local.setLongitude(mapeamento.getLongitude());
        float distancia = Principal_Activity.localizacao.distanceTo(local);

        return distancia;
    }

    private void organizar() {
        Collections.sort(mapeamentos, new Comparator<Mapeamento>() {
            @Override
            public int compare(Mapeamento mapeamento, Mapeamento t1) {
                return Float.valueOf(distancia(mapeamento)).compareTo(distancia(t1));
            }
        });
    }

}
