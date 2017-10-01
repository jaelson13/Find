package find.com.find.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 19/09/2017.
 */

public class Alterar_Usuario_Fragmento extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private EditText edtNome;
    private TextView edtEmail;
    private TextView edtSenha;
    private EditText edtUrlImagem;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private Button btnAlterar;

    public static Alterar_Usuario_Fragmento newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Alterar_Usuario_Fragmento fragmento = new Alterar_Usuario_Fragmento();
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
        View view = inflater.inflate(R.layout.cardview_alterar_dados_ususario, container, false);

        edtNome = (EditText) view.findViewById(R.id.alterar_edtNome);
        edtEmail = (TextView) view.findViewById(R.id.alterar_edtEmail);
        edtSenha = (TextView) view.findViewById(R.id.alterar_edtSenha);
        rbFeminino = (RadioButton) view.findViewById(R.id.alterar_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.alterar_rbMasculino);
        btnAlterar = (Button) view.findViewById(R.id.alterar_btnAlterar);
        atribuirDadosUser();

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    UsuarioApplication.getUsuario().setNome(edtNome.getText().toString());
                    if (rbMasculino.isSelected()) {
                        UsuarioApplication.getUsuario().setSexo("Masculino");
                    } else {
                        UsuarioApplication.getUsuario().setSexo("Feminino");
                    }

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getUsuario());
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if(response.code() == 200){
                                Toast.makeText(getContext(), "Dados Alterados", Toast.LENGTH_SHORT).show();
                            }
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

    private void atribuirDadosUser() {
        edtNome.setText(UsuarioApplication.getUsuario().getNome());
        edtEmail.setText(UsuarioApplication.getUsuario().getEmail());
        edtEmail.setClickable(false);
        edtSenha.setText(UsuarioApplication.getUsuario().getSenha());
        edtSenha.setClickable(false);
        if (UsuarioApplication.getInstacia().getUsuario().getSexo().equals("Masculino")) {
            rbMasculino.setChecked(true);
        } else {
            rbFeminino.setChecked(true);

        }
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(edtNome.getText().toString())) {
            edtNome.setError("Campo não pode ser vazio");
            return false;
        }

        return true;
    }


}
