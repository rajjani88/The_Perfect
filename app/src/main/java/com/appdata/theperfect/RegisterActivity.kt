package com.appdata.theperfect

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import com.appdata.theperfect.adapter.MyAdapter
import com.appdata.theperfect.databinding.ActivityRegisterBinding
import com.appdata.theperfect.interfaces.ICategoryView
import com.appdata.theperfect.interfaces.IRegisterView
import com.appdata.theperfect.model.*
import com.appdata.theperfect.presenter.CategoryPresenter
import com.appdata.theperfect.presenter.RegisterPresenter
import com.appdata.theperfect.storage.StorageUtil
import com.appdata.theperfect.utils.NetworkAlertUtility
import com.appdata.theperfect.utils.ValidationMethod
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : BaseActivity(), IRegisterView, ICategoryView {

    private lateinit var binding: ActivityRegisterBinding
    private var role: String = "1"
    private lateinit var registerPresenter: RegisterPresenter
    private lateinit var categoryPresenter: CategoryPresenter
    private lateinit var arrayList: ArrayList<ModelCategoryData>
    private lateinit var myAdapter: MyAdapter
    private lateinit var arrayLocationData: ArrayList<ModelInnerCountryData>
    private lateinit var arrayCountry: ArrayList<ModelInnerCountryData>
    private lateinit var arrayTown: ArrayList<ModelInnerCountryData>
    private lateinit var arrayProvince: ArrayList<ModelInnerCountryData>
    private lateinit var arrayZip: ArrayList<ModelInnerCountryData>
    private var selectedCountry: Int = -1
    private var selectedTown: Int = -1
    private var selectedProvince: Int = -1
    private var selectedZip: Int = -1
    private var selected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        registerPresenter = RegisterPresenter()
        registerPresenter.view = this

        categoryPresenter = CategoryPresenter()
        categoryPresenter.view = this

        arrayLocationData = ArrayList()
        arrayCountry = ArrayList()
        arrayTown = ArrayList()
        arrayProvince = ArrayList()
        arrayZip = ArrayList()

        getCountryData()

        binding.btnSubmit.setOnClickListener {
//            val intent = Intent(this@RegisterActivity, RegisterTwoActivity::class.java)
//            startActivity(intent)
            onSubmitData()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }



        arrayList = ArrayList()

        myAdapter = MyAdapter(
            this@RegisterActivity, 0,
            arrayList, MyAdapter.OnItemClick {
                selected = ""
                for (i in 0 until arrayList.size) {
                    if (arrayList[i].isSelected) {
                        selected = selected + "," + arrayList[i].categoryId
                    }
                }
                Log.e("selected:-->", "" + selected)
                if (selected.startsWith(","))
                    selected = selected.substring(1);
                println("" + selected)
            }
        )
        binding.categorySpinner.adapter = myAdapter

        if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit") {
            setEditData()
        }

//        if(!binding.categorySpinner.hasBeenOpened()){
//            Toast.makeText(this,"spinner closed",Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(this,"spinner opened",Toast.LENGTH_SHORT).show()
//        }

        /*  binding.categorySpinner.onItemClickListener =
              AdapterView.OnItemClickListener { parent, view, position, id ->

                  val selected = ""
                  for (i in 0 until arrayList.size) {
                      if (arrayList[i].isSelected) {
                          println(selected + "," + arrayList[i].categoryName)
                      }
                  }
              }*/

//        binding.categorySpinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    Toast.makeText(this@RegisterActivity, "Something changed", LENGTH_SHORT).show()
//                } // to close the onItemSelected
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//            }

    }

    private fun setEditData() {

        binding.inputLayoutPassword.visibility = View.GONE
        binding.inputLayoutConfirmPassword.visibility = View.GONE
        binding.edtEmail.isEnabled=false
        binding.edtMnumber.isEnabled=false

        binding.inputLayoutNumber.setBackgroundColor(resources.getColor(R.color.bgEditNumber))
        binding.inputLayoutEmail.setBackgroundColor(resources.getColor(R.color.bgEditNumber))

        val modelUser = StorageUtil(this).userDetail
        binding.edtEmail.setText(modelUser.emailID)
        binding.edtName.setText(modelUser.userName)
        binding.edtMnumber.setText(modelUser.mobileNo)
        binding.edtPassword.setText(modelUser.password)
        binding.edtOccupation.setText(modelUser.occupation)
        binding.edtAddress.setText(modelUser.address)
        binding.countryDropdown.setText(modelUser.country)
        binding.townDropdown.setText(modelUser.town)
        binding.provinceDropdown.setText(modelUser.province)
        binding.zipDropdown.setText(modelUser.zipCode)

        if(modelUser.loopkingFor!=null) {
            selected = modelUser.loopkingFor
            if (selected != null && selected.isNotEmpty() && role == "2") {
                val arr = selected.split(",")
                for (i in arr.indices) {
                    var a = arr[i].toInt()
                    for (j in 0 until arrayList.size) {
                        if (a == arrayList[j].categoryId) {
                            arrayList[j].isSelected = true
                        }
                    }
                }
            }
        }
    }

    private fun getCategoryData() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            val jsonParams = JSONObject()
            try {
                jsonParams.put("status", "1")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            categoryPresenter.categoryData(this, jsonParams)
        } else {
            Toast.makeText(this, resources.getString(R.string.msg_no_network), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getCountryData() {
        if (NetworkAlertUtility.isConnectingToInternet(this)) {
            registerPresenter.getCountryData(this)
        } else {
            Toast.makeText(this, resources.getString(R.string.msg_no_network), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onSubmitData() {
        if (binding.edtEmail.text.toString().isEmpty())
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
        else if (!ValidationMethod.isValidEmail(binding.edtEmail.text.toString().trim()))
            Toast.makeText(this, "Please enter valid email.", Toast.LENGTH_SHORT).show()
        else if (binding.edtName.text.toString().isEmpty())
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
        else if (binding.edtMnumber.text.toString().isEmpty())
            Toast.makeText(this, "Please enter your mobile number.", Toast.LENGTH_SHORT).show()
        else if (binding.edtPassword.text.toString()
                .isEmpty() && (intent.hasExtra("from") && intent.getStringExtra("from") != "edit")
        )
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show()
        else if (binding.edtPassword.text.toString().length < 6 && (intent.hasExtra("from") && intent.getStringExtra(
                "from"
            ) != "edit")
        )
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT)
                .show()
        else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString() && (intent.hasExtra(
                "from"
            ) && intent.getStringExtra("from") != "edit")
        )
            Toast.makeText(this, "Password an confirm password should be same.", Toast.LENGTH_SHORT)
                .show()
        else if (binding.edtAddress.text.toString().isEmpty())
            Toast.makeText(this, "Please enter your address.", Toast.LENGTH_SHORT).show()
        else if (binding.edtOccupation.text.toString().isEmpty())
            Toast.makeText(this, "Please choose your occupation.", Toast.LENGTH_SHORT).show()
        else if (selected.isEmpty() && role == "2")
            Toast.makeText(this, "Please select category.", Toast.LENGTH_SHORT).show()
        else if (binding.countryDropdown.text.toString().isEmpty())
            Toast.makeText(this, "Please choose your country.", Toast.LENGTH_SHORT).show()
        else if (binding.townDropdown.text.toString().isEmpty())
            Toast.makeText(this, "Please choose your town.", Toast.LENGTH_SHORT).show()
        else if (binding.provinceDropdown.text.toString().isEmpty())
            Toast.makeText(this, "Please choose your province.", Toast.LENGTH_SHORT).show()
        else if (binding.zipDropdown.text.toString().isEmpty())
            Toast.makeText(this, "Please choose your area zip.", Toast.LENGTH_SHORT).show()
        else {
            val jsonParams = JSONObject()
            jsonParams.put("role", role.toString())
            jsonParams.put("email", binding.edtEmail.text.toString())
            jsonParams.put("username", binding.edtName.text.toString())
            jsonParams.put("phone", binding.edtMnumber.text.toString())
            jsonParams.put("alternatemobile", binding.edtMnumber.text.toString())
            jsonParams.put("password", binding.edtPassword.text.toString())
            jsonParams.put("address", binding.edtAddress.text.toString())
            jsonParams.put("occupation", binding.edtOccupation.text.toString())
            jsonParams.put("category_id", selected)
            jsonParams.put("country", binding.countryDropdown.text.toString())
            jsonParams.put("town", binding.townDropdown.text.toString())
            jsonParams.put("province", binding.provinceDropdown.text.toString())
            jsonParams.put("zipcode", binding.zipDropdown.text.toString())
            if (role == "2") {
                //Seller
                val intentttttt = Intent(this@RegisterActivity, RegisterTwoActivity::class.java)
                jsonParams.put("lookingfor", selected)
                if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit")
                    intentttttt.putExtra("from", "edit")
                intentttttt.putExtra("data", jsonParams.toString())
                startActivity(intentttttt)
            } else {
                if (NetworkAlertUtility.isConnectingToInternet(this)) {
                    if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit") {
                        val modelUser = StorageUtil(this).userDetail
                        jsonParams.put("id", modelUser.loginId)
                        registerPresenter.updateUser(this, jsonParams)
                    } else {
                        registerPresenter.register(this, jsonParams)
                    }
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.msg_no_network),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
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

    override fun onGetCategoryData(body: ResponseCategoryData?) {
        if (body!!.data != null && body.data.size > 0) {
            arrayList.addAll(body.data)
            val modelCategoryData = ModelCategoryData()
            modelCategoryData.categoryName = "Select Category"
            arrayList.add(0, modelCategoryData)
            myAdapter.notifyDataSetChanged()
            if (intent.hasExtra("from") && intent.getStringExtra("from") == "edit") {
                setEditData()
            }
            //   homeCategoryAdapter.setData(arrayList)
        }
    }

    override fun onRegisterSuccess(body: ResponseOauthLogin?) {
        Toast.makeText(this, "" + body!!.message, Toast.LENGTH_SHORT).show()
        if (body.data != null) {
            StorageUtil(this).saveUserDetail(body.data)
            val openMainActivity = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(openMainActivity)
        }
    }

    override fun onGetOtherData(body: ResponseOauthLogin?) {

    }

    override fun onGetLocationData(body: ResponseCountryData?) {
        if (body != null) {
            arrayLocationData.clear()
            arrayLocationData.addAll(body.data.data)
            setCountryData()
            //Toast.makeText(this, body.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCountryData() {
        arrayCountry.clear()
        for (i in 0 until arrayLocationData.size) {
            if (arrayLocationData[i].type == 1)
                arrayCountry.add(arrayLocationData[i])
        }
        if (arrayCountry.size > 0) {
            if (intent.hasExtra("role")) {
                role = intent.getStringExtra("role")!!
                if (role == "1")
                    binding.categorySpinner.visibility = View.GONE
                else
                    getCategoryData()
            }
            val adapterCountry = ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text1,
                arrayCountry
            )
            binding.countryDropdown.setAdapter(adapterCountry)
        } else {
            Toast.makeText(this, "No data available.", Toast.LENGTH_SHORT).show()
        }

        binding.countryDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.adapter.getItem(position) as ModelInnerCountryData
                selectedCountry = selectedItem.id
                println(selectedCountry)
                setTownData()
            }

    }

    private fun setTownData() {
        arrayTown.clear()
        for (i in 0 until arrayLocationData.size) {
            if (arrayLocationData[i].parentid == selectedCountry && arrayLocationData[i].type == 2)
                arrayTown.add(arrayLocationData[i])
        }
        if (arrayTown.size > 0) {
            val adapterCountry = ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text1,
                arrayTown
            )
            binding.townDropdown.setAdapter(adapterCountry)
        } else {
            Toast.makeText(this, "No data available.", Toast.LENGTH_SHORT).show()
        }

        binding.townDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.adapter.getItem(position) as ModelInnerCountryData
                selectedTown = selectedItem.id
                println(selectedTown)
                setProvinceData()
            }
    }

    private fun setProvinceData() {
        arrayProvince.clear()
        for (i in 0 until arrayLocationData.size) {
            if (arrayLocationData[i].parentid == selectedTown && arrayLocationData[i].type == 3)
                arrayProvince.add(arrayLocationData[i])
        }
        if (arrayProvince.size > 0) {
            val adapterCountry = ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text1,
                arrayProvince
            )
            binding.provinceDropdown.setAdapter(adapterCountry)
        } else {
            Toast.makeText(this, "No data available.", Toast.LENGTH_SHORT).show()
        }

        binding.provinceDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.adapter.getItem(position) as ModelInnerCountryData
                selectedProvince = selectedItem.id
                println(selectedProvince)
                setZipData()
            }
    }

    private fun setZipData() {
        arrayZip.clear()
        for (i in 0 until arrayLocationData.size) {
            if (arrayLocationData[i].parentid == selectedProvince && arrayLocationData[i].type == 4)
                arrayZip.add(arrayLocationData[i])
        }
        if (arrayZip.size > 0) {
            val adapterCountry = ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text1,
                arrayZip
            )
            binding.zipDropdown.setAdapter(adapterCountry)
        } else {
            Toast.makeText(this, "No data available.", Toast.LENGTH_SHORT).show()
        }

        binding.zipDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItem = parent.adapter.getItem(position) as ModelInnerCountryData
                selectedZip = selectedItem.id
                println(selectedZip)
                //setTownData()
            }
    }

}