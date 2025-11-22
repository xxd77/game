package com.example.tetris

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tetris.ui.theme.TetrisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TetrisTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    TetrisApp()
                }
            }
        }
    }
}

@Composable
fun TetrisApp() {
    val context = LocalContext.current
    val game = remember { TetrisGame() }

    // 简单音效（把 drop.mp3 和 clear.mp3 放到 res/raw）
    val dropSound = remember { MediaPlayer.create(context, R.raw.drop) }
    val clearSound = remember { MediaPlayer.create(context, R.raw.clear) }

    LaunchedEffect(game.isGameOver) {
        if (game.isGameOver) {
            dropSound.release()
            clearSound.release()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 40.dp)
    ) {
        TetrisBoard(
            game = game,
            onLineCleared = { clearSound.start() },
            onDrop = { dropSound.start() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            ControlButton("←") { game.moveLeft() }
            ControlButton("→") { game.moveRight() }
            ControlButton("↷") { game.rotate() }
            ControlButton("↓") { game.softDrop() }
            ControlButton("💥") { game.hardDrop(); dropSound.start() }
        }

        Spacer(modifier = Modifier.height(20.dp))
        androidx.compose.material3.Text(
            text = "分数: ${game.score}",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun ControlButton(text: String, onClick: () -> Unit) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier.size(64.dp)
    ) {
        androidx.compose.material3.Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}
