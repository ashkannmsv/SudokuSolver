package com.example.sudokusolver

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SudokuGridView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var board: Array<IntArray>? = null
    private val cellPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2f
    }
    private val boldLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4f
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    private val textPaintHighlight = Paint().apply {
        color = Color.BLUE
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    }

    fun setBoard(board: Array<IntArray>, highlightRow: Int = -1, highlightCol: Int = -1) {
        this.board = board
        this.highlightRow = highlightRow
        this.highlightCol = highlightCol
        invalidate()
    }

    private var highlightRow = -1
    private var highlightCol = -1

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val board = board ?: return

        val width = width.toFloat()
        val height = height.toFloat()
        val cellSize = width / 9
        val cellHeightSize = height / 9

        // Draw background
        canvas.drawColor(Color.WHITE)

        // Draw cells and numbers
        for (r in 0..8) {
            for (c in 0..8) {
                val x = c * cellSize
                val y = r * cellHeightSize

                // Draw cell background
                canvas.drawRect(x, y, x + cellSize, y + cellHeightSize, cellPaint)

                // Draw number if exists
                if (board[r][c] != 0) {
                    val paint = if (highlightRow == r && highlightCol == c) textPaintHighlight else textPaint
                    canvas.drawText(
                        board[r][c].toString(),
                        x + cellSize / 2,
                        y + cellHeightSize / 2 + 10,
                        paint
                    )
                }
            }
        }

        // Draw grid lines
        for (i in 0..9) {
            val pos = i * cellSize
            val paint = if (i % 3 == 0) boldLinePaint else linePaint
            canvas.drawLine(pos, 0f, pos, height, paint)
            val posH = i * cellHeightSize
            canvas.drawLine(0f, posH, width, posH, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = minOf(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(size, size)
    }
}
