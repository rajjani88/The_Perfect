package com.appdata.theperfect

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.databinding.ActivitySplashBinding
import com.appdata.theperfect.storage.StorageUtil
import java.text.SimpleDateFormat
import java.util.*


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (StorageUtil(this).userDetail != null) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }


    private fun checkDate(): Boolean {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val strDate = sdf.parse("24-11-2021")
        return System.currentTimeMillis() <= strDate.time
    }
}