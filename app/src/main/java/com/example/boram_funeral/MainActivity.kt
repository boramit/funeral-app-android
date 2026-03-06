package com.example.boram_funeral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.boram_funeral.ui.navigation.NavGraph
import com.example.boram_funeral.ui.theme.Boram_funeralTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Boram_funeralTheme {
                // Surface는 배경색을 설정하고 시스템 UI와 상호작용을 도와줍니다.
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // 테마에서 지정한 배경색 사용
                ) {
                    // 모든 화면 구성(사이드바 포함)은 NavGraph 내부에서 처리됩니다.
                    NavGraph()
                }
            }
        }
    }
}