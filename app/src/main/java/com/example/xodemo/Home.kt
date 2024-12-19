package com.example.xodemo


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
class Home : AppCompatActivity() {

    private val GRID_SIZE = 3
    private lateinit var boardButtons: Array<Array<Button>>
    private lateinit var tvGameStatus: TextView
    private var isPlayerXTurn = true // Player X starts
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvGameStatus = findViewById(R.id.tvGameStatus)
        val gridGameBoard = findViewById<GridLayout>(R.id.gridGameBoard)

        // Initialize the board
        boardButtons = Array(GRID_SIZE) { Array(GRID_SIZE) { Button(this) } }
        initializeBoard(gridGameBoard)

        // Restart button listener
        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            restartGame()
        }
    }

    // Initialize the board dynamically
    private fun initializeBoard(gridGameBoard: GridLayout) {
        var index = 0
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                val button = Button(this)
                button.layoutParams = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200
                }
                button.text = ""
                button.textSize = 32f
                button.setOnClickListener {
                    if (!isGameOver && button.text == "") {
                        playerMove(button)
                        if (!isGameOver) aiMove()
                    }
                }
                boardButtons[i][j] = button
                gridGameBoard.addView(button)
                index++
            }
        }
    }

    // Player move logic (Player X)
    private fun playerMove(button: Button) {
        if (isPlayerXTurn) {
            button.text = "X"
            button.setTextColor(resources.getColor(R.color.green))
            tvGameStatus.text = "AI's Turn"
        } else {
            button.text = "O"
            tvGameStatus.text = "Player X's Turn"
        }
        button.isEnabled = false
        if (checkWinner()) {
            if (isPlayerXTurn) {
                tvGameStatus.text = "Player X Wins!"
            } else {
                tvGameStatus.text = "AI Wins!"
            }
            isGameOver = true
        } else {
            isPlayerXTurn = !isPlayerXTurn // Switch turn
        }
    }

    // AI Move Logic (AI plays as O)
    private fun aiMove() {
        if (isGameOver) return

        var moveMade = false
        val random = Random
        while (!moveMade) {
            val i = random.nextInt(GRID_SIZE)
            val j = random.nextInt(GRID_SIZE)

            val button = boardButtons[i][j]
            if (button.text == "") {
                button.text = "O"
                button.isEnabled = false
                button.setTextColor(resources.getColor(R.color.orange))
                moveMade = true
                if (checkWinner()) {
                    tvGameStatus.text = "AI Wins!"
                    isGameOver = true
                } else {
                    isPlayerXTurn = true
                    tvGameStatus.text = "Player X's Turn"
                }
            }
        }
    }

    // Check for a winner
    private fun checkWinner(): Boolean {
        // Check rows and columns
        for (i in 0 until GRID_SIZE) {
            if (boardButtons[i][0].text == boardButtons[i][1].text &&
                boardButtons[i][1].text == boardButtons[i][2].text &&
                boardButtons[i][0].text.isNotEmpty()) {
                return true
            }
            if (boardButtons[0][i].text == boardButtons[1][i].text &&
                boardButtons[1][i].text == boardButtons[2][i].text &&
                boardButtons[0][i].text.isNotEmpty()) {
                return true
            }
        }

        // Check diagonals
        if (boardButtons[0][0].text == boardButtons[1][1].text &&
            boardButtons[1][1].text == boardButtons[2][2].text &&
            boardButtons[0][0].text.isNotEmpty()) {
            return true
        }

        if (boardButtons[0][2].text == boardButtons[1][1].text &&
            boardButtons[1][1].text == boardButtons[2][0].text &&
            boardButtons[0][2].text.isNotEmpty()) {
            return true
        }

        // Check for a tie
        var isTie = true
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                if (boardButtons[i][j].text == "") {
                    isTie = false
                    break
                }
            }
        }

        if (isTie) {
            tvGameStatus.text = "It's a Tie!"
            isGameOver = true
        }

        return false
    }

    // Restart the game
    private fun restartGame() {
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                boardButtons[i][j].text = ""
                boardButtons[i][j].isEnabled = true
            }
        }
        isGameOver = false
        isPlayerXTurn = true
        tvGameStatus.text = "Player X's Turn"
    }
}