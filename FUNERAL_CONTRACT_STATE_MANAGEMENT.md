# 장례 계약서 상태 관리 문서

> 보람 장례 컨설팅 앱 — 계약서 작성 플로우 상태 관리 설계 가이드

---

## 1. 전체 아키텍처 개요

```
UI Layer (Compose)
  └─ ReceptionBasicStep         ← Step 1 화면
  └─ DeceasedDetailStep         ← Step 2 화면
  └─ CasketShroudStep           ← Step 3 화면
  └─ FoodCateringStep           ← Step 4 화면

ViewModel Layer
  └─ ReceptionBasicViewModel    ← Step 1 전담
  └─ UseContractViewModel       ← Step 2 전담
  └─ CasketShroudViewModel      ← Step 3 전담
  └─ FoodCateringViewModel      ← Step 4 전담
  └─ FuneralContractViewModel   ← 마스터 (취합 + 저장)

Data Layer
  └─ FuneralContractRepository  ← 서버 API 통신
  └─ PdfGenerator               ← PDF 파일 생성
  └─ FuneralContractModels.kt   ← 상태 데이터 클래스
  └─ FuneralContractRequest.kt  ← API 요청/응답 모델
```

---

## 2. 계약 단계별 상태 정의

### Step 1 — 접수 기본 정보 (`ReceptionBasicState`)

| 필드 | 타입 | 설명 | 입력 방식 |
|------|------|------|-----------|
| `deathReason` | String | 부고사유 | Dropdown (병사/외인사/자연사/미상/기타/코로나) |
| `eventType` | String | 행사형태 | Dropdown (자체/타상조/보람그룹/무빈소/대관 행사) |
| `inflowPath` | String | 유입경로 | 텍스트 입력 |
| `roomName` | String | 빈소 | Dropdown (특실/1호실/2호실) |
| `chiefMournerName` | String | 상주명 | 텍스트 입력 |
| `deceasedName` | String | 고인명 | 텍스트 입력 |
| `directorName` | String | 지도사명 | 텍스트 입력 |

**ViewModel:** `ReceptionBasicViewModel`
**필수값:** `deceasedName`, `chiefMournerName`, `roomName`

---

### Step 2 — 장례식장 이용계약서 (`UseContractState`)

| 필드 | 타입 | 설명 |
|------|------|------|
| `contractYear` | String | 계약 연도 (자동 현재 연도) |
| `contractStartMonth` / `contractStartDay` | String | 계약 시작 월일 |
| `contractEndMonth` / `contractEndDay` | String | 계약 종료 월일 |
| `deceasedName` | String | 고인 성명 |
| `bongwan` | String | 본관 |
| `age` | String | 연령 |
| `gender` | String | 성별 |
| `jumin` | String | 주민번호 |
| `address` | String | 주소 |
| `roomType` | String | 호실 종류 |
| `roomDays` | String | 이용 일수 |
| `roomAmount` | String | 빈소 이용 금액 |
| `serviceItems` | `List<ContractServiceItemState>` | 서비스 항목 목록 |
| `consultantSignature` | `Bitmap?` | 상담자 서명 이미지 |
| `reviewerSignature` | `Bitmap?` | 확인자 서명 이미지 |
| `signYear/Month/Day` | String | 계약 서명일 |

**ViewModel:** `UseContractViewModel`
**필수값:** `deceasedName`, `address`

---

### Step 3 — 장례용품 계약서 (`CasketShroudState`)

| 필드 | 타입 | 설명 |
|------|------|------|
| `leftItems` | `List<FuneralSupplyItemState>` | 좌측 용품 (관, 수의, 장례 소품 34종) |
| `rightItems` | `List<FuneralSupplyItemState>` | 우측 용품 (유골함, 영정사진, 차량, 비아젬 20종) |
| `consultantSignature` | `Bitmap?` | 상담자 서명 |
| `reviewerSignature` | `Bitmap?` | 확인자 서명 |

**`FuneralSupplyItemState` 구조:**

| 필드 | 설명 |
|------|------|
| `name` | 품명 |
| `unit` | 단위 |
| `price` | 단가 (표시용, 편집 불가) |
| `quantity` | 수량 (사용자 입력) |
| `remarks` | 비고 (사용자 입력) |
| `isHeader` | 헤더 행 여부 |
| `isReadOnly` | 수량 입력 불가 여부 |

**ViewModel:** `CasketShroudViewModel`

---

### Step 4 — 장례음식 계약서 (`FoodCateringState`)

| 필드 | 타입 | 설명 |
|------|------|------|
| `foodItems` | `List<FoodItemState>` | 음식 항목 목록 (밥, 국, 무침, 반찬류 16종) |
| `ceremonyServices` | `List<CeremonyServiceState>` | 제례 서비스 (초배상, 성복제, 발인제, 상식, 노제, 위령제) |
| `consultantSignature` | `Bitmap?` | 상담자 서명 |
| `reviewerSignature` | `Bitmap?` | 확인자 서명 |
| `totalAmount` | String | 전체 합계 금액 |

**`CeremonyServiceState` 구조:**

| 필드 | 설명 |
|------|------|
| `category` | 종류 (예: 초배상, 성복제) |
| `priceUpper` | 上 등급 가격 |
| `priceMiddle` | 中 등급 가격 |
| `flatPrice` | 단일 가격 (상식, 위령제) |
| `selectedLevel` | 선택된 등급: "上", "中", "" |
| `totalAmount` | 최종 금액 |

**ViewModel:** `FoodCateringViewModel`

---

## 3. 마스터 상태 (`FuneralContractUiState`)

```kotlin
data class FuneralContractUiState(
    val contractId: String,       // UUID (자동 생성)
    val currentStep: Int,         // 현재 단계 (0~3)
    val totalSteps: Int = 4,      // 전체 단계 수
    val step1: ReceptionBasicState,
    val step2: UseContractState,
    val step3: CasketShroudState,
    val step4: FoodCateringState,
    val saveStatus: ContractSaveStatus  // 저장 진행 상태
)
```

---

## 4. 마스터 ViewModel (`FuneralContractViewModel`)

### 역할
- 전체 계약 상태(Step 1~4) 취합
- 단계 네비게이션 (goNextStep / goPrevStep / goToStep)
- 최종 저장 실행: `saveContract(context)`

### 저장 플로우

```
saveContract(context) 호출
  ├─ saveStatus = Saving
  ├─ PdfGenerator.generate(context, state)
  │     └─ 성공 → saveStatus = PdfSaved(path)
  │     └─ 실패 → saveStatus = Error("PDF 생성 실패")
  └─ repository.saveContract(request)
        └─ 성공 → saveStatus = Success(pdfPath, contractId)
        └─ 실패 → saveStatus = Error("DB 저장 실패 (PDF는 저장됨)")
```

### 저장 상태 (`ContractSaveStatus`)

```kotlin
sealed class ContractSaveStatus {
    object Idle       // 초기 상태
    object Saving     // 저장 진행 중 (로딩 표시)
    PdfSaved(path)    // PDF만 완료
    DbSaved(id)       // DB만 완료
    Success(pdf, id)  // 전체 성공 → 완료 알림
    Error(message)    // 오류 → 에러 다이얼로그
}
```

---

## 5. ViewModel 간 데이터 흐름

```
Step ViewModel ──────────────────── FuneralContractViewModel
                                           │
ReceptionBasicViewModel  ──syncStep1()──▶  step1: ReceptionBasicState
UseContractViewModel     ──syncStep2()──▶  step2: UseContractState
CasketShroudViewModel    ──syncStep3()──▶  step3: CasketShroudState
FoodCateringViewModel    ──syncStep4()──▶  step4: FoodCateringState
                                           │
                                     saveContract()
                                      ├── PDF 생성
                                      └── 서버 저장
```

각 단계 화면에서 `FuneralContractViewModel.syncStepN(state)` 를 호출하여 마스터 상태에 동기화합니다. 동기화 시점은 화면 이탈 직전(다음 단계 이동 버튼 클릭 시)이 권장됩니다.

---

## 6. Compose UI 연동 예시

```kotlin
@Composable
fun FuneralContractScreen(
    masterVm: FuneralContractViewModel = viewModel(),
    step1Vm: ReceptionBasicViewModel = viewModel(),
    step2Vm: UseContractViewModel = viewModel(),
    step3Vm: CasketShroudViewModel = viewModel(),
    step4Vm: FoodCateringViewModel = viewModel(),
) {
    val masterState by masterVm.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    UseContractStepContent(
        pagerState = pagerState,
        contractSteps = listOf(
            { ReceptionBasicStep(viewModel = step1Vm) },
            { DeceasedDetailStep(viewModel = step2Vm) },
            { CasketShroudStep(viewModel = step3Vm) },
            { FoodCateringStep(viewModel = step4Vm) },
        ),
        onClose = { /* 뒤로가기 */ },
        onFinish = {
            // 마지막 단계에서 최종 저장
            masterVm.syncStep1(step1Vm.uiState.value)
            masterVm.syncStep2(step2Vm.uiState.value)
            masterVm.syncStep3(step3Vm.uiState.value)
            masterVm.syncStep4(step4Vm.uiState.value)
            masterVm.saveContract(context)
        }
    )

    // 저장 결과 처리
    LaunchedEffect(masterState.saveStatus) {
        when (val status = masterState.saveStatus) {
            is ContractSaveStatus.Success -> {
                // 성공 알림, 화면 이동
            }
            is ContractSaveStatus.Error -> {
                // 에러 다이얼로그 표시
            }
            else -> {}
        }
    }
}
```

---

## 7. 파일 구조

```
app/src/main/java/com/example/boram_funeral/
│
├── data/funeral/
│   ├── model/
│   │   ├── FuneralContractModels.kt      ← 상태 데이터 클래스 (Step1~4 State)
│   │   └── FuneralContractRequest.kt     ← API 요청/응답 모델 + 변환 확장
│   ├── remote/
│   │   └── FuneralContractApiService.kt  ← Retrofit 인터페이스
│   └── FuneralContractRepository.kt      ← 서버 통신 리포지토리
│
├── ui/screens/funeral/logic/
│   ├── ReceptionBasicViewModel.kt        ← Step 1 ViewModel
│   ├── UseContractViewModel.kt           ← Step 2 ViewModel
│   ├── CasketShroudViewModel.kt          ← Step 3 ViewModel
│   ├── FoodCateringViewModel.kt          ← Step 4 ViewModel
│   └── FuneralContractViewModel.kt       ← 마스터 ViewModel
│
└── util/
    └── PdfGenerator.kt                   ← PDF 생성 유틸리티
```

---

## 8. PDF 저장 위치

- **저장 경로:** `getExternalFilesDir("Documents")` 또는 `filesDir`
- **파일명 형식:** `보람_장례계약서_{고인명}_{yyyyMMdd_HHmmss}.pdf`
- **페이지 구성:**
  - Page 1: 접수 기본 정보
  - Page 2: 장례식장 이용계약서 (고인정보 + 서명)
  - Page 3: 장례용품 계약서 (선택 용품 목록 + 서명)
  - Page 4: 장례음식 계약서 (주문 내역 + 서명)

---

## 9. 서버 API 명세

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| POST | `/api/funeral/contract` | 계약서 전체 저장 |
| GET | `/api/funeral/contract/{contractId}` | 계약서 조회 |

**요청 바디 (`FuneralContractRequest`):**
```json
{
  "contract_id": "uuid",
  "step1": { "death_reason": "...", "event_type": "...", ... },
  "step2": { "contract_year": "2026", "deceased_name": "...", ... },
  "step3": { "left_items": [...], "right_items": [...] },
  "step4": { "food_items": [...], "ceremony_services": [...], "total_amount": "..." }
}
```

---

## 10. 추가 개선 사항 (TODO)

- [ ] 각 Step 화면의 로컬 `remember` 상태를 ViewModel의 StateFlow로 교체
- [ ] 서명 이미지를 Base64 인코딩하여 서버 전송
- [ ] Room DB를 통한 오프라인 임시 저장 지원
- [ ] PDF에 로고 이미지 및 테이블 스타일 적용
- [ ] 단계별 유효성 검사 후 다음 단계 이동 제한