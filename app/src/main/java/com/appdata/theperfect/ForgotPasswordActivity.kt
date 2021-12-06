package com.appdata.theperfect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.databinding.ActivityForgotPasswordBinding
import com.appdata.theperfect.databinding.ActivityLoginBinding
import com.appdata.theperfect.interfaces.ILoginView
import com.appdata.theperfect.model.ResponseDefaultData
import com.appdata.theperfect.model.ResponseOauthLogin
import com.appdata.theperfect.presenter.LoginPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import com.appdata.theperfect.utils.ValidationMethod
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject


class ForgotPasswordActivity : BaseActivity(), ILoginView {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        loginPresenter = LoginPresenter()
        loginPresenter.view = this

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSend.setOnClickListener {
            if (binding.edtMobile.text.toString().isEmpty())
                Toast.makeText(this, "Please enter your register email address.", Toast.LENGTH_SHORT).show()
            else
                sendMail()
        }
    }

    private fun sendMail() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("email", binding.edtMobile.text.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            loginPresenter.forgotPassword(this, jsonParams)
        } else {
            Toast.makeText(this, resources.getString(R.string.msg_no_network), Toast.LENGTH_SHORT)
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

    override fun onLoginSuccess(body: ResponseOauthLogin) {
        // no use here
    }

    override fun onSendSuccess(body: ResponseDefaultData?) {
        Toast.makeText(this, body!!.message, Toast.LENGTH_SHORT)
            .show()
        finish()
    }
}