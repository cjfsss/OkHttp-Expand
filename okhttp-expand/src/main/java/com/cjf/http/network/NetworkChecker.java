package com.cjf.http.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.Keep;


/**
 * <p>Title: NetworkChecker </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 16:25
 */
public class NetworkChecker {

    public interface NetType {
        int Wifi = 1;
        int Wired = 2;
        int Mobile = 3;
        int Mobile2G = 4;
        int Mobile3G = 5;
        int Mobile4G = 6;
        int Mobile5G = 7;
    }

    private ConnectivityManager mManager;

    public NetworkChecker(Context context) {
        this.mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Check the network is enable.
     */
    public boolean isAvailable() {
        return isWifiConnected() || isWiredConnected() || isMobileConnected();
    }

    /**
     * To determine whether a WiFi network is available.
     */
    public final boolean isWifiConnected() {
        return isAvailable(NetType.Wifi);
    }

    /**
     * To determine whether a wired network is available.
     */
    public final boolean isWiredConnected() {
        return isAvailable(NetType.Wired);
    }

    /**
     * Mobile Internet connection.
     */
    public final boolean isMobileConnected() {
        return isAvailable(NetType.Mobile);
    }

    /**
     * 2G Mobile Internet connection.
     */
    public final boolean isMobile2GConnected() {
        return isAvailable(NetType.Mobile2G);
    }

    /**
     * 3G Mobile Internet connection.
     */
    public final boolean isMobile3GConnected() {
        return isAvailable(NetType.Mobile3G);
    }

    /**
     * 4G Mobile Internet connection.
     */
    public final boolean isMobile4GConnected() {
        return isAvailable(NetType.Mobile4G);
    }

    /**
     * 5G Mobile Internet connection.
     */
    public final boolean isMobile5GConnected() {
        return isAvailable(NetType.Mobile5G);
    }

    /**
     * According to the different type of network to determine whether the network connection.
     */
    public final  boolean isAvailable(int netType) {
        return isConnected(netType, mManager.getActiveNetworkInfo());
    }

    @SuppressLint("ObsoleteSdkInt")
    private static boolean isConnected(int netType, NetworkInfo networkInfo) {
        if (networkInfo == null) return false;

        switch (netType) {
            case NetType.Wifi: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
            case NetType.Wired: {
                if (!isConnected(networkInfo)) return false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
                    return networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET;
                return false;
            }
            case NetType.Mobile: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            case NetType.Mobile2G: {
                if (!isConnected(NetType.Mobile, networkInfo)) return false;
                return isMobileSubType(NetType.Mobile2G, networkInfo);
            }
            case NetType.Mobile3G: {
                if (!isConnected(NetType.Mobile, networkInfo)) return false;
                return isMobileSubType(NetType.Mobile3G, networkInfo);
            }
            case NetType.Mobile4G: {
                if (!isConnected(NetType.Mobile, networkInfo)) return false;
                return isMobileSubType(NetType.Mobile4G, networkInfo);
            }
            case NetType.Mobile5G: {
                if (!isConnected(NetType.Mobile, networkInfo)) return false;
                return isMobileSubType(NetType.Mobile5G, networkInfo);
            }
        }
        return false;
    }

    /**
     * Whether network connection.
     */
    private static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private static boolean isMobileSubType(int netType, NetworkInfo networkInfo) {
        switch (networkInfo.getType()) {
            case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: {
                return netType == NetType.Mobile2G;
            }
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP: {
                return netType == NetType.Mobile3G;
            }
            case TelephonyManager.NETWORK_TYPE_IWLAN:
            case TelephonyManager.NETWORK_TYPE_LTE: {
                return netType == NetType.Mobile4G;
            }
            case TelephonyManager.NETWORK_TYPE_NR:
                return netType == NetType.Mobile5G;
            default: {
                String subtypeName = networkInfo.getSubtypeName();
                if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                        || subtypeName.equalsIgnoreCase("WCDMA")
                        || subtypeName.equalsIgnoreCase("CDMA2000")) {
                    return netType == NetType.Mobile3G;
                }
                break;
            }
        }
        return false;
    }
}
