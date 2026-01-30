package com.example.sudokusolver

class SudokuSolver(private val board: Array<IntArray>) {
    private val candidates: Array<Array<MutableSet<Int>>> = Array(9) { r ->
        Array(9) { c ->
            if (board[r][c] != 0) mutableSetOf() else (1..9).toMutableSet()
        }
    }

    init {
        initializeCandidates()
    }

    private fun initializeCandidates() {
        for (r in 0..8) {
            for (c in 0..8) {
                if (board[r][c] != 0) {
                    val num = board[r][c]
                    removeFromPeers(r, c, num)
                }
            }
        }
    }

    private fun removeFromPeers(r: Int, c: Int, num: Int) {
        // Remove from row
        for (col in 0..8) {
            candidates[r][col].remove(num)
        }
        // Remove from column
        for (row in 0..8) {
            candidates[row][c].remove(num)
        }
        // Remove from box
        val boxR = 3 * (r / 3)
        val boxC = 3 * (c / 3)
        for (i in boxR until boxR + 3) {
            for (j in boxC until boxC + 3) {
                candidates[i][j].remove(num)
            }
        }
    }

    private fun setValue(r: Int, c: Int, num: Int) {
        board[r][c] = num
        candidates[r][c].clear()
        removeFromPeers(r, c, num)
    }

    private fun constraintPropagation(): Boolean {
        var changed = true
        while (changed) {
            changed = false

            // Naked Singles
            for (r in 0..8) {
                for (c in 0..8) {
                    if (board[r][c] == 0 && candidates[r][c].size == 1) {
                        val num = candidates[r][c].first()
                        setValue(r, c, num)
                        changed = true
                    } else if (board[r][c] == 0 && candidates[r][c].isEmpty()) {
                        return false
                    }
                }
            }

            // Hidden Singles in rows
            for (r in 0..8) {
                for (num in 1..9) {
                    val positions = (0..8).filter { c ->
                        board[r][c] == 0 && candidates[r][c].contains(num)
                    }
                    if (positions.isEmpty() && !board[r].contains(num)) return false
                    if (positions.size == 1) {
                        val c = positions[0]
                        if (candidates[r][c].size > 1) {
                            candidates[r][c] = mutableSetOf(num)
                            setValue(r, c, num)
                            changed = true
                        }
                    }
                }
            }
        }
        return true
    }

    private fun findMRV(): Pair<Int, Int>? {
        var minCandidates = 10
        var bestCell: Pair<Int, Int>? = null
        for (r in 0..8) {
            for (c in 0..8) {
                if (board[r][c] == 0 && candidates[r][c].size < minCandidates) {
                    minCandidates = candidates[r][c].size
                    bestCell = r to c
                }
            }
        }
        return bestCell
    }

    fun solve(): Boolean {
        if (!constraintPropagation()) return false

        val cell = findMRV() ?: return true

        val (r, c) = cell
        if (candidates[r][c].isEmpty()) return false

        for (num in candidates[r][c].sorted()) {
            val boardBackup = board.map { it.copyOf() }.toTypedArray()
            val candidatesBackup = candidates.map { row ->
                row.map { it.toMutableSet() }.toTypedArray()
            }.toTypedArray()

            setValue(r, c, num)
            if (solve()) return true

            for (i in 0..8) {
                for (j in 0..8) {
                    board[i][j] = boardBackup[i][j]
                    candidates[i][j] = candidatesBackup[i][j]
                }
            }
        }
        return false
    }

    fun getBoard(): Array<IntArray> = board
}
