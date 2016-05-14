package www.jeranderic.gattahomes;

import android.app.Application;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class BeaconListener extends Application implements BeaconManager.MonitoringListener, BeaconManager.ServiceReadyCallback {

    private BeaconManager beaconManager;
    private Region region;
    public int ID;

    @Override
    public void onCreate() {
        super.onCreate();
        ID = 0;
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(5000, 5000);
        region = new Region("Gatta Home Showcase", UUID.fromString("0C22AC37-4957-55F7-AAF6-9579F324E008"), null, null);
        beaconManager.connect(this);
    }

    private void setBeaconId(int id) {
        this.ID = id;
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        int closestBeacon = 0;
        int strongestSignal = 0;
        for (Beacon b : list) {
            if (b.getRssi() > strongestSignal) {
                closestBeacon = b.getMajor();
                strongestSignal = b.getRssi();
            }
        }
        this.setBeaconId(closestBeacon);
    }

    @Override
    public void onExitedRegion(Region region) {

    }

    @Override
    public void onServiceReady() {
        this.beaconManager.startMonitoring(this.region);
    }
}