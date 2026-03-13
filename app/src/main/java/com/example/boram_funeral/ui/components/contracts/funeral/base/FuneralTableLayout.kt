package com.example.boram_funeral.ui.components.contracts.funeral.base

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.runtime.*
import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.boram_funeral.ui.components.contracts.funeral.FuneralServiceItem


@Composable
fun RowScope.LabelCell(
    text: String,
    weight: Float = 1f,
    modifier: Modifier = Modifier, // 확장성을 위해 modifier 파라미터 추가
    fontSize: TextUnit = 12.sp,
) {
    Box(
        modifier = modifier
            .weight(weight)
            .fillMaxHeight()           // 부모 Row의 높이가 커지면 같이 늘어남
            .heightIn(min = 48.dp)      // 최소 48dp 높이 유지 (InputCell과 통일)
            .background(Color(0xFFF5F5F5))
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(4.dp), // 내부 여백 조정
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ColumnScope.LabelCell(
    text: String,
    modifier: Modifier = Modifier // 유연성을 위해 modifier를 추가합니다.
) {
    Box(
        modifier = modifier
            .fillMaxWidth() // 세로 묶음이므로 가로를 꽉 채웁니다.
            .background(Color(0xFFF5F5F5))
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(vertical = 12.dp, horizontal = 8.dp), // 상하 여백 조절
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RowScope.InputCell(
    value: String,
    onValueChange: (String) -> Unit,
    weight: Float = 1f,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    fontSize: TextUnit = 12.sp,
    isReadOnly: Boolean = false
) {

    val textAlign = when (alignment) {
        Alignment.CenterEnd -> TextAlign.End
        Alignment.CenterStart -> TextAlign.Start
        else -> TextAlign.Center
    }

    BasicTextField(
        value = value,
        readOnly = isReadOnly,
        onValueChange = onValueChange,
        // 필기 인식이 잘 되도록 포커스 및 터치 영역 최적화
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .defaultMinSize(minHeight = 48.dp)
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(horizontal = 8.dp),
        textStyle = TextStyle(fontSize = fontSize, textAlign = textAlign),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(8.dp),
                contentAlignment = alignment,
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = Color.LightGray, fontSize = 11.sp)
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun ColumnScope.InputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth() // 세로 모드에서는 보통 가로를 꽉 채웁니다.
            .height(48.dp)
            .focusable()
            .border(0.5.dp, Color(0xFFD1D1D1)),
        textStyle = TextStyle(fontSize = 16.sp),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = Color.LightGray, fontSize = 11.sp)
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun InlineInputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    width: Dp = 100.dp
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(width),
        textStyle = TextStyle(
            fontSize = 12.sp, // 일반 Text 컴포넌트와 크기를 맞춤
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center // 문장 중간에서는 중앙 정렬이 예쁩니다.
        ),
        decorationBox = { innerTextField ->
            Column {
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(color = Color.Gray, fontSize = 12.sp),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
                // 밑줄만 살짝 주면 '입력란'임을 알 수 있어 UX가 좋아집니다.
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    )
}

@Composable
fun MiniInputCell(
    value: String,
    onValueChange: (String) -> Unit,
    width: Dp = 40.dp
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(width)
            .height(30.dp), // 전체 높이
        textStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center),
        singleLine = true,
        decorationBox = { innerTextField ->
            // Column을 사용하여 위에는 텍스트, 아래에는 선을 배치
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.weight(1f), // 텍스트가 위쪽 공간을 차지
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
                // 선을 텍스트 바로 아래에 배치
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
    )
}

@Composable
fun RowScope.SelectCell(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    weight: Float = 1f,
    placeholder: String = "선택"
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(0.5.dp, Color(0xFFD1D1D1)) // 테이블 테두리 유지
    ) {
        // 이미 만들어둔 커스텀 드롭다운 호출
        CustomDropdownField(
            label = "",
            shape = RectangleShape,
            border = null,
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            fontSize = 12.sp,
            placeholder = placeholder,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// 개별 호실 한 줄을 만드는 컴포넌트
@Composable
fun RoomPriceRow(
    roomName: String,  // "201호실"
    seatCount: String, // "56석"
    size: String,      // "50평형"
    priceH: String,    // "26,000원"
    priceD: String,    // "624,000원"
    totalAmount: String, // 금액 칸에 들어갈 값
    onAmountChange: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)) {

        // 1. 호실 명 + 자리 수 칸 (세로로 분리)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. 호실 이름 칸
            LabelCell(
                text = roomName,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            // 2. 좌석 수 칸 (원하시는 대로 별도의 LabelCell로 분리)
            LabelCell(
                text = "($seatCount)",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        // 2. 나머지 칸들
        InputCell(value = size, onValueChange = {}, weight = 1f, isReadOnly = true)
        InputCell(value = priceH, onValueChange = {}, weight = 1f, isReadOnly = true)
        InputCell(value = priceD, onValueChange = {}, weight = 1f, isReadOnly = true)

        // 3. 단위 칸 (x [ ] 일)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("x", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            MiniInputCell(value = "", onValueChange = {}, width = 35.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("일", fontSize = 12.sp)
        }

        // 4. 금액 칸 (우측 정렬)
        InputCell(
            value = totalAmount,
            onValueChange = onAmountChange,
            weight = 1.5f,
            alignment = Alignment.CenterEnd
        )
    }
}
// 장례 서비스
@Composable
fun ServiceInputRow(
    title: String,
    price: String,
    unitText: String, // "일" 또는 "회"
    fontSize: TextUnit = 12.sp
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)) {
        // [구분] 수정 불가
        InputCell(value = title, onValueChange = {}, weight = 2f, isReadOnly = true, fontSize = fontSize)

        // [요금] 수정 불가
        InputCell(value = price, onValueChange = {}, weight = 3f, isReadOnly = true, fontSize = fontSize)

        // [단위] x __ 일/회 (가운데 정렬된 입력창)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("x", fontSize = 12.sp, letterSpacing = (-0.5).sp)
            Spacer(modifier = Modifier.width(2.dp))
            // 수량을 입력하는 작은 창
            MiniInputCell(value = "", onValueChange = {}, width = 30.dp)
            Spacer(modifier = Modifier.width(2.dp))
            Text(unitText, fontSize = 12.sp, letterSpacing = (-0.5).sp)
        }

        // [금액] 최종 계산 결과 또는 입력창 (우측 정렬)
        InputCell(value = "", onValueChange = {}, weight = 1.5f, alignment = Alignment.CenterEnd)
    }
}

@Composable
fun TotalServiceRow(
    countValue: String,        // 입력된 숫자 (예: "5")
    onCountChange: (String) -> Unit,
    unitText: String,          // "일" 또는 "회"
    totalAmount: String = ""   // 최종 계산된 금액
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // 높이 자동 조절
            .background(Color(0xFFF5F5F5)) // 합계 특유의 배경색
            .border(0.5.dp, Color(0xFFDDDDDD))   // 표의 하단 테두리 강조
    ) {
        // 2. [단위] 영역 (weight 1f) - ServiceInputRow의 단위칸 디자인 계승
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "합 계",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            // 숫자 입력창 (언더라인 스타일을 원하시면 MiniInputCell 내부에 선을 추가하거나
            // 아래처럼 BasicTextField를 직접 커스텀하세요)
            MiniInputCell(
                value = countValue,
                onValueChange = onCountChange,
                width = 100.dp
            )

            Spacer(modifier = Modifier.width(2.dp))
            Text("원", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        
    }
}

@Composable
fun ContractFooter(
    year: String = "2026", // 현재 연도 반영
    month: String = "",
    day: String = "",
    onSignatureClick: () -> Unit,
    updateTick: Int,      // 화면 갱신 신호
    capturedPath: Path?   // 서명 데이터
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 날짜 영역 ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("${year}",style = TextStyle(fontSize = 16.sp))
            Text("년", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))

            HandwrittenDateUnit(width = 80.dp)
            Text("월", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))

            HandwrittenDateUnit(width = 80.dp)
            Text("일", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 임대인 / 임차인 영역 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // 임대인 영역
            Row(modifier = Modifier.weight(1.2f), verticalAlignment = Alignment.Top) {
                Text("임대인 : ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Column (){
                    Text(text = "보람상조개발 주식회사 보람인천장례식장", fontSize = 13.sp)
                    Text(text = "인천광역시 서구 경명대로 468 (경서동)", fontSize = 13.sp)
                    Text(text = "오준오 (날인생략)", fontSize = 13.sp)
                    Text(text = "김기태 (날인생략)", fontSize = 13.sp)
                    Text(text = "이창우 (날인생략)", fontSize = 13.sp)
                }
            }

            // 임차인 서명란
            Row(
                modifier = Modifier.weight(0.8f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                Text("임차인 : ", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))

                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 48.dp)
                        .background(Color(0xFFF5F5F5))
                        .clickable { onSignatureClick() },
                    contentAlignment = Alignment.Center
                ) {
                    // ⭐ 핵심: updateTick이 변할 때마다 이 블록 내부를 강제로 다시 구성합니다.
                    key(updateTick) {
                        val currentPathToDraw = capturedPath

                        if (currentPathToDraw != null) {
                            Canvas(modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clipToBounds()) {
                                val pathBounds = currentPathToDraw.getBounds()

                                if (pathBounds.width > 0 && pathBounds.height > 0) {
                                    val scaleX = size.width / pathBounds.width
                                    val scaleY = size.height / pathBounds.height
                                    val finalScale = minOf(scaleX, scaleY) * 0.8f

                                    withTransform({
                                        // 2. 먼저 박스의 정중앙으로 캔버스를 옮깁니다.
                                        translate(center.x, center.y)

                                        // 3. 서명을 박스 크기에 맞춰 줄입니다.
                                        scale(finalScale, finalScale, Offset.Zero)

                                        // 4. 서명의 '중심점'이 0,0(박스 중앙)에 오도록 서명 자체를 밉니다.
                                        translate(-pathBounds.center.x, -pathBounds.center.y)
                                    }) {
                                        drawPath(
                                            path = currentPathToDraw,
                                            color = Color.Black,
                                            style = Stroke(
                                                width = 3f,
                                                cap = StrokeCap.Round,
                                                join = StrokeJoin.Round
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("성명 (인)", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HandwrittenDateUnit(
    width: Dp = 100.dp,
    height: Dp = 40.dp
) {
    val drawPath = remember { Path() }
    val eraserPath = remember { Path() }
    var updateTick by remember { mutableStateOf(0) }

    // 입력 내용이 있는지 여부를 판단 (Path가 비어있지 않으면 true)
    val hasContent = updateTick > 0 && (!drawPath.isEmpty || !eraserPath.isEmpty)

    Box(
        modifier = Modifier
            .size(width, height)
            .background(Color(0xFFF5F5F5))
            .clipToBounds()
    ) {
        // 1. 그리기 캔버스 영역
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val firstDown = awaitFirstDown()
                        // 펜 타입이 지우개거나 버튼이 눌린 경우 등 (필요시 추가 가능)
                        val isEraserMode = firstDown.type == PointerType.Eraser

                        val targetPath = if (isEraserMode) eraserPath else drawPath
                        targetPath.moveTo(firstDown.position.x, firstDown.position.y)
                        updateTick++

                        while (true) {
                            val event = awaitPointerEvent()
                            val anyPressed = event.changes.any { it.pressed }
                            if (anyPressed) {
                                event.changes.forEach { change ->
                                    targetPath.lineTo(change.position.x, change.position.y)
                                    change.consume()
                                }
                                updateTick++
                            } else break
                        }
                    }
                }
        ) {
            updateTick.let {
                drawIntoCanvas { canvas ->
                    val nativeCanvas = canvas.nativeCanvas
                    val checkpoint = nativeCanvas.saveLayer(0f, 0f, size.width, size.height, null)

                    drawPath(
                        path = drawPath,
                        color = Color.Black,
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    drawPath(
                        path = eraserPath,
                        color = Color.Transparent,
                        style = Stroke(width = 30f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                        blendMode = BlendMode.Clear
                    )

                    nativeCanvas.restoreToCount(checkpoint)
                }
            }
        }

        // 2. 삭제 버튼 (내용이 있을 때만 우측 상단에 표시)
        // AnimatedVisibility를 사용하면 부드럽게 나타나고 사라집니다.
        AnimatedVisibility(
            visible = hasContent,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = {
                    drawPath.reset()
                    eraserPath.reset()
                    updateTick = 0 // 초기화하여 버튼 숨김
                },
                modifier = Modifier
                    .size(16.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color.Red,
                    modifier = Modifier.size(11.dp)
                )
            }
        }
    }
}
@Composable
fun SignatureDialog(
    onDismiss: () -> Unit,      // 다이얼로그 닫기
    onConfirm: (Path) -> Unit   // 서명 완료 시 Path 전달
) {
    val path = remember { Path() }
    // 화면을 강제로 다시 그리게 만들기 위한 상태 (Path 변경 감지용)
    var updateTick by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "임차인 서명",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- 실제 서명이 이뤄지는 캔버스 영역 ---
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFF9F9F9)) // 캔버스 배경색
                        .border(1.dp, Color.LightGray)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        path.moveTo(offset.x, offset.y)
                                        updateTick++
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()

                                        path.lineTo(change.position.x, change.position.y)
                                        updateTick++
                                    }
                                )
                            }
                    ) {
                        // updateTick이 바뀔 때마다 Canvas가 다시 그려짐
                        updateTick.let {
                            drawPath(
                                path = path,
                                color = Color.Black,
                                style = Stroke(
                                    width = 6f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- 하단 버튼 (지우기 / 취소 / 완료) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                        path.reset()
                        updateTick++
                        onDismiss()
                    }) {
                        Text("취소")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // 현재까지 그려진 Path를 전달하고 닫기
                            onConfirm(Path().apply { addPath(path) })
                            onDismiss()
                        }
                    ) {
                        Text("완료")
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.FullWidthHeaderCell(item: FuneralServiceItem, totalWeight: Float) {
    val bgColor = if (item.isYellowHeader) Color.Yellow else Color(0xFFF5F5F5)
    Box(
        modifier = Modifier
            .weight(totalWeight)
            .fillMaxHeight() // 부모 Row의 높이에 맞춤
            .background(bgColor)
            .border(0.5.dp, Color(0xFFD1D1D1)), // 이미지와 맞추기 위해 검정색 테두리 권장
        contentAlignment = Alignment.Center // 가로/세로 모두 중앙 정렬
    ) {
        Text(
            text = item.name,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
}

@Composable
fun RowScope.TableCellItem(item: FuneralServiceItem?) {
    if (item != null) {

        Box(
            modifier = Modifier
            .weight(2.5f)
            .fillMaxHeight()
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(8.dp),
            contentAlignment = Alignment.CenterStart){
            // 1. 품명
            Text(
                text = item.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2. 수량 (수정된 입력 필드)
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFD1D1D1))
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (item.isReadOnly) {
                // 읽기 전용일 때: 텍스트만 표시 (수정 불가)
                Text(
                    text = item.unit,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = Color.DarkGray
                )
            } else {
                // 입력 가능할 때: 이전과 동일하게 TextField 표시
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = item.quantity.value,
                        onValueChange = { item.quantity.value = it },
                        modifier = Modifier.weight(1f),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            textAlign = TextAlign.End,
                            fontSize = 12.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    if (item.unit.isNotEmpty()) {
                        Text(text = item.unit, fontSize = 12.sp, modifier = Modifier.padding(start = 2.dp))
                    }
                }
            }
        }

        // 3. 금액 (위아래 분리 로직 포함)
        Column(
            modifier = Modifier
                .weight(2.3f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFD1D1D1))
        ) {
            val prices = item.price.split("/")
            prices.forEachIndexed { index, price ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .let {
                            if (index > 0) it.drawBehind {
                                drawLine(
                                    Color(0xFFD1D1D1),
                                    Offset(0f, 0f),
                                    Offset(size.width, 0f),
                                    1f
                                )
                            } else it
                        }
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = if(price.trim().isEmpty()) "" else "${price.trim()}원", fontSize = 12.sp)
                }
            }
        }
    } else {
        Box(modifier = Modifier
            .weight(6f)
            .fillMaxHeight()
            .border(0.5.dp, Color(0xFFD1D1D1)))
    }
}

@Composable
fun SignatureArea(label: String, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    // 서명 데이터를 Path로 관리
    var capturedPath by remember { mutableStateOf<Path?>(null) }
    // 화면 갱신을 위한 티커
    var updateTick by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color(0xFFF5F5F5))
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        // ⭐ 핵심: 기존에 사용하시던 로직 그대로 적용
        key(updateTick) {
            val currentPathToDraw = capturedPath

            if (currentPathToDraw != null) {
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clipToBounds()) {
                    val pathBounds = currentPathToDraw.getBounds()

                    if (pathBounds.width > 0 && pathBounds.height > 0) {
                        val scaleX = size.width / pathBounds.width
                        val scaleY = size.height / pathBounds.height
                        val finalScale = minOf(scaleX, scaleY) * 0.8f

                        withTransform({
                            translate(center.x, center.y)
                            scale(finalScale, finalScale, Offset.Zero)
                            translate(-pathBounds.center.x, -pathBounds.center.y)
                        }) {
                            drawPath(
                                path = currentPathToDraw,
                                color = Color.Black,
                                style = Stroke(
                                    width = 3f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }
            } else {
                Text("$label (인)", fontSize = 12.sp, color = Color.Gray)
            }
        }

        if (showDialog) {
            SignatureDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newPath ->
                    capturedPath = newPath // 전달받은 Path 저장
                    updateTick++ // 화면 강제 갱신
                    showDialog = false
                }
            )
        }
    }
}