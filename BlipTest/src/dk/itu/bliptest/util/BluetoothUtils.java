package dk.itu.bliptest.util;

import java.util.Locale;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.TextUtils;

public class BluetoothUtils {

	public static String getBluetoothMacAdress() {
		BluetoothAdapter bluetoothDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
		if ((bluetoothDefaultAdapter != null) && (bluetoothDefaultAdapter.isEnabled())) {
			String bluetoothMacAdress = BluetoothAdapter.getDefaultAdapter().getAddress();
			return TextUtils.isEmpty(bluetoothMacAdress) ? null : bluetoothMacAdress.replace(":", "").toLowerCase(Locale.US);
		}
		return null;
	}

	public static boolean isDiscoverable() {
		BluetoothAdapter bluetoothDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
		if ((bluetoothDefaultAdapter != null) && (bluetoothDefaultAdapter.isEnabled())) {
			if (bluetoothDefaultAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
				return true;
		}
		return false;
	}

	public static void enableBluetoothSettings(Activity activity) {
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
		activity.startActivityForResult(discoverableIntent, 0);
	}
}
