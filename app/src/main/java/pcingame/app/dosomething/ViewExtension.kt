package pcingame.app.dosomething

import android.view.View

fun View.click(timeDelay: Long = DEFAULT_INTERVAL, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(timeDelay) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}