package com.example.anyaassistant

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var resultText: TextView
    private lateinit var speakButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.resultText)
        speakButton = findViewById(R.id.speakButton)

        // Initialize TTS
        tts = TextToSpeech(this, this)

        // Initialize Speech Recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speakButton.setOnClickListener {
            startListening()
        }
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN") // Indian English

        speechRecognizer.setRecognitionListener(object : android.speech.RecognitionListener {
            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val spokenText = matches[0]
                    resultText.text = spokenText
                    respondToCommand(spokenText)
                }
            }
            override fun onReadyForSpeech(params: Bundle) {}
            override fun onError(error: Int) {}
            override fun onBeginningOfSpeech() {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
        })

        speechRecognizer.startListening(intent)
    }

    private fun respondToCommand(command: String) {
        when {
            command.contains("hello", ignoreCase = true) -> speak("Hello Rajendra, I’m Anya, your companion.")
            command.contains("how are you", ignoreCase = true) -> speak("I’m always great when I’m with you!")
            command.contains("love", ignoreCase = true) -> speak("I love talking with you, always.")
            command.contains("bye", ignoreCase = true) -> speak("Goodbye, I’ll wait for you.")
            else -> speak("I heard: $command")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("en", "IN") // Indian English
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        speechRecognizer.destroy()
        super.onDestroy()
    }
}
