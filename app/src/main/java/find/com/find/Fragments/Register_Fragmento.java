package find.com.find.Fragments;

import android.app.Activity;
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
    //Constantites
    private static final int PICK_IMAGE = 123;
    private static final int CAM_IMAGE = 124;

    //Normais
    private EditText edtNome, edtEmail, edtSenha;
    private RadioButton rbFeminino, rbMasculino;
    private Button btnCadastrar;
    private ImageButton btnVoltar, btnOpImage, btnCamera, btnGalery;
    private ImageView icImage;
    private boolean open;

    //Imagem
    private Uri imagemSelecionada;
    private Bitmap bitmap;
    private Call<Usuario> call;


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
        rbFeminino = (RadioButton) view.findViewById(R.id.register_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.register_rbMasculino);
        btnVoltar = (ImageButton) view.findViewById(R.id.register_btnVoltar);

        //Pegar imagem
        btnOpImage = (ImageButton) view.findViewById(R.id.register_btnOpImage);
        btnCamera = (ImageButton) view.findViewById(R.id.register_btnCamera);
        btnGalery = (ImageButton) view.findViewById(R.id.register_btnGalery);
        icImage = (ImageView) view.findViewById(R.id.register_icImage);
        btnOpImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (open) {
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Camera imagem"), CAM_IMAGE);
                }

            }
        });

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Selecionar imagem"), PICK_IMAGE);
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
                if (validarCampos()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(edtNome.getText().toString());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    if (rbFeminino.isChecked()) {
                        usuario.setSexo(rbFeminino.getText().toString());
                    } else {
                        usuario.setSexo(rbMasculino.getText().toString());
                    }

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, UsuarioApplication.getToken().getToken());
                    if (imagemSelecionada != null) {
                        File file = new File(Validacoes.getPath(getContext(),imagemSelecionada));
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
                                Toast.makeText(getContext(), "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Login_Activity.class);
                                startActivity(intent);
                            } else if (response.code() == 204) {

                                edtEmail.setError("Email já existe");

                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.e("Falha", t.getMessage());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                imagemSelecionada = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagemSelecionada);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                icImage.setImageBitmap(bitmap);
            }

            if (requestCode == CAM_IMAGE) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                icImage.setImageBitmap(bitmap);
                imagemSelecionada = Validacoes.getImageUri(getContext(), bitmap);

            }
        }
    }


}
