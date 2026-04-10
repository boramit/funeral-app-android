package com.example.boram_funeral.ui.components.common.Button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 버튼 프리셋 사이즈.
 *
 * 개별 파라미터([width], [height], [fontSize])로 덮어쓸 수 있다.
 */
enum class ButtonSize(val width: Dp, val height: Dp, val fontSize: TextUnit) {
    Small(width = 80.dp,  height = 36.dp, fontSize = 12.sp),
    Medium(width = 100.dp, height = 48.dp, fontSize = 14.sp),
    Large(width = 120.dp, height = 64.dp, fontSize = 16.sp),
}

/**
 * 공통 버튼 컴포넌트.
 *
 * [size]로 너비·높이·폰트를 한 번에 지정하고,
 * 필요한 항목만 개별 파라미터로 덮어쓸 수 있다.
 *
 * @param text 버튼 라벨
 * @param onClick 클릭 콜백
 * @param size 프리셋 사이즈 (기본값: Medium)
 * @param modifier 외부 Modifier
 * @param fullWidth true 이면 가용 너비를 모두 채운다
 * @param width 너비 직접 지정 — [Dp.Unspecified] 이면 size.width 사용
 * @param height 높이 직접 지정 — [Dp.Unspecified] 이면 size.height 사용
 * @param fontSize 폰트 크기 직접 지정 — null 이면 size.fontSize 사용
 * @param letterSpacing 자간
 * @param icon 좌측 아이콘 (선택)
 * @param iconSize 아이콘 크기
 * @param iconSpacing 아이콘과 텍스트 사이 간격
 * @param backgroundColor 버튼 배경색
 * @param contentColor 텍스트·아이콘 색상
 * @param shape 버튼 모양
 * @param horizontalPadding 좌우 내부 패딩
 * @param verticalPadding 상하 내부 패딩
 * @param enabled 활성화 여부
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    size: ButtonSize = ButtonSize.Medium,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    width: Dp = Dp.Unspecified,
    height: Dp = Dp.Unspecified,
    fontSize: TextUnit? = null,
    letterSpacing: TextUnit = (-0.5).sp,
    icon: ImageVector? = null,
    iconSize: Dp = 20.dp,
    iconSpacing: Dp = 4.dp,
    backgroundColor: Color = Color(0xFF263446),
    contentColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(8.dp),
    horizontalPadding: Dp = 8.dp,
    verticalPadding: Dp = 0.dp,
    enabled: Boolean = true,
) {
    val resolvedWidth  = if (width  != Dp.Unspecified) width  else size.width
    val resolvedHeight = if (height != Dp.Unspecified) height else size.height

    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .width(resolvedWidth)
            .height(resolvedHeight),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Gray,
        ),
        shape = shape,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                )
                Spacer(Modifier.width(iconSpacing))
            }
            Text(
                text = text,
                fontSize = fontSize ?: size.fontSize,
                letterSpacing = letterSpacing,
            )
        }
    }
}
