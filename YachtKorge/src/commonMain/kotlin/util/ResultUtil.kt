package util

import GameState

fun getFinalText(final: GameState, players: List<String>): String {
    val maxScore = players.mapNotNull {
        final.scoreBoard?.get(it)?.total
    }.maxByOrNull { it } ?: 0

    val winners = players.map {
        it to final.scoreBoard?.get(it)?.total
    }.filter {
        it.second != null && it.second == maxScore
    }.map {
        it.first
    }

    return winners.joinToString() + " WIN!!!"
}
