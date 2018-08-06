package cf.playhi.freezeyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.preference.PreferenceManager;

public class ScreenLockListener {

    private final Context mContext;
    private final ScreenLockBroadcastReceiver mScreenLockReceiver;

    ScreenLockListener(Context context) {
        mContext = context;
        mScreenLockReceiver = new ScreenLockBroadcastReceiver();
    }

    private class ScreenLockBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Intent.ACTION_SCREEN_OFF:
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("onekeyFreezeWhenLockScreen", false)) {
                            if (Build.VERSION.SDK_INT >= 26) {
                                context.startForegroundService(
                                        new Intent(context, OneKeyFreezeService.class)
                                                .putExtra("autoCheckAndLockScreen", false));
                            } else {
                                context.startService(
                                        new Intent(context, OneKeyFreezeService.class)
                                                .putExtra("autoCheckAndLockScreen", false));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenLockReceiver, filter);
    }

    public void unregisterListener() {
        mContext.unregisterReceiver(mScreenLockReceiver);
    }
}
