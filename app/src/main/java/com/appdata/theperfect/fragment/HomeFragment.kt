package com.appdata.theperfect.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.appdata.theperfect.MainActivity
import com.appdata.theperfect.R
import com.appdata.theperfect.adapter.HomeCategoryAdapter
import com.appdata.theperfect.databinding.FragmentHomeBinding
import com.appdata.theperfect.databinding.ProgresBarAllBinding
import com.appdata.theperfect.interfaces.ICategoryView
import com.appdata.theperfect.model.ModelCategoryData
import com.appdata.theperfect.model.ResponseCategoryData
import com.appdata.theperfect.presenter.CategoryPresenter
import com.appdata.theperfect.utils.GridSpacingItemDecoration
import com.appdata.theperfect.utils.NetworkAlertUtility
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), ICategoryView {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryPresenter: CategoryPresenter
    private var progressDialog: Dialog? = null
    private lateinit var arrayList: ArrayList<ModelCategoryData>
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        arrayList = ArrayList()

        categoryPresenter = CategoryPresenter()
        categoryPresenter.view = this

        getCategoryData()

        if ((activity as MainActivity).isInitialized) {
            (activity as MainActivity).ivMenu.visibility=View.VISIBLE
            (activity as MainActivity).rlCount.visibility=View.VISIBLE
            (activity as MainActivity).ivDrawerMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    activity as MainActivity,
                    R.drawable.ic_baseline_menu_24
                )
            )
        }

        /*root.yesbutton.setOnClickListener {view:View->
             Navigation.findNavController(view).navigate(R.id.action_questionFragment_to_correctFragment)
         }*/

        /*  root.nobutton.setOnClickListener {view:View->
             Navigation.findNavController(view).navigate(R.id.action_questionFragment_to_wrongFragment)
         }*/

        homeCategoryAdapter =
            HomeCategoryAdapter(activity, arrayList, HomeCategoryAdapter.OnItemClick {
//            val intent = Intent(activity, UserListActivity::class.java)
//            startActivity(intent)
                val bundle = Bundle()
                bundle.putString("cat_id", arrayList[it].categoryId.toString())
                bundle.putInt("from", 1)
                Navigation.findNavController(requireView()).navigate(R.id.actionList, bundle)
            })

        val spanCount = 3 // 3 columns
        val includeEdge = false
        val spacing = 30 // 50px

        binding.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerView.adapter = homeCategoryAdapter
        binding.recyclerView.isNestedScrollingEnabled = false

        return binding.root
    }

    private fun getCategoryData() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("status", "1")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            categoryPresenter.categoryData(activity, jsonParams)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun enableLoadingBar(enable: Boolean, s: String?) {
        if (enable) loadProgressBar(activity) else dismissProgressBar()
    }

    override fun onError(reason: String?) {
        try {
            val jObjError = JSONObject(reason)
            val msg = if (jObjError.has("Message")) jObjError.getString("Message") else ""
            Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun dialogAccountDeactivate(reason: String?) {

    }

    override fun onErrorToast(reason: String?) {

    }

    override fun onGetCategoryData(body: ResponseCategoryData) {
        if (body.data != null && body.data.size > 0) {
            arrayList.addAll(body.data)
            homeCategoryAdapter.setData(arrayList)
        }
    }

    fun loadProgressBar(mContext: Context?) {
        if (progressDialog == null) {
            val progresBarAllBinding: ProgresBarAllBinding = DataBindingUtil.inflate(
                LayoutInflater
                    .from(activity), R.layout.progres_bar_all, null, false
            )
            progressDialog = Dialog(mContext!!)
            Objects.requireNonNull(progressDialog!!.window)
                ?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            Objects.requireNonNull(progressDialog!!.window)
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setCancelable(false)
            progressDialog!!.setContentView(progresBarAllBinding.getRoot())
        }
        if (!progressDialog!!.isShowing()) progressDialog!!.show()
    }

    fun dismissProgressBar() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = null
        }
    }
}