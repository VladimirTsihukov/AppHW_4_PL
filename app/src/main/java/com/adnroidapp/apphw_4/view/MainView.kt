package com.adnroidapp.apphw_4.view

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun showJpgImage(uri: Uri)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showError(message: String)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSuccessToast(message: String)

}