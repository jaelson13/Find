package find.com.find.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import find.com.find.Activies.Principal_Activity;
import find.com.find.Model.Mapeamento;

import find.com.find.R;
import find.com.find.Recycles.Locais_ListAdapter;
import find.com.find.Util.Validacoes;


/**
 * Created by Jaelson on 13/09/2017.
 */

public class Locais_Fragmento extends Fragment {

    private List<Mapeamento> listaMapeamentoCategoria = new ArrayList<>();
    private RecyclerView recyclerView;
    private Spinner spnCategorias;

    public static Locais_Fragmento newInstance() {
        Locais_Fragmento fragmento = new Locais_Fragmento();
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_locais, container, false);
        Log.i("lista",String.valueOf(Principal_Activity.mapeamentos.size()));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_list);
        recyclerView.setAdapter(new Locais_ListAdapter(Principal_Activity.mapeamentos, getContext()));
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        spnCategorias = (Spinner) view.findViewById(R.id.spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.layout_spinner, Validacoes.categorias);
        spnCategorias.setAdapter(arrayAdapter);
        mostrarSpinner();

        return view;
    }

    private void mostrarSpinner() {
        spnCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spnCategorias.getSelectedItem().toString()) {
                    case "Todas Categorias":
                        recyclerView.setAdapter(new Locais_ListAdapter(Principal_Activity.mapeamentos, getContext()));
                        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layout);
                        break;
                    case "Alimentação / Bebidas":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Banco":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Compras":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Hospedagem":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Lazer":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Religião":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Saúde":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                    case "Turismo":
                        listaMapeamentoCategoria.clear();
                        for(Mapeamento mapeamento : Principal_Activity.mapeamentos){
                            if(spnCategorias.getSelectedItem().toString().equals(mapeamento.getCategoria())){
                                listaMapeamentoCategoria.add(mapeamento);
                            }
                        }
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new Locais_ListAdapter(listaMapeamentoCategoria, getContext()));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}