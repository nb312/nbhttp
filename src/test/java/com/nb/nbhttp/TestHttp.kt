package com.nb.nbhttp

import com.nb.nbhttp.inter.BaseController
import com.nb.nbhttp.inter.BaseServiceCreator
import com.nb.nbhttp.inter.NBaseCallback
import com.nb.nbhttp.param.NBaseResponse
import com.nb.nbhttp.param.NBaseParam
import io.reactivex.Observable
import org.junit.Test
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by NieBin on 2018-07-09
 * Github: https://github.com/nb312
 * Email: niebin312@gmail.com
 */
open class BaseParam : NBaseParam() {
    init {
        hostUrl = "http://app.dev.51szzc.com/"
        timeStamp = System.currentTimeMillis()
    }

}

data class TestParam(var time: String = "") : BaseParam()

interface ITestService {

    @GET("/gen-sign")
    fun test(@Query("time") time: String): Observable<NBaseResponse<String>>

}

open class BaseConfigCallback<BodyItem> {
    fun successState(content: NBaseResponse<BodyItem>) {

    }

    fun onError(err: String) {

    }
}

abstract class ConfigCallback<BodyItem> : BaseConfigCallback<BodyItem>() {

}

interface IConfig {
    var testParam: TestParam
    fun onTestSuccess(body: String?)
    fun onTestError(err: String?)

}

interface ITestController {
    fun test(iConfig: IConfig)
}

class TestController : BaseController(), ITestController {

    override fun test(iConfig: IConfig) {
        var serviceCreator = BaseServiceCreator(iConfig.testParam)
        var service = serviceCreator.createService(ITestService::class.java)
        addDisposable(serviceCreator.onSubscribe(service.test(iConfig.testParam.time), TestConfigCallBack(iConfig)))
    }


}

class TestConfigCallBack(var iConfig: IConfig) : NBaseCallback<String>(iConfig.testParam) {
    override fun onSuccessState(NBaseResponse: NBaseResponse<String>) {
        iConfig.onTestSuccess(NBaseResponse.body)
    }

    override fun onError(err: String) {
        iConfig.onTestError(err)
    }
}


class TestHttp {
    @Test
    fun test() {
        var testController = TestController()
        var iconfig = object : IConfig {
            override var testParam: TestParam = TestParam("${System.currentTimeMillis()}")

            override fun onTestSuccess(body: String?) {
                println("body = $body")
            }

            override fun onTestError(err: String?) {
                println("error = $err")
            }

        }
        testController.test(iconfig)
    }

    @Test
    fun testList() {
        var l = mutableListOf("A", "D", "E", "HH")
        var x = l.subList(0, 2)
        println(x)
    }
}


