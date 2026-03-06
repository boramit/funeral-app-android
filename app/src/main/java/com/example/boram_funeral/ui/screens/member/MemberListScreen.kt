package com.example.boram_funeral.ui.screens.member

import MemberTable
import android.Manifest
import com.example.boram_funeral.R
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField
import com.example.boram_funeral.ui.components.common.Input.CustomTextField
import com.example.boram_funeral.ui.components.common.Input.DatePickerField
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel
import com.example.boram_funeral.ui.theme.boram_Br_Color
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.boram_funeral.ui.components.member.SearchBarComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    onBackClick: () -> Unit,
    memberViewModel: MemberViewModel = viewModel(),
    counselViewModel: CounselViewModel = viewModel(),
    navController: NavController
) {


    var isDialogOpen by remember { mutableStateOf(false) }
    var showRecordingDialog by remember { mutableStateOf(false) } // 녹음 안내 창 상태

    val scrollState = rememberScrollState()

    val events by memberViewModel.eventArray.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한 허용됨 -> 녹음 시작 및 이동
            counselViewModel.startRecording(context)
            navController.navigate("counsel") {
                popUpTo("counsel") { inclusive = true }
            }
        } else {
            // 권한 거부됨
            Toast.makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    val uiState by counselViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // 좌우 끝으로 자동 배치
                verticalAlignment = Alignment.CenterVertically // 높이 중앙 정렬 (추천)
                // horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이템 사이 간격
            ){
                IconButton(modifier = Modifier.width(32.dp).height(32.dp),onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_prev),
                        contentDescription = "뒤로가기",
                        // 만약 코드에서 색상을 강제로 바꾸고 싶다면 tint를 사용하세요.
                        // 원래 SVG 색상을 유지하려면 Color.Unspecified를 주면 됩니다.
                        tint = Color.Unspecified
                    )
                }
                Text(text = "행사 리스트", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                CustomButton(
                    size = ButtonSize.Small,
                    text = "신규 등록",
                    fullWidth = false,
                    icon = Icons.Default.Add,
                    onClick = { isDialogOpen = true },
                    horizontalPadding = 12.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Column(
            modifier = Modifier.fillMaxSize(), // 전체 화면
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchBarComponent(
                modifier = Modifier.fillMaxWidth(),
                onSearchClick = { name, start, end ->
                    println("성함: $name, 시작: $start, 종료: $end")
                })
            // 리스트 구현 (함수 분리 없이 직접 작성)
            MemberTable(
                events = events, // 관찰 중인 데이터 전달
                onEventClick = { eventId ->
                    navController.navigate("member_details/$eventId")
                    println("클릭된 행사 ID: $eventId")
                },
                modifier = Modifier.weight(1f) // 남은 하단 공간을 다 채움
            )
        }

        ModalSheet(
            isOpen = isDialogOpen,
            onDismiss = { isDialogOpen = false },
            contentPadding = 28.dp
        ) {
            // 여기에 넣고 싶은 UI 커스텀
            Column(modifier = Modifier.width(320.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "상담 정보 입력", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(modifier = Modifier.padding(vertical = 2.dp),
                    text = "녹음이 시작되면 상담녹음 사실에 대해 \n 유가족분께 고지 해주세요.",
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .verticalScroll(scrollState)
                        .padding(bottom = 8.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp) // 줄 간격
                ){
                        CustomTextField(
                            label = "고인명",
                            value = uiState.deceasedName,
                            onValueChange = { input ->
                                counselViewModel.updateField { it.copy(deceasedName = input) }
                            },
                            placeholder = "고인명을 작성 해 주세요."
                        )
                        CustomDropdownField(
                            label = "장례식장",
                            options = counselViewModel.funeralOption,
                            selectedOption = uiState.selectedFuneral,
                            onOptionSelected = { selection ->
                                // ✅ 선택된 값으로 상태 업데이트
                                counselViewModel.updateField { it.copy(selectedFuneral = selection) }
                            },
                            fontSize = 13.sp,
                            placeholder = "장례식장을 선택해주세요.",
                        )
                        DatePickerField(
                            label = "안치일시",
                            value = uiState.burialDate,
                            onDateSelected = { selectedDate ->
                                // 2. updateField를 통해 uiState 내의 burialDate 필드만 업데이트합니다.
                                counselViewModel.updateField { it.copy(burialDate = selectedDate) }
                            },
                            placeholder = "안치 날짜를 선택해주세요."
                            )
                        DatePickerField(
                            label = "입실일시",
                            value = uiState.checkInDate,
                            onDateSelected = { selectedDate ->
                                // 2. updateField를 통해 uiState 내의 burialDate 필드만 업데이트합니다.
                                counselViewModel.updateField { it.copy(checkInDate = selectedDate) }
                            },
                            placeholder = "입실 날짜를 선택해주세요."
                            )
                        DatePickerField(
                            label = "퇴실일시",
                            value = uiState.checkOutDate,
                            onDateSelected = { selectedDate ->
                                // 2. updateField를 통해 uiState 내의 burialDate 필드만 업데이트합니다.
                                counselViewModel.updateField { it.copy(checkOutDate = selectedDate) }
                            },
                            placeholder = "퇴실 날짜를 선택해주세요."
                            )
                        DatePickerField(
                            label = "발인일시",
                            value = uiState.departureDate,
                            onDateSelected = { selectedDate ->
                                // 2. updateField를 통해 uiState 내의 burialDate 필드만 업데이트합니다.
                                counselViewModel.updateField { it.copy(departureDate = selectedDate) }
                            },
                            placeholder = "발인 날짜를 선택해주세요."
                        )
//                        CustomDropdownField(
//                            label = "이송수단",
//                            options = counselViewModel.transportTypes,
//                            selectedOption = counselViewModel.selectedTransport,
//                            onOptionSelected = { counselViewModel.selectedTransport = it },
//                            fontSize = 13.sp
//                        )
//                        CustomDropdownField(
//                            label = "우선순위",
//                            options = counselViewModel.priorityTypes,
//                            selectedOption = counselViewModel.selectedPriority,
//                            onOptionSelected = { counselViewModel.selectedPriority = it },
//                            fontSize = 13.sp
//                        )
//                        DatePickerField(label = "이동 일시", value = counselViewModel.selectedDate, onDateSelected = {}, readOnly = true)
//
//                        CustomTextField(
//                            label = "이송 시간",
//                            value = counselViewModel.selectedTime,
//                            onValueChange = {
//                                if (it.length <= 5) counselViewModel.selectedTime = it
//                            },
//                            placeholder = "00:00",
//                            modifier = Modifier.fillMaxWidth(),fontSize = 13.sp
//                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier
                        .width(320.dp).navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ){
                    Box(modifier = Modifier.weight(1f)){
                        CustomButton(
                            size = ButtonSize.Medium,
                            text = "취소",
                            fullWidth = true,
                            onClick = { isDialogOpen = false },
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(1f)){
                        CustomButton(
                            backgroundColor = boram_Br_Color,
                            size = ButtonSize.Medium,
                            text = "저장 (녹음 시작)",
                            fullWidth = true,
                            onClick = {
                                counselViewModel.saveData(
                                    onSuccess = {
                                        isDialogOpen = false
                                        showRecordingDialog = true
                                    })
                            }
                        )
                    }
                }
            }
        }
        if (showRecordingDialog) {
            ModalSheet(
                isOpen = showRecordingDialog,
                onDismiss = { showRecordingDialog = false }
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).width(200.dp).height (180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Mic, // 플러스(+) 모양 아이콘
                            contentDescription = "녹음", // 시각 장애인을 위한 설명 (필수)
                            tint = boram_Br_Color, // 아이콘 색상 지정
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = "지금부터 녹음이\n시작됩니다.",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    CustomButton(
                        size = ButtonSize.Medium,
                        text = "확인 (상담 시작)",
                        fullWidth = true,
                        backgroundColor = boram_Br_Color,
                        onClick = {
                            showRecordingDialog = false

                            val permission = Manifest.permission.RECORD_AUDIO
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                counselViewModel.startRecording(context)
                                navController.navigate("counsel") {
                                    popUpTo("counsel") { inclusive = true }
                                }
                            } else {
                                launcher.launch(permission)
                            }
                        }
                    )
                }
            }
        }

    }
}


