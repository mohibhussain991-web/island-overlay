package com.mohib.island

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var islandView: View

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        islandView = FrameLayout(this).apply {
            setBackgroundColor(Color.BLACK)
        }

        val params = WindowManager.LayoutParams(
            600,   // width in px — tuned in step 4
            80,    // height in px — tuned in step 4
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }

        // Read the real status bar / cutout height and offset below it
        islandView.setOnApplyWindowInsetsListener { view, insets ->
            val topInset = insets.getInsets(WindowInsets.Type.statusBars()).top
            params.y = topInset + 8 // small gap below status bar
            windowManager.updateViewLayout(view, params)
            insets
        }

        windowManager.addView(islandView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::islandView.isInitialized) {
            windowManager.removeView(islandView)
        }
    }
}
