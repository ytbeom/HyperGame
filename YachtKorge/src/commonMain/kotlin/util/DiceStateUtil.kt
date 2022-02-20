package util

fun getDiceKept(keepDices: List<Int>?, diceIndex: Int): Boolean {
    return when {
        keepDices.isNullOrEmpty() -> false
        keepDices.contains(diceIndex) -> true
        else -> false
    }
}

fun getDiceState(keepDices: List<Int>?, diceIndex: Int): String {
    return if (getDiceKept(keepDices, diceIndex)) {
        "KEEP"
    } else {
        "FREE"
    }
}
