package util

import MAIN_COLOR
import SUB_COLOR
import Score
import WHITE
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA

fun cellColor(row: Int, col: Int): RGBA {
    return when (Pair(row, col)) {
        Pair(0, 0), Pair(0, 7), Pair(0, 14) -> Colors[WHITE]
        Pair(1, 0), Pair(2, 0), Pair(1, 7), Pair(2, 7), Pair(1, 14), Pair(2, 14) -> Colors[SUB_COLOR]
        else -> Colors[MAIN_COLOR]
    }
}

fun getCategoryText(row: Int): Pair<String, RGBA> {
    return when (row) {
        0 -> Pair("Categories", Colors[MAIN_COLOR])
        1 -> Pair("Aces", Colors[WHITE])
        2 -> Pair("Deuces", Colors[WHITE])
        3 -> Pair("Threes", Colors[WHITE])
        4 -> Pair("Fours", Colors[WHITE])
        5 -> Pair("Fives", Colors[WHITE])
        6 -> Pair("Sixes", Colors[WHITE])
        7 -> Pair("SubTotal", Colors[MAIN_COLOR])
        8 -> Pair("Choice", Colors[WHITE])
        9 -> Pair("4 of a Kind", Colors[WHITE])
        10 -> Pair("Full House", Colors[WHITE])
        11 -> Pair("S. Straight", Colors[WHITE])
        12 -> Pair("L. Straight", Colors[WHITE])
        13 -> Pair("Yacht", Colors[WHITE])
        14 -> Pair("Total", Colors[MAIN_COLOR])
        else -> Pair("", Colors[WHITE])
    }
}

fun getScore(score: Score?, row: Int): String {
    if (score == null) {
        return ""
    }
    return when (row) {
        1 -> if (score.aces == null) "-" else score.aces.toString()
        2 -> if (score.deuces == null) "-" else score.deuces.toString()
        3 -> if (score.threes == null) "-" else score.threes.toString()
        4 -> if (score.fours == null) "-" else score.fours.toString()
        5 -> if (score.fives == null) "-" else score.fives.toString()
        6 -> if (score.sixes == null) "-" else score.sixes.toString()
        7 -> score.subtotal.toString()
        8 -> if (score.choice == null) "-" else score.choice.toString()
        9 -> if (score.fourOfAKind == null) "-" else score.fourOfAKind.toString()
        10 -> if (score.fullHouse == null) "-" else score.fullHouse.toString()
        11 -> if (score.smallStraight == null) "-" else score.smallStraight.toString()
        12 -> if (score.largeStraight == null) "-" else score.largeStraight.toString()
        13 -> if (score.yacht == null) "-" else score.yacht.toString()
        14 -> score.total.toString()
        else -> ""
    }
}
