package util

import Decision
import GamePlot
import InvalidGamePlotException
import TrialState
import currentGameState
import isStarted
import plotIndex
import trialState

fun updateGameState(gamePlot: GamePlot) {
    val gameState = gamePlot.gameStateList[plotIndex]
    when (trialState.value) {
        TrialState.INIT ->
            if (isStarted) {
                println("go to ROLLING STATE!!")
                currentGameState.update(
                    currentGameState.value.copy(
                        decision = Decision(
                            keepDices = emptyList()
                        )
                    )
                )
                trialState.update(TrialState.ROLLING)
            }
            else {
                println("start game!")
                currentGameState.update(
                    currentGameState.value.copy(
                        turn = gameState.turn,
                        trial = gameState.trial,
                        player = gameState.player
                    )
                )
                isStarted = true
            }
        TrialState.ROLLING -> {
            println("go to SHOW DICES STATE!!")
            currentGameState.update(
                currentGameState.value.copy(
                    dices = gameState.dices,
                )
            )
            trialState.update(TrialState.SHOW_DICES)
        }
        TrialState.SHOW_DICES ->
            if (!gameState.decision.decision.isNullOrBlank() || gameState.trial == 3) {
                println("go to PUT SCORE STATE!!")
                val scoreBoard = if (plotIndex != gamePlot.gameStateList.size -1) {
                    gamePlot.gameStateList[plotIndex + 1].scoreBoard
                }
                else {
                    gamePlot.final.scoreBoard
                }
                currentGameState.update(
                    currentGameState.value.copy(
                        scoreBoard = scoreBoard
                    )
                )
                trialState.update(TrialState.PUT_SCORE)
            }
            else {
                println("go to KEEP DICES STATE!!")
                currentGameState.update(
                    currentGameState.value.copy(
                        decision = currentGameState.value.decision.copy(
                            keepDices = gameState.decision.keepDices
                        ),
                    )
                )
                trialState.update(TrialState.KEEP_DICES)
            }
        TrialState.KEEP_DICES -> {
            if (plotIndex != gamePlot.gameStateList.size -1) {
                println("go to ROLLING STATE!!")
                plotIndex += 1
                currentGameState.update(
                        currentGameState.value.copy(
                                turn = gamePlot.gameStateList[plotIndex].turn,
                                trial = gamePlot.gameStateList[plotIndex].trial
                        )
                )
                trialState.update(TrialState.ROLLING)
            }
            else {
                println("go to FINISH STATE!!")
                trialState.update(TrialState.FINISH)
            }
        }
        TrialState.PUT_SCORE -> {
            if (plotIndex != gamePlot.gameStateList.size -1) {
                println("go to INIT STATE!!")
                plotIndex += 1
                currentGameState.update(
                    currentGameState.value.copy(
                        turn = gamePlot.gameStateList[plotIndex].turn,
                        trial = gamePlot.gameStateList[plotIndex].trial,
                        player = gamePlot.gameStateList[plotIndex].player,
                        dices = listOf(0, 0, 0, 0, 0),
                        decision = Decision()
                    )
                )
                trialState.update(TrialState.INIT)
            }
            else {
                println("go to FINISH STATE!!")
                trialState.update(TrialState.FINISH)
            }
        }
        else -> return
    }
}

fun finishGame(gamePlot: GamePlot) {
    currentGameState.update(
        currentGameState.value.copy(
            scoreBoard = gamePlot.final.scoreBoard
        )
    )
    trialState.update(TrialState.FINISH)
}
