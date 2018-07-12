package com.nb.nbhttp.util

import java.util.*

/** Created by NieBin on 2018/7/8
 * GitHub: https://github.com/nb312
 * Email: niebin312@gmail.com
 */

object SignUtil {
    val UTF_8 = "UTF-8"

    /**
     * 根据请求url后的参数进行签名.
     *
     * @paramN requestUrl 请求url
     * @paramN timeStamp  时间戳
     * @return 签名结果
     */
    fun sign(requestUrl: String, timeStamp: Long): String {
        var requestUrl = requestUrl
        requestUrl = StringUtil.getURLDecode(requestUrl, UTF_8)
        if (!requestUrl.contains("?")) {
            return sign(TreeMap(), timeStamp)
        }
        requestUrl = requestUrl.substring(requestUrl.indexOf("?") + 1)
        val queryKeyValues = requestUrl.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //TreeMap默认升序
        val requestParameter = TreeMap<String, String>()
        for (queryKeyValue in queryKeyValues) {
            val key = queryKeyValue.substring(0, queryKeyValue.indexOf("="))
            val value = queryKeyValue.substring(queryKeyValue.indexOf("=") + 1)
            requestParameter[key] = value
        }
        return sign(requestParameter, timeStamp)
    }

    /**
     * 根据请求TreeMap进行签名.
     *
     * @paramN requestParameter 排过序的TreeMap
     * @paramN timeStamp        时间戳
     * @return 签名结果
     */
    fun sign(requestParameter: TreeMap<String, String>, timeStamp: Long): String {
        val signResult: String
        val signStringBuilder = StringBuilder("")
        val set = requestParameter.entries
        for ((key, value) in set) {
            signStringBuilder.append(value)
        }
        signStringBuilder.append(timeStamp)
        signResult = StringUtil.getMD5Bit16(signStringBuilder.toString())
        return signResult
    }

}