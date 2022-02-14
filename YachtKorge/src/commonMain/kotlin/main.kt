import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.dynamic.dyn
import com.soywiz.korio.file.std.*
import com.soywiz.korio.serialization.json.Json

const val WIDTH = 1600
const val HEIGHT = 900
const val WHITE = "#ffffff"
const val MAIN_COLOR = "#00583a"
const val SUB_COLOR = "#008256"

val trialState = ObservableProperty(TrialState.INIT)
val index = ObservableProperty(0)
val diceImageSource = ObservableProperty(0)

suspend fun main() = Korge(width = WIDTH, height = HEIGHT, title = "YACHT", bgcolor = Colors[MAIN_COLOR]) {
	val font = TtfFont(resourcesVfs["DOS_font.ttf"].readAll())

	// Set name of the players
	val player1Name = "P-1"
	val player2Name = "P-2"

	// Draw table background
	val tableWidth = WIDTH * 0.35
	solidRect(tableWidth, HEIGHT.toDouble(), Colors[WHITE]).xy(0, 0)

	// Draw cell
	val titleCellRow = 3
	val scoreCellRow = 12
	val cellMargin = 2.0
	val scoreCellWidth = (tableWidth - 4 * cellMargin) / 4
	val scoreCellWidthList = listOf(scoreCellWidth * 2, scoreCellWidth, scoreCellWidth)
	val scoreCellHeight = (HEIGHT - cellMargin * (titleCellRow + scoreCellRow + 1)) / (scoreCellRow + titleCellRow * 1.2)
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
			solidRect(scoreCellWidthList[i], scoreCellHeightList[j], cellColor(i, j)).xy(x, y)
			cellText(i, j, player1Name, player2Name)?.let {
				text(it.first, 35.0, it.second, font, TextAlignment.MIDDLE_CENTER)
					.xy(x + scoreCellWidthList[i] / 2, y + scoreCellHeightList[j] / 2)
			}
		}
	}

	// Draw title, player name, turn, trial, dices
	val titleWidth = WIDTH - tableWidth
	val titleHeight = HEIGHT / 6.0
	text("HyperGame -YACHT-", 80.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + titleWidth / 2, titleHeight / 2)

	val stateBoardWidth = WIDTH * 0.35
	val playerNameY = HEIGHT * 0.3
	text(player1Name, 60.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth / 4, playerNameY)
	text(player2Name, 60.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth - stateBoardWidth / 4, playerNameY)
	text("vs.", 40.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER)
		.xy(tableWidth + stateBoardWidth / 2, playerNameY)

	val diceSize = 100
	val diceBoardWidth = WIDTH - tableWidth - stateBoardWidth
	val diceImageSourceList = listOf(
		resourcesVfs["0.png"].readBitmapSlice(),
		resourcesVfs["1.png"].readBitmapSlice(),
		resourcesVfs["2.png"].readBitmapSlice(),
		resourcesVfs["3.png"].readBitmapSlice(),
		resourcesVfs["4.png"].readBitmapSlice(),
		resourcesVfs["5.png"].readBitmapSlice(),
		resourcesVfs["6.png"].readBitmapSlice(),
	)

	container {
		val dice1Image = image(diceImageSourceList[diceImageSource.value]) {
			diceImageSource.observe {
				bitmap = diceImageSourceList[diceImageSource.value]
			}
		}.size(diceSize, diceSize).xy(1300, 300)
		text(diceImageSource.value.toString(), 30.0, Colors[WHITE], font) {
			alignLeftToRightOf(dice1Image, 75)
			alignTopToTopOf(dice1Image)
			diceImageSource.observe {
				text = diceImageSource.value.toString()
			}
		}
		val dice2Image = image(diceImageSourceList[diceImageSource.value]) {
			diceImageSource.observe {
				bitmap = diceImageSourceList[diceImageSource.value]
			}

		}.size(diceSize, diceSize).xy(1300, 420)
		text(diceImageSource.value.toString(), 30.0, Colors[WHITE], font) {
			alignLeftToRightOf(dice2Image, 75)
			alignTopToTopOf(dice2Image)
			diceImageSource.observe {
				text = diceImageSource.value.toString()
			}
		}
		val dice3Image = image(diceImageSourceList[diceImageSource.value]) {
			diceImageSource.observe {
				bitmap = diceImageSourceList[diceImageSource.value]
			}
		}.size(diceSize, diceSize).xy(1300, 540)
		text(diceImageSource.value.toString(), 30.0, Colors[WHITE], font) {
			alignLeftToRightOf(dice3Image, 75)
			alignTopToTopOf(dice3Image)
			diceImageSource.observe {
				text = diceImageSource.value.toString()
			}
		}
		val dice4Image = image(diceImageSourceList[diceImageSource.value]) {
			diceImageSource.observe {
				bitmap = diceImageSourceList[diceImageSource.value]
			}
		}.size(diceSize, diceSize).xy(1300, 660)
		text(diceImageSource.value.toString(), 30.0, Colors[WHITE], font) {
			alignLeftToRightOf(dice4Image, 75)
			alignTopToTopOf(dice4Image)
			diceImageSource.observe {
				text = diceImageSource.value.toString()
			}
		}
		val dice5Image = image(diceImageSourceList[diceImageSource.value]) {
			diceImageSource.observe {
				bitmap = diceImageSourceList[diceImageSource.value]
			}
		}.size(diceSize, diceSize).xy(1300, 780)
		text(diceImageSource.value.toString(), 30.0, Colors[WHITE], font) {
			alignLeftToRightOf(dice5Image, 75)
			alignTopToTopOf(dice5Image)
			diceImageSource.observe {
				text = diceImageSource.value.toString()
			}
		}
	}

	// build game plot from json file: 노가다 필요할 것으로 보임 깔깔
	val test = resourcesVfs["test.json"].readString()
	println(test)
	val json = Json.parse(test)
	println(json.dyn["player1Name"])
	println(json.dyn["player2Name"])
	val intList = json.dyn["intList"].toList()
	println(intList.size)
	intList.forEach {
		println(it.dyn["trial"])
	}


	text(trialState.value.toString(), 50.0, Colors[WHITE], font, TextAlignment.MIDDLE_CENTER) {
		trialState.observe {
			text = it.toString()
		}
	}.xy(1300, 250)
//
//	val initialGameState = GameState()
//	val gameStateList: List<GameState> = //read from file

	addEventListener<KeyEvent> {
		if (it.key == Key.SPACE && it.typeUp) {
			updateGameState()

		}
	}

	addEventListener<MouseEvent> {
		if (it.typeClick) {
			updateGameState()
		}
	}

}

fun updateGameState() {
	if (trialState.value == TrialState.SHOW_DECISION) {
		index.update(index.value + 1)
		diceImageSource.update((diceImageSource.value + 1) % 7)
	}
	trialState.update(trialState.value.nextTrialState())
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

enum class TrialState {
	INIT {
		override fun nextTrialState() = ROLLING
	},
	ROLLING {
		override fun nextTrialState() = SHOW_DICES
	},
	SHOW_DICES {
		override fun nextTrialState() = SHOW_DECISION
	},
	SHOW_DECISION {
		override fun nextTrialState() = INIT
	};

	abstract fun nextTrialState(): TrialState
}

data class GamePlot (
	val player1Name: String,
	val player2Name: String,
	val intList: List<GameState>
)

data class GameState (
	val turn: Int = 0,
	val player: String = "",
	val trial: Int? = null,
	val dices: List<Int> = listOf(0, 0, 0, 0, 0),
//	val scoreBoard: Map<String, Score>,
	val decision: Decision = Decision()
)

data class Score (
	val aces: String = "-",
	val deuces: String = "-",
	val threes: String = "-",
	val fours: String = "-",
	val fives: String = "-",
	val sixes: String = "-",
	val choice: String = "-",
	val fourOfAKind: String = "-",
	val fullHouse: String = "-",
	val smallStraight: String = "-",
	val largeStraight: String = "-",
	val yacht: String = "-"
) {
	private fun getScore(score: String): Int {
		return if (score == "-") 0 else score.toInt()
	}

	fun getSubTotal(): String {
		val subTotal = (getScore(aces) + getScore(deuces) + getScore(threes)
			+ getScore(fours) + getScore(fives) + getScore(sixes))
		return if (subTotal > 63) (subTotal + 63).toString() else subTotal.toString()
	}

	fun getTotal(): String {
		return (getSubTotal().toInt() + getScore(choice) + getScore(fourOfAKind) + getScore(fullHouse)
			+ getScore(fullHouse) + getScore(smallStraight) + getScore(largeStraight) + getScore(yacht)).toString()
	}
}

data class Decision (
	val keepDices: List<Int>? = null,
	val decision: String? = null
)
