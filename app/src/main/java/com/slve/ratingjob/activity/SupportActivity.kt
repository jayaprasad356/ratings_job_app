package com.slve.ratingjob.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.slve.ratingjob.databinding.ActivitySupportBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SupportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySupportBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySupportBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        ZohoSalesIQ.Chat.show()
        val initConfig = InitConfig()

        ZohoSalesIQ.init(application, "ZsXqACvPGa%2BGFoj7gbPHf1Bxn0kvISRLwip%2FMt2StwEhomu%2FRYhphYjJCa1ydcGE_in", "besnHXXmUHNWu67FrvYAs5G90%2FjiQyqrVlaR5l85SjIdLsbSN0tMHDZTTe7XTSQz6ITrP3thJxlwPWNnExduHLQHXlPhFoqjNLHrFoWmYj6Ib7G3s%2FLXJw%3D%3D", initConfig, object :
            InitListener {
            override fun onInitSuccess() {
                // fit place to show the chat launcher
//                ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)

            }

            override fun onInitError(errorCode: Int, errorMessage: String) {
                // Handle initialization errors
            }



        })

        // zohoSalesIQ open chat


        // Set your font configurations










        return setContentView(binding!!.root)
    }

    override fun onResume() {
        super.onResume()
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)
    }





}