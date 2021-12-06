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


class LoginActivity : BaseActivity(), ILoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var verifyDialog: BottomSheetDialog
    private lateinit var bsDialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginPresenter = LoginPresenter()
        loginPresenter.view = this

        val text =
            "<font color=#000000>We are just a call away… <br>Call us on</font> <font color=#FF725E> +1–6661777 </font><font color=#000000>for any support</font>"
        binding.tvSupport.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY);

        binding.llSignUp.setOnClickListener {
            val openMainActivity = Intent(this@LoginActivity, ChooseUserActivity::class.java)
            startActivity(openMainActivity)
        }

        binding.tvForgotPass.setOnClickListener {
            val openMainActivity = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(openMainActivity)
        }

        binding.btnLogin.setOnClickListener {
//            val openMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(openMainActivity)
            if (binding.edtMobile.text.toString().isEmpty())
                Toast.makeText(this, "Please enter your register mobile number.", Toast.LENGTH_SHORT).show()
            else if (binding.edtPassword.text.toString().isEmpty())
                Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
            else
                callLogin()
        }
    }

    private fun callLogin() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("userid", binding.edtMobile.text.toString())
                jsonParams.put("password", binding.edtPassword.text.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            loginPresenter.loginUser(this, jsonParams)
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
        Toast.makeText(this, "" + body.message, Toast.LENGTH_SHORT).show()
        if (body.data != null) {
            StorageUtil(this).saveUserDetail(body.data)
            val openMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
            openMainActivity.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(openMainActivity)
        }
    }

    override fun onSendSuccess(body: ResponseDefaultData?) {
        //no use here
    }
}