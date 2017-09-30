package find.com.find.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import find.com.find.Activies.Login_Activity;
import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioAtivoSingleton;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 13/09/2017.
 */

public class Login_Fragmento extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;


    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrar;
    private Button btnLogar;
    private ImageButton btnVoltar;
    private boolean flag;


    public static Login_Fragmento newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Login_Fragmento fragmento = new Login_Fragmento();
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
        View view = inflater.inflate(R.layout.fragmento_login, container, false);
        edtEmail = (EditText) view.findViewById(R.id.login_edtEmail);
        edtSenha = (EditText) view.findViewById(R.id.login_edtSenha);
        btnLogar = (Button) view.findViewById(R.id.login_btnLogar);
        btnCadastrar = (Button) view.findViewById(R.id.btnCriarConta);
        btnVoltar = (ImageButton) view.findViewById(R.id.login_btnVoltar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.container, Register_Fragmento.newInstance(1));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Principal_Activity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
                    final Call<Usuario> call = servicos.fazerLogin(edtEmail.getText().toString(), edtSenha.getText().toString());
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                            if (response.isSuccessful()) {
                                switch (response.code()) {
                                    case 200:
                                        Usuario usuario = response.body();
                                        Toast.makeText(getContext(), "Login efetuado", Toast.LENGTH_SHORT).show();
                                        UsuarioAtivoSingleton.setUsuario(usuario);
                                        Intent intent = new Intent(getActivity(), Principal_Activity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;
                                    case 400:
                                        Toast.makeText(getContext(), "Usuario desativado!", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getContext(), "Usuario ou senha invalidos", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.e("erroF",t.getMessage());
                            Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    private boolean validarCampos() {

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Preecha o email");
            return false;
        }

        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Preecha a senha");
            return false;
        }

        if (!validarEmail(edtEmail.getText().toString())) {
            edtEmail.setError("E-mail inválido");
            return false;
        }

        return true;
    }

    private boolean validarEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
