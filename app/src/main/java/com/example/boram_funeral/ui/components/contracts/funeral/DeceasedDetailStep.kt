package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.*
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import java.text.NumberFormat
import java.util.Locale

// ── 색상 상수 ─────────────────────────────────────────────────────────────────
private val COLOR_BRAND   = Color(0xFF05195F)
private val DIVIDER_COLOR = Color(0xFFD1D1D1)

// ── 공통 헬퍼 컴포저블 ─────────────────────────────────────────────────────────

@Composable
private fun ArticleTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun ArticleSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun ArticleDivider() {
    HorizontalDivider(thickness = 1.dp, color = DIVIDER_COLOR)
}

/** 빈소입실 일시 날짜 단위 입력 (년/월/일/시/분) */
@Composable
private fun DateUnitInput(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MiniInputCell(value = value, onValueChange = onValueChange, width = 32.dp)
        Text(unit, fontSize = 16.sp)
    }
}

// ── 메인 컴포저블 ──────────────────────────────────────────────────────────────

@Composable
fun DeceasedDetailStep(viewModel: ContractViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    var signatureData by remember { mutableStateOf<Path?>(null) }

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    // 부대시설 이용료 합계 자동 계산
    val serviceTotalFormatted = remember(uiState.serviceItems) {
        val total = uiState.serviceItems.sumOf { item ->
            item.unitPrice * (item.count.toLongOrNull() ?: 0L)
        }
        NumberFormat.getNumberInstance(Locale.KOREA).format(total)
    }

    Column(
        modifier = Modifier.fillMaxSize().imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "장례식장 이용 계약서",
                style = TextStyle(color = COLOR_BRAND, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // ── 계약서 상단 문구 ───────────────────────────────────────────────
            ArticleSection {
                FlowRow(
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    Text("${uiState.selectedFuneralHome.displayName} 을(를) 사업자로 하고 ", fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                        Text("故", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.deceasedName, onValueChange = viewModel::updateDeceasedName, placeholder = "고인 성함", width = 80.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("님", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                    Text("의", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                        Text("상주", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.chiefMourner, onValueChange = viewModel::updateChiefMourner, placeholder = "상주 성함", width = 80.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("님을(를) 이용자로 하여", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("다음과 같이 장례식장 이용 계약을 체결한다.", fontSize = 16.sp)
            }

            // ── 제1조 계약기간 ─────────────────────────────────────────────────
            ArticleSection {
                ArticleTitle("제1조 (계약기간)")
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    Text("계약기간은 ", fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                        Text(uiState.year + "년", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.contractStartMonth, onValueChange = viewModel::updateContractStartMonth, placeholder = "월", width = 40.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(" 월", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.contractStartDay, onValueChange = viewModel::updateContractStartDay, placeholder = "일", width = 40.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(" 일", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("부터", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                        Text(uiState.year + "년", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.contractEndMonth, onValueChange = viewModel::updateContractEndMonth, placeholder = "월", width = 40.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(" 월", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        InlineInputCell(value = uiState.contractEndDay, onValueChange = viewModel::updateContractEndDay, placeholder = "일", width = 40.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(" 일", fontSize = 16.sp)
                    }
                    Text("까지 한다.", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            ArticleDivider()

            // ── 제2조 이용자 인적사항 ──────────────────────────────────────────
            ArticleSection {
                ArticleTitle("제2조(이용자 인적사항)")
                Spacer(modifier = Modifier.height(8.dp))
                // 고인 정보
                Column(modifier = Modifier.fillMaxWidth()) {
                    // 행 1: 고인명, 본관, 연령, 성별
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell("고인명")
                        InputCell(value = uiState.deceasedName, onValueChange = viewModel::updateDeceasedName, weight = 2f, placeholder = "이름 입력")
                        LabelCell("본관")
                        InputCell(value = uiState.bongwan, onValueChange = viewModel::updateBongwan, weight = 1.5f)
                        LabelCell("연령")
                        InputCell(value = uiState.age, onValueChange = viewModel::updateAge, weight = 1f)
                        LabelCell("성별")
                        InputCell(value = uiState.gender, onValueChange = viewModel::updateGender, weight = 1.5f)
                    }
                    // 행 2: 주민번호, 주소
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell("주민 번호", height = 60.dp)
                        InputCell(value = uiState.jumin, onValueChange = viewModel::updateJumin, weight = 3f, placeholder = "000000-0000000")
                        LabelCell("주소", height = 60.dp)
                        InputCell(value = uiState.address, onValueChange = viewModel::updateAddress, weight = 5f)
                    }
                    // 행 3: 종교, 직분/세례명, 사망장소, 사망일시
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
                        LabelCell("종교")
                        InputCell(value = uiState.religion, onValueChange = viewModel::updateReligion, weight = 1f, placeholder = "종교 입력")
                        LabelCell("직분 / 세례명")
                        InputCell(value = uiState.baptismalName, onValueChange = viewModel::updateBaptismalName, weight = 1f)
                        Column(modifier = Modifier.weight(3f)) {
                            LabelCell("사망장소", height = 28.dp)
                            InputCell(value = uiState.deathPlace, onValueChange = viewModel::updateDeathPlace)
                        }
                        Column(modifier = Modifier.weight(3f)) {
                            LabelCell("사망일시", height = 28.dp)
                            InputCell(value = uiState.deathDateTime, onValueChange = viewModel::updateDeathDateTime)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // 임차인(계약자) 정보
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell("임차인 (계약자)")
                        InputCell(value = uiState.contractorName, onValueChange = viewModel::updateContractorName, weight = 2f, placeholder = "이름 입력")
                        LabelCell("주민등록 번호", height = 60.dp)
                        InputCell(value = uiState.contractorJumin, onValueChange = viewModel::updateContractorJumin, weight = 2f, placeholder = "000000-0000000")
                        LabelCell("관계")
                        InputCell(value = uiState.contractorRelation, onValueChange = viewModel::updateContractorRelation, weight = 2f)
                    }
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Column(modifier = Modifier.weight(3f)) {
                            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                LabelCell("집전화")
                                InputCell(value = uiState.contractorHomeTel, onValueChange = viewModel::updateContractorHomeTel, weight = 2f)
                            }
                            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                LabelCell("휴대폰")
                                InputCell(value = uiState.contractorMobile, onValueChange = viewModel::updateContractorMobile, weight = 2f)
                            }
                        }
                        LabelCell("주소")
                        InputCell(value = uiState.contractorAddress, onValueChange = viewModel::updateContractorAddress, weight = 5f)
                    }
                }
            }

            ArticleDivider()

            // ── 제3조 장례식장 정보 ────────────────────────────────────────────
            ArticleSection {
                ArticleTitle("제3조(장례식장에 관한 정보)")
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    // 안치실 + 안치일시
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("안치실")
                            InputCell(value = uiState.mortuary, onValueChange = viewModel::updateMortuary, weight = 2f)
                        }
                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
                            LabelCell("안치일시")
                            DateTimeInputBlock(
                                year = uiState.year,
                                month = uiState.mortuaryMonth, onMonthChange = viewModel::updateMortuaryMonth,
                                day = uiState.mortuaryDay, onDayChange = viewModel::updateMortuaryDay,
                                hour = uiState.mortuaryHour, onHourChange = viewModel::updateMortuaryHour,
                                minute = uiState.mortuaryMinute, onMinuteChange = viewModel::updateMortuaryMinute,
                            )
                        }
                    }
                    // 빈소 + 입관일시
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("빈소")
                            InputCell(value = uiState.funeralRoom, onValueChange = viewModel::updateFuneralRoom, weight = 2f)
                        }
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("입관일시")
                            DateTimeInputBlock(
                                year = uiState.year,
                                month = uiState.coffinMonth, onMonthChange = viewModel::updateCoffinMonth,
                                day = uiState.coffinDay, onDayChange = viewModel::updateCoffinDay,
                                hour = uiState.coffinHour, onHourChange = viewModel::updateCoffinHour,
                                minute = uiState.coffinMinute, onMinuteChange = viewModel::updateCoffinMinute,
                            )
                        }
                    }
                    // 장지 + 발인일시
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("장지")
                            InputCell(value = uiState.burialPlace, onValueChange = viewModel::updateBurialPlace, weight = 2f)
                        }
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("발인일시")
                            DateTimeInputBlock(
                                year = uiState.year,
                                month = uiState.departureMonth, onMonthChange = viewModel::updateDepartureMonth,
                                day = uiState.departureDay, onDayChange = viewModel::updateDepartureDay,
                                hour = uiState.departureHour, onHourChange = viewModel::updateDepartureHour,
                                minute = uiState.departureMinute, onMinuteChange = viewModel::updateDepartureMinute,
                            )
                        }
                    }
                }
                // 빈소입실 일시
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("빈소입실 일시", fontSize = 16.sp)
                        DateUnitInput(uiState.year,         viewModel::updateYear,         "년")
                        DateUnitInput(uiState.checkInMonth, viewModel::updateCheckInMonth, "월")
                        DateUnitInput(uiState.checkInDay,   viewModel::updateCheckInDay,   "일")
                        DateUnitInput(uiState.checkInHour,  viewModel::updateCheckInHour,  "시")
                        DateUnitInput(uiState.checkInMinute,viewModel::updateCheckInMinute,"분")
                    }
                }
            }

            ArticleDivider()

            // ── 제4조 장례식장 이용료 ─────────────────────────────────────────
            ArticleSection {
                ArticleTitle("제4조(장례식장 이용료)")
                // 빈소임대료
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("※빈소임대료※", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        // ← 데이터의 "빈소임대료(1f)" 위치와 일치
                        LabelCell("구분", modifier = Modifier.weight(1f).fillMaxHeight())
                        // ← 데이터의 Column(6.5f) 위치와 일치
                        Row(modifier = Modifier.weight(6.5f).fillMaxHeight()) {
                            // 내부는 RoomPriceRow 와 동일한 weight 구조 (합계 7.5f)
                            LabelCell("호실",   modifier = Modifier.weight(1f).fillMaxHeight())   // roomCol(1f)
                            LabelCell("면적",   modifier = Modifier.weight(1f).fillMaxHeight())   // size(1f)
                            Column(modifier = Modifier.weight(3f).fillMaxHeight()) {              // priceH(1.5)+priceD(1.5)
                                LabelCell("요금", height = 48.dp)
                                Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                                    LabelCell("1시간", modifier = Modifier.weight(1f).fillMaxHeight())
                                    LabelCell("1일",   modifier = Modifier.weight(1f).fillMaxHeight())
                                }
                            }
                            LabelCell("단위",   modifier = Modifier.weight(1f).fillMaxHeight())   // day(1f)
                            LabelCell("금액",   modifier = Modifier.weight(1.5f).fillMaxHeight()) // total(1.5f)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        LabelCell(text = "빈소임대료", modifier = Modifier.weight(1f).fillMaxHeight())
                        Column(modifier = Modifier.weight(6.5f)) {
                            uiState.roomPriceItems.forEach { item ->
                                RoomPriceRow(
                                    roomName = item.roomName,
                                    seatCount = item.seatCount,
                                    size = item.size,
                                    priceH = item.pricePerHour,
                                    priceD = item.pricePerDay,
                                    totalAmount = item.totalAmount,
                                    dayCount = item.dayCount,
                                    onDayCountChange = { count -> viewModel.updateRoomDayCount(item.roomName, count) },
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "빈소임대료 및 안치료는 24시간을 1일, 24시간 미만 12시간 이상일 경우 1일 계산\n12시간 미만일 경우 시간단위 산정, 1시간 미만은 1시간으로 산정한다.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }
                // 부대시설 이용료
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("※부대시설 이용료※", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).background(Color(0xFFF5F5F5))) {
                        LabelCell("구 분", modifier = Modifier.weight(2f).fillMaxHeight())
                        LabelCell("요 금", modifier = Modifier.weight(3f).fillMaxHeight())
                        LabelCell("단 위", modifier = Modifier.weight(1f).fillMaxHeight())
                        LabelCell("금 액", modifier = Modifier.weight(1.5f).fillMaxHeight())
                    }
                    uiState.serviceItems.forEach { item ->
                        ServiceInputRow(
                            title         = item.title,
                            price         = item.price,
                            unitText      = item.unitText,
                            countValue    = item.count,
                            onCountChange = { count -> viewModel.updateServiceCount(item.title, count) },
                            totalAmount   = item.totalAmount,
                            onAmountChange = {}
                        )
                    }
                    TotalServiceRow(
                        countValue    = "",
                        onCountChange = {},
                        unitText      = "원",
                        totalAmount   = serviceTotalFormatted
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "본 계약과 관련한 세부 내용은 '보람장례식장 이용약관'에 기재되어 있으니 계약서 작성 전 장례식장 이용약관에 대한 \n설명을 받으신 후 작성하시기 바라며, 계약서 작성이 끝나면 계약서, 약관을 교부 받으시기 바랍니다.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }
            }

            // ── 서명 ──────────────────────────────────────────────────────────
            ContractFooter(
                capturedPath = signatureData,
                updateTick = uiState.signatureUpdateTick,
                onSignatureClick = { viewModel.showSignatureDialog() }
            )
        }

        // 서명 다이얼로그
        if (uiState.isSignatureDialogVisible) {
            SignatureDialog(
                onDismiss = { viewModel.dismissSignatureDialog() },
                onConfirm = { path ->
                    signatureData = Path().apply { addPath(path) }
                    viewModel.confirmSignature()
                }
            )
        }
    }
}
