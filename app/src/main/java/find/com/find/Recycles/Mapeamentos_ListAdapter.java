package find.com.find.Recycles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.Validacoes;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

/**
 * Created by Jaelson on 19/10/2017.
 */

public class Mapeamentos_ListAdapter extends RecyclerView.Adapter {
    private List<Mapeamento> mapeamentos;
    private Context context;
    private Mapeamentos_RecycleViewHolder holder;
    private Mapeamentos_RecycleViewHolder lista;

    //Imagem
    private Uri imagemSelecionada;
    private Bitmap bitmap;


    public Mapeamentos_ListAdapter(List<Mapeamento> mapeamentos, Context context) {
        this.mapeamentos = mapeamentos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_mapeamentos,parent,false);

        holder = new Mapeamentos_RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        lista = (Mapeamentos_RecycleViewHolder) holder;
        final Mapeamento mapeamento = mapeamentos.get(position);

        lista.estabelecimento.setText(mapeamento.getNomeLocal());
        lista.endereco.setText(mapeamento.getEndereco());
        lista.descricao.setText(mapeamento.getDescricao());
        lista.nota.setRating(mapeamento.getNota());
        lista.notaAvaliacoes.setText("Nota "+String.valueOf(mapeamento.getNota()));
        LayerDrawable stars = (LayerDrawable) lista.nota.getProgressDrawable();
        if (lista.nota.getRating() < 2) {
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else if (lista.nota.getRating() > 1 && lista.nota.getRating() < 4) {
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        } else {
            stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }
        Validacoes.carregarImagemMap(context,mapeamento.getUrlImagem(),lista.imagem);

    }

    @Override
    public int getItemCount() {
        return mapeamentos.size();
    }


}
