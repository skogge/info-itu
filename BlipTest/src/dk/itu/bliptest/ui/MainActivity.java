package dk.itu.bliptest.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import dk.itu.bliptest.R;
import dk.itu.bliptest.blip.BlipManager;
import dk.itu.bliptest.blip.BlipManager.BlipListener;
import dk.itu.bliptest.blip.model.BlipLocation;
import dk.itu.bliptest.util.BluetoothUtils;

public class MainActivity extends Activity implements LocationListener, BlipListener {

	private static final int REQUEST_INTERVAL = 5 * 1000; // 20 seconds
	private BlipManager mBlipManager;
	private LocationManager mLocationManager;
	private Marker mLocationMarker;
	private GroundOverlay mLocationOverlay;
	private GoogleMap mMapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMapView = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setupLocationManagers();
	}

	private void setupLocationManagers() {
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		String bluetoothMacAdress = BluetoothUtils.getBluetoothMacAdress();

		if (bluetoothMacAdress == null || !BluetoothUtils.isDiscoverable()) {
			BluetoothUtils.enableBluetoothSettings(this);
			return;
		}

		mBlipManager = BlipManager.getBlipManager();
		mBlipManager.requestBlipLocationUpdate(bluetoothMacAdress, REQUEST_INTERVAL, this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mLocationManager != null)
			mLocationManager.removeUpdates(this);
		if (mBlipManager != null)
			mBlipManager.removeUpdates();
	}

	private void updateUserLocation(LatLng latLng, float accuracy) {
		mMapView.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		drawUserLocationOnMap(latLng, accuracy);
	}

	private void drawUserLocationOnMap(LatLng latLng, float accuracy) {
		if (mLocationMarker != null)
			mLocationMarker.remove();

		if (mLocationOverlay != null)
			mLocationOverlay.remove();

		mLocationMarker = mMapView.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
				.position(latLng).anchor(0.5f, 0.5f));

		mLocationOverlay = mMapView.addGroundOverlay(new GroundOverlayOptions()
				.image(BitmapDescriptorFactory.fromResource(R.drawable.location_overlay)).anchor(0.5f, 0.5f).position(latLng, accuracy)
				.transparency(0));
	}

	@Override
	public void onBlipLocationChanged(BlipLocation newBlipLocation, BlipLocation oldBlipLocation) {
		// User is not at the ITU anymore - start requesting Android location
		if (newBlipLocation.ismHasError()) {
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, REQUEST_INTERVAL, 10, this);
			return;
		}

		// User is at ITU - there is only need to listen for Blip location
		if (oldBlipLocation.ismHasError()) {
			mLocationManager.removeUpdates(this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		updateUserLocation(new LatLng(location.getLatitude(), location.getLongitude()), location.getAccuracy());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do nothing..

	}

	@Override
	public void onProviderEnabled(String provider) {
		// Do nothing..

	}

	@Override
	public void onProviderDisabled(String provider) {
		// Do nothing..

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			Toast.makeText(this, "The info@itu app cannot be run without bluetooth enabled", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			setupLocationManagers();
		}
	}
}
