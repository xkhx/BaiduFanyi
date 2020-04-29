package me.xiaox.baidufanyi

import me.xiaox.baidufanyi.api.TransApi
import me.xiaox.baidufanyi.util.LangUtils
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.subscribeGroupMessages

object BaiduFanyi : PluginBase() {

    private const val APP_ID = "填写你的APIID"
    private const val SECURITY_KEY = "填写你的密钥"

    private val api = TransApi(APP_ID, SECURITY_KEY)

    override fun onEnable() {
        subscribeGroupMessages {
            startsWith("#", removePrefix = true) {
                val command = it.split(" ")
                val root = command[0]
                val args = command.drop(1)
                when (root) {
                    "fanyi" -> {
                        if (args.isEmpty()) {
                            reply(
                                "[翻译] 命令列表\n" +
                                        "#fanyi [目标语言] [要翻译的内容]\n" +
                                        "#fanyi [当前语言] [目标语言] [要翻译的内容]\n" +
                                        "#fanyi query [key/value] [{key}/{value}] - 根据键/值获取键(获取语言简写/语言中文)"
                            )
                            return@startsWith
                        }
                        if (args[0].toLowerCase() == "query") {
                            if (args.size >= 3) {
                                if (args[1] == "key") {
                                    val simple = LangUtils.getChinese2Simple(args[2])
                                    reply("[翻译] 目标简写: $simple")
                                    return@startsWith
                                }
                                if (args[1] == "value") {
                                    val chinese = LangUtils.getSimple2Chinese(args[2])
                                    reply("[翻译] 目标中文: $chinese")
                                    return@startsWith
                                }
                                reply("[翻译] 正确的参数 #fanyi query [key/value] [{key}/{value}]")
                                return@startsWith
                            }
                            reply("[翻译] 正确的参数 #fanyi query [key/value] [{key}/{value}]")
                            return@startsWith
                        }

                        if (args.size >= 2) {
                            val data = if (LangUtils.isExist(args[1]) && args.size > 2) {
                                val query = args.drop(2).joinToString(" ")
                                api.getTransResult(
                                    query, args[0], args[1]
                                )
                            } else {
                                val query = args.drop(1).joinToString(" ")
                                api.getTransResult(
                                    query, "auto", args[0]
                                )
                            }
                            if (data.error_code != 52000) {
                                reply("[翻译] ${data.getErrorMessage()}")
                                return@startsWith
                            }
                            val from = LangUtils.getSimple2Chinese(data.from)
                            val to = LangUtils.getSimple2Chinese(data.to)
                            reply("[翻译] $from → $to\n${data.getResult()}")
                            return@startsWith
                        }
                    }
                }
            }
        }
    }
}