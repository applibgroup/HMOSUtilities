package com.matthewtamlin.android_utilities.library.helpers;



import ohos.app.Context;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.workscheduler.WorkInfo;
import ohos.net.NetCapabilities;
import static ohos.workscheduler.WorkInfo.NETWORK_TYPE_BLUETOOTH;
import static ohos.workscheduler.WorkInfo.NETWORK_TYPE_ETHERNET;
import static ohos.workscheduler.WorkInfo.NETWORK_TYPE_MOBILE;
import static ohos.workscheduler.WorkInfo.NETWORK_TYPE_WIFI;
import static ohos.workscheduler.WorkInfo.NETWORK_TYPE_ANY;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Provides information about the current internet connection.
 */
public class InternetHelper {
	/**
	 * Checks if an internet connection is currently available and returns the type.
	 *
	 * @param context
	 * 		the querying context, not null
	 *
	 * @return the type of the current internet connection, null if there is none
	 */
	public static ConnectionType getInternetConnectionType(final Context context) {
		checkNotNull(context, "context cannot be null.");

		final NetHandle info = getNetworkInfo(context);

		if (info == null) {
			return null;
		} else {
			switch ((int) info.getNetHandle()) {
				case NETWORK_TYPE_MOBILE:
					return ConnectionType.MOBILE;
				case NETWORK_TYPE_WIFI:
					return ConnectionType.WIFI;
				case NETWORK_TYPE_BLUETOOTH:
					return ConnectionType.BLUETOOTH;
				case NETWORK_TYPE_ETHERNET:
					return ConnectionType.ETHERNET;
				case NETWORK_TYPE_ANY:
					return ConnectionType.VPN;

				default:
					return ConnectionType.UNKNOWN;
			}
		}
	}

	/**
	 * @param context
	 * 		a context, not null
	 *
	 * @return the NetworkInfo for the supplied context
	 */
	private static NetHandle getNetworkInfo(final Context context) {
		final NetManager connectivityManager = NetManager.getInstance(context);
		return connectivityManager.getAppNet();
	}

	/**
	 * The different internet connection types available to an Android device.
	 */
	public enum ConnectionType {
		/**
		 * The device is routing all traffic through a WIFI network.
		 */
		WIFI,

		/**
		 * The device is routing all traffic through the mobile data connection.
		 */
		MOBILE,

		/**
		 * The device is routing all traffic through a bluetooth connection.
		 */
		BLUETOOTH,

		/**
		 * The device is routing all traffic through an ethernet connection.
		 */
		ETHERNET,


		/**
		 * The device is routing all traffic through a VPN. The actual nature of the connection is
		 * obscured.
		 */
		VPN,

		/**
		 * The device has an active internet connection, but its nature is unknown.
		 */
		UNKNOWN,
	}
}