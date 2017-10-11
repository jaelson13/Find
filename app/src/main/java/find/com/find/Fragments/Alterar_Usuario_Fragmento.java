package find.com.find.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
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
 * Created by Jaelson on 19/09/2017.
 */

public class Alterar_Usuario_Fragmento extends Fragment {

    //Constantites
    private static final int PICK_IMAGE = 123;
    private static final int CAM_IMAGE = 124;
    private Button btnFecharCard;
    private Button btnFecharCardDados;

    private TextView tvNome, tvEmail, tvSenha, tvSexo;
    private CardView cardView, cardViewDados;
    private EditText senhaAtual, novaSenha, edtNome;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private FloatingActionButton card_btnAlterarDados;
    private Button card_btnAlterar, btnAlterar;

    private ImageButton btnOpImage, btnCamera, btnGalery;
    private CircleImageView icPerfil;
    private boolean open;

    //Imagem
    private Uri imagemSelecionada;
    private Bitmap bitmap;

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
        btnFecharCard = (Button) view.findViewById(R.id.card_fechar);
        btnFecharCardDados = (Button) view.findViewById(R.id.card_fecharDados);

        //Card Dados
        cardViewDados = (CardView) view.findViewById(R.id.card_alterarDados);
        card_btnAlterarDados = (FloatingActionButton) view.findViewById(R.id.card_btnAlterarDados);
        btnAlterar = (Button) view.findViewById(R.id.alterar_btnAlterar);
        edtNome = (EditText) view.findViewById(R.id.alterar_edtNome);
        rbFeminino = (RadioButton) view.findViewById(R.id.alterar_rbFeminino);
        rbMasculino = (RadioButton) view.findViewById(R.id.alterar_rbMasculino);
        icPerfil = (CircleImageView) view.findViewById(R.id.alterar_icPerfil);
        atribuirDadosUser();
        cardsView();

        //Pegar imagem
        btnOpImage = (ImageButton) view.findViewById(R.id.register_btnOpImage);
        btnCamera = (ImageButton) view.findViewById(R.id.register_btnCamera);
        btnGalery = (ImageButton) view.findViewById(R.id.register_btnGalery);
        pegarImagem();
        //Fim Pegar Imagem

        card_btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarSenha()) {
                    UsuarioApplication.getUsuario().setSenha(novaSenha.getText().toString());

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, UsuarioApplication.getToken().getToken());
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getToken().getToken(), UsuarioApplication.getUsuario());
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

                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, UsuarioApplication.getToken().getToken());
                    final Call<Usuario> call = servicos.atualizarUsuario(UsuarioApplication.getToken().getToken(), UsuarioApplication.getUsuario());
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
                icPerfil.setImageBitmap(bitmap);
                alterarUsuarioImagem();
            }

            if (requestCode == CAM_IMAGE) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                icPerfil.setImageBitmap(bitmap);
                imagemSelecionada = Validacoes.getImageUri(getContext(), bitmap);
                alterarUsuarioImagem();
            }
        }
    }

    private void alterarUsuarioImagem() {
        FindApiService servicos = FindApiAdapter.createService(FindApiService.class, UsuarioApplication.getToken().getToken());
        File file = new File(Validacoes.getPath(getContext(), imagemSelecionada));
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);
        Call<ResponseBody> call = servicos.atualizarUsuarioImagem(UsuarioApplication.getUsuario().getIdUsuario(),UsuarioApplication.getUsuario().getEmail(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Imagem alterada com sucesso", Toast.LENGTH_SHORT).show();
                    String url = null;
                    try {
                        url = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UsuarioApplication.getUsuario().setUrlImgPerfil(url);
                    Log.i("url",UsuarioApplication.getUsuario().getUrlImgPerfil());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Falha", t.getMessage());
                Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
            }
        });
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

        if (UsuarioApplication.getUsuario().getSexo().equals("Masculino")) {
            rbMasculino.setChecked(true);
        } else {
            rbFeminino.setChecked(true);
        }
        if (UsuarioApplication.getUsuario().getUrlImgPerfil() != null) {
            Picasso.with(getContext()).invalidate(UsuarioApplication.getUsuario().getUrlImgPerfil());
            Picasso.with(getActivity()).load(UsuarioApplication.getUsuario().getUrlImgPerfil()).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(icPerfil);
        }
    }


    private boolean validarCampos() {
        if (TextUtils.isEmpty(edtNome.getText().toString())) {
            edtNome.setError("Campo não pode ser vazio");
            return false;
        }

        return true;
    }


    private void cardsView() {
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


        btnFecharCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
            }
        });


        btnFecharCardDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewDados.setVisibility(View.GONE);
            }
        });
    }

    private void pegarImagem() {
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
    }
}
