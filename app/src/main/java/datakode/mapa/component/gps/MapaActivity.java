package datakode.mapa.component.gps;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public GPSTrackerImplementation gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Inicializa o listener (Ouvinte GPS)  do LocationTracker
        initGPSListener();
    }

    @Override
    protected void onDestroy() {
        //Finaliza o listener GPS antes de desativar a activity
        gps.stopUsingGPS();
        super.onDestroy();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void initGPSListener(){
        gps = new GPSTrackerImplementation(this);
        if(gps.canGetLocation()){
            setMarkedLocation(gps.getLocation());
        }else{
            Log.e("DATAKODE","Localização indisponivel");
        }
    }
    public void setMarkedLocation(Location location){
        if(location!=null){
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Minha Localização"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }else{
            Log.e("DATAKODE","Localização indisponivel");
        }
    }
}
