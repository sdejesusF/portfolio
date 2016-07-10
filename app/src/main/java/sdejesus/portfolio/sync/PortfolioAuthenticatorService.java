package sdejesus.portfolio.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sdejesus on 7/3/16.
 */

public class PortfolioAuthenticatorService extends Service {
    private PortfolioAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new PortfolioAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
