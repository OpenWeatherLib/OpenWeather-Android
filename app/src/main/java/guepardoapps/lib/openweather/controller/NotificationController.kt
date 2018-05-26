package guepardoapps.lib.openweather.controller

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.annotation.NonNull
import guepardoapps.lib.openweather.extensions.circleBitmap
import guepardoapps.lib.openweather.models.INotificationContent

class NotificationController(@NonNull private val context: Context) : INotificationController {
    //private val tag: String = NotificationController::class.java.canonicalName

    val currentWeatherId: Int = 260520181
    val forecastWeatherId: Int = 260520182

    private val channelId: String = "guepardoapps.lib.openweather"
    private val channelName: String = "OpenWeather"
    private val channelDescription: String = "Notifications for open weather library"

    private var notificationManager: NotificationManager? = null

    init {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun create(notificationContent: INotificationContent) {
        var bitmap = BitmapFactory.decodeResource(context.resources, notificationContent.largeIcon)
        bitmap = bitmap.circleBitmap(bitmap.height, bitmap.width, Color.BLACK)

        val intent = Intent(context, notificationContent.receiver)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(context, channelId)
                .setContentTitle(notificationContent.title)
                .setContentText(notificationContent.text)
                .setSmallIcon(notificationContent.icon)
                .setLargeIcon(bitmap)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager?.notify(notificationContent.id, notification)
    }

    override fun close() {
        notificationManager?.deleteNotificationChannel(channelId)
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, channelName, importance)

        channel.description = channelDescription
        channel.enableLights(false)
        //channel.lightColor = Color.GREEN
        channel.enableVibration(false)
        //channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }
}