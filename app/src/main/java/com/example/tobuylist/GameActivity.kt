package com.example.tobuylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val actionBar = supportActionBar
        actionBar!!.hide()

        gameView = GameView(this)
        setContentView(gameView)
    }
}
