package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.boram_funeral.ui.components.contracts.funeral.base.HandwrittenAreaCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel

// ── 색상 ──────────────────────────────────────────────────────────────────────

private val ColorPrimary   = Color(0xFF05195F)
private val ColorBg        = Color(0xFFF9F9F9)
private val ColorBorder    = Color(0xFFCCCCCC)
private val ColorTableHead = Color(0xFFF5F7FA)
private val ColorText      = Color(0xFF222222)
private val ColorSub       = Color(0xFF555555)
private val ColorNotice    = Color(0xFF888888)

// ── 메인 Screen ───────────────────────────────────────────────────────────────

@Composable
fun PrivacyConsentStep(viewModel: ContractViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    // 서명 Path — UI 레이어에서만 관리
    var signatureData by remember { mutableStateOf<Path?>(null) }

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {

            // ── 제목 ──────────────────────────────────────────────────────────
            Text(
                text = "개인정보 수집 및 이용에 관한 동의",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // ── 안내 문구 ─────────────────────────────────────────────────────
            Text(
                text = "장례행사(이하, '서비스'라 합니다.)의 원활한 진행을 위하여 보람상조개발(주) 보람인천장례식장(이하, '장례식장'이라 합니다)은 고인 및 유가족 관계인의 개인정보를 수집하고 있으며 장례식장이 취득한 개인정보는 \"통신비밀보호법\", \"전기통신사업법\" 및 \"개인정보보호법\" 등 준수하여야 할 관련 법령상의 개인정보 보호 규정을 준수하여 이용 됩니다.",
                style = TextStyle(fontSize = 14.sp, color = ColorSub, lineHeight = 20.sp),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // ── 1. 개인정보 수집·이용 동의 (필수) ────────────────────────────
            SectionTitle("1. 개인정보 수집·이용 동의")
            Spacer(modifier = Modifier.height(4.dp))
            SectionLabel("■ 필수사항")
            Text(
                text = "본 서비스와 관련하여 장례식장이 귀하의 개인정보를 아래와 같이 수집·이용하기 위해 동의를 구합니다.",
                style = TextStyle(fontSize = 14.sp, color = ColorSub, lineHeight = 19.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrivacyTable(
                rows = listOf(
                    PrivacyRow(
                        item = "성명, 전화번호, 주소",
                        purpose = "1. 화장을 위한 신고 대행\n2. 화장장 예약 대행\n3. 장례비용 정산, 회계처리\n4. 고객 민원 및 고충 처리\n5. 분쟁조정을 위한 기록보존",
                        note = "",
                        retention = "제공 동의일로 부터 5년"
                    )
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            ConsentCheckRow(
                label = "개인정보 수집 및 이용에 동의하십니까?",
                agreed = uiState.privacyCollectionAgree,
                onAgree = { viewModel.updatePrivacyCollectionAgree(true) },
                onDisagree = { viewModel.updatePrivacyCollectionAgree(false) }
            )

            NoticeTexts(
                listOf(
                    "단, 보유기간 이후에도 민원처리 또는 법령상 의무이행을 위하여 필요한 경우에 한하여 별도 보관할 수 있습니다.",
                    "귀하는 상기 개인정보 제공을 거부할 권리가 있습니다. 그러나 동의하지 않을 경우 일부 서비스가 제한될 수 있습니다."
                )
            )

            SectionDivider()

            // ── 2. 상품 홍보 동의 (선택) ──────────────────────────────────────
            SectionLabel("■ 상품 홍보에 관한 동의 (선택)")
            Text(
                text = "장례식장이 귀하의 개인정보를 아래와 같이 수집·이용하기 위해 동의를 구합니다.",
                style = TextStyle(fontSize = 14.sp, color = ColorSub, lineHeight = 19.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PrivacyTable(
                rows = listOf(
                    PrivacyRow(
                        item = "성명, 전화번호, 주소",
                        purpose = "상조상품 등의 홍보",
                        note = "",
                        retention = "제공 동의일로 부터 5년"
                    )
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            ConsentCheckRow(
                label = "개인정보 수집 및 이용에 동의하십니까?",
                agreed = uiState.privacyMarketingAgree,
                onAgree = { viewModel.updatePrivacyMarketingAgree(true) },
                onDisagree = { viewModel.updatePrivacyMarketingAgree(false) }
            )

            NoticeTexts(
                listOf("귀하는 상기 개인정보 제공을 거부할 수 있습니다. 그러나 동의하지 않을 경우 상조상품 등에 대한 정보를 제공 받을 수 없습니다.")
            )

            SectionDivider()

            // ── 3. 고유식별번호 수집·이용 동의 ───────────────────────────────
            SectionTitle("2. 고유식별번호(주민등록번호) 수집·이용 동의")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "본 서비스와 관련하여 장례식장이 귀하의 고유식별번호를 아래와 같이 수집·이용하기 위해 동의를 구합니다.",
                style = TextStyle(fontSize = 14.sp, color = ColorSub, lineHeight = 19.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrivacyTable(
                rows = listOf(
                    PrivacyRow(
                        item = "주민등록번호",
                        purpose = "1. 화장을 위한 신고 대행\n2. 화장장 예약 대행",
                        note = "장사 등에 관한 법률 제8조에 따른, 화장의 신고 및 예약 대행",
                        retention = "장례행사 종료 시까지"
                    )
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            ConsentCheckRow(
                label = "개인정보 수집 및 이용에 동의하십니까?",
                agreed = uiState.privacyIdNumberAgree,
                onAgree = { viewModel.updatePrivacyIdNumberAgree(true) },
                onDisagree = { viewModel.updatePrivacyIdNumberAgree(false) }
            )

            NoticeTexts(
                listOf(
                    "단, 보유기간 이후에도 민원처리 또는 법령상 의무이행을 위하여 필요한 경우에 한하여 별도 보관할 수 있습니다.",
                    "귀하는 상기 고유식별정보 제공을 거부할 수 있습니다. 그러나 동의하지 않을 경우 화장을 위한 신고대행, 화장장 예약 대행 서비스가 제한될 수 있습니다."
                )
            )

            SectionDivider()

            // ── 4. 제3자 제공 동의 ────────────────────────────────────────────
            SectionTitle("3. 마케팅 활용을 위한 개인정보의 제3자 제공 동의")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "귀하의 개인정보를 아래와 같이 제3자에게 제공함에 동의를 구합니다.",
                style = TextStyle(fontSize = 14.sp, color = ColorSub, lineHeight = 19.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            ThirdPartyTable(
                agreed = uiState.privacyThirdPartyAgree,
                onAgree = { viewModel.updatePrivacyThirdPartyAgree(true) },
                onDisagree = { viewModel.updatePrivacyThirdPartyAgree(false) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            NoticeTexts(
                listOf(
                    "귀하는 상기 개인정보 제공을 거부할 권리가 있습니다. 그러나 동의하지 않는 경우 상조상품에 대한 정보, 각종 이벤트, 부가혜택 등의 정보를 받을 수 없습니다.",
                    "제3자 제공 동의의 철회\n① 본 동의서에 동의하여 이미 제3자에게 제공된 개인정보라 하더라도, 언제든지 열람, 정정, 삭제를 요구할 수 있습니다.\n② 마케팅활용동의를 통해 이미 제공된 개인정보에 대해 동의 철회를 요청하시는 경우, 철회 요청일로부터 최대 7일 이내에 안전하게 삭제 처리됩니다."
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── 하단 서명 푸터 ────────────────────────────────────────────────
            PrivacyFooter(
                year = remember { java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString() },
                updateTick = uiState.privacySignatureUpdateTick,
                capturedPath = signatureData,
                onSignatureClick = { viewModel.showPrivacySignatureDialog() }
            )
        }
    }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    if (uiState.isPrivacySignatureDialogVisible) {
        SignatureDialog(
            onDismiss = { viewModel.dismissPrivacySignatureDialog() },
            onConfirm = { path ->
                signatureData = Path().apply { addPath(path) }
                viewModel.confirmPrivacySignature()
            }
        )
    }
}

// ── 하단 서명 푸터 ────────────────────────────────────────────────────────────

@Composable
private fun PrivacyFooter(
    year: String,
    updateTick: Int,
    capturedPath: Path?,
    onSignatureClick: () -> Unit
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
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
                        .border(1.dp, ColorBorder, RoundedCornerShape(4.dp))
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
                                    val s = minOf(
                                        size.width / bounds.width,
                                        size.height / bounds.height
                                    ) * 0.8f
                                    withTransform({
                                        translate(center.x, center.y)
                                        scale(s, s, Offset.Zero)
                                        translate(-bounds.center.x, -bounds.center.y)
                                    }) {
                                        drawPath(
                                            path = capturedPath,
                                            color = Color.Black,
                                            style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("성명 (인)", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// ── 동의 체크박스 행 ──────────────────────────────────────────────────────────

@Composable
private fun ConsentCheckRow(
    label: String,
    agreed: Boolean?,
    onAgree: () -> Unit,
    onDisagree: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorText),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Checkbox(
                    checked = agreed == true,
                    onCheckedChange = { if (it) onAgree() },
                    colors = CheckboxDefaults.colors(checkedColor = ColorPrimary)
                )
                Text("동의", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Checkbox(
                    checked = agreed == false,
                    onCheckedChange = { if (it) onDisagree() },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE53935))
                )
                Text("미동의", fontSize = 14.sp)
            }
        }
    }
}

// ── 개인정보 테이블 ───────────────────────────────────────────────────────────

private data class PrivacyRow(
    val item: String,
    val purpose: String,
    val note: String,
    val retention: String
)

@Composable
private fun PrivacyTable(rows: List<PrivacyRow>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorBorder, RoundedCornerShape(4.dp))
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTableHead)
                .height(IntrinsicSize.Min)
        ) {
            TableCell("수집 항목", 1.5f, isHeader = true)
            VerticalLine()
            TableCell("수집 목적", 2.5f, isHeader = true)
            VerticalLine()
            TableCell("비 고", 1.5f, isHeader = true)
            VerticalLine()
            TableCell("보유 기간", 1.5f, isHeader = true)
        }
        Divider(color = ColorBorder, thickness = 1.dp)
        // 데이터
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                TableCell(row.item, 1.5f)
                VerticalLine()
                TableCell(row.purpose, 2.5f)
                VerticalLine()
                TableCell(row.note, 1.5f)
                VerticalLine()
                TableCell(row.retention, 1.5f)
            }
        }
    }
}

// ── 제3자 제공 테이블 (동의여부 컬럼 테이블 오른쪽 끝 — 전체 공유) ──────────────

@Composable
private fun ThirdPartyTable(
    agreed: Boolean?,
    onAgree: () -> Unit,
    onDisagree: () -> Unit
) {
    val rows = listOf(
        listOf("보람상조라이프㈜",  "1. 상품판매, 마케팅, 이벤트 안내\n2. 상품홍보, 부가서비스 혜택안내", "성명, 생년월일,\n전화번호", "제공 동의일로 부터 5년"),
        listOf("보람재향상조㈜",   "1. 상품판매, 마케팅 이벤트 안내\n2. 상품홍보, 부가서비스 혜택안내",  "성명, 생년월일,\n전화번호", "제공 동의일로 부터 5년"),
        listOf("보람상조애니콜㈜", "1. 상품판매, 마케팅 이벤트 안내\n2. 상품홍보, 부가서비스 혜택안내",  "성명, 생년월일,\n전화번호", "제공 동의일로 부터 5년"),
        listOf("보람상조실로암㈜", "1. 상품판매, 마케팅 이벤트 안내\n2. 상품홍보, 부가서비스 혜택안내",  "성명, 생년월일,\n전화번호", "제공 동의일로 부터 5년"),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorBorder, RoundedCornerShape(4.dp))
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTableHead)
                .height(IntrinsicSize.Min)
                .padding(6.dp)
        ) {
            TableCell("제공받는자", 1.5f, isHeader = true)
            VerticalLine()
            TableCell("제공 목적", 2.5f, isHeader = true)
            VerticalLine()
            TableCell("제공 항목", 1.5f, isHeader = true)
            VerticalLine()
            TableCell("보유 기간", 1.5f, isHeader = true)
            VerticalLine()
            TableCell("동의여부", 1.5f, isHeader = true)
        }
        Divider(color = ColorBorder, thickness = 1.dp)

        // 데이터 행 + 오른쪽 끝 동의여부는 첫 행에만 rowspan처럼 배치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 왼쪽 4컬럼 — 4개 행을 세로로 쌓음
            Column(modifier = Modifier.weight(7f)) {
                rows.forEachIndexed { idx, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        TableCell(row[0], 1.5f)
                        VerticalLine()
                        TableCell(row[1], 2.5f)
                        VerticalLine()
                        TableCell(row[2], 1.5f)
                        VerticalLine()
                        TableCell(row[3], 1.5f)
                    }
                    if (idx < rows.lastIndex) Divider(color = ColorBorder, thickness = 1.dp)
                }
            }

            // 오른쪽 동의여부 — 4개 행 전체 높이를 차지하는 단일 셀
            VerticalLine()
            Row(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = agreed == true,
                        onCheckedChange = { if (it) onAgree() },
                        colors = CheckboxDefaults.colors(checkedColor = ColorPrimary),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("동의", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = agreed == false,
                        onCheckedChange = { if (it) onDisagree() },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE53935)),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("미동의", fontSize = 12.sp)
                }
            }
        }
    }
}

// ── 공통 유틸 Composable ──────────────────────────────────────────────────────

@Composable
private fun RowScope.TableCell(text: String, weight: Float, isHeader: Boolean = false) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = ColorText,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .padding(6.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
private fun RowScope.VerticalLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(ColorBorder)
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorPrimary),
        modifier = Modifier.padding(bottom = 6.dp, top = 4.dp)
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorText),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun NoticeTexts(notices: List<String>) {
    Column(modifier = Modifier.padding(top = 6.dp)) {
        notices.forEach { notice ->
            Text(
                text = "※ $notice",
                style = TextStyle(fontSize = 14.sp, color = ColorNotice, lineHeight = 24.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun SectionDivider() {
    Divider(
        color = ColorBorder,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

