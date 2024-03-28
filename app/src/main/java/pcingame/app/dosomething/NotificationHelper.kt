package pcingame.app.dosomething

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import androidx.core.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"
        const val NOTIFICATION_CHANNEL_NAME = "My Channel"
        const val NOTIFICATION_DESCRIPTION = "This is my notification channel"
        const val NOTIFICATION_TITLE = "My Notification"
        const val NOTIFICATION_CONTENT = "This is a notification created with Kotlin."
    }

    fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                description = NOTIFICATION_DESCRIPTION
            }

            notificationManager.createNotificationChannel(channel)
        }

        val mainIntent  = MainActivity.newIntent(context.applicationContext)
        //mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(mainIntent)
        val notificationPendingIntent = stackBuilder
            .getPendingIntent(getUniqueId(), FLAG_IMMUTABLE)

        val notification  = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_CONTENT)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

       notificationManager.notify(getUniqueId(), notification)
    }

    private fun getUniqueId() = ((System.currentTimeMillis() %10000).toInt())
}