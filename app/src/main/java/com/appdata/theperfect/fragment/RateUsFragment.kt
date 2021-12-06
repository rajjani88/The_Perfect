package com.appdata.theperfect.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdata.theperfect.ChooseUserActivity
import com.appdata.theperfect.MainActivity
import com.appdata.theperfect.R
import com.appdata.theperfect.adapter.DateListAdapter
import com.appdata.theperfect.databinding.FragmentRateUsBinding
import com.appdata.theperfect.databinding.FragmentSellerDetailBinding
import com.appdata.theperfect.databinding.ProgresBarAllBinding
import com.appdata.theperfect.interfaces.IRating
import com.appdata.theperfect.model.ModelSelllerListData
import com.appdata.theperfect.model.ResponseRatingData
import com.appdata.theperfect.presenter.RatingPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.card.MaterialCardView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class RateUsFragment : Fragment(), IRating {

    private lateinit var binding: FragmentRateUsBinding
    private lateinit var mMap: GoogleMap
    lateinit var cards: ArrayList<MaterialCardView>
    lateinit var strokes: ArrayList<FrameLayout>
    private lateinit var modelSelllerListData: ModelSelllerListData
    private lateinit var ratingPresenter: RatingPresenter
    private var currentRatingData:Float=0f
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRateUsBinding.inflate(inflater, container, false)

        ratingPresenter = RatingPresenter()
        ratingPresenter.view = this

        (activity as MainActivity).ivMenu.visibility=View.GONE
        (activity as MainActivity).rlCount.visibility=View.GONE

        (activity as MainActivity).ivDrawerMenu.setImageDrawable(
            ContextCompat.getDrawable(
                activity as MainActivity,
                R.drawable.ic_baseline_back
            )
        )

        binding.btnSubmit.setOnClickListener {
            if(currentRatingData==0f){
                Toast.makeText(activity, "Please rate Us.", Toast.LENGTH_SHORT).show()
            }else{
                submitRating()
            }
        }

        binding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
               // Toast.makeText(activity, "Given rating is: $p1", Toast.LENGTH_SHORT).show()
                currentRatingData=p1
            }

        modelSelllerListData = requireArguments().getParcelable("data")!!

        return binding.root
    }


    private fun submitRating() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val map = HashMap<String, RequestBody>()
            map["RateUserID"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                modelSelllerListData.id
            )
            map["Seller_id"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                modelSelllerListData.sellerID
            )
            map["RatingStar"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                currentRatingData.toString()
            )
            map["txtDescription"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                binding.edtDescription.text.toString()
            )
            ratingPresenter.submitRating(activity, map)
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

    override fun onRatingSuccess(body: ResponseRatingData?) {
        if (body != null) {
            Toast.makeText(activity, "" + body.message, Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireView()).navigate(R.id.action_go_home)
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
            progressDialog!!.setContentView(progresBarAllBinding.root)
        }
        if (!progressDialog!!.isShowing) progressDialog!!.show()
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

//RateUserID:2
//Seller_id:18
//RatingStar:2
//txtDescription:Verry good:2
//Seller_id:18
//RatingStar:2
//txtDescription:Verry good