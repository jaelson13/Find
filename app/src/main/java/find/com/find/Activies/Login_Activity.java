package find.com.find.Activies;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import find.com.find.Fragments.Login_Fragmento;
import find.com.find.Fragments.Register_Fragmento;
import find.com.find.R;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       abrirFragment();
    }

    private void abrirFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.container, Login_Fragmento.newInstance(1));
        ft.addToBackStack(null);
        ft.commit();
    }
}
