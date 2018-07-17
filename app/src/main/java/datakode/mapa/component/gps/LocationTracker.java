package datakode.mapa.component.gps;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public abstract class LocationTracker extends Service implements LocationListener {

    private final Context mContext;

    //Instancia da ultima localização percorrida
    private Location location;

    //A distância mínima para alterar atualizações no trajeto, em metros
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros
    // O tempo mínimo entre atualizações em milissegundos
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minuto

    // Declarando o  Location Manager
    protected LocationManager locationManager;

    public static final int TAG_CODE_PERMISSION_LOCATION = 1;

    public LocationTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();
        }else{
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.i("DATAKODE","provider " + LocationManager.GPS_PROVIDER + " location " + location.toString());
                    }else{
                        Log.i("DATAKODE","provider location is null");
                    }
                }

            }else{
                ActivityCompat.requestPermissions((Activity)mContext , new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        TAG_CODE_PERMISSION_LOCATION);
            }
        }


    }
    public void onLocationChanged(Location location) {
        location = this.location;
        Log.i("DATAKODE","GPS : Acurácia: " + location.getAccuracy() + " , Latitude: " + location.getLatitude()+ " , Longitude: " + location.getLongitude());
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    /**
     * Função para checar se GPS/wifi estão habilitados
     * @return boolean
     * */
    public boolean canGetLocation() {
        return location!=null;
    }
    public Location getLocation(){
        return location;
    }
    /**
     * Função para mostrar o diálogo de alerta de configurações
     * Ao pressionar o botão Configurações, você verá as opções de configuração
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS ...");
        alertDialog.setMessage(R.string.gps_is_not_enable);
        alertDialog.setPositiveButton(R.string.configuracoes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


}
