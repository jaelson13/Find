package find.com.find.Activies;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import find.com.find.Fragments.Alterar_Usuario_Fragmento;
import find.com.find.Fragments.CaixaDialog_Fragmento;
import find.com.find.Fragments.Locais_Fragmento;
import find.com.find.Fragments.Map_User_Fragmento;
import find.com.find.Fragments.Register_Map_Fragmento;
import find.com.find.Model.Feedback;
import find.com.find.Model.Mapeamento;
import find.com.find.Model.UsuarioApplication;
import find.com.find.R;
import find.com.find.Recycles.Feedback_ListAdapter;
import find.com.find.Recycles.Locais_ListAdapter;
import find.com.find.Rota.DirectionFinder;
import find.com.find.Rota.DirectionFinderListener;
import find.com.find.Rota.Route;
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
        GoogleApiClient.OnConnectionFailedListener, LocationListener,DirectionFinderListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest locatioRequest;
    public static Location localizacao;
    private LatLng mOrigem;

    private Spinner spnCategorias;
    private NavigationView navigationView;
    private Button btnEntrar;
    private static final String TAG = Principal_Activity.class.getSimpleName(), EXTRA_DIALOG = "dialog";
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, REQUEST_CHECAR_GPS = 2, REQUEST_ERRO_PLAY_SERVICES = 1;
    private boolean mDeveExibirDialog, conexao;
    private GoogleMap mMap;
    public static List<Mapeamento> mapeamentos = new ArrayList<>();

    private TextView estabelecimento;
    private TextView descricao;
    private TextView endereco;
    private RatingBar nota;
    private ImageView imagem;
    private CardView cardView,cardViewFeedback;
    private TextView avaliar;
    private Button btnFechar,btnFecharCard2;
    private TextView btnVerAvaliacoes;
    private RecyclerView recyclerView;
    private TextView semAv;
    private FloatingActionButton btnTracaRota;

    private AlertDialog.Builder alerta_acesso;
    private AlertDialog.Builder alerta_feedback;

    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        alerta_acesso = new AlertDialog.Builder(this, R.style.AlertDialog);
        alerta_feedback = new AlertDialog.Builder(this, R.style.AlertDialog);


        estabelecimento = (TextView) findViewById(R.id.local_txtestabelecimento);
        endereco = (TextView) findViewById(R.id.local_txtendereco);
        descricao = (TextView) findViewById(R.id.local_txtdescricao);
        imagem = (ImageView) findViewById(R.id.local_imagem);
        nota = (RatingBar) findViewById(R.id.local_rtnota);
        cardView = (CardView) findViewById(R.id.card_Dados);
        cardViewFeedback = (CardView) findViewById(R.id.card_feedbacks);
        btnFechar = (Button) findViewById(R.id.local_btnFechar);
        btnFecharCard2 = (Button) findViewById(R.id.local_btnFecharCard2);
        avaliar = (TextView) findViewById(R.id.local_avaliar);
        btnVerAvaliacoes = (TextView) findViewById(R.id.local_btnVerAvaliacoes);
        semAv = (TextView) findViewById(R.id.recycle_list_semAv);
        btnTracaRota = (FloatingActionButton) findViewById(R.id.local_btnTracarRota);

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

        mDeveExibirDialog = savedInstanceState == null;
        ImageView imv = (ImageView) findViewById(R.id.imgLocal);

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localizacao != null) {
                    mOrigem = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addConnectionCallbacks(this).addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        googleApiClient.connect();
        requestLocation();
        mapFragment.getMapAsync(this);

    }

    private void mostrarSpinner() {
        spnCategorias = (Spinner) findViewById(R.id.spnCategorias);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.layout_spinner, Validacoes.categorias);
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
        tvNome.setText(UsuarioApplication.getUsuario().getNome());
        tvEmail.setText(UsuarioApplication.getUsuario().getEmail());
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
            case R.id.nav_loginmapear:
                alerta_acesso.setMessage("Para mapear um local você deve está logado.")
                        .setTitle("Alerta!")
                        .setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Principal_Activity.this, Login_Activity.class);
                                startActivity(i);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.nav_mapa:
                Intent intent2 = new Intent(this, Principal_Activity.class);
                startActivity(intent2);
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
        todosMapeamentos();
        mostrarSpinner();
        ativarMinhaLocalizacao();
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleApiClient.connect();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
            } else {
                checarPermissaoLocalizacao();
            }
        }else {
            googleApiClient.connect();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
        }
    }
    private void checarPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permissão de localização necessária")
                        .setMessage("Este aplicativo precisa da permissão de localização, aceite para usar a funcionalidade de localização.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Principal_Activity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    //Connexão Google play services
    private void requestLocation(){
        locatioRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(1000);
    }

    @Override
    public void onLocationChanged(Location location) {
        localizacao = location;
        Log.i("local", "Localização: " + String.valueOf(localizacao.getLongitude()) + "|" + String.valueOf(localizacao.getLongitude()));

    }

    @Override
    public void onConnected(Bundle bundle) {
        verificarGPS();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        localizacao = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (localizacao == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locatioRequest, this);
        } else {
            mOrigem = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 15));
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
        testarBotaoEntrar();
        requestLocation();
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
                        //  ativarMinhaLocalizacao();
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


    private void mostrarMarcadores() {
        int cont = 0;
        mMap.clear();
        do {
            for (final Mapeamento mapeamento : mapeamentos) {

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

                marcador.zIndex(cont);
                cont++;

                mMap.addMarker(marcador);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final int index = Math.round(marker.getZIndex());
                        estabelecimento.setText(mapeamentos.get(index).getNomeLocal());
                        endereco.setText(mapeamentos.get(index).getEndereco() + ", " + mapeamentos.get(index).getNumeroLocal());
                        descricao.setText(mapeamentos.get(index).getDescricao());
                        Glide.with(getBaseContext()).load(mapeamentos.get(index).getUrlImagem()).into(imagem);
                        nota.setRating(mapeamentos.get(index).getNota());
                        LayerDrawable stars = (LayerDrawable) nota.getProgressDrawable();
                        if (nota.getRating() < 2) {
                            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        } else if (nota.getRating() > 1 && nota.getRating() < 4) {
                            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                        } else {
                            stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                        }

                        cardView.setVisibility(View.VISIBLE);
                        btnFechar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cardView.setVisibility(View.GONE);
                            }
                        });
                        avaliar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (UsuarioApplication.getUsuario() == null) {
                                    alerta_acesso.setMessage("Para avaliar um local você deve está logado.")
                                            .setTitle("Alerta!")
                                            .setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(Principal_Activity.this, Login_Activity.class);
                                                    startActivity(i);
                                                }
                                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                } else {
                                    CaixaDialog_Fragmento caixaDialog_fragmento = CaixaDialog_Fragmento.newInstance(mapeamentos.get(index).getIdMapeamento());
                                    caixaDialog_fragmento.show(getFragmentManager(), "dialog");
                                }
                            }
                        });
                        btnVerAvaliacoes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FindApiService service = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                                Call<List<Feedback>> call = service.getFeedBacks(mapeamentos.get(index).getIdMapeamento());
                                call.enqueue(new Callback<List<Feedback>>() {
                                    @Override
                                    public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                                        if(response.code() == 200){
                                           if(response.body().isEmpty()){
                                               cardViewFeedback.setVisibility(View.VISIBLE);
                                               semAv.setVisibility(View.VISIBLE);

                                           }else {
                                               recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
                                               recyclerView.setAdapter(null);
                                               recyclerView.setAdapter(new Feedback_ListAdapter(response.body(), getBaseContext()));
                                               LinearLayoutManager layout = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                                               recyclerView.setLayoutManager(layout);
                                               cardViewFeedback.setVisibility(View.VISIBLE);
                                           }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Feedback>> call, Throwable t) {

                                    }
                                });

                            }
                        });
                        btnFecharCard2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cardViewFeedback.setVisibility(View.GONE);
                                semAv.setVisibility(View.GONE);
                            }
                        });
                        btnTracaRota.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cardView.setVisibility(View.GONE);
                                LatLng localPartida = new LatLng(localizacao.getLatitude(),localizacao.getLongitude());
                                LatLng localDestino = new LatLng(mapeamentos.get(index).getLatitude(),mapeamentos.get(index).getLongitude());
                                enviarRequisicao(localPartida,localDestino);
                            }
                        });
                        return false;
                    }
                });
                //Log.i("dados", mapeamento.getCategoria());

            }
        } while (mapeamentos == null);
    }

    //Fim Marcadores
    private void mostrarMarcadoresPorCategoria(String categoria) {
        int cont = 0;
        mMap.clear();
        do {
            for (final Mapeamento mapeamento : mapeamentos) {
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
                    marcador.zIndex(cont);
                    cont++;

                    mMap.addMarker(marcador);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            final int index = Math.round(marker.getZIndex());
                            estabelecimento.setText(mapeamentos.get(index).getNomeLocal());
                            endereco.setText(mapeamentos.get(index).getEndereco() + ", " + mapeamentos.get(index).getNumeroLocal());
                            descricao.setText(mapeamentos.get(index).getDescricao());
                            Glide.with(getBaseContext()).load(mapeamentos.get(index).getUrlImagem()).into(imagem);
                            nota.setRating(mapeamentos.get(index).getNota());
                            LayerDrawable stars = (LayerDrawable) nota.getProgressDrawable();
                            if (nota.getRating() < 2) {
                                stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            } else if (nota.getRating() > 1 && nota.getRating() < 4) {
                                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                            } else {
                                stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                            }

                            cardView.setVisibility(View.VISIBLE);
                            btnFechar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardView.setVisibility(View.GONE);
                                }
                            });
                            avaliar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (UsuarioApplication.getUsuario() == null) {
                                        alerta_acesso.setMessage("Para avaliar um local você deve está logado.")
                                                .setTitle("Alerta!")
                                                .setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(Principal_Activity.this, Login_Activity.class);
                                                        startActivity(i);
                                                    }
                                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    } else {
                                        CaixaDialog_Fragmento caixaDialog_fragmento = CaixaDialog_Fragmento.newInstance(mapeamentos.get(index).getIdMapeamento());
                                        caixaDialog_fragmento.show(getFragmentManager(), "dialog");
                                    }
                                }
                            });
                            btnVerAvaliacoes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FindApiService service = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
                                    Call<List<Feedback>> call = service.getFeedBacks(mapeamentos.get(index).getIdMapeamento());
                                    call.enqueue(new Callback<List<Feedback>>() {
                                        @Override
                                        public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                                            if(response.code() == 200){
                                                if(response.body().isEmpty()){
                                                    cardViewFeedback.setVisibility(View.VISIBLE);
                                                    semAv.setVisibility(View.VISIBLE);

                                                }else {
                                                    recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
                                                    recyclerView.setAdapter(null);
                                                    recyclerView.setAdapter(new Feedback_ListAdapter(response.body(), getBaseContext()));
                                                    LinearLayoutManager layout = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                                                    recyclerView.setLayoutManager(layout);
                                                    cardViewFeedback.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Feedback>> call, Throwable t) {

                                        }
                                    });

                                }
                            });
                            btnFecharCard2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardViewFeedback.setVisibility(View.GONE);
                                    semAv.setVisibility(View.GONE);
                                }
                            });
                            btnTracaRota.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardView.setVisibility(View.GONE);
                                    LatLng localPartida = new LatLng(localizacao.getLatitude(),localizacao.getLongitude());
                                    LatLng localDestino = new LatLng(mapeamentos.get(index).getLatitude(),mapeamentos.get(index).getLongitude());
                                    enviarRequisicao(localPartida,localDestino);
                                }
                            });
                            return false;
                        }
                    });
                    //Log.i("dados", mapeamento.getCategoria());
                }

            }
        }
        while (mapeamentos == null);

    }

    //Fim Marcadores
    private void todosMapeamentos() {

        FindApiService service = FindApiAdapter.createService(FindApiService.class, Validacoes.token);
        Call<List<Mapeamento>> call = service.getAllMapeamentos();
        call.enqueue(new Callback<List<Mapeamento>>() {
            @Override
            public void onResponse(Call<List<Mapeamento>> call, Response<List<Mapeamento>> response) {
                if (response.code() == 200) {
                    mapeamentos = response.body();
                    mostrarMarcadores();
                }
            }

            @Override
            public void onFailure(Call<List<Mapeamento>> call, Throwable t) {

            }
        });

    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Traçando rota.",
                "Carregando..", true);
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
    private void enviarRequisicao(LatLng localPartida,LatLng localDestino){
        try {
            new DirectionFinder(this,localPartida,localDestino).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}

