package com.example.boram_funeral.ui.screens.contract.pdf

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View

/**
 * 현재 화면(Activity의 decorView)을 비트맵으로 캡처하는 헬퍼.
 *
 * Pager가 특정 Step으로 이동된 직후 호출하면
 * 해당 Step의 렌더링 결과를 그대로 캡처합니다.
 */
object PdfCaptureHelper {

    private const val TAG = "PdfCaptureHelper"

    /**
     * 현재 Activity의 루트 뷰를 비트맵으로 캡처합니다.
     * Compose View의 경우 draw()가 정상 동작합니다.
     */
    fun captureCurrentScreen(context: Context): Bitmap? {
        return try {
            val activity = context as? Activity ?: return null
            val rootView: View = activity.window.decorView.rootView

            val width  = rootView.width.takeIf  { it > 0 } ?: return null
            val height = rootView.height.takeIf { it > 0 } ?: return null

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)
            rootView.draw(canvas)

            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "화면 캡처 실패", e)
            null
        }
    }

    /**
     * 특정 View만 캡처합니다.
     * Pager 내부의 특정 페이지 View를 직접 전달할 때 사용합니다.
     */
    fun captureView(view: View): Bitmap? {
        return try {
            val width  = view.width.takeIf  { it > 0 } ?: return null
            val height = view.height.takeIf { it > 0 } ?: return null

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)
            view.draw(canvas)

            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "View 캡처 실패", e)
            null
        }
    }
}