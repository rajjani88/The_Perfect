package com.appdata.theperfect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdata.theperfect.adapter.HomeListAdapter
import com.appdata.theperfect.databinding.ActivityUserListBinding


class UserListActivity : AppCompatActivity() {

    private lateinit var homeListAdapter: HomeListAdapter
    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

//        homeListAdapter = HomeListAdapter(this, HomeListAdapter.OnItemClick {
//        })
//
//        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        binding.recyclerView.layoutManager=linearLayoutManager
//        binding.recyclerView.itemAnimator = DefaultItemAnimator()
//        binding.recyclerView.adapter = homeListAdapter
//        binding.recyclerView.isNestedScrollingEnabled = false
    }
}