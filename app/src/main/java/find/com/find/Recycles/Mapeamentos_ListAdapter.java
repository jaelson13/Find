package find.com.find.Recycles;

import android.content.Context;
import android.graphics.Bitmap;
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
    private String[] categorias = {"Alimentação / Bebidas", "Banco", "Compras", "Hospedagem", "Lazer", "Religião", "Turismo"};
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
        Validacoes.carregarImagemMap(context,mapeamento.getUrlImagem(),lista.imagem);


        lista.card_descricao.setText(mapeamento.getDescricao());
        lista.card_estabelecimento.setText(mapeamento.getNomeLocal());
        lista.card_endereco.setText(mapeamento.getEndereco());
        lista.card_numero.setText(mapeamento.getNumeroLocal());
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.layout_spinner, categorias);
        lista.card_spnCategorias.setAdapter(arrayAdapter);

        for(int i = 0;i<categorias.length;i++){
            if(mapeamento.getCategoria().equals(categorias[i])){
                lista.card_spnCategorias.setSelection(i);
            }
        }

        lista.btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.card_alterarDados.setVisibility(View.VISIBLE);
            }
        });

        lista.card_btnfechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.card_alterarDados.setVisibility(View.GONE);
            }
        });

        lista.card_btnAlterDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {
                    mapeamento.setCategoria(lista.card_spnCategorias.getSelectedItem().toString());
                    mapeamento.setDescricao(lista.card_descricao.getText().toString());
                    mapeamento.setEndereco(lista.card_endereco.getText().toString());
                    mapeamento.setNumeroLocal(lista.card_numero.getText().toString());
                    mapeamento.setNomeLocal(lista.card_estabelecimento.getText().toString());

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                    final Call<Mapeamento> call = servicos.atualizarMapeamento(mapeamento);
                    call.enqueue(new Callback<Mapeamento>() {
                        @Override
                        public void onResponse(Call<Mapeamento> call, Response<Mapeamento> response) {
                            if (response.code() == 200) {
                                Toast.makeText(context, "Dados Alterados", Toast.LENGTH_SHORT).show();
                                lista.card_alterarDados.setVisibility(View.GONE);
                                lista.descricao.setText(mapeamento.getDescricao());
                                lista.estabelecimento.setText(mapeamento.getNomeLocal());
                                lista.endereco.setText(mapeamento.getEndereco());
                                lista.card_numero.setText(mapeamento.getNumeroLocal());
                                for(int i = 0;i<categorias.length;i++){
                                    if(mapeamento.getCategoria().equals(categorias[i])){
                                        lista.card_spnCategorias.setSelection(i);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Mapeamento> call, Throwable t) {
                            Toast.makeText(context, "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mapeamentos.size();
    }

    //METODO PARA VALIDAR OS CAMPOS DE ALTERAÇÃO
    private boolean validarCampos() {
        if (TextUtils.isEmpty(lista.card_descricao.getText().toString())) {
            lista.card_descricao.setError("Campo não pode ser vazio");
            return false;
        }
        if (TextUtils.isEmpty(lista.card_endereco.getText().toString())) {
            lista.card_endereco.setError("Campo não pode ser vazio");
            return false;
        }
        if (TextUtils.isEmpty(lista.card_estabelecimento.getText().toString())) {
            lista.card_estabelecimento.setError("Campo não pode ser vazio");
            return false;
        }
        if (TextUtils.isEmpty(lista.card_numero.getText().toString())) {
            lista.card_numero.setError("Campo não pode ser vazio");
            return false;
        }
        return true;
    }

}
