package find.com.find.Activies;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import find.com.find.Fragments.Register_Fragmento;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.NotificacaoIdService;
import find.com.find.Util.Validacoes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrar;
    private Button btnLogar;
    private ImageButton btnVoltar;
    private CardView card_enviarEmail;
    private EditText card_edtEmail;
    private Button card_fechar,card_btnRecuperarSenha,login_btnRecupSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.login_edtEmail);
        edtSenha = (EditText)findViewById(R.id.login_edtSenha);
        btnLogar = (Button) findViewById(R.id.login_btnLogar);
        btnCadastrar = (Button)findViewById(R.id.btnCriarConta);
        btnVoltar = (ImageButton) findViewById(R.id.login_btnVoltar);

        //CARDVIEW
        card_enviarEmail = (CardView)findViewById(R.id.card_enviarEmail);
        card_btnRecuperarSenha = (Button) findViewById(R.id.card_btnRecuperarSenha);
        card_fechar = (Button) findViewById(R.id.card_fechar);
        card_edtEmail = (EditText) findViewById(R.id.card_edtEmail);
        login_btnRecupSenha = (Button) findViewById(R.id.login_btnRecupSenha);
        cardsView();

        //CHAMADA PARA RECUPERAR SENHA
        card_btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(card_edtEmail.getText().toString())) {
                    edtEmail.setError("Preecha o email");
                }else{
                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class,Validacoes.token);
                    final Call<Void> call = servicos.recuperarSenha(card_edtEmail.getText().toString());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            switch (response.code()) {
                                case 200:
                                    Toast.makeText(getBaseContext(), "Senha enviada, Acesse seu email", Toast.LENGTH_SHORT).show();
                                    card_enviarEmail.setVisibility(View.GONE);
                                    break;
                                case 204:
                                    Toast.makeText(getBaseContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                                    card_edtEmail.setError("Email inválido");
                                    break;
                                case 404:
                                    Toast.makeText(getBaseContext(), "Erro inesperado", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }


                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("erroF", t.getMessage());
                            // Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //CHAMAR FRAGMENTO CADASTRO
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.container, Register_Fragmento.newInstance());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //VOLTAR A TELA PRINCIPAL
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Principal_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //CHAMADA RESPONSÁVEL POR FAZER LOGIN
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class,Validacoes.token);
                    String email = edtEmail.getText().toString();
                    String senha = edtSenha.getText().toString();
                    String senhaSha = Validacoes.convertSha1(senha);
                    Log.i("senha",senhaSha);
                    final Call<Usuario> call = servicos.fazerLogin(email,senha);
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            switch (response.code()) {
                                case 200:
                                    Usuario usuario = response.body();
                                    Toast.makeText(getBaseContext(), "Login efetuado", Toast.LENGTH_SHORT).show();
                                    UsuarioApplication.setUsuario(usuario);
                                    if (UsuarioApplication.getUsuario().getTokenFirebase() == null){
                                        alterarUsuarioToken();
                                    }else if(!UsuarioApplication.getUsuario().getTokenFirebase().equals(NotificacaoIdService.token)){
                                        alterarUsuarioToken();
                                    }
                                    Intent intent = new Intent(Login_Activity.this, Principal_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 204:
                                    Toast.makeText(getBaseContext(), "Usuario desativado!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getBaseContext(), "Usuario ou senha invalidos", Toast.LENGTH_SHORT).show();
                                    break;

                            }

                        }


                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.e("erroF", t.getMessage());
                            // Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void alterarUsuarioToken() {
        UsuarioApplication.getUsuario().setTokenFirebase(NotificacaoIdService.token);
        FindApiService service = FindApiAdapter.createService(FindApiService.class,Validacoes.token);
        Call<Usuario> call = service.atualizarUsuario(UsuarioApplication.getUsuario());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.code()==200){
                    Log.i("atualizacao","Usuario Atualizado - Token: "+UsuarioApplication.getUsuario().getTokenFirebase());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    //VALIDAR CAMPOS DO CADASTRO
    private boolean validarCampos() {

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Preecha o email");
            return false;
        }

        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Preecha a senha");
            return false;
        }

        if (!Validacoes.validarEmail(edtEmail.getText().toString())) {
            edtEmail.setError("E-mail inválido");
            return false;
        }

        return true;
    }

    //METODO RESPONSAVEL POR CARREGAR OS CARDS VIEW QUE SÃO UTILIZADOS
    private void cardsView() {
        login_btnRecupSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_enviarEmail.setVisibility(View.VISIBLE);
            }
        });


        card_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_enviarEmail.setVisibility(View.GONE);
            }
        });
    }

}
