package find.com.find.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import es.dmoral.toasty.Toasty;
import find.com.find.Activies.Login_Activity;
import find.com.find.Activies.Principal_Activity;
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

/**
 * Created by Jaelson on 13/09/2017.
 */

public class Register_Fragmento extends Fragment {
    //Normais
    private EditText edtNome, edtEmail, edtSenha;
    private RadioButton rbFeminino, rbMasculino;
    private Button btnCadastrar,btnEntrar;
    private ImageButton btnVoltar, btnOpImage;
    private ImageView icImage;


    //Imagem
    private File file;
    private Uri imagemSelecionada;
    private Call<Usuario> call;

    ProgressDialog progressDialog;

    public static Register_Fragmento newInstance() {
        Register_Fragmento fragmento = new Register_Fragmento();
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_register, container, false);

        edtNome = (EditText) view.findViewById(R.id.register_edtNome);
        edtEmail = (EditText) view.findViewById(R.id.register_edtEmail);
        edtSenha = (EditText) view.findViewById(R.id.register_edtSenha);
        btnCadastrar = (Button) view.findViewById(R.id.register_btnCadastrar);
        btnEntrar = (Button) view.findViewById(R.id.register_btnEntrar);
        rbFeminino = (RadioButton) view.findViewById(R.id.register_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.register_rbMasculino);
        btnVoltar = (ImageButton) view.findViewById(R.id.register_btnVoltar);

        //Pegar imagem
        btnOpImage = (ImageButton) view.findViewById(R.id.register_btnOpImage);
        icImage = (ImageView) view.findViewById(R.id.register_icImage);
        pegarImagem();
        //Fim Pegar Imagem

        //VOLTAR PARA A TELA PRINCIPAL
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //CHAMADA PARA CADASTRAR O USUÁRIO
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Validacoes.verificaConexao(getContext())){
                if (validarCampos()) {
                    progressDialog = ProgressDialog.show(getContext(), "Por favor aguarde!",
                            "Carregando..", true);
                    Usuario usuario = new Usuario();
                    usuario.setNome(edtNome.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    if (rbFeminino.isChecked()) {
                        usuario.setSexo(rbFeminino.getText().toString());
                    } else {
                        usuario.setSexo(rbMasculino.getText().toString());
                    }

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                    if (imagemSelecionada != null) {
                        try {
                        String path = imagemSelecionada.toString();
                        Log.i("uri",imagemSelecionada.toString());
                        file = new File(new URI(path));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);
                        call = servicos.salvarUsuarioImagem(usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getSexo(), body);
                    } else {
                        call = servicos.salvarUsuario(usuario);
                    }
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                            if (response.code() == 201) {
                                progressDialog.dismiss();
                                Toasty.success(getContext(), "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Login_Activity.class);
                                startActivity(intent);
                            } else if (response.code() == 204) {
                                progressDialog.dismiss();
                                edtEmail.setError("Email já existe");
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toasty.error(getContext(), "Sem conexão..", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Toasty.error(getContext(), "Sem conexão..", Toast.LENGTH_LONG).show();
            }

            }
        });
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    //METODO RESPONSÁVEL PELO LAYOUT REFERENTE A PEGAR A IMAGEM
    private void pegarImagem() {
        btnOpImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getContext(), Register_Fragmento.this);
            }
        });
    }
    //VALIDAR OS CAMPOS PARA O CADASTRO
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

        if (!Validacoes.validarEmail(edtEmail.getText().toString())) {
            edtEmail.setError("E-mail inválido");
            return false;
        }

        if (!rbFeminino.isChecked() && !rbMasculino.isChecked()) {
            rbFeminino.setError("Seleciona o sexo");
            rbMasculino.setError("Seleciona o sexo");
            return false;
        }
        return true;
    }

    //METODO RESPONSAVEL POR CAPTURAR A IMAGEM SELECIONADA E CHAMAR O METODO PARA MUDAR NO SERVIDOR
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setRequestedSize(1024, 1024)
                        .setOutputCompressQuality(70)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .start(getContext(), this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagemSelecionada = result.getUri();
                icImage.setImageURI(imagemSelecionada);
            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
