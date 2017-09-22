package find.com.find.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import find.com.find.Activies.Login_Activity;
import find.com.find.Model.Usuario;
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
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtConfSenha;
    private RadioGroup radioGenero;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private Button btnCadastrar;
    private Spinner spnCategorias;
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

        edtNome = (EditText) view.findViewById(R.id.register_edtNome);
        edtEmail = (EditText) view.findViewById(R.id.register_edtEmail);
        edtSenha = (EditText) view.findViewById(R.id.register_edtSenha);
        edtConfSenha = (EditText) view.findViewById(R.id.register_edtConfSenha);
        btnCadastrar = (Button) view.findViewById(R.id.register_btnCadastrar);
        rbFeminino = (RadioButton) view.findViewById(R.id.register_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.register_rbMasculino);
        radioGenero = (RadioGroup) view.findViewById(R.id.radioGenero);

        spnCategorias = (Spinner) view.findViewById(R.id.spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.layout_spinner,categorias);
        spnCategorias.setAdapter(arrayAdapter);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    Usuario usuario = new Usuario();
                    usuario.setNome(edtNome.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    usuario.setStatus(true);
                    if (rbFeminino.isChecked()) {
                        usuario.setSexo(rbFeminino.getText().toString());
                    } else {
                        usuario.setSexo(rbMasculino.getText().toString());
                    }

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Usuario> call = servicos.salvarUsuario(usuario);
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            //String retorno = response.body();

                            Toast.makeText(getContext(), "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),Login_Activity.class);
                            startActivity(intent);



                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(edtNome.getText().toString())) {
            edtNome.setError("Preecha o nome");
            return false;
        }

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Preecha o email");
            return false;
        }

        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Preecha a senha");
            return false;
        }

        if (TextUtils.isEmpty(edtConfSenha.getText().toString())) {
            edtSenha.setError("Confirme a senha");
            return false;
        }

        if(!edtConfSenha.getText().toString().equals(edtSenha.getText().toString())){
            edtSenha.setError("Senhas não são iguais");
            return false;
        }
        if (!rbFeminino.isChecked() && !rbMasculino.isChecked()) {
            rbFeminino.setError("Seleciona o sexo");
            rbMasculino.setError("Seleciona o sexo");
            return false;
        }


        return true;
    }


}
