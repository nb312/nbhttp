package com.nb.nbhttp.inter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by NieBin on 2018-07-09
 * Github: https://github.com/nb312
 * Email: niebin312@gmail.com
 */
interface IBaseController {
    fun addDisposable(disposable: Disposable)
    fun unAllDisposable()
}

open class BaseController : IBaseController {
    private var disposables = CompositeDisposable()
    override fun addDisposable(disposable: Disposable) {
        if (disposable != null){
            disposables.add(disposable)
        }
    }

    override fun unAllDisposable() {
        disposables.dispose()
    }

}