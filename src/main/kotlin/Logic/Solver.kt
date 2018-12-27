package Logic

class Solver(dimensionOfTheField: Int) {
    private val dimension = dimensionOfTheField
    val desk: Desk
    private var position: Pair<Int, Int> //текущая позиция
    private var soughtPosition: Pair<Int, Int> //искомая позиция
    val sorted: MutableMap<Pair<Int, Int>, Int> //список уже отсортированных значений

    init {
        desk = Desk(dimension)
        position = desk.findKey(1)
        soughtPosition = 0 to (dimension - 1) / 2
        sorted = mutableMapOf()
    }

    private fun findNewCell(position: Pair<Int, Int>): Pair<Int, Int> {
        val key = position
        val result: Pair<Int, Int>
        when {
            key.first - 1 in 0 until dimension && key.second + 1 !in 0 until dimension -> { //Выход за правую границу
                result = if (!sorted.containsKey(key.first - 1 to 0))
                    key.first - 1 to 0
                else key.first + 1 to key.second
            }

            key.first - 1 !in 0 until dimension && key.second + 1 in 0 until dimension -> { //Выход за верхнюю границу
                result = if (!sorted.containsKey(dimension - 1 to key.second + 1))
                    dimension - 1 to key.second + 1
                else key.first + 1 to key.second
            }

            key.first - 1 !in 0 until dimension && key.second + 1 !in 0 until dimension -> { //Выход за обе границы
                result = if (!sorted.containsKey(dimension - 1 to 0))
                    dimension - 1 to 0
                else key.first + 1 to key.second
            }

            else -> { //Есть правая верхняя клетка
                result = if (!sorted.containsKey(key.first - 1 to key.second + 1))
                    key.first - 1 to key.second + 1
                else key.first + 1 to key.second
            }
        }
        return result
    }

    private fun pathToCell(position: Pair<Int, Int>, soughtPosition: Pair<Int, Int>) {
        //если мы проходоим через отсортированную клетку, то начинаем записывать путь,
        //начиная от неё, до конечной клетки, чтобы позже вернуть её на место
        var added = false
        val nums = mutableListOf<Pair<Int, Int>>()
        var newPosition = soughtPosition
        var actualPosition = position
        var previousPosition: Pair<Int, Int>

        while (actualPosition != soughtPosition) {
            val x1 = actualPosition.first
            val x2 = newPosition.first
            val y1 = actualPosition.second
            val y2 = newPosition.second
            previousPosition = actualPosition

            when {
                x2 - x1 == 0 && y2 - y1 > 0
                        || x2 - x1 < 0 && y2 - y1 > 0
                        || x2 - x1 > 0 && y2 - y1 > 0 -> { //искомая клетка правее и выше, либо правее и ниже
                    if (sorted.containsKey(x1 to y1 + 1) && sorted[x1 to y1 + 1] == desk.table[x1 to y1 + 1] )
                        added = true
                    desk.changeCell(x1 to y1, x1 to y1 + 1)
                    actualPosition = x1 to y1 + 1
                }

                x2 - x1 == 0 && y2 - y1 < 0
                        || x2 - x1 < 0 && y2 - y1 < 0
                        || x2 - x1 > 0 && y2 - y1 < 0 -> { //искомая клетка выше и левее, либо ниже и левее
                    if (sorted.containsKey(x1 to y1 - 1) && sorted[x1 to y1 - 1] == desk.table[x1 to y1 - 1] )
                        added = true
                    desk.changeCell(x1 to y1, x1 to y1 - 1)
                    actualPosition = x1 to y1 - 1
                }

                x2 - x1 > 0 && y2 - y1 == 0 -> { //искомая клетка ниже
                    if (sorted.containsKey(x1 + 1 to y1) && sorted[x1 + 1 to y1] == desk.table[x1 + 1 to y1] )
                        added = true
                    desk.changeCell(x1 to y1, x1 + 1 to y1)
                    actualPosition = x1 + 1 to y1
                }

                x2 - x1 < 0 && y2 - y1 == 0 -> { //искомая клетка выше
                    if (sorted.containsKey(x1 - 1 to y1) && sorted[x1 - 1 to y1] == desk.table[x1 - 1 to y1] )
                        added = true
                    desk.changeCell(x1 to y1, x1 - 1 to y1)
                    actualPosition = x1 - 1 to y1
                }
            }
            if (added) nums.add(previousPosition)
        }
        //После того, как дошли до искомой клетки, проверяем, внесена ли она в список отсортированных
        //и является ли эта позиция искомой
        if (!sorted.containsKey(actualPosition) && actualPosition == this.soughtPosition)
            sorted[actualPosition] = desk.table[actualPosition]!!

        //Если в ходе перемещения были затронуты отсортированные клетки, тогда проходим по nums в обратном порядке
        if (added) {
            nums.reverse()
            while (!nums.isEmpty()) {
                actualPosition = nums.first()
                newPosition = if (nums.size > 1) nums[1]
                else nums[0]
                pathToCell(actualPosition, newPosition)
                nums.remove(nums.first())
            }
        }
    }

    fun solveIt() {
        //Для каждого из чисел от 1 до dimension^2 мы проводим расстановку. Для числа 1 уже имеются позиция и искомая
        for (i in 1..Math.pow(dimension.toDouble(), 2.0).toInt()) {
            pathToCell(position, soughtPosition)
            //для всех последующих искомых используется функция findNewCell(условия работы будут в курсовой)
            soughtPosition = findNewCell(soughtPosition)
            //актуализируем нашу позицию путем функции findKey
            position = if (i + 1 <= Math.pow(dimension.toDouble(), 2.0).toInt()) desk.findKey(i + 1)
            else desk.findKey(Math.pow(dimension.toDouble(), 2.0).toInt())
        }
    }
}