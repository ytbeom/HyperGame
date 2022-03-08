import com.soywiz.klock.TimeSpan
import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Rectangle
import util.*

var plotIndex = 0
var isStarted = false

val trialState = ObservableProperty(TrialState.INIT)
val currentGameState = ObservableProperty(GameState())

suspend fun main() = Korge(width = WIDTH, height = HEIGHT, title = title, bgcolor = Colors[MAIN_COLOR],
	virtualWidth = VIRTUAL_WIDTH, virtualHeight = VIRTUAL_HEIGHT) {
	val font = TtfFont(resourcesVfs["DOS_font.ttf"].readAll())

	// Read json file
	val gamePlot = readGamePlot()

	// Init scoreBoard
	currentGameState.update(
		currentGameState.value.copy(
			scoreBoard = gamePlot.players.associateWith {
				Score()
			}
		)
	)

	// Draw table background
	val defaultTableWidthRatio = 0.35
	val additionalTableWidthRatio = 0.1
	val tableWidth = VIRTUAL_WIDTH * (defaultTableWidthRatio + (gamePlot.players.size - 2) * additionalTableWidthRatio)
	solidRect(tableWidth, VIRTUAL_HEIGHT.toDouble(), Colors[WHITE]).xy(0, 0)

	// Draw cell
	val titleCellRow = 3
	val scoreCellRow = 12
	val titleCellHeightRatio = 1.2
	val cellMargin = 2.0
	val scoreCellWidth = (tableWidth - (gamePlot.players.size + 2) * cellMargin) / (gamePlot.players.size + 2)
	val cellWidthList = listOf(scoreCellWidth * 2).plus(gamePlot.players.map { scoreCellWidth })
	val scoreCellHeight = ((VIRTUAL_HEIGHT - cellMargin * (titleCellRow + scoreCellRow + 1))
		/ (scoreCellRow + titleCellRow * titleCellHeightRatio))
	val cellHeightList = listOf(
		scoreCellHeight * titleCellHeightRatio, // "Categories" and player's name
		scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight,
		scoreCellHeight * titleCellHeightRatio, // Subtotal
		scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight, scoreCellHeight,
		scoreCellHeight * titleCellHeightRatio, // Total
	)

	// Draw categories
	for (row in 0 until titleCellRow + scoreCellRow) {
		val y = cellMargin * (row + 1) + cellHeightList.subList(0, row).sum()
		val categoryCell = solidRect(cellWidthList[0], cellHeightList[row], cellColor(0, row)).xy(cellMargin, y)
		getCategoryText(row).let {
			text(it.first, 35.0, it.second, font).centerOn(categoryCell)
		}
	}

	// Draw player score
	for ((playerIndex, player) in gamePlot.players.withIndex()) {
		val col = playerIndex + 1
		for (row in 0 until titleCellRow + scoreCellRow) {
			val x = cellMargin * (col + 1) + cellWidthList.subList(0, col).sum()
			val y = cellMargin * (row + 1) + cellHeightList.subList(0, row).sum()
			val scoreCell = solidRect(cellWidthList[col], cellHeightList[row], Colors[SUB_COLOR]).xy(x, y)
			if (row == 0) {
				text(player, 35.0, Colors[WHITE], font).centerOn(scoreCell)
			} else {
				text("", 35.0, Colors[WHITE], font) {
					currentGameState.observe {
						text = getScore(it.scoreBoard?.get(player), row)
						// 점수에 따라 text의 width가 바뀌므로, centerOn을 observe 내에서 해줘야 정상적으로 위치함
						centerOn(scoreCell)
					}
				}
			}
		}
		// Draw dimmer
		solidRect(scoreCellWidth, VIRTUAL_HEIGHT.toDouble() - cellMargin * 2, Colors[BLACK]) {
			alpha(0.6).xy(cellMargin * (col + 1) + cellWidthList.subList(0, (col)).sum(), cellMargin)
			currentGameState.observe {
				alpha = if (player == currentGameState.value.player) 0.0 else 0.6
			}
			trialState.observe {
				alpha = if (trialState.value == TrialState.FINISH) 0.0 else this.alpha
			}
		}
	}

	// Draw title
	val titleWidth = VIRTUAL_WIDTH - tableWidth
	val titleHeight = VIRTUAL_HEIGHT / 6.0
	val title = text(title, 80.0, Colors[WHITE], font) {
		setTextBounds(Rectangle(0.0, 0.0, titleWidth, titleHeight))
		alignment = TextAlignment.MIDDLE_CENTER
		xy(tableWidth, 0.0)
	}

	// Draw dices and keep/free
	val diceSize = 100.0
	val diceStateTextWidth = 120.0
	val diceMargin = (VIRTUAL_HEIGHT - titleHeight - diceSize * 5) / 6.0
	val diceBoardWidth = diceSize + diceStateTextWidth + diceMargin * 3
	val diceImageSourceList = listOf(
		resourcesVfs["0.png"].readBitmapSlice(),
		resourcesVfs["1.png"].readBitmapSlice(),
		resourcesVfs["2.png"].readBitmapSlice(),
		resourcesVfs["3.png"].readBitmapSlice(),
		resourcesVfs["4.png"].readBitmapSlice(),
		resourcesVfs["5.png"].readBitmapSlice(),
		resourcesVfs["6.png"].readBitmapSlice(),
	)

	for (i in 0 until 5) {
		val diceStateText = text(getDiceState(currentGameState.value.decision.keepDices, i), 40.0, Colors[WHITE], font) {
			setTextBounds(Rectangle(0.0, 0.0, diceStateTextWidth, diceSize))
			alignment = TextAlignment.MIDDLE_CENTER
			xy(VIRTUAL_WIDTH - diceMargin - diceStateTextWidth, titleHeight + diceSize * (i) + diceMargin * (i + 1))
			currentGameState.observe {
				text = getDiceState(currentGameState.value.decision.keepDices, i)
			}
		}

		image(diceImageSourceList[currentGameState.value.dices[i]]) {
			size(diceSize, diceSize)
			alignTopToTopOf(diceStateText)
			alignRightToLeftOf(diceStateText, diceMargin)
			currentGameState.observe {
				bitmap = diceImageSourceList[currentGameState.value.dices[i]]
			}
			trialState.observe {
				alpha = if (it == TrialState.ROLLING && !getDiceKept(currentGameState.value.decision.keepDices, i)) {
					0.0
				} else {
					1.0
				}
			}
		}

		sprite(SpriteAnimation(diceImageSourceList.shuffled(), TimeSpan(100.0))) {
			size(diceSize, diceSize)
			alignTopToTopOf(diceStateText)
			alignRightToLeftOf(diceStateText, diceMargin)
			alpha = 0.0
			playAnimationLooped()
			trialState.observe {
				alpha = if (it == TrialState.ROLLING && !getDiceKept(currentGameState.value.decision.keepDices, i)) {
					1.0
				} else {
					0.0
				}
			}
		}
	}

	// Draw player name, turn, trial
	val stateBoardWidth = VIRTUAL_WIDTH - tableWidth - diceBoardWidth
	text(gamePlot.players.joinToString(" vs "), 50.0, Colors[WHITE], font) {
		setTextBounds(Rectangle(0.0, 0.0, stateBoardWidth, 150.0))
		alignLeftToLeftOf(title)
		alignTopToBottomOf(title, 50.0)
		alignment = TextAlignment.MIDDLE_CENTER
	}

	text("", 30.0, Colors[WHITE], font) {
		setTextBounds(Rectangle(0.0, 0.0, stateBoardWidth, 150.0))
		alignment = TextAlignment.MIDDLE_CENTER
		alignLeftToLeftOf(title)
		alignTopToBottomOf(title, 150.0)
		currentGameState.observe {
			text = """
				${currentGameState.value.player}'s turn
				turn: ${currentGameState.value.turn}, trial: ${currentGameState.value.trial}
				""".trimIndent()
		}
		trialState.observe {
			alpha = if (it == TrialState.FINISH) {
				0.0
			}
			else {
				1.0
			}
		}
	}

	// Draw result text
	text("", 50.0, Colors[WHITE], font) {
		setTextBounds(Rectangle(0.0, 0.0, stateBoardWidth, 100.0))
		alignment = TextAlignment.MIDDLE_CENTER
		alignLeftToLeftOf(title)
		alignTopToBottomOf(title, 400.0)
		trialState.observe {
			text = getFinalText(currentGameState.value, gamePlot.players)
			alpha = if (trialState.value == TrialState.FINISH) 1.0 else 0.0
		}
	}

	addEventListener<KeyEvent> {
		if (it.key == Key.SPACE && it.typeUp) {
			updateGameState(gamePlot)
		}
		if (it.key == Key.Q && it.typeUp) {
			finishGame(gamePlot)
		}
	}

	addEventListener<MouseEvent> {
		if (it.typeClick) {
			updateGameState(gamePlot)
		}
	}
}
