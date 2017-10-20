package find.com.find.Util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import find.com.find.Model.Mapeamento;
import find.com.find.R;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class ListAdapter extends RecyclerView.Adapter {
    private List<Mapeamento> mapeamentos;
    private Context context;

    public ListAdapter(List<Mapeamento> mapeamentos,Context context) {
        this.mapeamentos = mapeamentos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cardview_locais,parent,false);

        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecycleViewHolder lista = (RecycleViewHolder) holder;
        Mapeamento mapeamento = mapeamentos.get(position);

        lista.estabelecimento.setText(mapeamento.getNomeLocal());
        lista.endereco.setText(mapeamento.getEndereco());
        Validacoes.carregarImagemMap(context,mapeamento.getUrlImagem(),lista.imagem);
    }

    @Override
    public int getItemCount() {
        return mapeamentos.size();
    }
}
