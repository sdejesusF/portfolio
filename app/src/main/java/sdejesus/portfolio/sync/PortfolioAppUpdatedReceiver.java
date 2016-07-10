package sdejesus.portfolio.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by sdejesus on 7/9/16.
 */

public class PortfolioAppUpdatedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if (data.toString().equals("package:sdejesus.portfolio")) {
            PortfolioSyncAdapter.syncImmediately(context);
        }
    }
}
