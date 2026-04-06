package com.example.boram_funeral.ui.screens.counsel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.common.Tab.CustomSubTabControl
import com.example.boram_funeral.ui.components.common.Tab.CustomTabControl
import com.example.boram_funeral.ui.components.counsel.BasicInfoComponent
import com.example.boram_funeral.ui.components.counsel.EventDetailComponent
import com.example.boram_funeral.ui.components.counsel.LocationInfoComponent
import com.example.boram_funeral.ui.components.counsel.SaleAgreementComponent
import com.example.boram_funeral.ui.components.counsel.consultation.ServiceAgreementComponent
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.ConsultationViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.CounselingViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.CounselUiState
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel
import com.example.boram_funeral.ui.screens.counsel.model.ProductItem
import com.example.boram_funeral.ui.theme.boram_Br_Color

// ── 메인 화면 ─────────────────────────────────────────────────────────────────

@Composable
fun CounselScreen(
    counselViewModel: CounselViewModel,
    counselingViewModel: CounselingViewModel,
    contractViewModel: ContractViewModel,
    consultationViewModel: ConsultationViewModel,
    onBackClick: () -> Unit,
    navController: NavController,
) {
    val context  = LocalContext.current
    val uiState by counselViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        // ── 상단 헤더 ────────────────────────────────────────────────────────
        CounselHeader(
            isRecording = counselViewModel.isRecording,
            buttonText  = counselViewModel.buttonText,
            onBackClick = onBackClick,
            onConnect   = { counselViewModel.checkServerConnection() },
            onSave      = {
                counselingViewModel.saveCounselingInfo()
                consultationViewModel.saveSelectedProducts { saved ->
                    android.util.Log.d("CounselScreen", "상품 ${saved.size}개 저장")
                }
                counselViewModel.saveData(onSuccess = {
                    counselViewModel.stopAndSaveAudio(context) {
                        counselViewModel.clearAllInputs()
                        consultationViewModel.clearSelectedProducts()
                        android.widget.Toast.makeText(
                            context,
                            "상담 정보와 오디오 파일이 안전하게 저장되었습니다!",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                })
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── 스크롤 가능 본문 ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            DeceasedInfoBanner(
                isRecording  = counselViewModel.isRecording,
                deceasedName = uiState.deceasedName,
            )
            Spacer(modifier = Modifier.height(18.dp))
            EventSummaryRow(uiState = uiState)
            Spacer(modifier = Modifier.height(18.dp))

            CustomTabControl(
                tabs = listOf("사전상담", "상담화면"),
                modifier = Modifier.fillMaxWidth(),
            ) { index ->
                when (index) {
                    0 -> CounselingTabContent(viewModel = counselingViewModel)
                    1 -> ConsultationScreenContent(
                        onOpenContract = { contractViewModel.openContract() },
                        viewModel      = consultationViewModel,
                    )
                }
            }
        }
    }
}

// ── 헤더 ─────────────────────────────────────────────────────────────────────

@Composable
private fun CounselHeader(
    isRecording: Boolean,
    buttonText: String,
    onBackClick: () -> Unit,
    onConnect: () -> Unit,
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_prev),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified
                )
            }
            Text("신규 정보 등록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            CustomButton(
                backgroundColor = boram_Br_Color,
                size = ButtonSize.Small,
                text = buttonText,
                fullWidth = false,
                horizontalPadding = 12.dp,
                onClick = onConnect
            )
            CustomButton(
                size = ButtonSize.Small,
                text = "저장",
                fullWidth = false,
                horizontalPadding = 12.dp,
                onClick = onSave
            )
        }
    }
}

// ── 고인 배너 ─────────────────────────────────────────────────────────────────

@Composable
private fun DeceasedInfoBanner(isRecording: Boolean, deceasedName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 20f
                spotShadowColor = Color(0xFFD1D9E6)
                ambientShadowColor = Color(0xFFD1D9E6).copy(alpha = 0.5f)
                shape = RoundedCornerShape(8.dp)
                clip = false
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F3F4)),
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Gray, RoundedCornerShape(50.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_flower_icon),
                        contentDescription = "국화 아이콘",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                InfoText("삼가 고인의 명복을 빕니다.")
                Spacer(modifier = Modifier.width(8.dp))
                if (deceasedName.isNotBlank()) {
                    InfoText("故")
                    InfoText(deceasedName)
                    Spacer(modifier = Modifier.width(4.dp))
                    InfoText("님")
                }
            }
            Text(
                text = if (isRecording) "🔴 현재 녹음 중" else "⚪ 녹음 대기 중",
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

// ── 행사 정보 요약 (3컬럼) ────────────────────────────────────────────────────

@Composable
private fun EventSummaryRow(uiState: CounselUiState) {
    Text("행사 정보", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1F24)))
    Spacer(modifier = Modifier.height(20.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        StyledInfoBox(modifier = Modifier.weight(1f)) {
            SectionTitle("장례 일정 및 장소")
            InfoRow("계열사",   uiState.affiliate.ifBlank { "-" })
            InfoRow("장례식장", uiState.selectedFuneral.ifBlank { "-" })
            InfoRow("입실일",   uiState.checkInDate.ifBlank { "-" })
            InfoRow("발인일",   uiState.departureDate.ifBlank { "-" })
            InfoRow("빈소",     uiState.mortuary.ifBlank { "-" })
        }
        Spacer(modifier = Modifier.width(8.dp))
        StyledInfoBox(modifier = Modifier.weight(1f)) {
            SectionTitle("장례 진행 현황")
            InfoRow("행사",     uiState.eventType.ifBlank { "-" })
            InfoRow("상주",     uiState.chiefMourner.ifBlank { "-" })
            InfoRow("상품진행", uiState.productProgress.ifBlank { "-" })
            InfoRow("특이사항", uiState.specialNote.ifBlank { "-" })
        }
        Spacer(modifier = Modifier.width(8.dp))
        StyledInfoBox(modifier = Modifier.weight(1f)) {
            SectionTitle("정산내역")
            InfoRow("카드", uiState.cardPayment.ifBlank { "-" })
            InfoRow("현금", uiState.cashPayment.ifBlank { "-" })
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("총 금액", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray))
                Text(
                    text = uiState.totalAmount.ifBlank { "-" },
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = boram_Br_Color)
                )
            }
        }
    }
}

// ── 사전상담 탭 ───────────────────────────────────────────────────────────────

@Composable
fun CounselingTabContent(viewModel: CounselingViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(18.dp))
        Text("상담 정보", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1F24)))
        Spacer(modifier = Modifier.height(24.dp))

        BasicInfoComponent(
            counselingNo   = uiState.counselingNo,
            counselingType = uiState.counselingType,
            customerName   = uiState.customerName,
            phoneNumber    = uiState.phoneNumber,
            relationship   = uiState.relationship,
            typeOptions         = viewModel.counselingTypeOptions,
            relationshipOptions = viewModel.relationshipOptions,
            onTypeChange         = { viewModel.updateField { s -> s.copy(counselingType = it) } },
            onNameChange         = { viewModel.updateField { s -> s.copy(customerName = it) } },
            onPhoneChange        = { viewModel.updateField { s -> s.copy(phoneNumber = it) } },
            onRelationshipChange = { viewModel.updateField { s -> s.copy(relationship = it) } },
        )
        TabDivider()
        EventDetailComponent(
            funeralHome  = uiState.funeralHome,
            eventType    = uiState.eventType,
            patientName  = uiState.patientName,
            age          = uiState.age,
            religion     = uiState.religion,
            funeralHomeOptions  = viewModel.funeralHomeOptions,
            eventTypeOptions    = viewModel.eventTypeOptions,
            religionTypeOptions = viewModel.religionTypeOptions,
            onFuneralHomeChange = { viewModel.updateField { s -> s.copy(funeralHome = it) } },
            onEventTypeChange   = { viewModel.updateField { s -> s.copy(eventType = it) } },
            onPatientNameChange = { viewModel.updateField { s -> s.copy(patientName = it) } },
            onAgeChange         = { viewModel.updateField { s -> s.copy(age = it) } },
            onReligionChange    = { viewModel.updateField { s -> s.copy(religion = it) } },
        )
        TabDivider()
        LocationInfoComponent(
            locationAdmission = uiState.locationAdmission,
            locationCare      = uiState.locationCare,
            funeralCompany    = uiState.funeralCompany,
            onAdmissionChange = { viewModel.updateField { s -> s.copy(locationAdmission = it) } },
            onCareChange      = { viewModel.updateField { s -> s.copy(locationCare = it) } },
            onCompanyChange   = { viewModel.updateField { s -> s.copy(funeralCompany = it) } },
        )
        TabDivider()
        SaleAgreementComponent(
            saleStatus      = uiState.saleStatus,
            saleCategory    = uiState.saleCategory,
            companyName     = uiState.companyName,
            agreementDetail = uiState.agreementDetail,
            onStatusChange      = { viewModel.updateField { s -> s.copy(saleStatus = it) } },
            onCategoryChange    = { viewModel.updateField { s -> s.copy(saleCategory = it) } },
            onCompanyNameChange = { viewModel.updateField { s -> s.copy(companyName = it) } },
            onDetailChange      = { viewModel.updateField { s -> s.copy(agreementDetail = it) } },
        )
    }
}

// ── 상담화면 탭 ───────────────────────────────────────────────────────────────

@Composable
fun ConsultationScreenContent(
    onOpenContract: () -> Unit = {},
    viewModel: ConsultationViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    // 상품 상세 모달
    uiState.selectedProduct?.let { product ->
        val isAlreadySelected = product.id in uiState.selectedProductIds
        ProductDetailDialog(
            product           = product,
            isAlreadySelected = isAlreadySelected,
            onCancel          = { viewModel.cancelProduct() },
            onConfirm         = { viewModel.confirmProduct(product) },
        )
    }

    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CustomSubTabControl(
            selectedSubTab   = uiState.selectedTabIndex,
            onSubTabSelected = { viewModel.selectTab(it) },
        ) { index ->
            when (index) {
                0 -> Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    ServiceAgreementComponent(
                        onAgreementConfirmed = { },
                        onOpenContract       = onOpenContract,
                    )
                }
                else -> ProductCategoryContent(
                    items             = uiState.currentProducts,
                    selectedProductIds = uiState.selectedProductIds,
                    onItemClick       = { viewModel.openProduct(it) },
                    onRemoveItem      = { viewModel.removeProduct(it.id) },
                )
            }
        }

    }
}

// ── 상품 목록 컴포넌트 ────────────────────────────────────────────────────────

@Composable
private fun ProductCategoryContent(
    items: List<ProductItem>,
    selectedProductIds: Set<Int>,
    onItemClick: (ProductItem) -> Unit,
    onRemoveItem: (ProductItem) -> Unit,
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("등록된 상품이 없습니다.", color = Color(0xFF999999), fontSize = 14.sp)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(max = 800.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(items, key = { it.id }) { product ->
            val isSelected = product.id in selectedProductIds
            ProductListItem(
                product    = product,
                isSelected = isSelected,
                onClick    = { onItemClick(product) },
                onRemove   = { onRemoveItem(product) },
            )
        }
    }
}

@Composable
private fun ProductListItem(
    product: ProductItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    val borderColor = if (isSelected) Color(0xFF05195F) else Color(0xFFDDDDDD)
    val bgColor     = if (isSelected) Color(0xFFEEF2FF) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(if (isSelected) 1.5.dp else 0.5.dp, borderColor, RoundedCornerShape(8.dp))
            .background(bgColor, RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 썸네일
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (product.imageResId != null) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text("NO\nIMG", fontSize = 9.sp, color = Color(0xFFAAAAAA), textAlign = TextAlign.Center)
            }
            // 선택됨 뱃지
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color(0xFF05195F), CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) Color(0xFF05195F) else Color.Unspecified,
            )
            if (product.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(product.description, fontSize = 12.sp, color = Color(0xFF666666))
            }
        }

        if (product.price.isNotBlank()) {
            Text(product.price, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF05195F))
        }

        // 선택 해제 버튼
        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Check, contentDescription = "선택 해제", tint = Color(0xFF05195F), modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ── 상품 상세 모달 ────────────────────────────────────────────────────────────

@Composable
private fun ProductDetailDialog(
    product: ProductItem,
    isAlreadySelected: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))

                if (product.imageResId != null) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (product.description.isNotBlank()) {
                    Text(product.description, fontSize = 13.sp, color = Color(0xFF555555), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (product.price.isNotBlank()) {
                    Text(product.price, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF05195F), modifier = Modifier.fillMaxWidth())
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 취소 / 확인 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("취소")
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAlreadySelected) Color(0xFFE53935) else Color(0xFF05195F)
                        ),
                    ) {
                        Text(if (isAlreadySelected) "선택 해제" else "확인", color = Color.White)
                    }
                }
            }
        }
    }
}

// ── 재사용 UI 헬퍼 ────────────────────────────────────────────────────────────

@Composable
fun StyledInfoBox(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier
            .height(280.dp)
            .graphicsLayer {
                shadowElevation = 20f
                spotShadowColor = Color(0xFFD1D9E6)
                ambientShadowColor = Color(0xFFD1D9E6).copy(alpha = 0.5f)
                shape = RoundedCornerShape(8.dp)
                clip = false
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F3F4)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = content,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1F24)))
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray))
        Text(value, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1F24)))
    }
}

@Composable
private fun InfoText(text: String) {
    Text(text, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1F24)))
}

@Composable
private fun TabDivider() {
    Spacer(modifier = Modifier.height(20.dp))
    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFEEEEEE))
    Spacer(modifier = Modifier.height(20.dp))
}