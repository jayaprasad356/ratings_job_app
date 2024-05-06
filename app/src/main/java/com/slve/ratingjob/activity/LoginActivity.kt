package com.slve.ratingjob.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.slve.ratingjob.R
import com.slve.ratingjob.databinding.ActivityLoginBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {


    lateinit var binding: ActivityLoginBinding

    private lateinit var activity: Activity
    private lateinit var session: com.slve.ratingjob.helper.Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)


        binding.tvSignin.setOnClickListener {
            startActivity(Intent(this, ProfiledetailsActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgototpActivity::class.java))
        }


        setupViews()


        setContentView(binding.root)


    }

    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            if (binding.etPhoneNumber.text.toString().isEmpty()) {
                binding.etPhoneNumber.error = "Please enter mobile number"
                binding.etPhoneNumber.requestFocus()
            } else if (binding.etPhoneNumber.text.toString().length != 10) {
                binding.etPhoneNumber.error = "Please enter valid mobile number"
                binding.etPhoneNumber.requestFocus()
            }else if (binding.etPassword.text.toString().length <= 3){
                binding.etPassword.error = "Password minimum 4 letters"
                binding.etPassword.requestFocus()
            }
            else {
                login()
            }
        }
    }


    private fun login() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.MOBILE] = binding.etPhoneNumber.text.toString().trim()
        params[Constant.DEVICE_ID] =  Constant.getDeviceId(activity)
        params[Constant.PASSWORD] = binding.etPassword.text.toString().trim()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
                        //Toast.makeText(activity, jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        session!!.setBoolean("is_logged_in", true)
                        session!!.setData(
                            com.slve.ratingjob.helper.Constant.USER_ID, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.ID))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.NAME, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.NAME))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.MOBILE, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.MOBILE))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.EMAIL, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.EMAIL))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.AGE, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.AGE))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.CITY, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.CITY))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.STATE, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.STATE))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.REFER_CODE, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.REFER_CODE))


                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = jsonObject.getString(Constant.MESSAGE)

                        if (message.equals("Your Mobile Number is not Registered")) {
                            Toast.makeText(this,""+jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE),Toast.LENGTH_SHORT).show()
                          //  DialogUtils.showCustomDialog(this, ""+jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE))
                            val intent = Intent(this, ProfiledetailsActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            DialogUtils.showCustomDialog(this, ""+jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE))
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                DialogUtils.showCustomDialog(this, ""+java.lang.String.valueOf(response) + java.lang.String.valueOf(result))

            }
        }, this, com.slve.ratingjob.helper.Constant.LOGIN, params, true)


    }




}