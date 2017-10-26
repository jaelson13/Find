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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.Validacoes;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 19/09/2017.
 */

public class Register_Map_Fragmento extends Fragment {

    //Constantites
    private static final int PICK_IMAGE = 123;
    private static final int CAM_IMAGE = 124;

    private String[] categorias = {"Escolha uma categoria", "Alimentação / Bebidas", "Banco", "Compras", "Hospedagem", "Lazer", "Religião", "Turismo"};
    private EditText edtEstabelecimento, edtEndereco, edtNumero, edtDescricao;
    private Button btnSolicitar;
    private ImageButton btnOpImage, btnCamera, btnGalery;
    private Spinner spnCategoria;
    private CircleImageView imgMapeamento;

    private Uri imagemSelecionada;
    private Bitmap bitmap;
    private boolean open;

    public static Register_Map_Fragmento newInstance() {
        Register_Map_Fragmento fragmento = new Register_Map_Fragmento();
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_cadastro_mapeamento, container, false);

        //Pegar imagem
        btnOpImage = (ImageButton) view.findViewById(R.id.register_btnOpImage);
        btnCamera = (ImageButton) view.findViewById(R.id.register_btnCamera);
        btnGalery = (ImageButton) view.findViewById(R.id.register_btnGalery);
        imgMapeamento = (CircleImageView) view.findViewById(R.id.map_imgMapeamento);
        pegarImagem();

        edtEstabelecimento = (EditText) view.findViewById(R.id.map_edtEstabelecimento);
        edtEndereco = (EditText) view.findViewById(R.id.map_edtEndereco);
        edtNumero = (EditText) view.findViewById(R.id.map_edtNumero);
        edtDescricao = (EditText) view.findViewById(R.id.map_edtDescricao);
        btnSolicitar = (Button) view.findViewById(R.id.map_btnSolicitar);

        spnCategoria = (Spinner) view.findViewById(R.id.map_spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.layout_spinner, categorias);
        spnCategoria.setAdapter(arrayAdapter);


        //CHAMADA PARA SOLICITAR MAPEAMENTO
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    Mapeamento mapeamento = new Mapeamento();
                    mapeamento.setNomeLocal(edtEstabelecimento.getText().toString());
                    mapeamento.setCategoria(spnCategoria.getSelectedItem().toString());
                    mapeamento.setEndereco(edtEndereco.getText().toString());
                    mapeamento.setNumeroLocal(edtNumero.getText().toString());
                    mapeamento.setDescricao(edtDescricao.getText().toString());
                    mapeamento.setData(Validacoes.getDataAtual());
                    mapeamento.setIdUsuario(UsuarioApplication.getUsuario().getIdUsuario());
                    mapeamento.setLatitude(Principal_Activity.localizacao.getLatitude());
                    mapeamento.setLongitude(Principal_Activity.localizacao.getLongitude());

                    File file = new File(Validacoes.getPath(getContext(),imagemSelecionada));
                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);
                    FindApiService servicos = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                    final Call<Mapeamento> call = servicos.salvarMapeamento(mapeamento.getNomeLocal(),mapeamento.getEndereco(),
                            mapeamento.getDescricao(),mapeamento.getNumeroLocal(),mapeamento.getCategoria()
                            ,mapeamento.getData(),mapeamento.getLatitude(),mapeamento.getLongitude(),mapeamento.getIdUsuario(),body);
                    call.enqueue(new Callback<Mapeamento>() {
                        @Override
                        public void onResponse(Call<Mapeamento> call, Response<Mapeamento> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Solicitação feita com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Principal_Activity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Mapeamento> call, Throwable t) {
                            Toast.makeText(getContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    //vALIDAR cAMPOS DA SOLICITAÇÃO
    private boolean validarCampos() {

        if (TextUtils.isEmpty(edtEstabelecimento.getText().toString())) {
            edtEstabelecimento.setError("Preecha o estabelecimento");
            return false;
        }

        if (TextUtils.isEmpty(edtEndereco.getText().toString())) {
            edtEndereco.setError("Preecha o endereço");
            return false;
        }

        if (TextUtils.isEmpty(edtNumero.getText().toString())) {
            edtNumero.setError("Preecha o numero");
            return false;
        }

        if (spnCategoria.getPrompt() == "Escolha uma categoria") {
            TextView errorText = (TextView) spnCategoria.getSelectedView();
            errorText.setError("Escolha uma categoria");
            return false;
        }

        if (TextUtils.isEmpty(edtDescricao.getText().toString())) {
            edtNumero.setError("Preecha a descricao");
            return false;
        }


        return true;
    }

    //METODO PARA CAPTURAR/TIRAR A IMAGEM
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

    //RETORNO DA INTEÇÃO DAS FOTOS
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
                imgMapeamento.setImageBitmap(bitmap);
            }

            if (requestCode == CAM_IMAGE) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                imgMapeamento.setImageBitmap(bitmap);
                imagemSelecionada = Validacoes.getImageUri(getContext(), bitmap);

            }
        }
    }
}
