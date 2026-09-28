package to.holepunch.bare.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

public final class StateReceiver extends BroadcastReceiver {
  private final long nativeId;
  private final boolean isServer;

  public StateReceiver(long nativeId, boolean isServer) {
    this.nativeId = nativeId;
    this.isServer = isServer;
  }

  public void
  register(Context context) {
    IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      context.registerReceiver(this, filter, Context.RECEIVER_NOT_EXPORTED);
    } else {
      context.registerReceiver(this, filter);
    }
  }

  public void
  unregister(Context context) {
    try {
      context.unregisterReceiver(this);
    } catch (IllegalArgumentException e) {
      // Not registered.
    }
  }

  @Override
  public void
  onReceive(Context context, Intent intent) {
    if (!BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) return;

    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
    if (state == BluetoothAdapter.ERROR) return;

    nativeOnStateChange(nativeId, isServer, state);
  }

  private static native void
  nativeOnStateChange(long nativeId, boolean isServer, int state);
}
