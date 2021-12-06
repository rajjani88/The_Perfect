package com.appdata.theperfect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.databinding.ActivityRegisterTwoBinding
import com.appdata.theperfect.interfaces.IRegisterView
import com.appdata.theperfect.model.ResponseCountryData
import com.appdata.theperfect.model.ResponseOauthLogin
import com.appdata.theperfect.presenter.RegisterPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.util.PaymentsUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.moneris.googlepay.api.Constants
import com.moneris.googlepay.api.MonerisHttpsPost
import com.moneris.googlepay.api.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*



class RegisterTwoActivity : BaseActivity(), IRegisterView {

    private lateinit var binding: ActivityRegisterTwoBinding
    private lateinit var registerPresenter: RegisterPresenter

    private lateinit var data: String
    var jsonParams = JSONObject()


    //gpay
    private val SHIPPING_COST_CENTS = 0 * PaymentsUtil.CENTS.toLong()

    private lateinit var paymentsClient: PaymentsClient

    private lateinit var garmentList: JSONArray

    private lateinit var selectedGarment: JSONObject

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_two)

        if (intent.hasExtra("data")) {
            data = intent.getStringExtra("data").toString()
            jsonParams = JSONObject(data)
            println(jsonParams.toString())
        }

        if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit") {
            setEditData()
        }

        registerPresenter = RegisterPresenter()
        registerPresenter.view = this

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        //for paynow
        // Set up the mock information for our item in the UI.
        //selectedGarment = fetchRandomGarment()
        //displayGarment(selectedGarment)

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()

        //googlePayButton.setOnClickListener { requestPayment() }

        binding.btnPayNow.setOnClickListener {
            requestPayment()
           // onPayNowClick()

        }

        binding.btnSubmit.setOnClickListener {
            if (binding.edtBusiness.text.toString().isEmpty())
                Toast.makeText(this, "Please enter business name.", Toast.LENGTH_SHORT).show()
            else if (binding.edtProductName.text.toString().isEmpty())
                Toast.makeText(this, "Please enter product name.", Toast.LENGTH_SHORT).show()
            else if (!binding.checkboxTnc.isChecked)
                Toast.makeText(this, "Please check term and conditions.", Toast.LENGTH_SHORT).show()
            else if (binding.edtContactDetail.text.toString().isEmpty())
                Toast.makeText(this, "Please enter emergency contact details.", Toast.LENGTH_SHORT)
                    .show()
            else
                callRegister()
        }
    }

    //gpay section
    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            binding.btnPayNow.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        binding.btnPayNow.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val garmentPrice = 9.99
        val priceCents =
            Math.round(garmentPrice * PaymentsUtil.CENTS.toLong())
        //+ SHIPPING_COST_CENTS
        iamLog("sub price : $priceCents")

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
//                        data?.let { intent ->
//                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
//                        }
                        data?.let { handleMoneris(it) }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
                binding.btnPayNow.isClickable = true
            }
        }
    }

    //moneris
    fun handleMoneris(data: Intent) {
        val paymentData = PaymentData.getFromIntent(data)
        if (paymentData == null) {
            iamLog("payment data null")
        }

        try {

            val purchase = Request.Builder()
                .setTransactionType(Constants.TransactionCode.PURCHASE)
                .setOrderId("dummy" + Date().time)
                .setPaymentData(paymentData!!)
                .setAmount("9.99")
                .setCustId("cust1")
                .build()

            //.setRecur(recurInfo)
            //transection transport structure

            val monerisHttpsPost = MonerisHttpsPost.Builder()
               //test mode
               // .setStoreId("store5")
               // .setApiToken("yesguy")

               //productions
                .setStoreId("monatb1769")
                .setApiToken("mqHV69RmOBODEJfy68Kj")
                .setRequest(purchase)
                .setTestMode(true)
                .build()

            //send transection
            val response = monerisHttpsPost.send()

            //check transaction
            iamLog("response : ${response.message}")
            val numScope =  response.responseCode.toInt()
            if (numScope < 50){
                //approved
                iamLog("success")
                handlePaymentSuccess(paymentData)
            }else{
                //decline
                handleError(numScope)
            }

        } catch (e: Exception) {
            iamLog("error :" +e.message!!)
            handleError(-1)
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(
                this,
                getString(R.string.payments_show_name, billingName),
                Toast.LENGTH_LONG
            ).show()

            // Logging token string.
            Log.d(
                "GooglePaymentToken", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    private fun handleError(statusCode: Int) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    //gpay end


    //subscription click
    fun onPayNowClick(){

    }

    fun iamLog(string: String){
        Log.e("iamLog/RTA", string )
    }
    private fun setEditData() {
        val modelUser = StorageUtil(this).userDetail
        binding.edtBusiness.setText(modelUser.businessName)
        binding.edtProductName.setText(modelUser.productName)
        binding.edtContactDetail.setText(modelUser.emergencyContactNo)
    }

    private fun callRegister() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            try {
                jsonParams.put("businessname", binding.edtBusiness.text.toString())
                jsonParams.put("productname", binding.edtProductName.text.toString())
                jsonParams.put("businesslicence", "123456")
                jsonParams.put("emergencycontact", binding.edtContactDetail.text.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit") {
                val modelUser = StorageUtil(this).userDetail
                jsonParams.put("id", modelUser.loginId)
                registerPresenter.updateUser(this, jsonParams)
            }else
                registerPresenter.register(this, jsonParams)
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.msg_no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }
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

    override fun onRegisterSuccess(body: ResponseOauthLogin?) {
        Toast.makeText(this, "" + body!!.message, Toast.LENGTH_SHORT).show()
        if (body.data != null) {
            StorageUtil(this).saveUserDetail(body.data)
            val openMainActivity = Intent(this@RegisterTwoActivity, MainActivity::class.java)
            startActivity(openMainActivity)
            finishAffinity()
        }
    }

    override fun onGetOtherData(body: ResponseOauthLogin?) {

    }
    override fun onGetLocationData(body: ResponseCountryData?) {
        //no use in this activity
    }

}