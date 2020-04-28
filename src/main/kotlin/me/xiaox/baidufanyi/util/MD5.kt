package me.xiaox.baidufanyi.util

import java.security.MessageDigest

/**
 * MD5编码相关的类
 *
 * @author wangjingtao
 *
 * @since 2020/4/28 - 星空
 *
 */
object MD5 {
    private val hexDigits = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
        'e', 'f'
    )

    /**
     * 获得一个字符串的MD5值
     *
     * @param input 输入的字符串
     * @return 输入字符串的MD5值
     *
     */
    fun md5(input: String): String {
        kotlin.runCatching {
            val messageDigest = MessageDigest.getInstance("MD5")
            val resultByteArray = messageDigest.digest(input.toByteArray())
            return byteArrayToHex(resultByteArray)
        }
        return ""
    }

    private fun byteArrayToHex(byteArray: ByteArray): String {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        val resultCharArray = CharArray(byteArray.size * 2)
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        var index = 0
        for (b in byteArray) {
            resultCharArray[index++] = hexDigits[b.toInt() ushr 4 and 0xF]
            resultCharArray[index++] = hexDigits[b.toInt() and 0xF]
        }
        // 字符数组组合成字符串返回
        return String(resultCharArray)
    }
}