package com.slve.ratingjob.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.slve.ratingjob.adapter.TransactionAdapter
import com.slve.ratingjob.model.Transaction
import com.slve.ratingjob.databinding.ActivityTransactionBinding
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransactionActivity : AppCompatActivity() {

    lateinit var binding: ActivityTransactionBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvTransactions.layoutManager = linearLayoutManager

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        swipeRefreshLayout.setOnRefreshListener { transaction(swipeRefreshLayout) }
        transaction(swipeRefreshLayout)


        setContentView(binding.root)
    }

    private fun transaction(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params[com.slve.ratingjob.helper.Constant.USER_ID] = session.getData(com.slve.ratingjob.helper.Constant.USER_ID)
        com.slve.ratingjob.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
                        val g = Gson()
                        val transaction: java.util.ArrayList<Transaction> = java.util.ArrayList<Transaction>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Transaction = g.fromJson(jsonObject1.toString(), Transaction::class.java)
                                transaction.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = TransactionAdapter(activity, transaction)
                        binding.rvTransactions.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false

                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.slve.ratingjob.helper.Constant.TRANSACTIONS_LIST, params, true)


    }
}