import java.io.File

/**
 * Определение кол-ва уникальных ip-addresses IPv4
 * в текстовом файле формата одна строчка - одна запись
 * Идея решения в организации квази-битового
 * пространства, разряды которого показывают
 * наличие соответствующего адреса в источнике данных.
 * Таким образом размер источника лимитируется
 * внешними возможностями
 */
const val PATCH_NAME_resource : String = "ip_adr_test.txt"

/** 67108864 = 0xFF_FF_FF_FF(максимальное количество адресов ) / 64 (разрядность Long) */
const val MAX_ARR_IP : Int = 67108864
val IP_ARR   = LongArray (MAX_ARR_IP)
val IP_ARR_D = LongArray (MAX_ARR_IP)
var ipUnic = 0L

fun main()
{
    File(PATCH_NAME_resource).forEachLine { line -> inp_ipAdr(line) }
    println("Number of unique ip addresses : $ipUnic")
}
fun inp_ipAdr(a:String){
    val d = iPtoInt(a)
    if (d ==-1L) return
    if (ipRookie(arr_adr(d)))
        ipUnic++
    else
        ipUnic--
}
fun iPtoInt(a: String):Long{
    var d = 0L
    var i=4
    val lines: List<String> = a.replace('.','\n').lines()
    if (lines.size!=4) return -1L
    lines.forEach {
        i--
        val r = it.toLong()
        if ((r>255)and(r<0))  return -1L
        d=(r shl (i*8)) or d
    }
    return d
}
data class ArrAdr(val el : Int, val bt : Int)
fun arr_adr(adr:Long) : ArrAdr{
    val el = (adr / 64 ).toInt()
    val bt = (adr - (el * 64)).toInt()
    return ArrAdr(el, bt)
}
fun ipRookie(a : ArrAdr):Boolean{
    var d = IP_ARR[a.el]
    var r = (d shr a.bt) and 1

    if (r.toInt() == 1) { // адрес встречался
        d = IP_ARR_D[a.el]
        r = (d shr a.bt) and 1
        if (r.toInt() == 1) // более 1-го раза
            ipUnic++
        else
            r = (1 shl a.bt).toLong()
            d = d or r
            IP_ARR_D[a.el] = d // второй раз
        return false
    }
    r = (1 shl a.bt).toLong()
    d = d or r
    IP_ARR[a.el] = d // первое обнаружение адреса в источнике
    return true
}