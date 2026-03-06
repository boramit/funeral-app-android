package com.example.boram_funeral.ui.screens.counsel

import CounselingViewModel
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel
import com.example.boram_funeral.ui.theme.boram_Br_Color

@Composable
fun CounselScreen(
    counselViewModel: CounselViewModel,
    counselingViewModel: CounselingViewModel,
    onBackClick: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp)

    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // 좌우 끝으로 자동 배치
                verticalAlignment = Alignment.CenterVertically // 높이 중앙 정렬 (추천)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    IconButton(modifier = Modifier
                        .width(32.dp)
                        .height(32.dp),onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_prev),
                            contentDescription = "뒤로가기",
                            // 만약 코드에서 색상을 강제로 바꾸고 싶다면 tint를 사용하세요.
                            // 원래 SVG 색상을 유지하려면 Color.Unspecified를 주면 됩니다.
                            tint = Color.Unspecified
                        )
                    }
                    Text(text = "신규 정보 등록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row (
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CustomButton(
                        backgroundColor = boram_Br_Color,
                        size = ButtonSize.Small,
                        text = counselViewModel.buttonText,
                        fullWidth = false,
                        horizontalPadding = 12.dp,
                        onClick = { counselViewModel.checkServerConnection() }
                    )
                    CustomButton(
                        size = ButtonSize.Small,
                        text = "저장",
                        fullWidth = false,
                        horizontalPadding = 12.dp,
                        onClick = {
                            // 1. 먼저 입력한 텍스트 데이터(UI State)를 콘솔에 찍고 저장 준비
                            counselingViewModel.saveCounselingInfo()

                            // 2. 서버 저장(saveData) 호출
                            counselViewModel.saveData(onSuccess = {
                                // 3. 데이터 저장 성공 시 오디오 중지 및 파일 저장
                                counselViewModel.stopAndSaveAudio(context) {
                                    counselViewModel.clearAllInputs()
                                    // 4. 모든 과정 완료 시 토스트 알림
                                    android.widget.Toast.makeText(
                                        context,
                                        "상담 정보와 오디오 파일이 안전하게 저장되었습니다!",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Column (
            modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        shadowElevation = 20f
                        spotShadowColor = Color(0xFFD1D9E6) // 연한 보라/파란색 그림자 (고급스러운 느낌)
                        ambientShadowColor = Color(0xFFD1D9E6).copy(alpha = 0.5f)
                        shape = RoundedCornerShape(8.dp)
                        clip = false // 그림자가 박스 밖으로 번지게 허용
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F3F4))
            ){
                Row (
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(28.dp)
                                .height(28.dp)
                                .background(
                                    shape = RoundedCornerShape(50.dp), color = Color.Gray
                                )
                        ){
                            Icon(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                painter = painterResource(id = R.drawable.ic_flower_icon),
                                contentDescription = "국화 아이콘",
                                tint = Color.Unspecified
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier,
                            text = "삼가 고인의 명복을 빕니다.",
                            style = TextStyle(
                                fontSize = 15.sp,         // 글자 크기 키움
                                fontWeight = FontWeight.Bold, // 강조
                                color = Color(0xFF1E1F24),
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier,
                            text = "故",
                            style = TextStyle(
                                fontSize = 15.sp,         // 글자 크기 키움
                                fontWeight = FontWeight.Bold, // 강조
                                color = Color(0xFF1E1F24),
                            ))
                        Text(
                            modifier = Modifier,
                            text = "홍길동",
                            style = TextStyle(
                                fontSize = 15.sp,         // 글자 크기 키움
                                fontWeight = FontWeight.Bold, // 강조
                                color = Color(0xFF1E1F24),
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            modifier = Modifier,
                            text = "님",
                            style = TextStyle(
                                fontSize = 15.sp,         // 글자 크기 키움
                                fontWeight = FontWeight.Bold, // 강조
                                color = Color(0xFF1E1F24),
                            ))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = if (counselViewModel.isRecording) "🔴 현재 녹음 중" else "⚪ 녹음 대기 중",
                        style = TextStyle(
                            fontSize = 12.sp, // 글자 크기 키움
                            fontWeight = FontWeight.Medium, // 강조
                        )
                    )
                }
            }
            Column (modifier = Modifier
                .fillMaxWidth()
                ){
                Spacer(modifier = Modifier.height(18.dp))
                Column (modifier = Modifier.fillMaxWidth()){
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text="행사 정보",
                        style = TextStyle(
                            fontSize = 16.sp,// 글자 크기 키움
                            fontWeight = FontWeight.Bold, // 강조
                            color = Color(0xFF1E1F24),
                        ))
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (modifier = Modifier
                        .fillMaxWidth()
                    ){
                        StyledInfoBox(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column (modifier = Modifier.padding(16.dp)){
                                Text(
                                    text = "장례 일정 및 장소",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold, // 강조
                                        color = Color(0xFF1E1F24),
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Column (){
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            text = "계열사",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "보람상조개발(주)",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            text = "장례식장",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "보람인천식장",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "입실일",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "2025-12-10",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Text(
                                            text = "발인일",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "2025-12-12",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "빈소",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "VIP2호실",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        StyledInfoBox(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column (modifier = Modifier.padding(16.dp)){
                                Text(
                                    text = "장례 진행 현황",
                                    style = TextStyle(
                                        fontSize = 16.sp,            // 글자 크기 키움
                                        fontWeight = FontWeight.Bold, // 강조
                                        color = Color(0xFF1E1F24),

                                        )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Column (){
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "행사",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "자체행사",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "상주",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "홍길동",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "상품진행",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "699상품",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "특이사항",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Column (horizontalAlignment = Alignment.Start){
                                            Text(
                                                text = "(-) 할인 1,920,000원",
                                                style = TextStyle(
                                                    fontSize = 14.sp,           // 글자 크기 키움
                                                    fontWeight = FontWeight.Bold, // 강조
                                                    color = Color(0xFF1E1F24),
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "(-) 접객실 50%",
                                                style = TextStyle(
                                                    fontSize = 14.sp,           // 글자 크기 키움
                                                    fontWeight = FontWeight.Bold, // 강조
                                                    color = Color(0xFF1E1F24),
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        StyledInfoBox(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column (modifier = Modifier.padding(16.dp)){
                                Text(
                                    text = "정산내역",
                                    style = TextStyle(
                                        fontSize = 16.sp,           // 글자 크기 키움
                                        fontWeight = FontWeight.Bold, // 강조
                                        color = Color(0xFF1E1F24),
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Column (){
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "카드",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "2,000,000원",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {
                                        Text(
                                            text = "현금",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "6,920,000원",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = Color(0xFF1E1F24),
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    )
                                    {
                                        Text(
                                            text = "총 금액",
                                            style = TextStyle(
                                                fontSize = 14.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Medium, // 강조
                                                color = Color.Gray,
                                            )
                                        )
                                        Text(
                                            text = "8,920,000원",
                                            style = TextStyle(
                                                fontSize = 20.sp,           // 글자 크기 키움
                                                fontWeight = FontWeight.Bold, // 강조
                                                color = boram_Br_Color,
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    CustomTabControl(
                        tabs = listOf("사전상담", "상담화면"),
                        modifier = Modifier.fillMaxWidth(),
//                        contentHeight = 700.dp,
                    ) { index ->
                        when (index) {
                            0 -> CounselingTabContent(viewModel = counselingViewModel)
                            1 -> ConsultationScreenContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StyledInfoBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(280.dp)
            .graphicsLayer {
                shadowElevation = 20f
                spotShadowColor = Color(0xFFD1D9E6) // 연한 보라/파란색 그림자 (고급스러운 느낌)
                ambientShadowColor = Color(0xFFD1D9E6).copy(alpha = 0.5f)
                shape = RoundedCornerShape(8.dp)
                clip = false // 그림자가 박스 밖으로 번지게 허용
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F3F4))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}

// 1번 탭: 사전상담 (행사 정보 등)
@Composable
fun CounselingTabContent(
    modifier: Modifier = Modifier,
    viewModel: CounselingViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    // 전체 스크롤이 가능하도록 verticalScroll 추가 (ERP 특성상 필드가 많아지므로 필수)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "상담 정보",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1F24)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        // 기본데이터
        BasicInfoComponent(
            // 기본 정보 = 상태값
            counselingNo = uiState.counselingNo,
            counselingType = uiState.counselingType,
            customerName = uiState.customerName,
            phoneNumber = uiState.phoneNumber,
            relationship = uiState.relationship,

            // 기본 정보 = 옵션 데이터
            typeOptions = viewModel.counselingTypeOptions,
            relationshipOptions = viewModel.relationshipOptions,
            
            onTypeChange = { input ->
                viewModel.updateField { it.copy(counselingType = input) }
            },
            onNameChange = { input ->
                viewModel.updateField { it.copy(customerName = input) }
            },
            onPhoneChange = { input ->
                viewModel.updateField { it.copy(phoneNumber = input) }
            },
            onRelationshipChange = { input ->
                viewModel.updateField { it.copy(relationship = input) }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,            // 선의 두께
            color = Color(0xFFEEEEEE)    // 선의 색상 (연한 회색 추천)
        )
        Spacer(modifier = Modifier.height(20.dp))
        EventDetailComponent(
            funeralHome = uiState.funeralHome,
            eventType = uiState.eventType,
            patientName = uiState.patientName,
            age = uiState.age,
            religion = uiState.religion,
            
            // 행사 정보 = 옵션 데이터
            funeralHomeOptions = viewModel.funeralHomeOptions,
            eventTypeOptions = viewModel.eventTypeOptions,
            religionTypeOptions = viewModel.religionTypeOptions,

            onFuneralHomeChange = { input ->
                viewModel.updateField { it.copy(funeralHome = input) }
            },
            onEventTypeChange = { input ->
                viewModel.updateField { it.copy(eventType = input) }
            },
            onPatientNameChange = { input ->
                viewModel.updateField { it.copy(patientName = input) }
            },
            onAgeChange = { input ->
                viewModel.updateField { it.copy(age = input) }
            },
            onReligionChange = { input ->
                viewModel.updateField { it.copy(religion = input) }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,            // 선의 두께
            color = Color(0xFFEEEEEE)    // 선의 색상 (연한 회색 추천)
        )
        Spacer(modifier = Modifier.height(20.dp))
        LocationInfoComponent(
            locationAdmission = uiState.locationAdmission,
            locationCare = uiState.locationCare,
            funeralCompany = uiState.funeralCompany,
            onAdmissionChange = { input ->
                viewModel.updateField { it.copy(locationAdmission = input )}
            },
            onCareChange = { input ->
                viewModel.updateField { it.copy(locationCare = input )}
            },
            onCompanyChange = { input ->
                viewModel.updateField { it.copy(funeralCompany = input )}
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,            // 선의 두께
            color = Color(0xFFEEEEEE)    // 선의 색상 (연한 회색 추천)
        )
        Spacer(modifier = Modifier.height(20.dp))
        SaleAgreementComponent(
            saleStatus = uiState.saleStatus,
            saleCategory = uiState.saleCategory,
            companyName = uiState.companyName,
            agreementDetail = uiState.agreementDetail,
            onStatusChange = { input ->
                viewModel.updateField { it.copy(saleStatus = input) }
            },
            onCategoryChange = { input ->
                viewModel.updateField { it.copy(saleCategory = input) }
            },
            onCompanyNameChange = { input ->
                viewModel.updateField { it.copy(companyName = input) }
            },
            onDetailChange = { input ->
                viewModel.updateField { it.copy(agreementDetail = input) }
            }
        )
    }
}

// 2번 탭: 상담화면
@Composable
fun ConsultationScreenContent() {
    // 1. 탭 인덱스 상태
    var selectedSubTabIndex by remember { mutableIntStateOf(0) }

    // 2. 각 탭에서 사용할 데이터 상태 (상태 끌어올리기)
    var companyName by remember { mutableStateOf("") }
    var agreementDetail by remember { mutableStateOf("") }
    var saleStatus by remember { mutableStateOf("미적용") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState()))
    {
        CustomSubTabControl(
            selectedSubTab = selectedSubTabIndex,
            onSubTabSelected = { selectedSubTabIndex = it }
        ) { index ->
            // index에 따라 실제 입력 컴포넌트 호출
            when (index) {
                0 -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    ServiceAgreementComponent(
                        onAgreementConfirmed = { isAgreed ->
                            println("사용자 동의 여부: $isAgreed")
                        }
                    )
                }

                1 -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Text("상세조건 설정 화면", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("여기에 추가 옵션이나 상세 설정 필드를 넣으세요.")
                }
            }
        }
    }
}