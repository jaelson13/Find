package find.com.find.Activies;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import find.com.find.Fragments.Alterar_Usuario_Fragmento;
import find.com.find.Fragments.Locais_Fragmento;
import find.com.find.Fragments.Map_User_Fragmento;
import find.com.find.Fragments.Register_Map_Fragmento;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.Usuario;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Services.FindApiAdapter;
import find.com.find.Services.FindApiService;
import find.com.find.Util.PermissionUtils;
import find.com.find.Util.Validacoes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest locatioRequest;
    public static Location localizacao;
    private LatLng mOrigem;

    private String[] categorias = {"Todas Categorias", "Alimentação / Bebidas", "Banco", "Compras", "Hospedagem", "Lazer", "Religião", "Turismo"};
    private Spinner spnCategorias;
    private NavigationView navigationView;
    private Button btnEntrar;
    private static final String TAG = Principal_Activity.class.getSimpleName(), EXTRA_DIALOG = "dialog";
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, REQUEST_CHECAR_GPS = 2, REQUEST_ERRO_PLAY_SERVICES = 1;
    private boolean mDeveExibirDialog, flagEnableMap = false, conexao, flag;
    private int mTentativas;
    private Handler mHandler;
    private GoogleMap mMap;
    public static List<Mapeamento> mapeamentos = new ArrayList<>();
    public static List<Mapeamento> mapeamentosUser = new ArrayList<>();
    private List<Mapeamento> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        testarBotaoEntrar();
        ajusteToolbarNav();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        //Mapa
        mHandler = new Handler();
        mDeveExibirDialog = savedInstanceState == null;
        ImageView imv = (ImageView) findViewById(R.id.imgLocal);

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ativarMinhaLocalizacao();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addConnectionCallbacks(this).addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        googleApiClient.connect();
        locatioRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        mapFragment.getMapAsync(this);

    }

    private void mostrarSpinner() {
        spnCategorias = (Spinner) findViewById(R.id.spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), R.layout.layout_spinner, categorias);
        spnCategorias.setAdapter(arrayAdapter);

        spnCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spnCategorias.getSelectedItem().toString()) {
                    case "Todas Categorias":
                        mostrarMarcadores();
                        break;
                    case "Alimentação / Bebidas":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Banco":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Compras":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Hospedagem":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Lazer":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Religião":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Saúde":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                    case "Turismo":
                        mostrarMarcadoresPorCategoria(spnCategorias.getSelectedItem().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //CONFIGURAÇÃO LAYOUT
    private void testarBotaoEntrar() {
        btnEntrar = (Button) findViewById(R.id.principal_btnEntrar);
        if (UsuarioApplication.getUsuario() != null) {
            btnEntrar.setVisibility(View.GONE);
            btnEntrar.setClickable(false);
        } else {
            btnEntrar.setVisibility(View.VISIBLE);
            btnEntrar.setClickable(true);
        }
    }

    private void ajusteToolbarNav() {
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
    }


    private void navigationMenu() {

        if (UsuarioApplication.getUsuario() == null) {
            navigationView.getMenu().clear();
            navigationView.inflateHeaderView(R.layout.nav_header_principal_login);
            navigationView.inflateMenu(R.menu.activity_home2_drawerlogin);

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateHeaderView(R.layout.nav_header_principal_);
            navigationView.inflateMenu(R.menu.activity_home2_drawer);
            atualizarDadosNavegationView();
        }

    }

    private void atualizarDadosNavegationView() {
        View header = navigationView.getHeaderView(0);
        TextView tvNome = (TextView) header.findViewById(R.id.nome_user);
        TextView tvEmail = (TextView) header.findViewById(R.id.email_user);
        CircleImageView icPerfil = (CircleImageView) header.findViewById(R.id.icPerfil);
        tvNome.setText(UsuarioApplication.getInstacia().getUsuario().getNome());
        tvEmail.setText(UsuarioApplication.getInstacia().getUsuario().getEmail());
        if (UsuarioApplication.getUsuario().getUrlImgPerfil() != null) {
            Validacoes.carregarImagemUser(getBaseContext(), icPerfil);
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

    //Evento para saber qual item menu foi clicado
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm;
        FragmentTransaction ft;

        switch (id) {
            case R.id.nav_mapearlocal:
                onResume();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.container, Register_Map_Fragmento.newInstance());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_ativos:
                onResume();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.container, Map_User_Fragmento.newInstance(1));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_pendentes:
                onResume();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.container, Map_User_Fragmento.newInstance(2));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_alterardados:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.container, Alterar_Usuario_Fragmento.newInstance());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_locais:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction().replace(R.id.container, Locais_Fragmento.newInstance());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_sair:
                UsuarioApplication.setUsuario(null);
                Intent intent = new Intent(this, Principal_Activity.class);
                startActivity(intent);
                finish();
                break;
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
        exibirMarcadores();
        mostrarSpinner();
        todosMapeamentos();


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

    //Ativa a localição atual do usuário
    private void ativarMinhaLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permissão para acessar a localização do usuário
            PermissionUtils.requestPermission(Principal_Activity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Conseguiu acessar a localização do android
            mOrigem = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mostrarMarcadores();
        }
    }

    //Connexão Google play services

    @Override
    public void onLocationChanged(Location location) {
        localizacao = location;
        Toast.makeText(getBaseContext(), "Localização: " + String.valueOf(localizacao.getLongitude()) + "|" + String.valueOf(localizacao.getLongitude()), Toast.LENGTH_SHORT);
        ativarMinhaLocalizacao();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        localizacao = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (localizacao == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locatioRequest, this);
        } else {
            ativarMinhaLocalizacao();
        }

    }

    //Caso a conexão seja suspensa, connecta de novo
    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    //Caso a conexão seja falha
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Inicie uma atividade que tente resolver o erro
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Conexão de serviços de localização falhou com o código" +
                    connectionResult.getErrorCode());
        }
    }

    //Ao retornar o app mostra localização do usuário
    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
        ativarMinhaLocalizacao();
        testarBotaoEntrar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onStop();

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
                        ativarMinhaLocalizacao();
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
                onResume();
            } else {
                Toast.makeText(this, "É necessário habilitar a configuração de localização para utilizar o aplicativo", Toast.LENGTH_LONG).show();
            }
        }

    }


    //Verifica se há conexao com a internet
    public void verificaConexao() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                do {
                    ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conectivtyManager.getActiveNetworkInfo() != null
                            && conectivtyManager.getActiveNetworkInfo().isAvailable()
                            && conectivtyManager.getActiveNetworkInfo().isConnected()) {
                        conexao = true;

                    } else {
                        conexao = false;
                        Toast.makeText(getBaseContext(), "Sem internet", Toast.LENGTH_LONG);
                    }
                } while (!conexao);
            }
        }, 10000);

    }


    //Inicio Exibir Marcadores
    private void exibirMarcadores() {

        FindApiService service = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
        Call<List<Mapeamento>> call = service.getMapeamentos();
        call.enqueue(new Callback<List<Mapeamento>>() {
            @Override
            public void onResponse(Call<List<Mapeamento>> call, Response<List<Mapeamento>> response) {
                if (response.code() == 200) {
                    lista = response.body();
                    mostrarMarcadores();
                }
            }

            @Override
            public void onFailure(Call<List<Mapeamento>> call, Throwable t) {

            }


        });

    }


    private void mostrarMarcadores() {
        int cont = 0;
        mMap.clear();
        do {
            for (final Mapeamento mapeamento : lista) {

                MarkerOptions marcador = new MarkerOptions();
                LatLng local = new LatLng(mapeamento.getLatitude(), mapeamento.getLongitude());
                marcador.position(local);
                Log.i("categoria", mapeamento.getCategoria());
                switch (mapeamento.getCategoria()) {
                    case "Alimentação / Bebidas":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_alimentacao_bebidas));
                        break;
                    case "Banco":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_banco));
                        break;
                    case "Compras":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_compras));
                        break;
                    case "Hospedagem":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_hospedagem));
                        break;
                    case "Lazer":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_lazer));
                        break;
                    case "Religião":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_religiao));
                        break;
                    case "Saúde":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_saude));
                        break;
                    case "Turismo":
                        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_turismo));
                        break;
                }

                marcador.title(mapeamento.getCategoria());
                marcador.zIndex(cont);
                cont++;
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {

                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {
                        int index = Math.round(marker.getZIndex());
                        View v = getLayoutInflater().inflate(R.layout.cardview_inforomacao_marcador, null);

                        CircleImageView imagem = (CircleImageView) v.findViewById(R.id.card_imgMarcador);
                        TextView estabelecimento = (TextView) v.findViewById(R.id.card_txtEstabelecimento);
                        TextView endereco = (TextView) v.findViewById(R.id.card_txtEndereco);
//                        Validacoes.carregarImagemMap(getBaseContext(), lista.get(index).getUrlImagem(), imagem);
                        estabelecimento.setText(lista.get(index).getNomeLocal());
                        endereco.setText(lista.get(index).getEndereco() + ", " + mapeamento.getNumeroLocal());
                        Glide.with(getBaseContext()).load(lista.get(index).getUrlImagem()).into(imagem);

                        return v;
                    }
                });

                mMap.addMarker(marcador);
                Log.i("dados", mapeamento.getCategoria());

            }
        } while (lista == null);
    }

    //Fim Marcadores
    private void mostrarMarcadoresPorCategoria(String categoria) {
        int cont = 0;
        mMap.clear();
        do {
            for (final Mapeamento mapeamento : lista) {
                if (mapeamento.getCategoria().equals(categoria)) {
                    MarkerOptions marcador = new MarkerOptions();
                    LatLng local = new LatLng(mapeamento.getLatitude(), mapeamento.getLongitude());
                    marcador.position(local);
                    switch (mapeamento.getCategoria()) {
                        case "Todas Categorias":
                            mostrarMarcadores();
                            break;
                        case "Alimentação / Bebidas":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_alimentacao_bebidas));
                            break;
                        case "Banco":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_banco));
                            break;
                        case "Compras":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_compras));
                            break;
                        case "Hospedagem":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_hospedagem));
                            break;
                        case "Lazer":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_lazer));
                            break;
                        case "Religião":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_religiao));
                            break;
                        case "Saúde":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_saude));
                            break;
                        case "Turismo":
                            marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_turismo));
                            break;
                    }

                    marcador.title(mapeamento.getCategoria());
                    marcador.zIndex(cont);
                    cont++;
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {

                            return null;
                        }

                        @Override
                        public View getInfoContents(final Marker marker) {
                            int index = Math.round(marker.getZIndex());
                            View v = getLayoutInflater().inflate(R.layout.cardview_inforomacao_marcador, null);

                            CircleImageView imagem = (CircleImageView) v.findViewById(R.id.card_imgMarcador);
                            TextView estabelecimento = (TextView) v.findViewById(R.id.card_txtEstabelecimento);
                            TextView endereco = (TextView) v.findViewById(R.id.card_txtEndereco);
//                        Validacoes.carregarImagemMap(getBaseContext(), lista.get(index).getUrlImagem(), imagem);
                            estabelecimento.setText(lista.get(index).getNomeLocal());
                            endereco.setText(lista.get(index).getEndereco() + ", " + mapeamento.getNumeroLocal());
                            Glide.with(getBaseContext()).load(lista.get(index).getUrlImagem()).into(imagem);

                            return v;
                        }
                    });

                    mMap.addMarker(marcador);
                    Log.i("dados", mapeamento.getCategoria());
                }
            }
        }
        while (lista == null);

    }

    //Fim Marcadores
    private void todosMapeamentos() {

        FindApiService service = FindApiAdapter.createService(FindApiService.class,Validacoes.token);
        Call<List<Mapeamento>> call = service.getAllMapeamentos();
        call.enqueue(new Callback<List<Mapeamento>>() {
            @Override
            public void onResponse(Call<List<Mapeamento>> call, Response<List<Mapeamento>> response) {
                if (response.code() == 200) {
                    mapeamentos = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Mapeamento>> call, Throwable t) {

            }
        });

    }


}



