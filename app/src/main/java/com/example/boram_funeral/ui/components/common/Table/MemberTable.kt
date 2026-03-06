import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel.EventItem
import com.example.boram_funeral.ui.theme.boram_Br_Color

@Composable
fun getStatusTheme(status: MemberViewModel.EventStatus): Pair<Color, Color> {
    return when (status) {
        MemberViewModel.EventStatus.ONGOING -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
        MemberViewModel.EventStatus.COMPLETED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
    }
}

@Composable
fun MemberTable(
    events: List<EventItem>,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val noWidth = 40.dp
    val stWidth = 50.dp
    val atWidth = 100.dp
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            // 1. 테이블 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA))
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NO", modifier = Modifier.width(noWidth), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("장례식장", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("고인명", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("안치일자", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("입실일자", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("퇴실일자", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("발인일자", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("상태", modifier = Modifier.width(stWidth), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("비고", modifier = Modifier.width(atWidth), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
            }

            // 2. 데이터 리스트
            LazyColumn {
                items(items = events, key = { it.id }) { event ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = event.id, modifier = Modifier.width(noWidth), fontSize = 12.sp, textAlign = TextAlign.Center)
                        Text(text = event.funeralHomeName, modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center)
                        Text(text = event.deceasedName, modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center)
                        Text(text = event.burialDate, modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        Text(text = event.checkInDate, modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        Text(text = event.checkOutDate, modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        Text(text = event.departureDate, modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        Box(
                            modifier = Modifier.width(stWidth),
                            contentAlignment = Alignment.Center
                        ) {
                            // 1. Enum을 통째로 넘겨서 테마 색상 결정
                            val (bgColor, textColor) = getStatusTheme(event.status)

                            Surface(
                                color = bgColor,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    // 2. 표시할 글자는 Enum 내부의 label 사용
                                    text = event.status.label,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.width(atWidth), // 헤더의 너비와 일치시키세요 (60dp가 좁다면 80dp 추천)
                            contentAlignment = Alignment.Center // 버튼을 칸 중앙에 배치
                        ) {
                            Surface(
                                onClick = { onEventClick(event.id) },
                                color = boram_Br_Color,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "자세히",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F1F1))
                }
            }
        }
    }
}