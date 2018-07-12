package com.nb.nbhttp

import android.content.Context

/** Created by NieBin on 2018/7/8
 * GitHub: https://github.com/nb312
 * Email: niebin312@gmail.com
 */

object NBHttpUtil {
    var publicContext: Context? = null
    var responseJsonList = mutableListOf<String>()
    var maxCacheJsonSize: Int = 10
    fun addRepJson(json: String) {
        if (responseJsonList.size >= maxCacheJsonSize) {
            responseJsonList.subList(0, maxCacheJsonSize)
        }
        responseJsonList.add(json)
    }
}