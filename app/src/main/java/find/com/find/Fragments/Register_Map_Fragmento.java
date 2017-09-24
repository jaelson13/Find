package find.com.find.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import find.com.find.Activies.Login_Activity;
import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioAtivoSingleton;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 19/09/2017.
 */

public class Register_Map_Fragmento extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private String[] categorias = {"Escolha uma categoria","Bares / Alimentação","Educação","Mercado","Shopping Center","Banco","Lazer","Saúde","Religião","Pontos Turísticos"};
    private EditText edtEstabelecimento;
    private EditText edtEndereco;
    private EditText edtNumero;
    private EditText edtDescricao;
    private Button btnSolicitar;
    private Spinner spnCategoria;
    private boolean flag;

    public static Register_Map_Fragmento newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        Register_Map_Fragmento fragmento = new Register_Map_Fragmento();
        fragmento.setArguments(args);
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_cadastro_mapeamento,container,false);

        edtEstabelecimento = (EditText) view.findViewById(R.id.register_map_edtEstabelecimento);
        edtEndereco = (EditText) view.findViewById(R.id.register_map_edtEndereco);
        edtNumero = (EditText) view.findViewById(R.id.register_map_edtNumero);
        edtDescricao = (EditText) view.findViewById(R.id.register_map_edtDescricao);
        btnSolicitar = (Button) view.findViewById(R.id.register_map_btnSolicitar);


        spnCategoria = (Spinner) view.findViewById(R.id.register_map_spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.layout_spinner,categorias);
        spnCategoria.setAdapter(arrayAdapter);


        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    Mapeamento mapeamento = new Mapeamento();
                    mapeamento.setNomeLocal(edtEstabelecimento.getText().toString());
                    mapeamento.setEndereco(edtEndereco.getText().toString());
                    mapeamento.setNumeroLocal(edtNumero.getText().toString());
                    mapeamento.setDescricao(edtDescricao.getText().toString());
                    mapeamento.setData(new Date());
                    mapeamento.setIdUsuario(UsuarioAtivoSingleton.getUsuario().getIdUsuario());
                    mapeamento.setLatitude((int) Principal_Activity.localizacao.getLatitude());
                    mapeamento.setLongitude((int) Principal_Activity.localizacao.getLongitude());

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Mapeamento> call = servicos.salvarMapeamento(mapeamento);
                    call.enqueue(new Callback<Mapeamento>() {
                        @Override
                        public void onResponse(Call<Mapeamento> call, Response<Mapeamento> response) {
                            Toast.makeText(getContext(), "Solicitação feita com sucesso!", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.addToBackStack(null);
                        }

                        @Override
                        public void onFailure(Call<Mapeamento> call, Throwable t) {
                            Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(edtEstabelecimento.getText().toString())) {
            edtEstabelecimento.setError("Preecha o estabelecimento");
            return false;
        }

        if (TextUtils.isEmpty(edtEndereco.getText().toString())) {
            edtEndereco.setError("Preecha o endereço");
            return false;
        }

        if (TextUtils.isEmpty(edtNumero.getText().toString())) {
            edtNumero.setError("Preecha o numero");
            return false;
        }

        if (spnCategoria.getPrompt() == "Escolha uma categoria") {
            TextView errorText = (TextView) spnCategoria.getSelectedView();
            errorText.setError("Escolha uma categoria");
            return false;
        }

        if (TextUtils.isEmpty(edtDescricao.getText().toString())) {
            edtNumero.setError("Preecha a descricao");
            return false;
        }

        return true;
    }


}
