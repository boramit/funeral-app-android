package com.example.boram_funeral.ui.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 현재 화면이 가로 모드인지 반환합니다.
 */
@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * 가로/세로 모드에 따라 다른 폰트 크기를 반환합니다.
 *
 * 사용 예:
 * ```
 * Text(
 *     text = "내용",
 *     fontSize = adaptiveFontSize(portrait = 16.sp, landscape = 13.sp)
 * )
 * ```
 */
@Composable
fun adaptiveFontSize(portrait: TextUnit, landscape: TextUnit): TextUnit {
    return if (isLandscape()) landscape else portrait
}

/**
 * 가로/세로 모드에 따라 다른 Dp 값을 반환합니다. (패딩, 간격, 크기 등에 사용)
 *
 * 사용 예:
 * ```
 * Modifier.padding(adaptiveDp(portrait = 16.dp, landscape = 10.dp))
 * ```
 */
@Composable
fun adaptiveDp(portrait: Dp, landscape: Dp): Dp {
    return if (isLandscape()) landscape else portrait
}
