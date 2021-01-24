package com.adnroidapp.apphw_4.presenter

import android.net.Uri
import com.adnroidapp.apphw_4.model.Model
import com.adnroidapp.apphw_4.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import java.io.File

class MainPresenter(private val model: Model) : MvpPresenter<MainView>() {

    fun saveJpgFile() {
        model.getDir()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                model.saveJPGFile(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.showSuccessToast("File save")
                    }, { e ->
                        viewState.showError("File not save: ${e.message}")
                    })
            }, {
                viewState.showError("File not fount")
            })
    }

    fun readAndShowImage(image: String) {
        model.getDir()
            .subscribeOn(Schedulers.io())
            .map {
                File(it, image).absolutePath    //перход от папки к файлу изображения -> к строке для пути
            }.map {
                Uri.parse(it)               //переход к Uri
            }.observeOn(AndroidSchedulers.mainThread())     //возвращаемся в поток UI
            .subscribe({
                viewState.showJpgImage(it)
            }, { e ->
                viewState.showError("Erro: ${e.message}")
            })
    }

    fun convertJPGtoPNG() {
        model.getDir()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation()) //переход в поток для вычислений
            .subscribe({
                model.convertJpgToPNG(it)       //конвертируем JPG в PNG
                    .observeOn(AndroidSchedulers.mainThread())      //перходим в поток main для работы с view
                    .subscribe({
                        viewState.showSuccessToast("Convert image PNG success")
                        readAndShowImage(Model.IMG_PNG)
                    }, { e ->
                        viewState.showError("Convert error: ${e.message}")
                    })
            }, { e ->
                viewState.showError("File not found: ${e.message}")
            })
    }
}