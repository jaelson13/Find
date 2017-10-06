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

    private TextView tvNome, tvEmail, tvSenha, tvSexo;
    private CardView cardView,cardViewDados;
    private EditText edtUrlImagem, senhaAtual, novaSenha,edtNome;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private FloatingActionButton card_btnAlterarDados;
    private Button card_btnAlterar,btnAlterar;

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

        //Principal
        tvNome = (TextView) view.findViewById(R.id.alterar_tvNome);
        tvEmail = (TextView) view.findViewById(R.id.alterar_tvEmail);
        tvSenha = (TextView) view.findViewById(R.id.alterar_tvSenha);
        tvSexo = (TextView) view.findViewById(R.id.alterar_tvSexo);
        //Card Senha
        cardView = (CardView) view.findViewById(R.id.card_alterarsenha);
        card_btnAlterar = (Button) view.findViewById(R.id.card_btnAlterarSenha);
        senhaAtual = (EditText) view.findViewById(R.id.senha_atual);
        novaSenha = (EditText) view.findViewById(R.id.nova_senha);
        //Card Dados
        cardViewDados = (CardView) view.findViewById(R.id.card_alterarDados);
        card_btnAlterarDados = (FloatingActionButton) view.findViewById(R.id.card_btnAlterarDados);
        btnAlterar = (Button) view.findViewById(R.id.alterar_btnAlterar);
        edtNome = (EditText) view.findViewById(R.id.alterar_edtNome);
        rbFeminino = (RadioButton) view.findViewById(R.id.alterar_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.alterar_rbMasculino);
        atribuirDadosUser();

        tvSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.VISIBLE);
            }
        });

        card_btnAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewDados.setVisibility(View.VISIBLE);
            }
        });

        Button btnFecharCard = (Button) view.findViewById(R.id.card_fechar);
        btnFecharCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
            }
        });

        Button btnFecharCardDados = (Button) view.findViewById(R.id.card_fecharDados);
        btnFecharCardDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewDados.setVisibility(View.GONE);
            }
        });
        card_btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarSenha()) {
                    UsuarioApplication.getUsuario().setSenha(novaSenha.getText().toString());

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class,UsuarioApplication.getToken().getToken());
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getToken().getToken(),UsuarioApplication.getUsuario());
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

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class,UsuarioApplication.getToken().getToken());
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getToken().getToken(),UsuarioApplication.getUsuario());
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Dados Alterados", Toast.LENGTH_SHORT).show();
                                cardViewDados.setVisibility(View.GONE);
                                atribuirDadosUser();
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
        tvNome.setText(UsuarioApplication.getUsuario().getNome());
        tvEmail.setText(UsuarioApplication.getUsuario().getEmail());
        tvSexo.setText(UsuarioApplication.getUsuario().getSexo());
        edtNome.setText(UsuarioApplication.getUsuario().getNome());
        if(UsuarioApplication.getUsuario().getSexo().equals("Masculino")){
            rbMasculino.setChecked(true);
        }else {
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
