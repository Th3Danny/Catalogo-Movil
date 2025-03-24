package com.example.catalogoapp.src.viewCatalogo.domain

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextoAVoz(ctx : Context) : TextToSpeech.OnInitListener{
    private val LANGUAGE_CODE = "es"
    private  val COUNTRY_CODE = "MX"
    private  val SPEECH_RATE = 0.7f
    private  val PITCH = 0.9f

    private var textToSpeech : TextToSpeech? = TextToSpeech(ctx, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val languageConfig  = Locale(LANGUAGE_CODE, COUNTRY_CODE)
            val result = textToSpeech?.setLanguage(languageConfig)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TEXTO_VOZ_TAG", "Language not supported")
            } else {
                textToSpeech?.setSpeechRate(SPEECH_RATE)
                textToSpeech?.setPitch(PITCH)
            }
        } else {
            Log.e("TEXTO_VOZ_TAG", "Error al inicializar TextToSpeech")
        }
    }

    fun speak(text: String) {
        Log.e("TEXTO_VOZ_TAG", text)
        Log.e("TEXTO_VOZ_TAG", textToSpeech?.isSpeaking.toString())
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        Log.e("TEXTO_VOZ_TAG", textToSpeech?.isSpeaking.toString())
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun release() {
        textToSpeech?.shutdown()
    }
}