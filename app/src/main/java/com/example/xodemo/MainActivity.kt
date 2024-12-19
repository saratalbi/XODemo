package com.example.xodemo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvGameTitle: TextView
    private lateinit var tvGameStatus: TextView
    private lateinit var gridGameBoard: GridLayout
    private lateinit var btnRestart: Button
    private lateinit var btnAI: Button

    private var board = Array(3) { arrayOfNulls<String>(3) }
    private var currentPlayer = "X"
    private var winner: String? = null
    private var isDraw = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        tvGameTitle = findViewById(R.id.tvGameTitle)
        tvGameStatus = findViewById(R.id.tvGameStatus)
        gridGameBoard = findViewById(R.id.gridGameBoard)
        btnRestart = findViewById(R.id.btnRestart)

        initializeGameBoard()
        updateGameStatus()

        btnRestart.setOnClickListener {
            resetGame()
        }

    }

    /**
     * Initialize the game board buttons and their click listeners.
     */
    private fun initializeGameBoard() {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cellId = resources.getIdentifier("btnCell${i * 3 + j + 1}", "id", packageName)
                val cellButton = findViewById<Button>(cellId)

                cellButton.setOnClickListener {
                    if (board[i][j] == null && winner == null) {
                        board[i][j] = currentPlayer
                        cellButton.text = currentPlayer

                        // Set text color for X and O
                        if (currentPlayer == "X") {
                            cellButton.setTextColor(ContextCompat.getColor(this, R.color.green))  // X = Blue
                        } else {
                            cellButton.setTextColor(ContextCompat.getColor(this, R.color.purple_200))   // O = Red
                        }

                        cellButton.isEnabled = false
                        checkGameStatus()
                        switchPlayer()
                        updateGameStatus()
                    }
                }
            }
        }
    }

    /**
     * Update the game status message.
     */
    private fun updateGameStatus() {
        when {
            winner != null -> {
                tvGameStatus.text = "Player $winner wins!"
                tvGameStatus.setTextColor(ContextCompat.getColor(this, R.color.blue))
            }
            isDraw -> {
                tvGameStatus.text = "It's a draw!"
                tvGameStatus.setTextColor(ContextCompat.getColor(this, R.color.blue))
            }
            else -> {
                tvGameStatus.text = "Player $currentPlayer's turn"
                tvGameStatus.setTextColor(ContextCompat.getColor(this, R.color.blue))
            }
        }
    }

    /**
     * Check for a win or draw condition.
     */
    private fun checkGameStatus() {
        // Check rows and columns
        for (i in 0 until 3) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                winner = currentPlayer
                return
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                winner = currentPlayer
                return
            }
        }

        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            winner = currentPlayer
            return
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            winner = currentPlayer
            return
        }

        // Check for draw
        isDraw = board.all { row -> row.all { cell -> cell != null } }
    }

    /**
     * Switch to the next player.
     */
    private fun switchPlayer() {
        if (winner == null && !isDraw) {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
        }
    }

    /**
     * Reset the game to its initial state.
     */
    private fun resetGame() {
        board = Array(3) { arrayOfNulls<String>(3) }
        currentPlayer = "X"
        winner = null
        isDraw = false
        updateGameStatus()

        // Reset buttons
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cellId = resources.getIdentifier("btnCell${i * 3 + j + 1}", "id", packageName)
                val cellButton = findViewById<Button>(cellId)
                cellButton.text = ""
                cellButton.setTextColor(ContextCompat.getColor(this, R.color.black)) // Default color
                cellButton.isEnabled = true
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(menu is MenuBuilder) (menu as MenuBuilder).setOptionalIconsVisible(true)
        val inflater: MenuInflater =menuInflater
        inflater.inflate(R.menu.menu_option,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_login->{
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_about_us->{
                val url = "https://playtictactoe.org/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
            R.id.menu_vs_ia->{
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                return true
            }
            else ->{
                return true
            }
        }
    }
}
