package find.com.find.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import find.com.find.Activies.Principal_Activity;

/**
 * Created by Jaelson on 14/11/2017.
 */

public class GPSTrack implements LocationListener {

    Context context;

    public GPSTrack(Context context) {
        super();
        this.context = context;
    }

    public Location getLocation(){
        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,10,this);
                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return loc;
            }else{
                Log.e("erro","desativado");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

            if (location != null) {
                Principal_Activity.localizacao = location;
                Log.i("LocationGPSTrack", "localizacao"+Principal_Activity.localizacao);
            } else {
                Log.i("LocationGPSTrack","Localização nula");
            }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
