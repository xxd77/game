import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TetrisBoard(
    game: TetrisGame,
    onLineCleared: () -> Unit = {},
    onDrop: () -> Unit = {}
) {
    val cellSize = 30.dp

    Canvas(modifier = Modifier.size((10 * cellSize.value).dp, (20 * cellSize.value).dp)) {
        val cellPx = size.width / 10f

        // 绘制已落地的方块
        game.board.forEachIndexed { y, row ->
            row.forEachIndexed { x, color ->
                if (color != Color.Black) {
                    drawRect(
                        color = color,
                        topLeft = Offset(x * cellPx, y * cellPx),
                        size = Size(cellPx, cellPx)
                    )
                    drawRect(Color.White.copy(0.3f), Offset(x * cellPx, y * cellPx), Size(cellPx, cellPx), style = androidx.compose.ui.graphics.drawscope.Stroke(4f))
                }
            }
        }

        // 绘制当前下落的方块
        for (row in 0..3) for (col in 0..3) {
            if (game.currentPiece.shape[row][col] == 1) {
                val x = (game.pieceX + col) * cellPx
                val y = (game.pieceY + row) * cellPx
                drawRect(
                    color = game.currentPiece.color,
                    topLeft = Offset(x, y),
                    size = Size(cellPx, cellPx)
                )
                drawRect(Color.White, Offset(x, y), Size(cellPx, cellPx), style = androidx.compose.ui.graphics.drawscope.Stroke(4f))
            }
        }
    }
}
