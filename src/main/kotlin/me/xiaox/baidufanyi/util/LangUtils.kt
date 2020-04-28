package me.xiaox.baidufanyi.util

object LangUtils {
    private val lang = mapOf(
        "zh" to "中文", "en" to "英语", "yue" to "粤语", "wyw" to "文言文",
        "jp" to "日语", "kor" to "韩语", "fra" to "法语", "spa" to "西班牙语",
        "th" to "泰语", "ara" to "阿拉伯语", "ru" to "俄语", "pt" to "葡萄牙语",
        "de" to "德语", "it" to "意大利语", "el" to "希腊语", "nl" to "荷兰语",
        "pl" to "波兰语", "bul" to "保加利亚语", "est" to "爱沙尼亚语", "dan" to "丹麦语",
        "fin" to "芬兰语", "cs" to "捷克语", "rom" to "罗马尼亚语", "slo" to "斯洛文尼亚语",
        "swe" to "瑞典语", "hu" to "匈牙利语", "cht" to "繁体中文", "vie" to "越南语"
    )

    /**
     * 根据简写获取中文名称
     */
    fun getSimple2Chinese(text: String): String {
        return lang[text] ?: "获取失败"
    }

    /**
     * 根据中文名称获取简写
     */
    fun getChinese2Simple(text: String): String {
        for ((k, v) in lang) {
            if (v == text) {
                return k
            }
        }
        return "获取失败"
    }
}