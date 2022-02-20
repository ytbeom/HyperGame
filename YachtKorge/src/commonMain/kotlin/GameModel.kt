import com.soywiz.korio.dynamic.Dyn
import com.soywiz.korio.dynamic.dyn
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.json.Json

enum class TrialState {
    INIT, ROLLING, SHOW_DICES, KEEP_DICES, PUT_SCORE, FINISH
}

data class GamePlot (
    val players: List<String>,
    val gameStateList: List<GameState>,
    val final: Map<String, Score>
)

data class GameState (
    val turn: Int = 0,
    val player: String = "",
    val trial: Int = 0,
    val dices: List<Int> = listOf(0, 0, 0, 0, 0),
    val scoreBoard: Map<String, Score>? = null,
    val decision: Decision = Decision()
)

data class Score (
    val aces: Int? = null,
    val deuces: Int? = null,
    val threes: Int? = null,
    val fours: Int? = null,
    val fives: Int? = null,
    val sixes: Int? = null,
    val subtotal: Int = 0,
    val choice: Int? = null,
    val fourOfAKind: Int? = null,
    val fullHouse: Int? = null,
    val smallStraight: Int? = null,
    val largeStraight: Int? = null,
    val yacht: Int? = null,
    val total: Int = 0
)

data class Decision (
    val keepDices: List<Int>? = null,
    val decision: String? = null
)

suspend fun readGamePlot(): GamePlot {
    val gamePlotJson = Json.parse(resourcesVfs["$fileName.json"].readString())
    val players = gamePlotJson.dyn["players"].toList().map { it.toString() }

    return GamePlot(
        players = players,
        gameStateList = gamePlotJson.dyn["log"].toList().map {
            GameState(
                turn = it.dyn["turn"].toInt(),
                player = it.dyn["player"].toString(),
                trial = it.dyn["trial"].toInt(),
                dices = it.dyn["dices"].toList().map { dice -> dice.toInt() },
                scoreBoard = readScoreBoard(it.dyn["scoreBoard"], players),
                decision = it.dyn["decision"].let { decision ->
                    Decision(
                        keepDices = decision.dyn["keepDices"].toList().map{ dice -> dice.toInt() },
                        decision = decision.dyn["decision"].toStringOrNull()
                    )
                }
            )
        },
        final = readScoreBoard(gamePlotJson.dyn["final"].dyn["scoreBoard"], players)
    )
}

fun readScoreBoard(scoreBoard: Dyn, players: List<String>): Map<String, Score> {
    return players.associateWith {
        readScore(scoreBoard.dyn[it])
    }
}

fun readScore(score: Dyn): Score {
    return Score(
        aces = if (score["aces"].isNull) null else score["aces"].toInt(),
        deuces = if (score["deuces"].isNull) null else score["deuces"].toInt(),
        threes = if (score["threes"].isNull) null else score["threes"].toInt(),
        fours = if (score["fours"].isNull) null else score["fours"].toInt(),
        fives = if (score["fives"].isNull) null else score["fives"].toInt(),
        sixes = if (score["sixes"].isNull) null else score["sixes"].toInt(),
        subtotal = score["subtotal"].toInt(),
        choice = if (score["choice"].isNull) null else score["choice"].toInt(),
        fourOfAKind = if (score["fourOfAKind"].isNull) null else score["fourOfAKind"].toInt(),
        fullHouse = if (score["fullHouse"].isNull) null else score["fullHouse"].toInt(),
        smallStraight = if (score["smallStraight"].isNull) null else score["smallStraight"].toInt(),
        largeStraight = if (score["largeStraight"].isNull) null else score["largeStraight"].toInt(),
        yacht = if (score["yacht"].isNull) null else score["yacht"].toInt(),
        total = score["total"].toInt()
    )
}
