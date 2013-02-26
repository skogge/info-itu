package dk.itu.bliptest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static String convertStreamToString(final InputStream is) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		final String NEWLINE = "\n";

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(NEWLINE);
			}
		} finally {
			is.close();
		}
		return sb.toString();
	}
}
