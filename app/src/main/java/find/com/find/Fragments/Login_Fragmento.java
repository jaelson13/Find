package find.com.find.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import find.com.find.R;

/**
 * Created by Jaelson on 13/09/2017.
 */

public class Login_Fragmento extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static Login_Fragmento newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
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
        View view = inflater.inflate(R.layout.fragmento_login,container,false);


        return view;
    }
}
