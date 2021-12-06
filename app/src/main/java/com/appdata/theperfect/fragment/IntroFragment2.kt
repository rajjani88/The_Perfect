package com.agency55.youprep.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.R
import com.appdata.theperfect.databinding.IntroSlide2Binding

class IntroFragment2 : Fragment() {

    private lateinit var binding: IntroSlide2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.intro_slide2, container, false)
        return binding.getRoot()
    }

}