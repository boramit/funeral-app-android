package com.example.boram_funeral.util

import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null

    // 녹음 시작
    fun start(outputFile: File) {
        // 안드로이드 버전에 따른 MediaRecorder 초기화
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)     // 마이크 사용
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // 파일 포맷 (mp4/m4a)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)   // 코덱
            setOutputFile(FileOutputStream(outputFile).fd)    // 파일 저장 위치

            prepare() // 준비
            start()   // 녹음 시작
        }
    }

    // 녹음 중지
    fun stop() {
        try {
            recorder?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recorder?.release()
            recorder = null
        }
    }

    // AudioRecorder 클래스 내부에 추가할 저장 함수 예시
    fun saveFileToPublicStorage(context: Context, tempFile: File) {
        try {
            // 1. 파일 정보 설정
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "Boram_Funeral_${System.currentTimeMillis()}.mp3")
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
                // 안드로이드 10(Q) 이상을 위한 경로 설정
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // ✅ 핵심 수정: Music 폴더 안에 boram-funeral 폴더 경로 지정
                    // 마지막에 반드시 슬래시(/)가 포함되어야 폴더로 인식됩니다.
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/boram-funeral/")
                    put(MediaStore.Audio.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            // 오디오 컬렉션에 삽입
            val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

            // 2. 파일 복사 실행
            uri?.let { targetUri ->
                // !! 대신 safe call(?.)과 use를 사용하여 안전하게 닫기
                resolver.openOutputStream(targetUri)?.use { outputStream ->
                    if (tempFile.exists()) {
                        tempFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    } else {
                        android.util.Log.e("SaveError", "원본 임시 파일이 존재하지 않습니다.")
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0)
                    resolver.update(targetUri, contentValues, null, null)
                }
            } ?: android.util.Log.e("SaveError", "URI 생성에 실패했습니다.")

        } catch (e: Exception) {
            // ✅ 여기가 핵심: 에러가 나도 앱이 죽지 않고 로그를 남깁니다.
            e.printStackTrace()
            android.util.Log.e("SaveError", "파일 저장 중 오류 발생: ${e.message}")
        }
    }
}