package me.xiaox.baidufanyi.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.xiaox.baidufanyi.util.HttpUtils
import me.xiaox.baidufanyi.util.MD5

data class TransApi(
    private val appId: String,
    private val securityKey: String
) {
    private val url = "https://fanyi-api.baidu.com/api/trans/vip/translate"
    private val json = Json(
        JsonConfiguration.Stable.copy(
            ignoreUnknownKeys = true
        )
    )

    fun getTransResult(query: String, from: String, to: String): ResultData {
        val salt = System.currentTimeMillis().toString()
        val params = mapOf(
            "q" to query,
            "from" to from,
            "to" to to,
            "appid" to appId,
            "salt" to salt,
            "sign" to MD5.md5(appId + query + salt + securityKey)
        )
        val result = HttpUtils.get(url, params)["result"] ?: ""
        return json.parse(ResultData.serializer(), result)
    }

    @Serializable
    data class ResultData(
        val error_code: Int = 52000,
        val trans_result: List<TransResult> = listOf()
    ) {
        fun getMessage(): String {
            return when (error_code) {
                52000 -> {
                    "成功"
                }
                52001 -> {
                    "错误代码: $error_code,请求超时,请重试."
                }
                52002 -> {
                    "错误代码: $error_code,系统错误,请重试."
                }
                52003 -> {
                    "错误代码: $error_code,未授权用户,请检查你的appid是否正确,或服务是否开通."
                }
                54000 -> {
                    "错误代码: $error_code,必填参数为空,请检查是否少传参数."
                }
                54001 -> {
                    "错误代码: $error_code,签名错误,请检查签名生成是否正确."
                }
                54003 -> {
                    "错误代码: $error_code,访问频率受限,请降低调用频率,或切换为高级版."
                }
                54004 -> {
                    "错误代码: $error_code,账户余额不足,请前往控制台充值."
                }
                54005 -> {
                    "错误代码: $error_code,长消息请求频繁,降低长消息的发送频率,3s后再试."
                }
                58000 -> {
                    "错误代码: $error_code,客户端IP非法."
                }
                58001 -> {
                    "错误代码: $error_code,译文语言方向不支持,请检查是否支持."
                }
                58002 -> {
                    "错误代码: $error_code,服务当前已关闭,请前往控制台开启服务."
                }
                90107 -> {
                    "错误代码: $error_code,认证未通过或未生效,请查看认证进度."
                }
                else -> {
                    "未知错误,错误代码: $error_code"
                }
            }
        }
    }

    @Serializable
    data class TransResult(
        val dst: String = ""
    )
}