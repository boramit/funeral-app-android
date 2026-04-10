package com.example.boram_funeral.ui.components.contracts.funeral.base


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import com.example.boram_funeral.ui.screens.contract.pdf.LocalIsPdfCapturing

/**
 * 테이블 셀 내부에서 태블릿 펜으로 직접 글씨를 쓸 수 있는 입력 컴포넌트.
 *
 * - [externalTick]: 외부(ViewModel)에서 내려오는 tick — 값이 바뀌면 강제 재구성
 * - [onDraw]: 첫 획이 시작될 때 ViewModel의 tick 증가 함수 호출
 * - [hint]: 아무것도 쓰지 않았을 때 표시되는 안내 텍스트 (X 버튼으로 지우면 다시 표시)
 * - 우측 상단 X 버튼으로 초기화 가능
 */
@Composable
fun HandwrittenAreaCell(
    modifier: Modifier = Modifier,
    externalTick: Int = 0,          // ViewModel tick (재구성 신호)
    onDraw: () -> Unit = {},        // 첫 획 시작 시 호출
    hint: String = "",              // 빈 상태일 때 표시할 힌트 텍스트
) {
    val drawPath   = remember { Path() }
    val eraserPath = remember { Path() }
    var localTick  by remember { mutableStateOf(0) }

    val hasContent = localTick > 0

    // externalTick이 0으로 리셋되면 내용 초기화
    LaunchedEffect(externalTick) {
        if (externalTick == 0) {
            drawPath.reset()
            eraserPath.reset()
            localTick = 0
        }
    }

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .clipToBounds()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val firstDown = awaitFirstDown()
                        val isEraser = firstDown.type == PointerType.Eraser
                        val targetPath = if (isEraser) eraserPath else drawPath

                        if (!isEraser && localTick == 0) onDraw()   // 첫 획 시작 알림

                        targetPath.moveTo(firstDown.position.x, firstDown.position.y)
                        localTick++

                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.changes.any { it.pressed }) {
                                event.changes.forEach { change ->
                                    targetPath.lineTo(change.position.x, change.position.y)
                                    change.consume()
                                }
                                localTick++
                            } else break
                        }
                    }
                }
        ) {
            localTick.let {
                drawIntoCanvas { canvas ->
                    val native = canvas.nativeCanvas
                    val cp = native.saveLayer(0f, 0f, size.width, size.height, null)

                    drawPath(
                        path = drawPath,
                        color = Color.Black,
                        style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    drawPath(
                        path = eraserPath,
                        color = Color.Transparent,
                        style = Stroke(width = 20f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                        blendMode = BlendMode.Clear
                    )

                    native.restoreToCount(cp)
                }
            }
        }

        // 힌트 — 아무것도 안 쓴 상태(localTick == 0)일 때만 표시
        // X 버튼으로 초기화하면 localTick = 0 이 되어 자동으로 다시 표시됨
        if (hint.isNotEmpty() && !hasContent) {
            Text(
                text = hint,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color(0xFFBBBBBB),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        // 내용이 있을 때만 X 버튼 표시 (PDF 캡처 중에는 숨김)
        val isPdfCapturing = LocalIsPdfCapturing.current
        AnimatedVisibility(
            visible = hasContent && !isPdfCapturing,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = {
                    drawPath.reset()
                    eraserPath.reset()
                    localTick = 0   // 0으로 리셋 → 힌트 자동으로 다시 표시
                },
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "지우기",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.LightGray, CircleShape)
                )
            }
        }
    }
}