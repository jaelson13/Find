package find.com.find.Recycles;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public Locais_ListAdapter(List<Mapeamento> mapeamentos, Context context) {
        this.mapeamentos = mapeamentos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_locais, parent, false);

        Locais_RecycleViewHolder holder = new Locais_RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Locais_RecycleViewHolder lista = (Locais_RecycleViewHolder) holder;
        organizar();
        Mapeamento mapeamento = mapeamentos.get(position);
        lista.estabelecimento.setText(mapeamento.getNomeLocal());
        lista.endereco.setText(mapeamento.getEndereco());
        Validacoes.carregarImagemMap(context, mapeamento.getUrlImagem(), lista.imagem);
        Location local = new Location("local");
        local.setLatitude(mapeamento.getLatitude());
        local.setLongitude(mapeamento.getLongitude());
        float distancia = Principal_Activity.localizacao.distanceTo(local) / 1000;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (distancia < 1) {
            lista.distancia.setText("Há " + decimalFormat.format(distancia) + "m");
        } else {
            lista.distancia.setText("Há " + decimalFormat.format(distancia) + "Km");

        }

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
