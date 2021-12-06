package com.appdata.theperfect.fragment

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdata.theperfect.MainActivity
import com.appdata.theperfect.R
import com.appdata.theperfect.adapter.DateListAdapter
import com.appdata.theperfect.adapter.TimeListAdapter
import com.appdata.theperfect.databinding.FragmentSellerDetailBinding
import com.appdata.theperfect.databinding.ProgresBarAllBinding
import com.appdata.theperfect.interfaces.ITimeSlotView
import com.appdata.theperfect.model.*
import com.appdata.theperfect.presenter.TimeSlotPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.GoogleMapWithScrollFix
import com.appdata.theperfect.utils.NetworkAlertUtility
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.card.MaterialCardView
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SellerDetailFragment : Fragment(), ITimeSlotView, OnMapReadyCallback {

    private lateinit var binding: FragmentSellerDetailBinding
    private lateinit var mMap: GoogleMap
    lateinit var cards: ArrayList<MaterialCardView>
    lateinit var strokes: ArrayList<FrameLayout>
    private lateinit var timeSlotPresenter: TimeSlotPresenter
    private var progressDialog: Dialog? = null
    private lateinit var arrayListTime: ArrayList<ModelAppointmentTimeData>
    private lateinit var arrayListDate: ArrayList<ModelAppointmentTimeData>
    private lateinit var timeListAdapter: TimeListAdapter
    private lateinit var dateListAdapter: DateListAdapter
    private lateinit var modelSelllerListData: ModelSelllerListData
    val position = LatLng(-33.920455, 18.466941)
    private lateinit var mCenterLatLong: LatLng
    private var mZoom: Float = 0F
    private var mTimerIsRunning: Boolean = false
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellerDetailBinding.inflate(inflater, container, false)

        modelSelllerListData = requireArguments().getParcelable("data")!!
        binding.tvName.text = ""
        binding.tvEmail.text = ""
        binding.tvCity.text = ""
        binding.tvServices.text = ""

        (activity as MainActivity).ivMenu.visibility = View.GONE
        (activity as MainActivity).rlCount.visibility = View.GONE

        if (modelSelllerListData != null) {
            println(modelSelllerListData.name)
            Glide.with(this@SellerDetailFragment).load(modelSelllerListData.profileimage)
                .into(binding.ivUser)
            binding.tvName.text = modelSelllerListData.name
            binding.tvEmail.text = "Email: " + modelSelllerListData.email
            binding.tvCity.text = "City: " + modelSelllerListData.town
            binding.tvServices.text = "Services: " + modelSelllerListData.businessname
            if (modelSelllerListData.ratingStar != null) {
                binding.ratingBar.visibility = View.VISIBLE
                binding.ratingBar.rating = modelSelllerListData.ratingStar.toFloat()
            } else {
                binding.ratingBar.visibility = View.GONE
            }

            val modelUser = StorageUtil(activity).userDetail
            binding.tvNameSecond.text = modelUser.userName
            binding.tvEmailSecond.text = "Email: " + modelUser.emailID
            binding.tvCitySecond.text = "City: " + modelUser.town
            /*Glide.with(this@SellerDetailFragment).load(modelUser.get)
                .into(binding.ivUserSecond)*/
        }

        arrayListTime = ArrayList()
        arrayListDate = ArrayList()

        timeSlotPresenter = TimeSlotPresenter()
        timeSlotPresenter.view = this

        val fragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fragment!!.getMapAsync(this)

        (activity as MainActivity).ivDrawerMenu.setImageDrawable(
            ContextCompat.getDrawable(
                activity as MainActivity,
                R.drawable.ic_baseline_back
            )
        )

        binding.btnAppointmant.setOnClickListener {
            binding.llButtons.visibility = View.GONE
            binding.constraintSecondUserUser.visibility = View.VISIBLE
            binding.viewLine.visibility = View.VISIBLE
            binding.card2.visibility = View.VISIBLE
            binding.card3.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.VISIBLE
        }

        if (modelSelllerListData != null && modelSelllerListData.cdate != null && modelSelllerListData.cdate.isNotEmpty()) {
            binding.btnAppointmant.performClick()
            binding.btnSubmit.text = "Update Appointment"
        }

        binding.cardTimeSlot1.setOnClickListener {
            setChecked(binding.cardTimeSlot1)
        }

        binding.cardTimeSlot2.setOnClickListener {
            setChecked(binding.cardTimeSlot2)
        }

        binding.cardTimeSlot3.setOnClickListener {
            setChecked(binding.cardTimeSlot3)
        }

        binding.btnSubmit.setOnClickListener {
            //Navigation.findNavController(requireView()).navigate(R.id.action_go_rating)

            var coutDate = 0
            var coutTime = 0
            for (i in 0 until arrayListDate.size) {
                if (arrayListDate[i].isSelected) {
                    selectedDate = arrayListDate[i].timeSlot
                }
                if (!arrayListDate[i].isSelected) {
                    coutDate++
                    if (coutDate == arrayListDate.size) {
                        Toast.makeText(
                            activity,
                            "Please select appointment Date.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@setOnClickListener
                    }
                }
            }

            for (i in 0 until arrayListTime.size) {
                if (arrayListTime[i].isSelected) {
                    selectedTime = (i + 1).toString()
                }
                if (!arrayListTime[i].isSelected) {
                    coutTime++
                    if (coutTime == arrayListTime.size) {
                        Toast.makeText(
                            activity,
                            "Please select appointment time.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@setOnClickListener
                    }
                }
            }

            if (::mCenterLatLong.isInitialized)
                submitAppointData()
            else
                Toast.makeText(
                    activity,
                    "Please select your location.",
                    Toast.LENGTH_SHORT
                ).show()
        }

        cards = ArrayList()
        strokes = ArrayList()

        cards.add(binding.cardTimeSlot1)
        cards.add(binding.cardTimeSlot2)
        cards.add(binding.cardTimeSlot3)

        strokes.add(binding.stroke1)
        strokes.add(binding.stroke2)
        strokes.add(binding.stroke3)

        dateListAdapter = DateListAdapter(activity, arrayListDate, DateListAdapter.OnItemClick {

        })

        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewDate.layoutManager = linearLayoutManager
        binding.recyclerViewDate.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewDate.adapter = dateListAdapter
        binding.recyclerViewDate.isNestedScrollingEnabled = false

        timeListAdapter = TimeListAdapter(activity, arrayListTime, TimeListAdapter.OnItemClick {

        })
        val linearLayoutManager2 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewTime.layoutManager = linearLayoutManager2
        binding.recyclerViewTime.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewTime.adapter = timeListAdapter
        binding.recyclerViewTime.isNestedScrollingEnabled = false

        getDateSlot()

        getTimeSlotData()

        return binding.root
    }

    private fun getTimeSlotData() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            timeSlotPresenter.appointmentDatetime(activity)
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun submitAppointData() {
        if (NetworkAlertUtility.isConnectingToInternet(activity)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("AppointmentDate", selectedDate)
                jsonParams.put("AppointTimeId", selectedTime)
                jsonParams.put("Latitude", mCenterLatLong.latitude.toString())
                jsonParams.put("Longitute", mCenterLatLong.longitude.toString())
                jsonParams.put("STATUS", "1")
                if (binding.btnSubmit.text.equals("Submit")) {
                    val data = StorageUtil(activity).userDetail
                    jsonParams.put("id", modelSelllerListData.appointmentid)
                    jsonParams.put("BuyerID", data.loginId)
                    jsonParams.put("SellerID", modelSelllerListData.id)
                    timeSlotPresenter.submitAppointment(activity, jsonParams)
                } else {
                    jsonParams.put("id", modelSelllerListData.id)
                    jsonParams.put("BuyerID", modelSelllerListData.buyerID)
                    jsonParams.put("SellerID", modelSelllerListData.sellerID)
                    timeSlotPresenter.updateAppointment(activity, jsonParams)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            println(jsonParams.toString())

        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun getDateSlot() {
        val t: Date
//        if (modelSelllerListData.cdate != null && modelSelllerListData.cdate.isNotEmpty()) {
//            val format = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
//            t = format.parse(modelSelllerListData.cdate)
//        } else {
        t = Calendar.getInstance().time
//        }
        println("Current time => $t")

        val df = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
        val firstDate: String = df.format(t)
        val a = incrementDateByOne(t)
        val secondDate: String = df.format(a)
        val b = incrementDateByOne(a)
        val thirdDate: String = df.format(b)
        val c = incrementDateByOne(b)
        val fourthDate: String = df.format(c)

        println("secondDate:-->$secondDate")
        println("thirdDate:-->$thirdDate")
        println("thirdDate:-->$fourthDate")

        val modelAppointmentTimeData1 = ModelAppointmentTimeData()
        modelAppointmentTimeData1.timeSlot = secondDate
        arrayListDate.add(0, modelAppointmentTimeData1)

        val modelAppointmentTimeData2 = ModelAppointmentTimeData()
        modelAppointmentTimeData2.timeSlot = thirdDate
        arrayListDate.add(1, modelAppointmentTimeData2)

        val modelAppointmentTimeData3 = ModelAppointmentTimeData()
        modelAppointmentTimeData3.timeSlot = fourthDate
        arrayListDate.add(2, modelAppointmentTimeData3)

        for (i in 0 until arrayListDate.size) {
            if (modelSelllerListData.appointmentDate != null
                && modelSelllerListData.appointmentDate.isNotEmpty()
                && modelSelllerListData.appointmentDate == arrayListDate[i].timeSlot
            ) {
                arrayListDate[i].isSelected = true
                selectedDate = arrayListDate[i].timeSlot
            } else {
                arrayListDate[i].isSelected = false
            }
        }

        dateListAdapter.refreshData(arrayListDate)

    }

    fun incrementDateByOne(date: Date?): Date? {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 1)
        return c.time
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        (childFragmentManager
            .findFragmentById(R.id.map) as GoogleMapWithScrollFix).setListener {
            //Here is the magic happens.
            //we disable scrolling of outside scroll view here
            binding.scrollView.requestDisallowInterceptTouchEvent(true)
        }

        var lat = 0.0
        var lng = 0.0

        if (modelSelllerListData.latitude != null && modelSelllerListData.latitude.isNotEmpty()) {
            lat = modelSelllerListData.latitude.toDouble()
            lng = modelSelllerListData.longitute.toDouble()
        } else {
            lat = 25.2048
            lng = 55.2708
        }

        val hyderadbad = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(hyderadbad).draggable(true).title("Dubai"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hyderadbad))
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnCameraMoveStartedListener(OnCameraMoveStartedListener {
//            mDragTimer.start()
//            mTimerIsRunning = true
        })

        mMap.setOnCameraIdleListener(OnCameraIdleListener { // Cleaning all the markers.
            if (mMap != null) {
                mMap.clear()
            }
            mCenterLatLong = mMap.cameraPosition.target
            try {
                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong.latitude
                mLocation.longitude = mCenterLatLong.longitude
                //Toast.makeText(activity,""+mCenterLatLong.latitude+"-"+mCenterLatLong.longitude,Toast.LENGTH_SHORT).show()
                // startIntentService(mLocation)
//                binding.locationMarkertext.text = "Lat : " + mCenterLatLong.latitude.toString() + "," + "Long : " + mCenterLatLong.longitude

                /* val geocoder = Geocoder(context, Locale.getDefault())
                 try {
                     val addresses  = geocoder.getFromLocation(mCenterLatLong.latitude, mCenterLatLong.longitude, 1)
                     val address: String = addresses[0]
                         .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                     val city: String = addresses.get(0).locality
                     val state: String = addresses.get(0).adminArea
                     val country: String = addresses.get(0).countryName
                     val postalCode: String = addresses.get(0).postalCode
                     val knownName: String = addresses.get(0).featureName
                   //  Toast.makeText(activity,""+address,Toast.LENGTH_SHORT).show()
                     //binding.locationMarkertext.text = address.toString()
                 } catch (e: IOException) {
                     e.printStackTrace()
                 }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mZoom = mMap.cameraPosition.zoom
//            if (mTimerIsRunning) {
//                mDragTimer.cancel()
//            }
        })
    }


    fun setChecked(selected: MaterialCardView?) {
        val index: Int = cards.indexOf(selected)
        cards[index].setCardBackgroundColor(Color.WHITE)
        cards[index].backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.color_pink))
        for (i in 0 until cards.size) {
            if (selected != cards[i])
                cards[i].backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.white))
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

    override fun onGetDataSlot(body: ResponseAppointmentTimeData) {
        if (body.data != null && body.data.size > 0) {
            arrayListTime.clear()
            arrayListTime.addAll(body.data)
            for (i in 0 until arrayListTime.size) {
                arrayListTime[i].isSelected = (modelSelllerListData.timeSlot != null
                        && modelSelllerListData.timeSlot.isNotEmpty()
                        && modelSelllerListData.timeSlot == arrayListTime[i].timeSlot)
            }
            timeListAdapter.refreshData(arrayListTime)
        }
    }

    override fun onSubmitData(body: ResponseSaveAppointment?) {
        if (body != null) {
            Toast.makeText(activity, "" + body.message, Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireView()).navigate(R.id.action_go_home)
        }
    }

    override fun onUpdateData(body: ResponseUpdateAppointment?) {
        if (body != null) {
            Toast.makeText(activity, "" + body.message, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
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