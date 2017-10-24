package find.com.find.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Jaelson on 13/09/2017.
 */

public class Map_User_Fragmento extends Fragment {

    private List<Mapeamento> mapeamentosUser = new ArrayList<>();
    private RecyclerView recyclerView;
    private int arg;
    private Spinner spnCategorias;
    private ProgressDialog dialog;

    public static Map_User_Fragmento newInstance(int arg) {
        Bundle args = new Bundle();
        args.putInt("teste", arg);
        Map_User_Fragmento fragmento = new Map_User_Fragmento();
        fragmento.setArguments(args);
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments().getInt("teste");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_mapeamentos_ususario, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_list);
        FindApiService service = FindApiAdapter.createService(FindApiService.class, UsuarioApplication.getToken().getToken());
        Call<List<Mapeamento>> call = service.getMapeamentosUser(UsuarioApplication.getUsuario().getIdUsuario());
        call.enqueue(new Callback<List<Mapeamento>>() {
            @Override
            public void onResponse(Call<List<Mapeamento>> call, Response<List<Mapeamento>> response) {
                if (response.code() == 200) {
                    mapeamentosUser = response.body();
                    switch (arg) {
                        case 1:
                            recyclerView.setAdapter(new ListAdapter(montarLista(), getContext()));
                            LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layout);
                            break;
                        case 2:
                            recyclerView.setAdapter(new ListAdapter(montarLista(), getContext()));
                            layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layout);
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Mapeamento>> call, Throwable t) {

            }
        });


        return view;
    }

    private List<Mapeamento> montarLista() {
        List<Mapeamento> lista = new ArrayList<>();
        for (Mapeamento mapeamento : mapeamentosUser) {
            switch (arg) {
                case 1:
                    if (mapeamento.isStatus()) {
                        lista.add(mapeamento);
                    }
                    break;
                case 2:
                    if (!mapeamento.isStatus()) {
                        lista.add(mapeamento);
                    }
                    break;
            }
        }
        return lista;
    }

    //Mapeamentos USER
    private void mapeamentosUsuario() {



    }
}