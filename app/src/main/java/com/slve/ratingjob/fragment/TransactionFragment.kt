package com.slve.ratingjob.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.slve.ratingjob.activity.HomeActivity
import com.slve.ratingjob.adapter.TransactionAdapter
import com.slve.ratingjob.databinding.FragmentTransactionBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.helper.Session
import com.slve.ratingjob.model.Transaction
import com.slve.ratingjob.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransactionFragment : Fragment() {


    lateinit var binding: FragmentTransactionBinding
    lateinit var activity: Activity
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        activity = getActivity() as Activity
        session = Session(activity)

        (activity as HomeActivity).binding.rlToolbar.visibility = View.VISIBLE

        session = Session(activity)
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvTransactions.layoutManager = linearLayoutManager

        swipeRefreshLayout.setOnRefreshListener { transaction(swipeRefreshLayout) }
        transaction(swipeRefreshLayout)



        return binding.root




    }


    private fun transaction(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(Constant.DATA)
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
                        DialogUtils.showCustomDialog(requireActivity(), ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.TRANSACTIONS_LIST, params, true)


    }


}