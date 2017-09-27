package find.com.find.Activies;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import find.com.find.Fragments.Register_Map_Fragmento;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioAtivoSingleton;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.PermissionUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private NavigationView navigationView;
    private CoordinatorLayout mCoordinatorLayout;
    private Button btnEntrar;
    private static final String TAG = Principal_Activity.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1,REQUEST_CHECAR_GPS = 2,REQUEST_ERRO_PLAY_SERVICES = 1;
    private static final String EXTRA_DIALOG = "dialog";
    private boolean mDeveExibirDialog, flagEnableMap = false;
    int mTentativas;
    Handler mHandler;
    GoogleMap mMap;
    LatLng mOrigem;
    Snackbar snackbar;
    public static Location localizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnEntrar = (Button) findViewById(R.id.principal_btnEntrar);
        if (UsuarioAtivoSingleton.getUsuario() != null) {
            btnEntrar.setVisibility(View.GONE);
            btnEntrar.setClickable(false);
        } else {
            btnEntrar.setVisibility(View.VISIBLE);
            btnEntrar.setClickable(true);
        }

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationMenu();

        mHandler = new Handler();
        mDeveExibirDialog = savedInstanceState == null;
        ImageView imv = (ImageView) findViewById(R.id.imgLocal);

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ultimaLocalizacao();
            }
        });

        //Testar
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addConnectionCallbacks(this).addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        googleApiClient.connect();
        mapFragment.getMapAsync(this);

    }

    //CONFIGURAÇÃO LAYOUT
    private void navigationMenu() {
        //navigationView.getMenu().clear();
        //navigationView.inflateHeaderView(R.layout.nav_header_principal_);
        //navigationView.inflateMenu(R.menu.activity_home2_drawer);

        if (UsuarioAtivoSingleton.getUsuario() == null) {
            navigationView.getMenu().clear();
            navigationView.inflateHeaderView(R.layout.nav_header_principal_login);
            navigationView.inflateMenu(R.menu.activity_home2_drawerlogin);

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateHeaderView(R.layout.nav_header_principal_);
            navigationView.inflateMenu(R.menu.activity_home2_drawer);

        }
    }

    //Ao pressionar o botão voltar do proprio aparelho
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Evento para saver qual item menu foi clicado
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_mapearlocal) {
            ultimaLocalizacao();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().replace(R.id.container, Register_Map_Fragmento.newInstance(1));
            ft.addToBackStack(null);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // CONFIGURAÇÃO MAPA

    //Carregar o mapa no fragmento
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        estilizarMap();
        mMap.setMinZoomPreference(10);
        enableMyLocation();
    }

    //Estilo do mapa
    private void estilizarMap() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Erro ao estilizar");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    //Pega a ultima localização do usuário/Requer permissão caso Android se >= 6
    private void ultimaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            mTentativas = 0;
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));
        } else if (mTentativas < 10) {
            mTentativas++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ultimaLocalizacao();
                }
            }, 2000);
        }
        localizacao = location;
        Log.e(TAG, String.valueOf(location.getLatitude()));
        Log.e(TAG, String.valueOf(location.getLongitude()));

    }

    //Ativa a localição atual do usuário
    private boolean enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(Principal_Activity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            flagEnableMap = true;

        } else {
            flagEnableMap = false;
        }
        return flagEnableMap;
    }


    //Connecção Google play services
    @Override
    public void onConnected(Bundle bundle) {
        verificarGPS();
    }

    //Caso a conexão seja suspensa, connecta de novo
    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    //Caso a conexão seja falha
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClient.disconnect();
    }


    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onStop();

    }

    //Ao retornar o app mostra localização do usuário
    @Override
    protected void onResume() {
        super.onResume();
        enableMyLocation();
    }

    //Método verificar se gps está aticvo
    private void verificarGPS() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder locationSenttingsRequest = new LocationSettingsRequest.Builder();
        locationSenttingsRequest.setAlwaysShow(true);
        locationSenttingsRequest.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient, locationSenttingsRequest.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        ultimaLocalizacao();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if (mDeveExibirDialog) {
                            try {
                                status.startResolutionForResult(Principal_Activity.this, REQUEST_CHECAR_GPS);
                                mDeveExibirDialog = false;
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.wtf("NGVL", "Isso não deveria acontecer");
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DIALOG, mDeveExibirDialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDeveExibirDialog = savedInstanceState.getBoolean(EXTRA_DIALOG, true);
    }

    //Verifica se o google play services está ativo e se o gps está ativo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            googleApiClient.connect();
        } else if (requestCode == REQUEST_CHECAR_GPS) {
            if (resultCode == RESULT_OK) {
                mTentativas = 0;
                mHandler.removeCallbacks(null);
                ultimaLocalizacao();
            } else {
                Toast.makeText(this, "É necessário habilitar a configuração de localização para utilizar o aplicativo", Toast.LENGTH_LONG).show();
            }
        }

    }


    //Verifica se há conexao com a internet
    public boolean verificaConexao() {
        boolean conectado;
        do {
            ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conectivtyManager.getActiveNetworkInfo() != null
                    && conectivtyManager.getActiveNetworkInfo().isAvailable()
                    && conectivtyManager.getActiveNetworkInfo().isConnected()) {
                conectado = true;
                snackbar.dismiss();
            } else {
                conectado = false;
                if(!snackbar.isShown()) {
                    snackbar = Snackbar.make(mCoordinatorLayout, "Sem Conexao a internet", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setActionTextColor(Color.WHITE);
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.RED);
                    snackbar.show();
                }

            }
        }while (!conectado);
        return verificaConexao();
    }

    //Alterar Dados do usuário
    public void alterarDados() {

        UsuarioAtivoSingleton.getUsuario().setNome("Nome");
        UsuarioAtivoSingleton.getUsuario().setSexo("Sexo");
        UsuarioAtivoSingleton.getUsuario().setSenha("Senha");
        FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
        final Call<Usuario> call = servicos.atualizarUsuario(UsuarioAtivoSingleton.getUsuario());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                //String retorno = response.body();

                Toast.makeText(getBaseContext(), "Dados Alterados com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), Login_Activity.class);
                startActivity(intent);


            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Desatiar conta do usuário
    public void desativarConta(){
        FindApiService servicos = FindApiAdapter.createService(FindApiService.class);
        final Call<Boolean> call = servicos.desativarUsuario(UsuarioAtivoSingleton.getUsuario().getIdUsuario());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //String retorno = response.body();

                Toast.makeText(getBaseContext(), "Conta Desativada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), Principal_Activity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
            }
        });

    }
}



