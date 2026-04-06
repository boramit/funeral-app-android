package com.example.boram_funeral.ui.screens.contract.pdf

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.ScrollState
import kotlinx.coroutines.delay

object PdfCaptureHelper {

    private const val TAG = "PdfCaptureHelper"

    private fun findActivity(context: Context): Activity? {
        var ctx = context
        while (ctx is android.content.ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    fun captureCurrentScreen(context: Context): Bitmap? {
        return try {
            val activity = findActivity(context) ?: return null
            captureView(activity.window.decorView.rootView)
        } catch (e: Exception) {
            Log.e(TAG, "화면 캡처 실패", e)
            null
        }
    }

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

    /**
     * 스크롤 가능한 뷰를 처음부터 끝까지 순서대로 캡처해 세로로 이어붙인 전체 비트맵을 반환합니다.
     *
     * 동작 순서:
     * 1) 맨 위로 스크롤 (아래 방향 드래그 반복)
     * 2) 현재 화면 캡처
     * 3) 아래로 드래그 → 스크롤 → 재캡처
     * 4) 캡처 내용이 더 이상 바뀌지 않으면 종료
     * 5) 겹치는 영역을 제거하고 이어붙이기
     */
    suspend fun captureFullScrollContent(
        view: View,
        contentTopPx: Int = 0,
        contentHeightPx: Int = 0,
    ): Bitmap {
        val width  = view.width.takeIf  { it > 0 } ?: return emptyBitmap()
        val height = view.height.takeIf { it > 0 } ?: return emptyBitmap()

        // 캡처 대상 영역 (0 이면 전체)
        val cropTop    = contentTopPx.coerceIn(0, height - 1)
        val cropHeight = if (contentHeightPx > 0) contentHeightPx.coerceIn(1, height - cropTop)
                         else height - cropTop

        // 드래그 거리: 콘텐츠 높이의 60%
        val scrollAmount = (cropHeight * 0.6f).toInt()

        // ── 1. 맨 위로 스크롤 ───────────────────────────────────────────────
        repeat(8) { performDrag(view, fromY = height * 0.15f, toY = height * 0.85f) }
        delay(600)

        val strips = mutableListOf<Bitmap>()
        var prevMiddleRow: IntArray? = null

        // ── 2. 반복 캡처 (최대 10회) ────────────────────────────────────────
        for (attempt in 0 until 10) {
            val fullCapture = captureView(view) ?: break

            // 콘텐츠 영역만 크롭
            val capture = Bitmap.createBitmap(fullCapture, 0, cropTop, width, cropHeight)
            fullCapture.recycle()

            // 콘텐츠 중앙 픽셀 행 샘플링 (비교용)
            val sampleSize = minOf(width, 50)
            val midRow = IntArray(sampleSize).also { arr ->
                capture.getPixels(arr, 0, sampleSize, 0, cropHeight / 2, sampleSize, 1)
            }

            // 이전과 동일 → 더 이상 스크롤 안 됨 → 종료
            if (prevMiddleRow != null && midRow.contentEquals(prevMiddleRow)) {
                capture.recycle()
                break
            }

            strips.add(capture)
            prevMiddleRow = midRow

            // ── 3. 아래로 스크롤 ─────────────────────────────────────────────
            performDrag(view, fromY = height * 0.85f, toY = height * 0.25f)
            delay(450)
        }

        return stitchStrips(strips, viewHeight = cropHeight, scrollAmount = scrollAmount)
    }

    // ── 드래그 제스처 ──────────────────────────────────────────────────────────
    // 느린 속도 + UP 이벤트를 늦게 보내 플링(관성 스크롤) 방지
    private fun performDrag(view: View, fromY: Float, toY: Float) {
        val cx = view.width * 0.85f          // 오른쪽 가장자리 (버튼 영역 회피)
        val startTime = SystemClock.uptimeMillis()
        val steps = 12

        for (i in 0..steps) {
            val y = fromY + (toY - fromY) * i.toFloat() / steps
            val action = if (i == 0) MotionEvent.ACTION_DOWN else MotionEvent.ACTION_MOVE
            MotionEvent.obtain(startTime, startTime + i * 16L, action, cx, y, 0).also {
                view.dispatchTouchEvent(it)
                it.recycle()
            }
        }
        // UP를 2 초 뒤 타임스탬프로 → 속도 ≈ 0 → 플링 없음
        MotionEvent.obtain(startTime, startTime + 2000L, MotionEvent.ACTION_UP, cx, toY, 0).also {
            view.dispatchTouchEvent(it)
            it.recycle()
        }
    }

    // ── 스트립 이어붙이기 ──────────────────────────────────────────────────────
    private fun stitchStrips(strips: List<Bitmap>, viewHeight: Int, scrollAmount: Int): Bitmap {
        if (strips.isEmpty()) return emptyBitmap()
        if (strips.size == 1) return strips[0]

        val width = strips[0].width
        // 스크롤할 때마다 중복되는 높이
        val overlapHeight = viewHeight - scrollAmount
        // 전체 높이 = 첫 스트립 + (이후 스트립별 새로 보인 부분)
        val totalHeight = viewHeight + (strips.size - 1) * scrollAmount

        val result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawColor(android.graphics.Color.WHITE)

        // 첫 스트립 전체
        canvas.drawBitmap(strips[0], 0f, 0f, null)

        // 이후 스트립: 겹치는 부분(위 overlapHeight px)을 잘라낸 뒤 이어붙임
        for (idx in 1 until strips.size) {
            val strip = strips[idx]
            val srcRect  = android.graphics.Rect(0, overlapHeight, width, viewHeight)
            val dstTop   = (viewHeight + (idx - 1) * scrollAmount).toFloat()
            val dstRect  = android.graphics.RectF(0f, dstTop, width.toFloat(), dstTop + scrollAmount)
            canvas.drawBitmap(strip, srcRect, dstRect, null)
        }

        strips.forEach { it.recycle() }
        return result
    }

    /**
     * ScrollState를 programmatic하게 제어하여 전체 내용을 캡처합니다.
     * 드래그 시뮬레이션 없이 안정적으로 전체 계약서를 캡처합니다.
     */
    suspend fun captureWithScrollState(
        scrollState: ScrollState,
        view: View,
        contentTopPx: Int = 0,
        contentHeightPx: Int = 0,
    ): Bitmap {
        val width  = view.width.takeIf  { it > 0 } ?: return emptyBitmap()
        val height = view.height.takeIf { it > 0 } ?: return emptyBitmap()

        val cropTop   = contentTopPx.coerceIn(0, height - 1)
        val viewportH = if (contentHeightPx > 0) contentHeightPx.coerceIn(1, height - cropTop)
                        else height - cropTop

        // 맨 위로 스크롤 후 렌더링 대기
        scrollState.scrollTo(0)
        delay(300)

        val maxScroll = scrollState.maxValue
        if (maxScroll <= 0) {
            // 스크롤 없음 — 현재 화면만 캡처
            val full = captureView(view) ?: return emptyBitmap()
            val cropH = viewportH.coerceAtMost(full.height - cropTop)
            val crop = Bitmap.createBitmap(full, 0, cropTop, width, cropH)
            full.recycle()
            return crop
        }

        val strips          = mutableListOf<Bitmap>()
        val scrollPositions = mutableListOf<Int>()
        var currentScroll   = 0

        while (true) {
            scrollState.scrollTo(currentScroll)
            delay(200)

            val full = captureView(view) ?: break
            val cropH = viewportH.coerceAtMost(full.height - cropTop)
            val crop = Bitmap.createBitmap(full, 0, cropTop, width, cropH)
            full.recycle()
            strips.add(crop)
            scrollPositions.add(currentScroll)

            if (currentScroll >= maxScroll) break
            currentScroll = (currentScroll + viewportH).coerceAtMost(maxScroll)
        }

        if (strips.isEmpty()) return emptyBitmap()
        if (strips.size == 1) return strips[0]

        // 전체 높이 = maxScroll + viewportH (마지막 스크롤 위치 + 뷰포트 높이)
        val totalHeight = maxScroll + viewportH
        val result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawColor(android.graphics.Color.WHITE)

        for ((idx, strip) in strips.withIndex()) {
            canvas.drawBitmap(strip, 0f, scrollPositions[idx].toFloat(), null)
        }
        strips.forEach { it.recycle() }
        return result
    }

    private fun emptyBitmap() = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}
