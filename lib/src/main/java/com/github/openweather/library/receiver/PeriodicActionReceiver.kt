package com.github.openweather.library.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.github.openweather.library.common.Constants
import com.github.openweather.library.services.intent.OpenWeatherIntentService

internal class PeriodicActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(PeriodicActionReceiver::class.java.simpleName, "onReceive")
        /*
        val bootIntent = Intent(context, OpenWeatherIntentService::class.java).apply {
            putExtra(Constants.OpenWeatherIntentService.IntentActionKey, Constants.OpenWeatherIntentService.IntentActionEnum.PeriodicReload)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(bootIntent)
        } else {
            context?.startService(bootIntent)
        }
        */
    }

    companion object {
        const val requestCode = 432834
    }
}