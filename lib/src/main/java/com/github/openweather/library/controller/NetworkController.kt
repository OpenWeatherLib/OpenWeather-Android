package com.github.openweather.library.controller

import android.content.Context
import androidx.annotation.NonNull
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log
import com.github.openweather.library.common.Constants
import java.net.NetworkInterface
import java.net.SocketException

@Suppress("DEPRECATION")
internal class NetworkController(@NonNull private val context: Context) : INetworkController {
    private val tag: String = NetworkController::class.java.simpleName

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun isInternetConnected(): Pair<NetworkInfo?, Boolean> {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return Pair(activeNetwork, activeNetwork?.isConnectedOrConnecting == true)
    }

    override fun getIpAddress(): String {
        val ip = StringBuilder()

        try {
            val enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces()

            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface = enumNetworkInterfaces.nextElement()
                val enumInetAddress = networkInterface.inetAddresses

                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()

                    if (inetAddress.isSiteLocalAddress) {
                        ip.append("SiteLocalAddress: ")
                                .append(inetAddress.hostAddress)
                                .append(Constants.String.NewLine)
                    }
                }
            }
        } catch (exception: SocketException) {
            Log.e(tag, exception.message)
            ip.append("Error: Something Wrong! ")
                    .append(exception.toString())
                    .append(Constants.String.NewLine)
        }

        return ip.toString()
    }

    override fun isWifiConnected(): Pair<NetworkInfo?, Boolean> {
        val networkPair = isInternetConnected()
        val isWiFi: Boolean = networkPair.first?.type == ConnectivityManager.TYPE_WIFI
        return Pair(networkPair.first, networkPair.second && isWiFi)
    }

    override fun isHomeWifiConnected(ssid: String): Boolean {
        val networkPair = isWifiConnected()
        if (!networkPair.second) {
            return false
        }

        if (networkPair.first?.type == ConnectivityManager.TYPE_WIFI) {
            return try {
                wifiManager.connectionInfo.ssid.contains(ssid)
            } catch (exception: Exception) {
                val errorString = if (exception.message == null) "HomeSSID failed" else exception.message
                Log.e(tag, errorString)
                false
            }

        }

        Log.w(tag, "Active network is not wifi: ${networkPair.first?.type}")
        return false
    }

    override fun getWifiSsid(): String {
        val networkPair = isWifiConnected()
        if (!networkPair.second) {
            return Constants.String.Empty
        }

        if (networkPair.first?.type == ConnectivityManager.TYPE_WIFI) {
            return wifiManager.connectionInfo.ssid
        }

        Log.w(tag, "Active network is not wifi: ${networkPair.first?.type}")
        return Constants.String.Empty
    }

    override fun getWifiDBM(): Int {
        var dbm = Constants.Defaults.Zero

        if (wifiManager.isWifiEnabled) {
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo != null) {
                dbm = wifiInfo.rssi
            }
        }

        return dbm
    }

    override fun setWifiState(newWifiState: Boolean) {
        wifiManager.isWifiEnabled = newWifiState
    }

    override fun isMobileConnected(): Pair<NetworkInfo?, Boolean> {
        val networkPair = isInternetConnected()
        val isMobile: Boolean = networkPair.first?.type == ConnectivityManager.TYPE_MOBILE
        return Pair(networkPair.first, networkPair.second && isMobile)
    }

    override fun setMobileDataState(newMobileDataState: Boolean) {
        try {
            telephonyManager.javaClass.getDeclaredMethod("setDataEnabled", Boolean::class.javaPrimitiveType).invoke(telephonyManager, newMobileDataState)
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
        }
    }
}