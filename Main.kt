package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun main() {
    menu()
}

fun menu() {
    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        when (val bases = readln()) {
            "/exit" -> break
            else -> {
                val (srb, trb) = bases.split(" ")
                while (true) {
                    print(
                        "Enter number in base $srb to convert to base $trb (To go back type /back)"
                    )
                    when (val inpNum = readln()) {
                        "/back" -> break
                        else -> converse(srb, trb, inpNum)
                    }
                }
            }
        }
    }
}

fun converse(srb: String, trb: String, input: String) {
    val inputs: List<String>
    val wholePart: String
    val departed: String
    if (input.contains('.')) {
        inputs = input.split(".")
        wholePart = intPart(srb, trb, inputs[0])
        departed = "." + fractalPart(srb, trb, inputs[1])
    } else {
        wholePart = intPart(srb, trb, input)
        departed = ""
    }
    println("Conversion result: $wholePart$departed\n")
}

fun intPart(srb: String, trb: String, integerP: String): String {
    val srbB = srb.toBigInteger()
    var dec = BigInteger.ZERO
    val temp = integerP.reversed()
    for (i in temp.indices) {
        val rem = if (temp[i].code < 60) temp[i].toString().toBigInteger()
        else (temp[i].code - 87).toBigInteger()
        dec += (srbB.pow(i) * rem)
    }
    val trbI = trb.toBigInteger()
    var res = ""
    var tmp = dec
    while (true) {
        var rem: String = (tmp % trbI).toString()
        rem = if (rem.toBigInteger() >= BigInteger.TEN) (rem.toInt() + 87).toChar().toString()
        else rem
        res += rem
        tmp /= trbI
        if (tmp == BigInteger.ZERO) break
    }
    return res.reversed()
}

fun fractalPart(srb: String, trb: String, fractal: String): String {
    val srbD = srb.toBigDecimal()
    var dec = BigDecimal.ZERO
    var prd: BigDecimal
    for (i in fractal.indices) {
        prd = if (fractal[i].code < 60) fractal[i].toString().toBigDecimal()
        else (fractal[i].code - 87).toBigDecimal()
        dec += ((BigDecimal.ONE.setScale(5) / srbD).pow(i + 1) * prd)
    }
    val trbD = trb.toBigDecimal()
    var res = ""
    var tmp = dec.setScale(5, RoundingMode.HALF_EVEN)
    repeat(5) {
        if (tmp == BigDecimal.ZERO) res += "0" else {
            tmp *= trbD
            val ints = tmp.setScale(5).toString()
            val added = if (ints.toDouble().toInt() > 9) (ints.toDouble().toInt() + 87).toChar().toString()
            else ints.toDouble().toInt().toString()
            res += added
            tmp -= ints.toBigDecimal()
        }
    }
    return res
}