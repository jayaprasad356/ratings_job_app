package com.slve.ratingjob.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.slve.ratingjob.adapter.SelltomarketAdapter
import com.slve.ratingjob.databinding.ActivitySelltomarketBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.helper.Session
import com.slve.ratingjob.model.Selltomarket
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SelltomarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelltomarketBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelltomarketBinding.inflate(layoutInflater)
        activity = this
        session = Session(activity)

        session.setData(Constant.MARKET_ID, "market_id")


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvMarket.layoutManager = linearLayoutManager


        val plan_id = intent.getStringExtra("plan_id")
        val products = intent.getStringExtra("products")
        val daily_quantity = intent.getStringExtra("daily_quantity")
        val unit = intent.getStringExtra("unit")
        val daily_income = intent.getStringExtra("daily_income")

//        binding.tvProductName.text = products


        binding.btnSell.setOnClickListener {


            val Intent  = Intent(activity, DataEntryActivity::class.java)
            startActivity(Intent)


        }



        market(plan_id)


        setContentView(binding.root)


    }



    private fun market(id: String?) {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = id!!
        ApiConfig.RequestToVolley({ result, response ->

            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    val msg = jsonObject.getString(Constant.MESSAGE).toString()
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
                        val g = Gson()
                        val selltomarket: java.util.ArrayList<Selltomarket> =
                            java.util.ArrayList<Selltomarket>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Selltomarket = g.fromJson(jsonObject1.toString(), Selltomarket::class.java)
                                selltomarket.add(group)
                            } else {
                                break
                            }
                        }
                        val plan_id = intent.getStringExtra("plan_id")
                        session.setData(Constant.PLAN_ID,plan_id)



                        session.setData(Constant.SLOT_ID, jsonArray.getJSONObject(0).getString("id"))
                    //    Toast.makeText(activity, "" + jsonArray.getJSONObject(0).getString("id"), Toast.LENGTH_SHORT).show()
                        session.setData(Constant.TOTAL_RATINGS,jsonArray.getJSONObject(0).getString("total_ratings") )
                        session.setData(Constant.TOTAL_INCOME,jsonArray.getJSONObject(0).getString("total_income") )


                        //important
                        val adapter = SelltomarketAdapter(activity,selltomarket)
                        binding.rvMarket.adapter = adapter


                    } else {

                        Toast.makeText(activity, "" + jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }, activity, Constant.SLOTS_LIST, params, true)


    }



    private fun apicall(id: String?) {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = id!!
        params["markets_id"] = session.getData(Constant.MARKET_ID)!!
//        Toast.makeText(activity, "" +session.getData(Constant.MARKET_ID), Toast.LENGTH_SHORT).show()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    val msg = jsonObject.getString(Constant.MESSAGE).toString()
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)

                        session.setData(Constant.MARKET_ID, "market_id")

                        Toast.makeText(activity, "" + jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, MyProductionActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(activity, "" + jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }, activity, Constant.CLAIM, params, true)


    }

}