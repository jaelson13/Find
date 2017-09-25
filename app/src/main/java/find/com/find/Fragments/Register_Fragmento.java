package find.com.find.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import find.com.find.Activies.Login_Activity;
import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Usuario;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 13/09/2017.
 */

public class Register_Fragmento extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private RadioGroup radioGenero;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private Button btnCadastrar;
    private ImageButton btnVoltar;
    private ImageButton btnOpImage;
    private ImageButton btnCamera;
    private ImageButton btnGalery;
    private ImageView icImage;
    private Uri imagemSelecionada;
    private boolean flag;
    private boolean open;

    public static Register_Fragmento newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        Register_Fragmento fragmento = new Register_Fragmento();
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
        View view = inflater.inflate(R.layout.fragmento_register,container,false);

        edtNome = (EditText) view.findViewById(R.id.register_edtNome);
        edtEmail = (EditText) view.findViewById(R.id.register_edtEmail);
        edtSenha = (EditText) view.findViewById(R.id.register_edtSenha);
        btnCadastrar = (Button) view.findViewById(R.id.register_btnCadastrar);
        rbFeminino = (RadioButton) view.findViewById(R.id.register_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.register_rbMasculino);
        radioGenero = (RadioGroup) view.findViewById(R.id.radioGenero);
        btnVoltar = (ImageButton) view.findViewById(R.id.register_btnVoltar);

        //Pegar imagem
        btnOpImage = (ImageButton) view.findViewById(R.id.register_btnOpImage);
        btnCamera = (ImageButton) view.findViewById(R.id.register_btnCamera);
        btnGalery = (ImageButton) view.findViewById(R.id.register_btnGalery);
        icImage = (ImageView) view.findViewById(R.id.register_icImage);
        btnOpImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(open){
                    btnCamera.setVisibility(View.GONE);
                    btnCamera.setClickable(false);

                    btnGalery.setVisibility(View.GONE);
                    btnGalery.setClickable(false);

                    open = false;

                } else {
                    btnCamera.setVisibility(View.VISIBLE);
                    btnCamera.setClickable(true);

                    btnGalery.setVisibility(View.VISIBLE);
                    btnGalery.setClickable(true);

                    open = true;
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(i,"Selecionar Foto"), 124);
            }
        });

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i, "Selecionar Foto"), 123);
            }
        });
        //Fim Pegar Imagem

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Principal_Activity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

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

        if(!validarEmail(edtEmail.getText().toString())){
            edtEmail.setError("E-mail inválido");
            return false;
        }

        if (!rbFeminino.isChecked() && !rbMasculino.isChecked()) {
            rbFeminino.setError("Seleciona o sexo");
            rbMasculino.setError("Seleciona o sexo");
            return false;
        }

        if(!validarEmailBanco(edtEmail.getText().toString())){
            edtEmail.setError("Email já existe");
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
    //TESTAR
    private boolean validarEmailBanco(String email) {
        FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
        final Call<Boolean> call = servicos.verificarEmail(email);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                flag = response.body();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
            }
        });

        return flag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
         if (requestCode == 123) {
                imagemSelecionada = data.getData();
                CropImage.activity(imagemSelecionada).setAspectRatio(1,1).start(getActivity());
            }

            if (requestCode == 124) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                icImage.setImageBitmap(bitmap);
            }
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagemSelecionada = result.getUri();
                icImage.setImageURI(imagemSelecionada);
            }
        }
    }
}
