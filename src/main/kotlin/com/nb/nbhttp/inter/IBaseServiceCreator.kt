package com.nb.nbhttp.inter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.nb.nbhttp.NBHttpUtil
import com.nb.nbhttp.param.NBaseResponse
import com.nb.nbhttp.param.HttpCode
import com.nb.nbhttp.param.NBaseParam
import com.nb.nbhttp.param.NDialogType
import com.nb.nbhttp.view.NBDialog
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Looper.getMainLooper
import android.util.Log


/** Created by NieBin on 2018/7/8
 * GitHub: https://github.com/nb312
 * Email: niebin312@gmail.com
 */


interface IBaseServiceCreator {
    fun <HttpService> createService(service: Class<HttpService>): HttpService
    fun <BodyItem> onSubscribe(observable: Observable<NBaseResponse<BodyItem>>, callback: NBaseCallback<BodyItem>): Disposable
}

open class NBaseCallback<BodyItem>(var paramN: NBaseParam) {
    var nDialog: NBDialog? = null
    open fun onSuccessState(NBaseResponse: NBaseResponse<BodyItem>) {

    }

    open fun onError(err: String) {
        paramN.errorFunc.invoke(err)
    }

    open fun onStart() {
        if (paramN.dialogType != NDialogType.NONE) {
            Handler(Looper.getMainLooper()).post {
                if (nDialog == null) {
                    nDialog = NBDialog(paramN.context, paramN)
                }
                nDialog?.show()
            }
        }


    }

    open fun onComplete() {
        if (paramN.dialogType != NDialogType.NONE) {
            Handler(Looper.getMainLooper()).post {
                nDialog?.show()
                nDialog?.dismiss()
            }
        }
    }

    open fun onUnauthorized() {

    }

    open fun noServer(type: Int) {

    }


    open fun noError(msg: String) {

    }

    open fun needLogin() {

    }

    open fun serviceBusy(msg: String) {

    }

    open fun noAuthorize() {

    }

    open fun notActEmail() {

    }
}

open class BaseServiceCreator(var paramN: NBaseParam) : IBaseServiceCreator {
    init {
        paramN.context = paramN.context ?: NBHttpUtil.publicContext
    }

    override fun <HttpService> createService(service: Class<HttpService>): HttpService {
        val gsn = GsonBuilder()
                .setLenient()
                .create()
        var retrofit = Retrofit.Builder()
                .baseUrl(paramN.hostUrl)
                .addConverterFactory(GsonConverterFactory.create(gsn))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(NBHttpClient(paramN).createHttpClient())
                .build()
        return retrofit.create(service)
    }

    override fun <BodyItem> onSubscribe(observable: Observable<NBaseResponse<BodyItem>>, callback: NBaseCallback<BodyItem>): Disposable {
        callback.onStart()
        var dispose = observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    handCode(paramN, it, callback)
                },

                        {
                            if (paramN.isToastError) {
                                Toast.makeText(NBHttpUtil.publicContext, it.message, Toast.LENGTH_SHORT).show()
                            }
                            callback.onError("${it.message}")
                        },
                        {
                            callback.onComplete()

                        })
        return dispose
    }


}


fun <BodyItem> handCode(paramN: NBaseParam, responseItemN: NBaseResponse<BodyItem>, callback: NBaseCallback<BodyItem>) {
    var errorMsg: String = when (responseItemN.code) {
        HttpCode.CODE_SUCCESS -> {
            callback.onSuccessState(responseItemN)
            ""
        }

        HttpCode.CODE_CREATE_TGT -> {
            callback.onSuccessState(responseItemN)
            ""
        }

        HttpCode.CODE_UNAUTHORIZED -> {
            callback.onUnauthorized()
            ""
        }

        HttpCode.CODE_NO_SERVER500 -> {
            responseItemN.message
        }

        HttpCode.CODE_NO_SERVER501 -> {
            responseItemN.message
        }

        HttpCode.CODE_NO_SERVER502 -> {
            responseItemN.message
        }
        HttpCode.CODE_NO_ERROR -> {
            responseItemN.message
        }

        HttpCode.CODE_NEED_LOGIN -> {
            responseItemN.message
        }

        HttpCode.CODE_SERVER_BUSY -> {
            responseItemN.message
        }

        HttpCode.CODE_FORBIDDEN -> {
            responseItemN.message
        }
        HttpCode.CODE_NOT_ACT_EMAIL -> {
            responseItemN.message
        }
        else -> {
            if (responseItemN.message.isEmpty()) {
                "This is an unknown error."
            } else {
                responseItemN.message
            }
        }
    }
    if (errorMsg.isNotEmpty() && paramN.isToastError) {
        Toast.makeText(NBHttpUtil.publicContext, errorMsg, Toast.LENGTH_SHORT).show()
    }
}