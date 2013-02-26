package dk.itu.bliptest.blip;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import dk.itu.bliptest.blip.model.BlipLocation;
import dk.itu.bliptest.util.NetworkUtils;

public class BlipExecutor {

	private static final String mBlipLocationUrl = "http://pit.itu.dk:7331/location-of/";
	private static final String ERROR = "error";

	public static BlipLocation requestBlipLocation(String bluetoothMacAddress) throws IOException, JSONException {
		if (TextUtils.isEmpty(bluetoothMacAddress))
			throw new IllegalArgumentException("bluetoothMacAdress cannot be null or empty");

		final String blipLocationUrl = mBlipLocationUrl + bluetoothMacAddress;

		final URL url = new URL(blipLocationUrl);
		final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		try {
			final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			final String jsonResponse = NetworkUtils.convertStreamToString(in);
			final JSONObject jsonObject = new JSONObject(jsonResponse);

			if (jsonObject.has(ERROR)) {
				return BlipLocation.newBlipLocationWithError(jsonObject.getString(ERROR));
			}

			return BlipLocation.newJsonToBlipLocation(jsonObject);
		} finally {
			urlConnection.disconnect();
		}
	}
}
