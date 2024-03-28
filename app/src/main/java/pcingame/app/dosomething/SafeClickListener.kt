package pcingame.app.dosomething

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

const val DEFAULT_INTERVAL = 1000L
var isLockClick = false

class SafeClickListener(
    private var defaultInterval: Long = DEFAULT_INTERVAL,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        if (isLockClick) return
        isLockClick = true
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
        Handler(Looper.getMainLooper()).postDelayed({ isLockClick = false }, defaultInterval)
    }
}