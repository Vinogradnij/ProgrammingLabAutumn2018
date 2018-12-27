import Logic.Solver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DeskTest {
    private val solver = Solver(5)

    init {
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                print("Пара - $x,$y, значение = ")
                println(solver.desk.matrix[x][y])
            }
        }
    }

    @Test
    fun findKey() {
        val randomNum = (Math.random() * 4.0).toInt()
        val box = solver.desk.table[randomNum to randomNum]
        assertEquals(randomNum to randomNum ,solver.desk.findKey(box!!))
    }

    @Test
    fun changeCell() {
        val cellFirst = solver.desk.table[0 to 0]
        val cellSecond = solver.desk.table[0 to 1]
        solver.desk.changeCell(0 to 0, 0 to 1)
        assertEquals(cellFirst, solver.desk.table[0 to 1])
        assertEquals(cellSecond, solver.desk.table[0 to 0])
    }

    @Test
    fun equals() {
        val randomNum = (Math.random() * 4.0).toInt()
        assertEquals(solver.desk.matrix[randomNum][randomNum], solver.desk.table[randomNum to randomNum])
    }

    @Test
    fun solveIt() {
        solver.solveIt()
        assertEquals(solver.sorted, solver.desk.table)
    }
}