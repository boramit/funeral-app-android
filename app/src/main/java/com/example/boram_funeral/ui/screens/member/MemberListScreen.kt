package com.example.boram_funeral.ui.screens.member

import com.example.boram_funeral.ui.components.common.Table.MemberTable
import com.example.boram_funeral.ui.screens.counsel.model.FuneralHomeOption
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField
import com.example.boram_funeral.ui.components.common.Input.CustomTextField
import com.example.boram_funeral.ui.components.common.Input.DatePickerField
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.components.member.SearchBarComponent
import com.example.boram_funeral.ui.screens.counsel.logic.CounselUiState
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel
import com.example.boram_funeral.ui.theme.boram_Br_Color
import com.example.boram_funeral.ui.utils.isLandscape

/**
 * 행사(상담) 목록 화면.
 *
 * - 상단: 뒤로가기 + 화면 제목 + 신규 등록 버튼
 * - 중단: 성함·날짜 검색바 + 행사 테이블
 * - 모달: 신규 상담 정보 입력 → 녹음 시작 확인
 *
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param memberViewModel 행사 목록 상태를 관리하는 ViewModel
 * @param counselViewModel 신규 상담 입력 상태를 관리하는 ViewModel
 * @param navController 화면 전환 컨트롤러
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    onBackClick: () -> Unit,
    memberViewModel: MemberViewModel = viewModel(),
    counselViewModel: CounselViewModel = viewModel(),
    navController: NavController,
) {
    var isNewCounselOpen by remember { mutableStateOf(false) }
    var isRecordingDialogOpen by remember { mutableStateOf(false) }

    val events by memberViewModel.eventArray.collectAsState()
    val uiState by counselViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 마이크 권한 요청 결과 처리
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            counselViewModel.startRecording(context)
            navController.navigate("counsel") { popUpTo("counsel") { inclusive = true } }
        } else {
            Toast.makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    val landscape = isLandscape()

    // ── 메인 레이아웃 ─────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = if (landscape) 16.dp else 24.dp,
                vertical   = if (landscape) 12.dp else 28.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier.size(32.dp),
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_prev),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified,
                )
            }
            Text(
                text = "행사 리스트",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                size = ButtonSize.Medium,
                text = "신규 등록",
                fullWidth = false,
                width    = if (landscape) 120.dp else 100.dp,
                height   = if (landscape) 44.dp  else 48.dp,
                fontSize = if (landscape) 16.sp  else 14.sp,
                iconSize = if (landscape) 18.dp  else 20.dp,
                icon = Icons.Default.Add,
                onClick = { isNewCounselOpen = true },
            )
        }

        Spacer(modifier = Modifier.height(if (landscape) 8.dp else 18.dp))

        // 검색바 + 행사 테이블
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(if (landscape) 8.dp else 16.dp),
        ) {
            SearchBarComponent(
                modifier = Modifier.fillMaxWidth(),
                onSearchClick = { name, start, end ->
                    Log.d("MemberList", "검색 — 성함: $name, 시작: $start, 종료: $end")
                },
            )
            MemberTable(
                events = events,
                modifier = Modifier.weight(1f),
                onEventClick = { eventId ->
                    Log.d("MemberList", "행사 클릭 — ID: $eventId")
                    navController.navigate("member_details/$eventId")
                },
            )
        }
    }

    // ── 모달: 신규 상담 정보 입력 ────────────────────────────────────────────
    NewCounselDialog(
        isOpen = isNewCounselOpen,
        uiState = uiState,
        funeralOptions = counselViewModel.funeralOptions,
        onFieldUpdate = counselViewModel::updateField,
        onDismiss = { isNewCounselOpen = false },
        onSave = {
            counselViewModel.saveData(
                onSuccess = {
                    isNewCounselOpen = false
                    isRecordingDialogOpen = true
                },
            )
        },
    )

    // ── 모달: 녹음 시작 확인 ─────────────────────────────────────────────────
    RecordingStartDialog(
        isOpen = isRecordingDialogOpen,
        onDismiss = { isRecordingDialogOpen = false },
        onConfirm = {
            isRecordingDialogOpen = false
            val permission = Manifest.permission.RECORD_AUDIO
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                counselViewModel.startRecording(context)
                navController.navigate("counsel") { popUpTo("counsel") { inclusive = true } }
            } else {
                micPermissionLauncher.launch(permission)
            }
        },
    )
}

// ── Private Composables ───────────────────────────────────────────────────────

/**
 * 신규 상담 정보를 입력하는 모달 다이얼로그.
 *
 * 고인명, 장례식장, 안치·입실·퇴실·발인 일시를 입력한 뒤
 * 저장 시 [onSave]를 호출하고 녹음 시작 모달로 이어진다.
 *
 * @param isOpen 모달 표시 여부
 * @param uiState 현재 입력 상태값 (CounselUiState)
 * @param funeralOptions 장례식장 선택 목록
 * @param onFieldUpdate 필드 변경 콜백 — CounselViewModel.updateField 에 대응
 * @param onDismiss 취소 또는 외부 클릭 콜백
 * @param onSave 저장 버튼 클릭 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewCounselDialog(
    isOpen: Boolean,
    uiState: CounselUiState,
    funeralOptions: List<FuneralHomeOption>,
    onFieldUpdate: ((CounselUiState) -> CounselUiState) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    val landscape   = isLandscape()
    val scrollState = rememberScrollState()

    ModalSheet(
        isOpen = isOpen,
        onDismiss = onDismiss,
        contentPadding = 28.dp,
    ) {
        Column(
            modifier = Modifier.width(if (landscape) 480.dp else 320.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 안내 헤더
            Text(
                text = "상담 정보 입력",
                fontSize = if (landscape) 24.sp else 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "녹음이 시작되면 상담녹음 사실에 대해\n유가족분께 고지 해주세요.",
                textAlign = TextAlign.Center,
                fontSize = if (landscape) 16.sp else 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = if (landscape) 24.sp else 18.sp,
            )

            // 입력 필드 (스크롤 영역)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CustomTextField(
                    label = "고인명",
                    labelFontSize = 14.sp,
                    value = uiState.deceasedName,
                    onValueChange = { input -> onFieldUpdate { it.copy(deceasedName = input) } },
                    placeholder = "고인명을 작성 해 주세요.",
                    height = 48.dp,
                    fontSize = if (landscape) 16.sp else 14.sp,
                    )
                Spacer(modifier = Modifier.height(8.dp))
                CustomDropdownField(
                    label = "장례식장",
                    labelFontSize = 14.sp,
                    options = funeralOptions.map { it.name },
                    selectedOption = uiState.selectedFuneral,
                    onOptionSelected = { name ->
                        val opt = funeralOptions.find { it.name == name }
                        onFieldUpdate { it.copy(selectedFuneral = name, selectedFuneralId = opt?.id ?: 0) }
                    },
                    placeholder = "장례식장을 선택해주세요.",
                    height = 48.dp,
                    fontSize = if (landscape) 16.sp else 14.sp,
                    )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(
                    label = "안치일시",
                    labelFontSize = 14.sp,
                    value = uiState.burialDate,
                    onDateSelected = { date -> onFieldUpdate { it.copy(burialDate = date) } },
                    placeholder = "안치 날짜를 선택해주세요.",
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(
                    label = "입실일시",
                    labelFontSize = 14.sp,
                    value = uiState.checkInDate,
                    onDateSelected = { date -> onFieldUpdate { it.copy(checkInDate = date) } },
                    placeholder = "입실 날짜를 선택해주세요.",
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(
                    label = "퇴실일시",
                    labelFontSize = 14.sp,
                    value = uiState.checkOutDate,
                    onDateSelected = { date -> onFieldUpdate { it.copy(checkOutDate = date) } },
                    placeholder = "퇴실 날짜를 선택해주세요.",
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(
                    label = "발인일시",
                    value = uiState.departureDate,
                    onDateSelected = { date -> onFieldUpdate { it.copy(departureDate = date) } },
                    placeholder = "발인 날짜를 선택해주세요.",
                )
            }

            // 버튼 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomButton(
                        size = ButtonSize.Medium,
                        text = "취소",
                        fullWidth = true,
                        onClick = onDismiss,
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    CustomButton(
                        size = ButtonSize.Medium,
                        text = "저장 (녹음 시작)",
                        fullWidth = true,
                        backgroundColor = boram_Br_Color,
                        onClick = onSave,
                    )
                }
            }
        }
    }
}

/**
 * 녹음 시작 안내 모달 다이얼로그.
 *
 * 신규 상담 저장 완료 후 표시되며, 확인 시 마이크 권한을 확인하고
 * 상담 화면으로 이동한다.
 *
 * @param isOpen 모달 표시 여부
 * @param onDismiss 외부 클릭 / 닫기 콜백
 * @param onConfirm 확인 버튼 클릭 콜백 (권한 체크 및 화면 이동 포함)
 */
@Composable
private fun RecordingStartDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val landscape = isLandscape()

    ModalSheet(
        isOpen = isOpen,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(if (landscape) 16.dp else 24.dp)
                .width(280.dp)
                .height(if (landscape) 160.dp else 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "녹음",
                tint = boram_Br_Color,
                modifier = Modifier.size(if (landscape) 24.dp else 32.dp),
            )
            Spacer(modifier = Modifier.height(if (landscape) 4.dp else 8.dp))
            Text(
                text = "지금부터 녹음이\n시작 됩니다.",
                textAlign = TextAlign.Center,
                fontSize = if (landscape) 20.sp else 18.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = if (landscape) 24.sp else 22.sp,
            )
            Spacer(modifier = Modifier.height(if (landscape) 12.dp else 20.dp))
            CustomButton(
                size = ButtonSize.Medium,
                text = "확인 (상담 시작)",
                fullWidth = true,
                backgroundColor = boram_Br_Color,
                onClick = onConfirm,
            )
        }
    }
}
