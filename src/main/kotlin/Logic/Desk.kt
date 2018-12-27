package Logic

class Desk (dimensionOfTheField: Int) {
    val table: MutableMap<Pair<Int,Int>, Int>
    val matrix: Array<IntArray>
    private val dimension = dimensionOfTheField

    init {
        val nums = mutableListOf<Int>()
        var i = 1
        while (i <= Math.pow(dimension.toDouble(), 2.0)) {
            nums.add(i)
            i++
        }
        table = mutableMapOf()
        matrix = Array(dimension) {IntArray(dimension)}
        var rangeOfRandom = Math.pow(dimension.toDouble(), 2.0).toInt()
        var randomNum: Int
        for (x in 0 until dimension) {
            for (y in 0 until dimension) {
                randomNum = (Math.random() * rangeOfRandom).toInt()
                matrix[x][y] = nums[randomNum]
                table[x to y] = nums[randomNum]
                nums.removeAt(randomNum)
                rangeOfRandom--
            }
        }
    }

    fun changeCell(keyFirst: Pair<Int,Int>, keySecond: Pair<Int,Int>) {
        if ((Math.abs(keyFirst.first - keySecond.first) == 1 && Math.abs(keyFirst.second - keySecond.second) == 0)
        || (Math.abs(keyFirst.first - keySecond.first) == 0 && Math.abs(keyFirst.second - keySecond.second) == 1)) {
            val box = matrix[keyFirst.first][keyFirst.second]
            matrix[keyFirst.first][keyFirst.second] = matrix[keySecond.first][keySecond.second]
            matrix[keySecond.first][keySecond.second] = box
            table[keyFirst] = matrix[keyFirst.first][keyFirst.second]
            table[keySecond] = matrix[keySecond.first][keySecond.second]
        }
    }

    fun findKey(value: Int): Pair<Int, Int> {
        val result = -1 to -1
        table.forEach {
            if (it.value == value) return it.key
        }
        return result
    }
}