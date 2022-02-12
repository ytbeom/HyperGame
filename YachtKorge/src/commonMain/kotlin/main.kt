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
	addChild(SolidRect(tableWidth, height, Colors[WHITE]).xy(0, 0))

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
			val x = cellMargin * (i + 1) + scoreCellWidthList.subList(0, i).sum()
			val y = cellMargin * (j + 1) + scoreCellHeightList.subList(0, j).sum()
			addChild(SolidRect(scoreCellWidthList[i], scoreCellHeightList[j], cellColor(i, j)).xy(x, y))
			cellText(i, j, player1Name, player2Name)?.let {
				addChild(Text(it.first, 35.0, it.second, font, TextAlignment.MIDDLE_CENTER)
					.xy(x + scoreCellWidthList[i] / 2, y + scoreCellHeightList[j] / 2)
				)
			}
		}
	}

	// Draw title, player name, turn, trial, dices
	val titleWidth = WIDTH - tableWidth
	val titleHeight = HEIGHT / 6.0
	addChild(Text("HyperGame -YACHT-", 80.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + titleWidth / 2, titleHeight / 2)
	)

	val stateBoardWidth = WIDTH * 0.35
	val playerNameY = HEIGHT * 0.3
	addChild(Text(player1Name, 60.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth / 4, playerNameY)
	)
	addChild(Text(player2Name, 60.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth - stateBoardWidth / 4, playerNameY)
	)
	addChild(Text("vs.", 40.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth / 2, playerNameY)
	)

	val diceBoardWidth = WIDTH - tableWidth - stateBoardWidth
	val dice1 = image(resourcesVfs["1.png"].readBitmap()).size(100, 100).xy(1300, 150)
	val dice2 = image(resourcesVfs["2.png"].readBitmap()).size(100, 100).xy(1300, 270)
	val dice3 = image(resourcesVfs["3.png"].readBitmap()).size(100, 100).xy(1300, 390)
	val dice4 = image(resourcesVfs["4.png"].readBitmap()).size(100, 100).xy(1300, 510)
	val dice5 = image(resourcesVfs["5.png"].readBitmap()).size(100, 100).xy(1300, 630)
	val dice6 = image(resourcesVfs["6.png"].readBitmap()).size(100, 100).xy(1300, 750)

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
