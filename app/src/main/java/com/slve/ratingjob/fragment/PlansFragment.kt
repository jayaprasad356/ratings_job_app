package com.slve.ratingjob.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.slve.ratingjob.adapter.MyplansAdapter
import com.slve.ratingjob.model.MyPlan
import com.slve.ratingjob.databinding.FragmentPlansBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PlansFragment : Fragment() {

    lateinit var binding: FragmentPlansBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlansBinding.inflate(inflater, container, false)

        activity = getActivity() as Activity
        session = com.slve.ratingjob.helper.Session(activity)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvPlans.layoutManager = linearLayoutManager

        myplans()


        return binding.root

    }

    private fun myplans() {


//        var myPlans = ArrayList<MyPlan>()
//
//        myPlans.add(MyPlan("1. ","Paddy","Video - 04:55 mins","Rs.180","Rs.2", "Rs.100", "90 days"))
//          myPlans.add(MyPlan("2. ","Wheat","Video - 04:55 mins","Rs.180","Rs.2", "Rs.100", "90 days"))
//        myPlans.add(MyPlan("3. ","Maize","Video - 04:55 mins","Rs.180","Rs.2", "Rs.100", "90 days"))
//
//        val adapter = MyplansAdapter(activity, myPlans)
//        binding.rvPlans.adapter = adapter


        val params: MutableMap<String, String> = HashMap()
        params[com.slve.ratingjob.helper.Constant.USER_ID]= session.getData(com.slve.ratingjob.helper.Constant.USER_ID)
        com.slve.ratingjob.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
                        val g = Gson()
                        val myPlans: java.util.ArrayList<MyPlan> =
                            java.util.ArrayList<MyPlan>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyPlan = g.fromJson(jsonObject1.toString(), MyPlan::class.java)
                                myPlans.add(group)
                            } else {
                                break
                            }
                        }
                      //  Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = MyplansAdapter(activity, myPlans)
                        binding.rvPlans.adapter = adapter

                    } else {
                        Toast.makeText(
                            getActivity(), "" + jsonObject.getString(com.slve.ratingjob.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.slve.ratingjob.helper.Constant.PLAN_LIST, params, true)



    }


}