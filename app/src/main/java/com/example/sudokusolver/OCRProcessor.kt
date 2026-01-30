package com.example.sudokusolver

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCancellableCoroutine

class OCRProcessor {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractNumbers(bitmap: Bitmap): Array<IntArray> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            
            recognizer.process(image)
                .addOnSuccessListener { text ->
                    val board = parseBoard(text.text, bitmap)
                    continuation.resume(board)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    continuation.resume(Array(9) { IntArray(9) })
                }
        }
    }

    private fun parseBoard(rawText: String, bitmap: Bitmap): Array<IntArray> {
        val board = Array(9) { IntArray(9) }
        
        // Extract all digits from text
        val digits = rawText.filter { it.isDigit() }.map { it.toString().toInt() }
        
        // Try to fit into 9x9 grid
        if (digits.size >= 81) {
            for (i in 0 until 81) {
                board[i / 9][i % 9] = digits[i]
            }
        } else {
            // Fallback: use OCR with position-based parsing
            val lines = rawText.split("\n")
            var boardRow = 0
            for (line in lines) {
                if (boardRow >= 9) break
                val lineDigits = line.filter { it.isDigit() }.map { it.toString().toInt() }
                for (i in 0 until minOf(lineDigits.size, 9)) {
                    board[boardRow][i] = lineDigits[i]
                }
                if (lineDigits.isNotEmpty()) boardRow++
            }
        }
        
        return board
    }
}
