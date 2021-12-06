package com.appdata.theperfect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.databinding.ActivityChooseUserBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView


class ChooseUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseUserBinding
    private lateinit var verifyDialog: BottomSheetDialog
    private lateinit var bsDialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    lateinit var cards: ArrayList<MaterialCardView>
    lateinit var strokes: ArrayList<FrameLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_user)

        cards = ArrayList()
        strokes = ArrayList()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        cards.add(binding.cardViewBuyer)
        cards.add(binding.cardViewSeller)

        strokes.add(binding.strokeBuyer)
        strokes.add(binding.strokeSeller)

        binding.cardViewBuyer.setOnClickListener {
            setChecked(binding.cardViewBuyer)
//            Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val openMainActivity = Intent(this@ChooseUserActivity, RegisterActivity::class.java)
            openMainActivity.putExtra("role", "1")
            openMainActivity.putExtra("from", "register")
            startActivity(openMainActivity)
//            },100)
        }

        binding.cardViewSeller.setOnClickListener {
            setChecked(binding.cardViewSeller)
//            Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val openMainActivity = Intent(this@ChooseUserActivity, RegisterActivity::class.java)
            openMainActivity.putExtra("role", "2")
            openMainActivity.putExtra("from", "register")
            startActivity(openMainActivity)
//            },100)
        }
    }

    fun setChecked(selected: MaterialCardView?) {
        val index: Int = cards.indexOf(selected)
        val stroke: FrameLayout = strokes.get(index)
        stroke.visibility = View.VISIBLE
        for (s in strokes) {
            if (s != stroke) {
                s.visibility = View.INVISIBLE
            }
        }
    }

    fun checkedCard(): MaterialCardView {
        var index = 0
        for (s in strokes) {
            if (s.visibility == View.VISIBLE) {
                index = strokes.indexOf(s)
            }
        }
        return cards.get(index)
    }
}