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
                        if (args.isNotEmpty()) {
                            if (args.size >= 2) {
                                val data = api.getTransResult(
                                    args[1], "auto", args[0]
                                )
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
                        reply("[翻译] 正确的参数 #fanyi [语言] [要翻译的内容]")
                        return@startsWith
                    }
                    "翻译查询" -> {
                        if (args.isEmpty()) {
                            reply("[翻译查询] 命令列表\n" +
                                    "#翻译查询 取简写 [目标语言] - 获取目标语言的简写\n" +
                                    "#翻译查询 取中文 [目标语言] - 获取目标语言的中文\n")
                            return@startsWith
                        }
                        if (args.size >= 2) {
                            if (args[0] == "取简写") {
                                val simple = LangUtils.getChinese2Simple(args[1])
                                reply("[翻译查询] 目标简写: $simple")
                                return@startsWith
                            }
                            if (args[0] == "取简写") {
                                val chinese = LangUtils.getSimple2Chinese(args[1])
                                reply("[翻译查询] 目标中文: $chinese")
                                return@startsWith
                            }
                        }
                    }
                }
            }
        }
    }
}