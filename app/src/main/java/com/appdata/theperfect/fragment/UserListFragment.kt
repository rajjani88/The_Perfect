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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdata.theperfect.MainActivity
import com.appdata.theperfect.R
import com.appdata.theperfect.adapter.HomeListAdapter
import com.appdata.theperfect.databinding.FragmentUserListBinding
import com.appdata.theperfect.databinding.ProgresBarAllBinding
import com.appdata.theperfect.interfaces.IListView
import com.appdata.theperfect.model.*
import com.appdata.theperfect.presenter.ListingUserPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class UserListFragment : Fragment(), IListView {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var homeListAdapter: HomeListAdapter
    private var catId: String = ""
    private var from: Int = 0
    private lateinit var listingUserPresenter: ListingUserPresenter
    private var progressDialog: Dialog? = null
    private lateinit var arrayList: ArrayList<ModelSelllerListData>
    private lateinit var arrZipData: ArrayList<ModelZipData>
    private var selectedZip: String = ""
    private lateinit var adapterZip: ArrayAdapter<ModelZipData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)

        listingUserPresenter = ListingUserPresenter()
        listingUserPresenter.view = this

        catId = requireArguments().getString("cat_id").toString()
        from = requireArguments().getInt("from")

        println(catId)

        arrayList = ArrayList()
        arrZipData = ArrayList()

        homeListAdapter = HomeListAdapter(activity, arrayList, HomeListAdapter.OnItemClick {
            val bundle = Bundle()
            bundle.putParcelable("data", arrayList[it])

            if (from == 1 || from == 2 || from == 4) {
                Navigation.findNavController(requireView()).navigate(R.id.action_detail, bundle)
            }

            if (from == 3 && (arrayList[it].ratingStar == null
                        || arrayList[it].ratingStar == "0"
                        || arrayList[it].ratingStar == "")
            ) {
                Navigation.findNavController(requireView()).navigate(R.id.action_go_rating, bundle)
            }

        })

        (activity as MainActivity).ivDrawerMenu.setImageDrawable(
            ContextCompat.getDrawable(
                activity as MainActivity,
                R.drawable.ic_baseline_back
            )
        )

        (activity as MainActivity).ivMenu.visibility=View.GONE
        (activity as MainActivity).rlCount.visibility=View.GONE

        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = homeListAdapter
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.inputLayoutZip.visibility = View.GONE

        when (from) {
            1 -> getCategoryData()
            2 -> getAppointmentList()
            3 -> getDealAppointmentList()
            4 -> {
                binding.inputLayoutZip.visibility = View.VISIBLE
                binding.zipDropdown.setText("")
                getZipList();
            }
        }

        return binding.root
    }

    private fun getCategoryData() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("category_id", catId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            listingUserPresenter.getSellerList(activity, jsonParams)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getAppointmentList() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val jsonParams = JSONObject()
            val map = HashMap<String, RequestBody>()
            map["BuyerID"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                StorageUtil(activity).userDetail.loginId.toString()
            )
            listingUserPresenter.getAppointmentList(activity, map)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getDealAppointmentList() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val jsonParams = JSONObject()
            val map = HashMap<String, RequestBody>()
            map["BuyerID"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                StorageUtil(activity).userDetail.loginId.toString()
            )
            listingUserPresenter.getDealAppointmentList(activity, map)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getDataUsingZip() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {

            val size: Int = arrayList.size
            arrayList.clear()
            homeListAdapter.notifyItemRangeRemoved(0, size)

            val map = HashMap<String, RequestBody>()
            map["zipcode"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                selectedZip
            )
            listingUserPresenter.getListUsingZip(activity, map)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getZipList() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            binding.zipDropdown.setText("")
            listingUserPresenter.getZipListData(activity)
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

    override fun onGetSellerList(body: ResponseSellerListData) {
        if (body.data != null && body.data.size > 0) {
            binding.tvNoData.visibility = View.GONE
            arrayList.clear()
            arrayList.addAll(body.data)
            homeListAdapter.refreshData(arrayList)
        } else {
            binding.tvNoData.visibility = View.VISIBLE
        }
    }

    override fun onGetZipData(body: ResponseZipData?) {
        if (body != null) {
            arrZipData.clear()
            arrZipData.addAll(body.data)
            setZipData()
        }
    }

    private fun setZipData() {
        if (arrZipData.size > 0) {
            val adapterZip = activity?.let {
                ArrayAdapter(
                    it,
                    R.layout.dropdown_menu_popup_item,
                    R.id.text1,
                    arrZipData
                )
            }
            binding.zipDropdown.setAdapter(adapterZip)
        } else {
            Toast.makeText(activity, "No data available.", Toast.LENGTH_SHORT).show()
        }

        binding.zipDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.adapter.getItem(position) as ModelZipData
                selectedZip = selectedItem.zipCodeName
                if (selectedZip != "") {
                    getDataUsingZip();
                }
            }
    }

    fun loadProgressBar(mContext: Context?) {
        if (progressDialog == null) {
            val progresBarAllBinding: ProgresBarAllBinding = DataBindingUtil.inflate(
                LayoutInflater
                    .from(mContext), R.layout.progres_bar_all, null, false
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

    override fun onResume() {
        super.onResume()
        when (from) {
            1 -> getCategoryData()
            2 -> getAppointmentList()
            3 -> getDealAppointmentList()
            4 -> {
                binding.inputLayoutZip.visibility = View.VISIBLE
                binding.zipDropdown.setText("")
                getZipList();
            }
        }
    }
}