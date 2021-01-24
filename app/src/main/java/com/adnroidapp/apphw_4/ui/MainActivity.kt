package com.adnroidapp.apphw_4.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.adnroidapp.apphw_4.R
import com.adnroidapp.apphw_4.model.Model
import com.adnroidapp.apphw_4.presenter.MainPresenter
import com.adnroidapp.apphw_4.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

const val TAG = "MainActivity"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter(Model()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.saveJpgFile() //записываем из drawable на sd карту в файл movie.jpg

        buttonPNG.setOnClickListener {
            presenter.convertJPGtoPNG()         //конвертируем image в png и показываем movie.png
        }

        buttonJPG.setOnClickListener {
            presenter.readAndShowImage(Model.IMG_JPG) //читаем movie.jpg и показываем на экран
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showJpgImage(uri: Uri) {
        imageView.setImageURI(uri)
    }

    override fun showSuccessToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}