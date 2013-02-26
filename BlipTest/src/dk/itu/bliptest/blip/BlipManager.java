package dk.itu.bliptest.blip;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import android.os.Handler;
import android.os.Looper;
import dk.itu.bliptest.blip.model.BlipLocation;

public class BlipManager {

	private BlipListener mBlipListener;
	private Handler mHandler;
	private Timer mTimer;
	private BlipLocation mLatestBlipLocation = BlipLocation.newInitialBlipLocation();

	private static BlipManager sBlipManager;

	public static interface BlipListener {
		public void onBlipLocationChanged(BlipLocation newBlipLocation, BlipLocation oldBlipLocation);
	}

	private BlipManager() {
		Looper looper = Looper.getMainLooper();

		// This really shouldn't be possible if the application is running, but
		// better safe than sorry
		if (looper == null)
			throw new IllegalThreadStateException("The blipManager couldn't attach to the main thread");

		mHandler = new Handler(looper);
	};

	public static BlipManager getBlipManager() {
		if (sBlipManager == null)
			sBlipManager = new BlipManager();

		return sBlipManager;
	}

	public void requestBlipLocationUpdate(String bluetoothMacAdress, int requestInterval, BlipListener blipListener) {
		if (blipListener == null)
			throw new IllegalArgumentException("blipListener cannot be null");

		removeUpdates();
		mBlipListener = blipListener;
		requestBlipLocation(bluetoothMacAdress, requestInterval);
	}

	public void removeUpdates() {
		interruptBlipRequest();
		if (mBlipListener != null) {
			synchronized (mBlipListener) {
				mBlipListener = null;
			}
		}
	}

	private void interruptBlipRequest() {
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	private void requestBlipLocation(final String bluetoothMacAdress, final int requestInterval) {
		mTimer = new Timer();

		TimerTask blipRequestTask = new TimerTask() {
			@Override
			public void run() {
				try {
					final BlipLocation blipLocation = BlipExecutor.requestBlipLocation(bluetoothMacAdress);

					if (!blipLocation.getmLocation().equals(mLatestBlipLocation.getmLocation()) && !Thread.currentThread().isInterrupted()) {
						final BlipLocation oldBlipLocation = mLatestBlipLocation;
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								if (mBlipListener != null)
									mBlipListener.onBlipLocationChanged(blipLocation, oldBlipLocation);
							}

						});
						synchronized (mLatestBlipLocation) {
							mLatestBlipLocation = blipLocation;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		mTimer.scheduleAtFixedRate(blipRequestTask, 0, requestInterval);
	}

}
