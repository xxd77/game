data class Tetromino(
    val type: Char,
    val shape: List<List<Int>>, // 4x4 矩阵，1表示有色块
    val color: Color
) {
    fun rotated(): Tetromino = copy(
        shape = List(4) { col ->
            List(4) { row -> shape[3 - row][col] }
        }
    )

    companion object {
        val types = listOf('I', 'O', 'T', 'S', 'Z', 'J', 'L')
        private val shapes = mapOf(
            'I' to listOf(
                listOf(0,0,0,0),
                listOf(1,1,1,1),
                listOf(0,0,0,0),
                listOf(0,0,0,0)
            ),
            'O' to listOf(
                listOf(1,1),
                listOf(1,1)
            ).let { padTo4x4(it) },
            'T' to listOf(
                listOf(0,1,0),
                listOf(1,1,1)
            ).let { padTo4x4(it) },
            'S' to listOf(
                listOf(0,1,1),
                listOf(1,1,0)
            ).let { padTo4x4(it) },
            'Z' to listOf(
                listOf(1,1,0),
                listOf(0,1,1)
            ).let { padTo4x4(it) },
            'J' to listOf(
                listOf(1,0,0),
                listOf(1,1,1)
            ).let { padTo4x4(it) },
            'L' to listOf(
                listOf(0,0,1),
                listOf(1,1,1)
            ).let { padTo4x4(it) }
        )

        private fun padTo4x4(small: List<List<Int>>): List<List<Int>> {
            return List(4) { row ->
                List(4) { col ->
                    if (row < small.size && col < small[0].size) small[row][col] else 0
                }
            }
        }

        val colors = mapOf(
            'I' to Color.Cyan, 'O' to Color.Yellow, 'T' to Color.Magenta,
            'S' to Color.Green, 'Z' to Color.Red, 'J' to Color.Blue, 'L' to Color(0xFFFFA500)
        )

        fun random(): Tetromino {
            val type = types.random()
            return Tetromino(type, shapes[type]!!, colors[type]!!)
        }
    }
}
