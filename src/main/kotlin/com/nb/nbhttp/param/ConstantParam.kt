package com.nb.nbhttp.param

import android.content.Context
import com.nb.nbhttp.inter.Timeout

/** Created by NieBin on 2018/7/8
 * GitHub: https://github.com/nb312
 * Email: niebin312@gmail.com
 */


open class NBaseParam {
    /**需要传入的header 参数*/
    var headerMap: HashMap<String, String> = hashMapOf()
    /**调用网络请求的时间戳 */
    var timeStamp: Long = 0L
    /**默认使用 如果用户没有传入的header 则使用默认值*/
    var nHeaderType: NHeaderType = NHeaderType.DEFAULT_IF_NOT
    /**this is the host of network.
     * FIXME at the end of the demo ,it need to remove this value.
     * */
    var hostUrl = "http://app.dev.51szzc.com/"
    /**can set the timeout type of network */
    var timeoutType = NTimeoutType.NORMAL
    /**set the timeout count ,the unit is milliseconds */
    var timeout: Timeout = Timeout()

    /**weather or not need to pass back the response string.*/
    var isNeedResponseString: Boolean = false

    /**if or not toast the error.*/
    var isToastError = false

    var context: Context? = null

    /**dialog's type */
    var dialogType: NDialogType = NDialogType.NONE
    /**this is the error callback*/
    var errorFunc = { err: String -> println("$err") }
    var httpJsonFunc: (json: String) -> Unit = {
        if (isNeedResponseString) {
            println("nbhttp-json:--------------------------------\n$it")
        }
    }
    /**if you set the true when you set the param,it will not call the http request */
    var isCanceled = false
}

enum class NDialogType {

    /**have not dialog*/
    NONE,

    /**could be cancelled by user */
    CANCELLABLE,

    /**could not be cancelled by user */
    UN_CANCELLABLE
}

enum class NTimeoutType {
    /**the timeout is 2 seconds*/
    SHORT,
    /**the timeout is 5 seconds*/
    NORMAL,
    /**the timeout is 10 seconds*/
    LONG,
    /**the timeout will be set by user*/
    SELF_DEFINE
}

enum class NHeaderType {
    /**完全使用默认参数*/
    ONLY_DEFAULT,
    /**如果用户没有传入的key值，则使用默认参数
     *  a. 如果传入header map为空，则使用系统默认的
     *  b. 如果header map传入不为空,则看是否包含有默认的一些值，没有其中的key，则添加进去
     */
    DEFAULT_IF_NOT,
    /**只使用用户的传入的header*/
    ONLY_SELF
}

/**
 * http 返回的基本类
 * */
open class NBaseResponse<BodyItem> {
    var success = false
    var code = "0"
    var body: BodyItem? = null
    var message = ""
    var _all_json_string_ = ""
}

object ResponseJsonKey {
    const val PATH = "_path"
    const val METHOD = "_method"
    const val HEADER = "_header"
    const val CODE = "code"
    const val SUCCESS = "success"
    const val BODY = "body"
    const val MESSAGE = "message"
    const val ALL_JSON_STRING = "_all_json_string_"
}

object HttpCode {
    /**
     * 请求成功.
     */
    const val CODE_SUCCESS = "200"
    /**
     * 登录或者注册，TGT创建成功.
     */
    const val CODE_CREATE_TGT = "201"
    /**
     * 未登录或票据失效，要求重新获取ST.
     */
    const val CODE_UNAUTHORIZED = "401"
    /**
     * 服务器繁忙.
     */
    const val CODE_NO_SERVER500 = "500"
    /**
     * 服务器维护.
     */
    const val CODE_NO_SERVER501 = "501"
    /**
     * 服务器维护.
     */
    const val CODE_NO_SERVER502 = "502"
    /**
     * 没有错误.
     */
    const val CODE_NO_ERROR = "0"
    /**
     * 身份令牌已失效，请重新登录.
     */
    const val CODE_NEED_LOGIN = "-100101"
    /**
     * 服务器繁忙.
     */
    const val CODE_SERVER_BUSY = "-100100"
    /**
     * 登录成功但不具有该接口业务的权限.
     */
    const val CODE_FORBIDDEN = "403"
    /**
     * 登录时未激活邮箱
     */
    const val CODE_NOT_ACT_EMAIL = "-101098"
}


