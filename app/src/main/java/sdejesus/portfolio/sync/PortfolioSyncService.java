package sdejesus.portfolio.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by sdejesus on 7/4/16.
 */

public class PortfolioSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PortfolioSyncAdapter sSPortfolioSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSPortfolioSyncAdapter == null) {
                sSPortfolioSyncAdapter = new PortfolioSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSPortfolioSyncAdapter.getSyncAdapterBinder();
    }
}