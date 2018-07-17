package datakode.mapa.component.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

public class GPSTrackerImplementation extends LocationTracker {
    private MapaActivity baseActivity;
    public GPSTrackerImplementation(MapaActivity context) {
        super(context);
        baseActivity = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        baseActivity.setMarkedLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}