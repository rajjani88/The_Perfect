package com.agency55.youprep.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.appdata.theperfect.R
import com.appdata.theperfect.databinding.IntroSlide1Binding

class IntroFragment1 : Fragment() {

    private lateinit var binding: IntroSlide1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.intro_slide1, container, false)
        return binding.getRoot()
    }

}