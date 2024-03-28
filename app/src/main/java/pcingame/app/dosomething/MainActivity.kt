package pcingame.app.dosomething

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.btnPush
import kotlinx.android.synthetic.main.activity_main.txtNotification

class MainActivity : AppCompatActivity() {

    private val notificationHelper = NotificationHelper(this)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationHelper.createNotificationChannel()
        } else {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val message = intent?.getStringExtra(NOTIFICATION_MESSAGE_TAG)
        txtNotification?.text = message
        btnPush?.click {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.e(TAG, "onCreate: PERMISSION GRANTED")
                    notificationHelper.createNotificationChannel()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Snackbar.make(
                        findViewById(R.id.layout_parent),
                        "Notification blocked",
                        Snackbar.LENGTH_LONG
                    ).setAction("Settings") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }.show()
                }

                else -> {
                    Log.e(TAG, "onCreate: ask for permissions")
                    if (Build.VERSION.SDK_INT >= 33) {
                        requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val NOTIFICATION_MESSAGE_TAG = "message from notification"
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            putExtra(
                NOTIFICATION_MESSAGE_TAG, "Hi ☕\uD83C\uDF77\uD83C\uDF70"
            )
        }
    }
}