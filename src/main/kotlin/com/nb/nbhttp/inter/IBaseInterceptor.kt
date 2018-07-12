package com.nb.nbhttp.inter

import com.alibaba.fastjson.JSONObject
import com.nb.nbhttp.NBHttpUtil
import com.nb.nbhttp.param.NBaseParam
import com.nb.nbhttp.param.NHeaderType
import com.nb.nbhttp.param.ResponseJsonKey
import com.nb.nbhttp.util.SignUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

/** Created by NieBin on 2018/7/8
 * GitHub: https://github.com/nb312
 * Email: niebin312@gmail.com
 * Header 的使用说明
 *  默认情况下
 *  a. 如果传入header map为空，则使用系统默认的
 *  b. 如果header map传入不为空,则看是否包含有默认的一些值，没有其中的key，则添加进去
 */


interface IBaseInterceptor : Interceptor {
    fun buildRequest(request: Request?): Request?
    fun buildResponse(response: Response?): Response?
}

open class BaseInterceptor(var paramN: NBaseParam) : IBaseInterceptor {
    private var defaultHeaders = hashMapOf<String, String>()
    override fun buildRequest(request: Request?): Request? {
        var url = request?.url().toString()
        var authSign = SignUtil.sign(url, paramN.timeStamp)
        defaultHeaders.clear()
        if (paramN.nHeaderType != NHeaderType.ONLY_SELF) {
            defaultHeaders["Accept"] = "application/json"
            defaultHeaders["Content-Type"] = "application/json"
            defaultHeaders["AUTH_HEADER_SIGN"] = "$authSign"
            defaultHeaders["AUTH_HEADER_TIME"] = "${paramN.timeStamp}"
            defaultHeaders["Accept-Language"] = getLocalLanguage()
        }

        if (paramN.nHeaderType == NHeaderType.ONLY_DEFAULT) {
            paramN.headerMap = hashMapOf()
            paramN.headerMap.putAll(defaultHeaders)
        } else if (paramN.nHeaderType == NHeaderType.DEFAULT_IF_NOT) {
            for (defKey in defaultHeaders.keys) {
                if (!paramN.headerMap.containsKey(defKey)) {
                    paramN.headerMap[defKey] = defaultHeaders[defKey] ?: ""
                }
            }
        }

        var requestBuilder = request?.newBuilder()
        for (key in paramN.headerMap.keys) {
            requestBuilder?.addHeader(key, paramN.headerMap[key])
        }
        requestBuilder?.method(request?.method(), request?.body())
        return requestBuilder?.build()
    }

    override fun buildResponse(response: Response?): Response? {
        var builder = response?.newBuilder()

        val responseBodyCopy = response?.peekBody(java.lang.Long.MAX_VALUE)//if just call the body().string ,it will get an error.

        val jsonObject = JSONObject()
        jsonObject[ResponseJsonKey.PATH] = response?.request()?.url()?.url().toString()
        jsonObject[ResponseJsonKey.METHOD] = response?.request()?.method()
        val headerObject = JSONObject()
        for (key in response?.request()?.headers()?.names() ?: arrayListOf<String>()) {
            headerObject[key] = response?.request()?.headers()?.get(key)
        }
        jsonObject[ResponseJsonKey.HEADER] = headerObject
        jsonObject[ResponseJsonKey.CODE] = "${response?.code()}"
        jsonObject[ResponseJsonKey.SUCCESS] = response?.isSuccessful
        jsonObject[ResponseJsonKey.BODY] = responseBodyCopy?.string()
        jsonObject[ResponseJsonKey.MESSAGE] = response?.message()
        //TODO if there is another code,you need to set it
        if (paramN.isNeedResponseString) {
            var jsonStr = jsonObject.toString()
            jsonObject[ResponseJsonKey.ALL_JSON_STRING] = jsonStr
            paramN.httpJsonFunc.invoke(jsonStr)
        }
        builder?.body(ResponseBody.create(response?.body()?.contentType(), jsonObject.toJSONString()))
        return builder?.build()
    }

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = buildRequest(chain?.request())
        return buildResponse(chain!!.proceed(request))
    }

    private fun getLocalLanguage(): String {
        var loc = NBHttpUtil.publicContext?.resources?.configuration?.locale
        return "${loc?.language}_${loc?.country}"
    }
}



