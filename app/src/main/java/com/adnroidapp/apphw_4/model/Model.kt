package com.adnroidapp.apphw_4.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Observable
import android.os.Environment
import android.util.Log
import com.adnroidapp.apphw_4.App
import com.adnroidapp.apphw_4.R
import com.adnroidapp.apphw_4.ui.TAG
import io.reactivex.rxjava3.core.Completable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.lang.RuntimeException

class Model {
    companion object {
        const val IMG_JPG = "movie.jpg"
        const val IMG_PNG = "movie.png"
        const val FOLDER_MY_MOVIES = "/MyMovies"
    }

    fun getDir(): Observable<File> {
        var sdCard: File? = null

        //получаем состояния карты
        val sdState = Environment.getExternalStorageState()

        if (sdState == Environment.MEDIA_MOUNTED) {
            try {
                sdCard = App.instance.applicationContext
                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                Log.v(TAG, "RepositoryImp: MEDIA_MOUNTED")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        } else if (sdState == Environment.MEDIA_SHARED) {
            try {
                sdCard = App.instance.filesDir
                Log.v(TAG, "RepositoryImp: MEDIA_SHARED")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        val sdCardMyMovies = sdCard?.let {
            Log.v(TAG, "Путь к папку с картинками = $it")
            File(it, FOLDER_MY_MOVIES)
        }

        sdCardMyMovies?.mkdir()
        return Observable.just(sdCardMyMovies)
    }

    fun saveJPGFile(file: File?): Completable = Completable.create { emitter ->
        file?.let { it ->
            saveJpg(it).let {
                if (it) {
                    emitter.onComplete()
                } else {
                    emitter.onError(RuntimeException("Ошибка при сохранении файла"))
                }
            }
        }
    }

    private fun saveJpg(file: File): Boolean {
        val fileConvert = File(file, IMG_JPG)
        if (fileConvert.exists()) fileConvert.delete()
        Log.v(TAG, "IMG saveJPG in JPG = ${file.absolutePath}")
        val bitmap = BitmapFactory.decodeResource(App.instance.resources, R.drawable.movie)
        var outStr: OutputStream? = null
        return try {
            outStr = FileOutputStream(fileConvert)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStr)
            outStr.flush()
            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        } finally {
            outStr?.close()
        }
    }

    fun convertJpgToPNG(file: File): Completable = Completable.create { emitter ->
        convertToPNG(file).let {
            if (it) {
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException("Error convert JPG in PNG"))
            }
        }
    }

    private fun convertToPNG(file: File): Boolean {
        var outStr: OutputStream? = null
        val oldFile = File(file, IMG_JPG)   //путь к jpg
        val bitmap = BitmapFactory.decodeFile(oldFile.absolutePath) // bitmap для jpg
        val newFile = File(file, IMG_PNG) //меняем файл и новый путь
        Log.d(TAG, "RepositoryIMp convert PNG = ${newFile.absolutePath}")
        return try {
            outStr = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStr) //конверт в png
            outStr.flush()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            outStr?.close()
        }
    }
}