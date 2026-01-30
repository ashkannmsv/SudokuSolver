package com.example.sudokusolver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2
    private val CAMERA_PERMISSION_CODE = 100

    private lateinit var puzzleGridView: SudokuGridView
    private lateinit var solutionGridView: SudokuGridView
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    private val ocrProcessor = OCRProcessor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        puzzleGridView = SudokuGridView(this)
        solutionGridView = SudokuGridView(this)
        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)

        findViewById<FrameLayout>(R.id.puzzleContainer).addView(puzzleGridView)
        findViewById<FrameLayout>(R.id.solutionContainer).addView(solutionGridView)

        findViewById<Button>(R.id.btnCapture).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                openCamera()
            }
        }

        findViewById<Button>(R.id.btnGallery).setOnClickListener {
            openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val bitmap = when (requestCode) {
                REQUEST_CAMERA -> data?.getParcelableExtra<Bitmap>("data")
                REQUEST_GALLERY -> {
                    val uri = data?.data ?: return
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                else -> null
            } ?: return

            processSudoku(bitmap)
        }
    }

    private fun processSudoku(bitmap: Bitmap) {
        progressBar.visibility = ProgressBar.VISIBLE
        statusText.text = "Extracting puzzle..."

        lifecycleScope.launch {
            try {
                val board = ocrProcessor.extractNumbers(bitmap)
                statusText.text = "Solving puzzle..."
                puzzleGridView.setBoard(board)

                val solver = SudokuSolver(board.map { it.copyOf() }.toTypedArray())
                val solved = solver.solve()

                if (solved) {
                    solutionGridView.setBoard(solver.getBoard())
                    statusText.text = "✓ Puzzle solved successfully!"
                } else {
                    statusText.text = "❌ Unable to solve this puzzle"
                }
            } catch (e: Exception) {
                statusText.text = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }
}
