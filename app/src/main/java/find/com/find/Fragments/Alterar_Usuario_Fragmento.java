package find.com.find.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

import java.util.Objects;

import find.com.find.Activies.Principal_Activity;
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

    private TextView edtNome, edtEmail, edtSenha, edtSexo;
    private CardView cardView;
    private EditText edtUrlImagem, senhaAtual, novaSenha;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private FloatingActionButton btnAlterar;
    private Button card_btnAlterar;

    public static Alterar_Usuario_Fragmento newInstance() {
        Alterar_Usuario_Fragmento fragmento = new Alterar_Usuario_Fragmento();
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardview_alterar_dados_ususario, container, false);

        edtNome = (TextView) view.findViewById(R.id.alterar_edtNome);
        edtEmail = (TextView) view.findViewById(R.id.alterar_edtEmail);
        edtSenha = (TextView) view.findViewById(R.id.alterar_edtSenha);
        edtSexo = (TextView) view.findViewById(R.id.alterar_edtSexo);
        cardView = (CardView) view.findViewById(R.id.card_alterarsenha);
        btnAlterar = (FloatingActionButton) view.findViewById(R.id.alterar_btnAlterar);
        card_btnAlterar = (Button) view.findViewById(R.id.card_btnAlterarSenha);
        senhaAtual = (EditText) view.findViewById(R.id.senha_atual);
        novaSenha = (EditText) view.findViewById(R.id.nova_senha);
        atribuirDadosUser();

        edtSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.VISIBLE);
            }
        });

        Button btnFecharCard = (Button) view.findViewById(R.id.card_fechar);
        btnFecharCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
            }
        });

        card_btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarSenha()) {
                    UsuarioApplication.getUsuario().setSenha(novaSenha.getText().toString());

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getUsuario());
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Senha alterada", Toast.LENGTH_SHORT).show();
                                cardView.setVisibility(View.GONE);
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


        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    UsuarioApplication.getUsuario().setNome(edtNome.getText().toString());
                    if (rbMasculino.isChecked()) {
                        UsuarioApplication.getUsuario().setSexo("Masculino");
                    } else {
                        UsuarioApplication.getUsuario().setSexo("Feminino");
                    }

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getUsuario());
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Dados Alterados", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Principal_Activity.class);
                                startActivity(intent);
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

    private boolean validarSenha() {
        if (TextUtils.isEmpty(senhaAtual.getText().toString())) {
            senhaAtual.setError("Campo não pode ser vazio");
            return false;
        }

        if (TextUtils.isEmpty(novaSenha.getText().toString())) {
            novaSenha.setError("Campo não pode ser vazio");
            return false;
        }

        if (!UsuarioApplication.getUsuario().getSenha().equals(senhaAtual.getText().toString())) {
            senhaAtual.setError("Senha incorreta");
            return false;
        }


        return true;
    }

    private void atribuirDadosUser() {
        edtNome.setText(UsuarioApplication.getUsuario().getNome());
        edtEmail.setText(UsuarioApplication.getUsuario().getEmail());
        edtSexo.setText(UsuarioApplication.getUsuario().getSexo());
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(edtNome.getText().toString())) {
            edtNome.setError("Campo não pode ser vazio");
            return false;
        }

        return true;
    }


}
