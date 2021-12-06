package com.appdata.theperfect

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.appdata.theperfect.databinding.ActivityMainBinding
import com.appdata.theperfect.fragment.HomeFragment
import com.appdata.theperfect.fragment.UserListFragment
import com.appdata.theperfect.interfaces.IListView
import com.appdata.theperfect.model.ResponseSellerListData
import com.appdata.theperfect.model.ResponseZipData
import com.appdata.theperfect.presenter.ListingUserPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class MainActivity : BaseActivity(), IListView {

    private lateinit var binding: ActivityMainBinding
    public lateinit var ivDrawerMenu: ImageView
    public lateinit var ivMenu: ImageView
    public lateinit var rlCount: RelativeLayout
    public var isInitialized: Boolean = false
    private lateinit var fragment: Fragment
    private lateinit var listingUserPresenter: ListingUserPresenter
    private var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ivDrawerMenu = findViewById(R.id.ivDrawerMenu)
        ivMenu = findViewById(R.id.ivMenu)
        rlCount = findViewById(R.id.rlCount)
        isInitialized = true

        role = StorageUtil(this).userDetail.role

        listingUserPresenter = ListingUserPresenter()
        listingUserPresenter.view = this

        rlCount.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("from", 2)
            Navigation.findNavController(this, R.id.navhostfragment)
                .navigate(R.id.actionListDetail, bundle)
        }

        ivMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("from", 4)
            Navigation.findNavController(this, R.id.navhostfragment)
                .navigate(R.id.actionListDetail, bundle)
        }

        binding.includeSetting.constraintHome.setOnClickListener {
            openCloseNavigationDrawer()
        }

        binding.includeSetting.constraintLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.includeSetting.constraintAppointmentDetail.setOnClickListener {
            openCloseNavigationDrawer()
            val bundle = Bundle()
            bundle.putInt("from", 2)
            Navigation.findNavController(this, R.id.navhostfragment)
                .navigate(R.id.actionListDetail, bundle)
        }

        binding.includeSetting.constraintDoneDeal.setOnClickListener {
            openCloseNavigationDrawer()
            val bundle = Bundle()
            bundle.putInt("from", 3)
            Navigation.findNavController(this, R.id.navhostfragment)
                .navigate(R.id.actionListDetail, bundle)
        }

        binding.includeSetting.constraintMyAccount.setOnClickListener {
            //openCloseNavigationDrawer()
            val openMainActivity = Intent(this, RegisterActivity::class.java)
            openMainActivity.putExtra("role", role)
            openMainActivity.putExtra("from", "edit")
            startActivity(openMainActivity)
        }

        binding.appBarMain.ivDrawerMenu.setOnClickListener {
            val controller = Navigation.findNavController(this, R.id.navhostfragment)
            when (controller.currentDestination!!.id) {
                R.id.userListFragment -> {
                    onBackPressedDispatcher.onBackPressed()
                    ivDrawerMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_baseline_menu_24
                        )
                    )
                }
                R.id.RateUsFragment -> {
                    onBackPressedDispatcher.onBackPressed()
                    ivDrawerMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_baseline_menu_24
                        )
                    )
                }
                R.id.sellerDetailFragment -> {
                    onBackPressedDispatcher.onBackPressed()
                    ivDrawerMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_baseline_back
                        )
                    )
                }
                else -> {
                    if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.openDrawer(GravityCompat.START);
                    } else {
                        binding.drawerLayout.closeDrawer(GravityCompat.END);
                    }
                }
            }
        }
    }

    fun openCloseNavigationDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }


    private fun getAppointmentList() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            val jsonParams = JSONObject()
            val map = HashMap<String, RequestBody>()
            map["BuyerID"] = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                StorageUtil(this).userDetail.loginId.toString()
            )
            listingUserPresenter.getAppointmentList(this, map)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun showLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        alertDialog.setTitle("Alert !")
        alertDialog.setMessage("Are you sure want to logout?")
        alertDialog.setPositiveButton(
            "Logout"
        ) { dialog1: DialogInterface?, which: Int ->
            dataClear()
        }
        alertDialog.setNegativeButton(
            "Cancel"
        ) { dialog1: DialogInterface, which: Int -> dialog1.dismiss() }
        alertDialog.show()
    }

    override fun onBackPressed() {
        val controller = Navigation.findNavController(this, R.id.navhostfragment)
        when (controller.currentDestination!!.id) {
            R.id.userListFragment -> {
                onBackPressedDispatcher.onBackPressed()
                ivDrawerMenu.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_baseline_menu_24
                    )
                )
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun dataClear() {
        StorageUtil(this@MainActivity).clearAll()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun getContext(): Context {
        return this
    }

    override fun enableLoadingBar(enable: Boolean, s: String?) {
        if (enable) loadProgressBar(this) else dismissProgressBar()
    }

    override fun onError(reason: String?) {
        try {
            val jObjError = JSONObject(reason)
            val msg = if (jObjError.has("Message")) jObjError.getString("Message") else ""
            Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show()
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
            binding.appBarMain.tvCount.visibility = View.VISIBLE
            binding.appBarMain.tvCount.text = body.data.size.toString()
        } else
            binding.appBarMain.tvCount.visibility = View.GONE
    }

    override fun onGetZipData(body: ResponseZipData?) {
        //No Use
    }

    override fun onStart() {
        super.onStart()
        getAppointmentList()
    }
}