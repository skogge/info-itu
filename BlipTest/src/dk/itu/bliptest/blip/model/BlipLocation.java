package dk.itu.bliptest.blip.model;

import org.json.JSONException;
import org.json.JSONObject;

public class BlipLocation {

	private String mClassOfDevice;
	private String mLastEvent;
	private String mLocation;
	private long mEventTimeStamp;
	private String mTerminalId;
	private String mErrorDescription;
	private boolean mHasError;

	private static final String EMPTY_LOCATION = "empty";

	private static final String CLASS_OF_DEVICE = "major-class-of-device";
	private static final String EVENT_DESCRIPTION = "last-event-description";
	private static final String LOCATION = "location";
	private static final String TIMESTAMP = "last-event-timestamp";
	private static final String TERMINAL_ID = "terminal-id";

	private BlipLocation() {
	};

	private BlipLocation(String mClassOfDevice, String mLastEvent, String mLocation, long mEventTimeStamp, String mTerminalId) {
		super();
		this.mClassOfDevice = mClassOfDevice;
		this.mLastEvent = mLastEvent;
		this.mLocation = mLocation;
		this.mEventTimeStamp = mEventTimeStamp;
		this.mTerminalId = mTerminalId;
	}

	public String getmClassOfDevice() {
		return mClassOfDevice;
	}

	public void setmClassOfDevice(String mClassOfDevice) {
		this.mClassOfDevice = mClassOfDevice;
	}

	public String getmLastEvent() {
		return mLastEvent;
	}

	public void setmLastEvent(String mLastEvent) {
		this.mLastEvent = mLastEvent;
	}

	public String getmLocation() {
		return mLocation;
	}

	public void setmLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public long getmEventTimeStamp() {
		return mEventTimeStamp;
	}

	public void setmEventTimeStamp(long mEventTimeStamp) {
		this.mEventTimeStamp = mEventTimeStamp;
	}

	public String getmTerminalId() {
		return mTerminalId;
	}

	public void setmTerminalId(String mTerminalId) {
		this.mTerminalId = mTerminalId;
	}

	public String getmErrorDescription() {
		return mErrorDescription;
	}

	public void setmErrorDescription(String mErrorDescription) {
		this.mErrorDescription = mErrorDescription;
	}

	public boolean ismHasError() {
		return mHasError;
	}

	public void setmHasError(boolean mHasError) {
		this.mHasError = mHasError;
	}

	@Override
	public String toString() {
		return "[" + mLocation + "]";
	}

	public static BlipLocation newJsonToBlipLocation(JSONObject object) throws JSONException {
		if (object == null)
			throw new NullPointerException("JSONObject cannot be null");

		final String classOfDevice = object.getString(CLASS_OF_DEVICE);
		final String eventDescription = object.getString(EVENT_DESCRIPTION);
		final String location = object.getString(LOCATION);
		final long timestamp = object.getLong(TIMESTAMP);
		final String terminalId = object.getString(TERMINAL_ID);

		return new BlipLocation(classOfDevice, eventDescription, location, timestamp, terminalId);
	}

	public static BlipLocation newBlipLocationWithError(String errorDescription) {
		BlipLocation blipLocation = new BlipLocation();
		blipLocation.setmErrorDescription(errorDescription);
		blipLocation.setmHasError(true);
		blipLocation.setmLocation("");
		return blipLocation;
	}

	public static BlipLocation newInitialBlipLocation() {
		BlipLocation blipLocation = new BlipLocation();
		blipLocation.setmErrorDescription(null);
		blipLocation.setmHasError(true);
		blipLocation.setmLocation(EMPTY_LOCATION);
		return blipLocation;
	}
}
