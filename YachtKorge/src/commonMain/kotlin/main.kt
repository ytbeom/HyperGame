import com.soywiz.klock.TimeSpan
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.hide
import com.soywiz.korge.view.tween.show
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.file.std.*

const val WIDTH = 1600
const val HEIGHT = 900
const val WHITE = "#ffffff"
const val MAIN_COLOR = "#00583a"
const val SUB_COLOR = "#008256"

suspend fun main() = Korge(width = WIDTH, height = HEIGHT, title = "YACHT", bgcolor = Colors[MAIN_COLOR]) {
	val font = TtfFont(resourcesVfs["DOS_font.ttf"].readAll())

	// Set name of the players
	val player1Name = "P-1"
	val player2Name = "P-2"

	// Draw table background
	val tableWidth = width * 0.35
	val tableBackground = SolidRect(tableWidth, height, Colors[WHITE])
	tableBackground.x = 0.0
	tableBackground.y = 0.0
	addChild(tableBackground)

	// Draw cell
	val titleCellRow = 3
	val scoreCellRow = 12
	val cellMargin = 2.0
	val scoreCellWidth = (tableWidth - 4 * cellMargin) / 4
	val scoreCellWidthList = listOf(scoreCellWidth * 2, scoreCellWidth, scoreCellWidth)
	val scoreCellHeight = (height - cellMargin * (titleCellRow + scoreCellRow + 1)) / (scoreCellRow + titleCellRow * 1.2)
	val scoreCellHeightList = listOf(
			scoreCellHeight * 1.2, // Categories and player name
			scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight,
			scoreCellHeight * 1.2, // Subtotal
			scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight,
			scoreCellHeight * 1.2, // Total
	)
	for (i in 0 until 3) {
		for (j in 0 until titleCellRow + scoreCellRow) {
			val cell = SolidRect(scoreCellWidthList[i], scoreCellHeightList[j], cellColor(i, j))
			val x = cellMargin * (i + 1) + scoreCellWidthList.subList(0, i).sum()
			val y = cellMargin * (j + 1) + scoreCellHeightList.subList(0, j).sum()
			cell.x = x
			cell.y = y
			addChild(cell)
			cellText(i, j, player1Name, player2Name)?.let {
//				val text = Text(textSize = 40.0, text = it.first, color = it.second,
//						alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
				val text = Text(font = font, textSize = 35.0, text = it.first, color = it.second,
						alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
				text.x = x + scoreCellWidthList[i] / 2
				text.y = y + scoreCellHeightList[j] / 2
				addChild(text)
			}

		}
	}

	// Draw title, player name, turn, trial, dices
	val titleWidth = WIDTH - tableWidth
	val titleHeight = HEIGHT / 6.0
	val title = Text(font = font, textSize = 80.0, text = "HyperGame -YACHT-", color = Colors[WHITE],
			alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
	title.x = tableWidth + titleWidth / 2
	title.y = titleHeight / 2
	addChild(title)

	val stateBoardWidth = WIDTH * 0.35
	val playerNameY = HEIGHT * 0.3
	val player1Text = Text(font = font, textSize = 60.0, text = player1Name, color = Colors[WHITE],
			alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
	player1Text.x = tableWidth + stateBoardWidth / 4
	player1Text.y = playerNameY
	addChild(player1Text)
	val player2Text = Text(font = font, textSize = 60.0, text = player2Name, color = Colors[WHITE],
			alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
	player2Text.x = tableWidth + stateBoardWidth - stateBoardWidth / 4
	player2Text.y = playerNameY
	addChild(player2Text)
	val vsText = Text(font = font, textSize = 40.0, text = "vs.", color = Colors[WHITE],
			alignment = TextAlignment.MIDDLE_CENTER, autoScaling = true)
	vsText.x = tableWidth + stateBoardWidth / 2
	vsText.y = playerNameY
	addChild(vsText)
	vsText.text = "vs."

	val diceBoardWidth = WIDTH - tableWidth - stateBoardWidth
	val dice1Image = resourcesVfs["1.png"].readBitmap()
	val dice1 = image(dice1Image).size(100, 100).xy(1200, 200)

//
//	val player1Score = ObservableProperty(Score())
//	val player2Score = ObservableProperty(Score())

}

fun cellColor(row: Int, col: Int): RGBA {
	return when (Pair(row, col)) {
		Pair(0, 0), Pair(0, 7), Pair(0, 14) -> Colors[WHITE]
		Pair(1, 0), Pair(2, 0), Pair(1, 7), Pair(2, 7), Pair(1, 14), Pair(2, 14) -> Colors[SUB_COLOR]
		else -> Colors[MAIN_COLOR]
	}
}

fun cellText(row: Int, col: Int, player1Name: String, player2Name: String): Pair<String, RGBA>? {
	return when (Pair(row, col)) {
		Pair(0, 0) -> Pair("Categories", Colors[MAIN_COLOR])
		Pair(1, 0) -> Pair(player1Name, Colors[WHITE])
		Pair(2, 0) -> Pair(player2Name, Colors[WHITE])
		Pair(0, 1) -> Pair("Aces", Colors[WHITE])
		Pair(0, 2) -> Pair("Deuces", Colors[WHITE])
		Pair(0, 3) -> Pair("Threes", Colors[WHITE])
		Pair(0, 4) -> Pair("Fours", Colors[WHITE])
		Pair(0, 5) -> Pair("Fives", Colors[WHITE])
		Pair(0, 6) -> Pair("Sixes", Colors[WHITE])
		Pair(0, 7) -> Pair("SubTotal", Colors[MAIN_COLOR])
		Pair(0, 8) -> Pair("Choice", Colors[WHITE])
		Pair(0, 9) -> Pair("4 of a Kind", Colors[WHITE])
		Pair(0, 10) -> Pair("Full House", Colors[WHITE])
		Pair(0, 11) -> Pair("S. Straight", Colors[WHITE])
		Pair(0, 12) -> Pair("L. Straight", Colors[WHITE])
		Pair(0, 13) -> Pair("Yacht", Colors[WHITE])
		Pair(0, 14) -> Pair("Total", Colors[MAIN_COLOR])
		else -> null
	}
}

//data class Score (
//	val aces: String = "-",
//	val deuces: String = "-",
//	val threes: String = "-",
//	val fours: String = "-",
//	val fives: String = "-",
//	val sixes: String = "-",
//	val subTotal: String = "-",
//	val choice: String = "-",
//	val fourOfAKind: String = "-",
//	val fullHouse: String = "-",
//	val smallStraight: String = "-",
//	val largeStraight: String = "-",
//	val yacht: String = "-",
//	val total: String = "-"
//)

