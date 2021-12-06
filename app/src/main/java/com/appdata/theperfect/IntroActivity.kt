package com.appdata.theperfect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.agency55.youprep.fragment.*
import com.appdata.theperfect.adapter.ViewPagerAdapter
import com.appdata.theperfect.databinding.ActivityIntroBinding
import java.util.*

class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var currentPos = 0

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 2000

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(IntroFragment1(), "intro1")
        pagerAdapter.addFragment(IntroFragment2(), "intro2")
        pagerAdapter.addFragment(IntroFragment3(), "intro3")
        pagerAdapter.addFragment(IntroFragment4(), "intro4")
        pagerAdapter.addFragment(IntroFragment5(), "intro5")
        pagerAdapter.addFragment(IntroFragment6(), "intro6")
        pagerAdapter.addFragment(IntroFragment7(), "intro7")

        binding.tvNext.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)

        binding.viewpager.adapter = pagerAdapter
        binding.viewpager.offscreenPageLimit = 7
        binding.tabLayoutRing.setupWithViewPager(binding.viewpager)
        binding.tabLayoutRing.setBackgroundColor(android.R.color.transparent)

        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPos = position
                binding.tvNext.text=resources.getString(R.string.next)

                if(currentPos==6) {
                    binding.tvNext.text = resources.getString(R.string.txt_to_start)
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        val openMainActivity = Intent(this@IntroActivity, LoginActivity::class.java)
                        startActivity(openMainActivity)
                        finish()
                    },2000)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        val handler = Handler()
        val Update = Runnable {
            if (currentPos == 7 - 1) {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    val openMainActivity = Intent(this@IntroActivity, LoginActivity::class.java)
                    startActivity(openMainActivity)
                    finish()
                },2000)

                //currentPage = 0
            }
            binding.viewpager.setCurrentItem(currentPos++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNext -> {
                currentPos += 1
                if (currentPos < 7) binding.viewpager.setCurrentItem(currentPos, true) else {
                    val openMainActivity = Intent(this@IntroActivity, LoginActivity::class.java)
                    startActivity(openMainActivity)
                    finish()
                }
            }
            R.id.tvSkip -> {
                val openMainActivity = Intent(this@IntroActivity, LoginActivity::class.java)
                startActivity(openMainActivity)
                finish()
            }
        }
    }
}