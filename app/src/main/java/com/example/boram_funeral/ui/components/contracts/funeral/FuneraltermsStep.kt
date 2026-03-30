package com.example.boram_funeral.ui.components.contracts.funeral

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.io.File
import java.io.FileOutputStream

// ── 약관 데이터 ───────────────────────────────────────────────────────────────

private val termsArticles = listOf(
    "제1조(목적) 이 약관은 장례식장을 운영하는 사업자(이하 '사업자'라 한다)와 장례식장을 이용하는 유족 등(이하 '이용자'라 한다) 간의 장례식장의 이용에 관한 제반 계약사항을 규정함을 목적으로 합니다.",
    "제2조(관계법령의 적용) 이 약관에서 규정되지 아니한 사항 또는 이 계약의 해석에 관하여 다툼이 있는 경우에는 사업자와 이용자가 합의하여 결정하되, 합의가 이루어지지 아니한 경우에는 약관의 규제에 관한 법률, 민법, 상법 등 관계법령 및 공정 타당한 일반관례에 따릅니다.",
    "제3조(용어의 정의)\n① '장례식장'이란 안치실, 빈소, 접객실, 예식실 등 시신을 모시고 조문객의 조문을 받으며 예식을 올리기 위한 일체의 시설을 말합니다.\n② '안치'란 시신의 부패와 세균번식 등을 막기 위하여 시신보관용 냉장시설에 시신을 모시는 것을 말합니다.\n③ '염습'이란 시신을 씻은 다음에 수의를 입히고 염포로 묶는 것을 말합니다.\n④ '입관'이란 시신을 관속으로 모시는 것을 말합니다.\n⑤ '빈소'란 조문객의 조문을 받기 위하여 마련된 장소를 말합니다.\n⑥ '접객실'이란 조문객을 대접하기 위하여 마련된 장소를 말합니다.\n⑦ '예식실'이란 고인에 대한 예식을 올리기 위해 마련된 장소를 말합니다.\n⑧ '발인'이란 이용자가 장사를 치르기 위해서 장례식장에서 관을 가지고 장지로 떠나는 것을 말합니다.\n⑨ '장례식장 이용계약'이란 장례식장의 시설, 장의용품, 음식 및 제물, 기타 물품 등을 사용하는 것에 대하여 사업자와 이용자가 체결하는 계약을 말합니다.",
    "제4조(계약기간) 사업자와 이용자는 장례식장 이용계약서에서 계약기간을 정합니다.",
    "제5조(이용시설) 사업자와 이용자는 장례식장 이용계약서에서 안치실, 빈소, 접객실, 예식실, 안치일시, 입관일시 등을 정합니다.",
    "제6조(이용료)\n① 이용료는 안치실·빈소·접객실·예식실의 이용료 및 염습비, 부대시설 이용비, 예식비, 청소비, 관리비 및 장의용품, 음식, 제물, 기타용품 구입비 등으로 구성합니다.\n② 안치실·빈소·접객실의 이용료는 안치일시를 기준으로 24시간을 1일로 하여 산정합니다. 다만, 24시간에 미달하는 시간은 그 시간이 12시간 이상인 경우에는 1일로 산정하고 12시간 미만인 경우에는 시간단위로 산정하되, 1시간 미만의 시간은 1시간으로 산정하며, 추가빈소를 사용할 경우 동일한 이용료가 부과됩니다.\n③ 청소 및 관리비는 빈소입실 시각으로부터 밤 12시를 기준으로 1일 요금으로 산정합니다.\n④ 장의용품 및 제물은 이용자가 주문한 품목에 대하여 비용을 정산하며, 사업자와 이용자가 정산서를 작성합니다.\n⑤ 음식은 이용자가 계약한 장례식장 별로 기본 준비음식이 세팅되고, 추가주문 수량을 합산하여 정산하며, 사업자와 이용자가 정산서를 작성합니다.\n⑥ 기타물품(매점용품 및 잡화)은 이용자가 계약한 장례식장 별로 기본 물품수량이 세팅되고, 추가 주문 수량을 합산하거나 기본 수량에서 실제 사용된 수량을 제외한 나머지를 반품하는 방법으로 정산하며, 사업자와 이용자가 정산서를 작성합니다.\n⑦ 이용자가 직접 염습을 하는 경우에도 사업자는 염습을 하는 데 소요되는 실비(수시비등)를 청구할 수 있습니다.\n⑧ 이용자는 발인하기 3시간 전까지 정산된 이용료의 전액을 지급하여야 하며, 이때 사업자는 각 내역에 따른 계산서를 교부하여야 합니다.",
    "제7조(사업자의 의무)\n① 사업자는 계약을 체결하는 장소인 사무실내의 보기 쉬운 곳에 이 약관과 이용료(내역별 금액)를 게시하여야 하며, 이용자에게 이 약관을 교부하여야 합니다.\n② 사업자는 이용자가 장례절차(종교별, 가문별 등)에 따라 엄숙하고도 편리하게 장례를 치를 수 있도록 장례식장을 쾌적하게 유지해야 하고, 적절한 양질의 서비스를 제공하여야 합니다.\n③ 사업자 및 그 종업원은 이용자에게 계약에서 정한 이용료 이외의 일체의 금품이나 물품을 요구하지 않으며, 사업자가 제공하는 장례용품의 사용을 강제하지 아니합니다.",
    "제8조(이용자의 의무)\n① 이용자는 장례식장의 질서를 유지하기 위한 사업자의 공정 타당한 제반 요청사항을 최대한 준수하도록 노력하여야 합니다.\n② 이용자는 장례식장의 이용과 관련하여 타인에게 불편을 주지 않도록 다음의 행위를 하지 말아야 하며, 이를 위반할 경우 사업자는 적정한 조치를 취할 수 있습니다.\n1. 장례식장내에 인화성, 폭발성 물품과 조리기구 및 기타 위험한 물품을 반입 또는 보관하는 행위\n2. 타인의 장례 또는 조문에 방해가 되는 고성방가, 소란, 지나친 종교행사 등 불쾌감을 주는 일체의 행위\n3. 장례식장의 시설물, 기구 등을 멸실·훼손하는 행위\n4. 식중독 및 위생사고 등의 방지를 위해 변질될 우려가 큰 음식을 사업자의 허락 없이 반입하는 행위\n5. 장례식을 진행함에 있어 조화(유사품도 포함)를 15개 이상 진열하는 행위\n6. 조문객에 대한 과대한 접대행위",
    "제9조(계약해지)\n① 사업자 또는 이용자는 상대방이 고의 또는 과실로 계약을 위반하는 경우에는 계약을 해지할 수 있습니다.\n② 제1항에 의하여 계약이 해지된 경우, 이용자는 계약해지 통지 시부터 3시간 이내에 시설물 및 기구를 반환하고 그때까지의 기간 동안의 이용료를 사업자에게 지급하여야 하며, 사업자는 이미 이용자에게서 수령한 금액이 있는 때에는 그 기간 동안의 이용료를 공제한 나머지 금액을 이용자에게 반환하여야 합니다.\n③ 제1항에 의하여 계약을 해지한 사업자 또는 이용자는 상대방의 고의·과실로 인해 손해를 입은 경우에는 제10조의 규정에 의하여 상대방에게 손해배상을 청구할 수 있습니다.",
    "제10조(계약위반으로 인한 책임) 사업자 또는 이용자는 고의 또는 과실로 계약을 위반하여 상대방에게 손해를 입힌 경우에는 그 손해를 배상할 책임을 집니다.",
    "제11조(사고로 인한 책임) 사업자는 시설물의 하자, 종업원의 고의·과실 등 사업자의 책임있는 사유로 인하여 장례식장내에서 발생한 사고에 대해서는 그 사고로 인한 손해를 배상할 책임을 집니다.",
    "제12조(휴대물에 대한 책임)\n① 사업자는 이용자 또는 조문객이 휴대한 물건을 사업자나 종업원에게 보관을 맡긴 경우에는, 그 물건의 멸실·훼손·도난 등에 대하여 불가항력으로 인한 것임을 증명하지 아니하면 그 손해를 배상할 책임을 면하지 못합니다.\n② 사업자는 이용자 또는 조문객이 보관을 맡기지 아니한 물건이라도 사업자나 종업원의 고의·과실로 인하여 멸실·훼손·도난 등이 된 때에는 그 손해를 배상할 책임을 집니다.\n③ 사업자는 이용자 또는 조문객의 물건에 대하여 책임이 없음을 게시한 때에도 제1항과 제2항에 의한 책임을 면하지 못합니다.\n④ 화폐, 유가증권 등의 고가물에 대하여는 이용자 또는 조문객이 그 종류와 액을 명시하여 사업자나 종업원에게 보관을 맡기지 아니한 경우에는, 사업자는 그 멸실·훼손·도난 등에 대하여 손해를 배상할 책임을 지지 아니합니다.",
    "제13조(사업자의 면책)\n① 사업자는 손해가 천재지변 등 불가항력적인 사유로 인하여 발생한 때에는 배상할 책임을 지지 아니합니다.\n② 이용자가 반입한 음식으로 인하여 식중독 등 사고가 발생하는 경우 사업자의 고의 또는 과실이 없는 한 사업자는 이에 대한 책임을 지지 아니합니다.\n③ 화폐, 유가증권 등의 고가물에 대하여는 이용자 또는 조문객이 그 종류와 가액을 명시하여 보관을 맡기지 아니한 경우에는 사업자는 그 손해를 배상할 책임을 지지 아니합니다.",
    "제14조(재판관할) 이 계약과 관련된 분쟁에 관한 소는 민사소송법상의 관할법원에 제기하여야 합니다."
)

// ── 메인 Composable ───────────────────────────────────────────────────────────

@Composable
fun FuneraltermsStep(
    viewModel: ContractViewModel,
    onPdfSaved: (path: String) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Path는 직렬화 불가 → remember로 로컬 관리 (Step 1과 동일한 패턴)
    var signaturePath by remember { mutableStateOf<Path?>(null) }

    // 날짜 상태 (현재 연도 고정, 월/일 입력)
    val year = "2026"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // ── 제목 ──────────────────────────────────────────────────────────
            Text(
                text = "보람상조개발(주) 보람인천장례식장 이용약관",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF05195F),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // ── 약관 본문 ─────────────────────────────────────────────────────
            termsArticles.forEach { article ->
                Text(
                    text = article,
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color(0xFF222222),
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 하단 서명 푸터 ────────────────────────────────────────────────
            TermsFooter(
                year = year,
                onSignatureClick = { viewModel.showTermsSignatureDialog() },
                updateTick = uiState.termsSignatureUpdateTick,
                capturedPath = signaturePath
            )
        }
    }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    if (uiState.termsIsSignatureDialogVisible) {
        SignatureDialog(
            onConfirm = { path ->
                signaturePath = path
                viewModel.confirmTermsSignature()
            },
            onDismiss = { viewModel.dismissTermsSignatureDialog() }
        )
    }
}

// ── 하단 서명 푸터 ────────────────────────────────────────────────────────────

@Composable
private fun TermsFooter(
    year: String,
    onSignatureClick: () -> Unit,
    updateTick: Int,
    capturedPath: Path?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 날짜
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("${year}년", style = TextStyle(fontSize = 16.sp))
            Spacer(modifier = Modifier.width(16.dp))
            HandwrittenDateUnit(width = 80.dp)
            Text("월", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))
            HandwrittenDateUnit(width = 80.dp)
            Text("일", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 임대인 / 임차인
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // 임대인
            Row(modifier = Modifier.weight(1.2f), verticalAlignment = Alignment.Top) {
                Text("임대인 : ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Column {
                    Text("보람상조개발 주식회사 보람인천장례식장", fontSize = 13.sp)
                    Text("인천광역시 서구 경명대로 468 (경서동)", fontSize = 13.sp)
                    Text("오준오 (날인생략)", fontSize = 13.sp)
                    Text("김기태 (날인생략)", fontSize = 13.sp)
                    Text("이창우 (날인생략)", fontSize = 13.sp)
                }
            }

            // 임차인 서명
            Row(
                modifier = Modifier.weight(0.8f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "임차인 : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 48.dp)
                        .background(Color(0xFFF5F5F5))
                        .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(4.dp))
                        .clickable { onSignatureClick() },
                    contentAlignment = Alignment.Center
                ) {
                    key(updateTick) {
                        if (capturedPath != null) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .clipToBounds()
                            ) {
                                val bounds = capturedPath.getBounds()
                                if (bounds.width > 0 && bounds.height > 0) {
                                    val scale = minOf(
                                        size.width / bounds.width,
                                        size.height / bounds.height
                                    ) * 0.8f
                                    withTransform({
                                        translate(center.x, center.y)
                                        scale(scale, scale, Offset.Zero)
                                        translate(-bounds.center.x, -bounds.center.y)
                                    }) {
                                        drawPath(
                                            path = capturedPath,
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

// ── 날짜 입력 박스 ────────────────────────────────────────────────────────────

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


// ── 서명 다이얼로그 ───────────────────────────────────────────────────────────

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