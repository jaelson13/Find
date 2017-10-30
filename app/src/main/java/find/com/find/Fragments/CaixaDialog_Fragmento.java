package find.com.find.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import find.com.find.Model.Feedback;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.Validacoes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaelson on 30/10/2017.
 */

public class CaixaDialog_Fragmento extends DialogFragment {

    private EditText comentario;
    private RatingBar notaF;
    private Button btnCancelar,btnEnviar;
    private int idMapeamento;

    public static CaixaDialog_Fragmento newInstance(int idMapeamento) {
        Bundle args = new Bundle();
        args.putInt("idMapeamento", idMapeamento);
        CaixaDialog_Fragmento caixaDialog_fragmento = new CaixaDialog_Fragmento();
        caixaDialog_fragmento.setArguments(args);
        return caixaDialog_fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idMapeamento = getArguments().getInt("idMapeamento");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notaF = (RatingBar) view.findViewById(R.id.feedback_rtnota);
        comentario = (EditText) view.findViewById(R.id.feedback_edtComentario);
        btnEnviar = (Button) view.findViewById(R.id.feedback_btnEnviar);
        btnCancelar = (Button) view.findViewById(R.id.feedback_btnCancelar);
        notaF.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float avaliacao, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) notaF.getProgressDrawable();
                if (notaF.getRating() < 3) {
                    stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else if (notaF.getRating() > 2 && notaF.getRating() < 4) {
                    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                } else {
                    stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                }

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback();
                feedback.setComentario(comentario.getText().toString());
                feedback.setNota(notaF.getRating());
                feedback.setIdUsuario(UsuarioApplication.getUsuario().getIdUsuario());
                feedback.setIdMapeamento(idMapeamento);

                FindApiService service = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                Call<Feedback> call = service.salvarFeedBack(feedback);
                call.enqueue(new Callback<Feedback>() {
                    @Override
                    public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                        if(response.code() == 200){
                            Toast.makeText(getActivity(), "Obrigado pelo seu feedback!",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Feedback> call, Throwable t) {

                    }
                });

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(getActivity(), "Cancelar",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_feedback, container, false);
    }
}
