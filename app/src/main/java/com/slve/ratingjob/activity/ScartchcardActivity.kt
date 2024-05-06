package com.slve.ratingjob.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anupkumarpanwar.scratchview.ScratchView
import com.anupkumarpanwar.scratchview.ScratchView.IRevealListener
import com.slve.ratingjob.databinding.ActivityScartchcardBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ScartchcardActivity : AppCompatActivity() {

    lateinit var binding: ActivityScartchcardBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session

    var dialog: Dialog? = null

    lateinit var scratch_id:String
    lateinit var chance:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScartchcardBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }



        chance = session.getData(com.slve.ratingjob.helper.Constant.CHANCES)




        binding.scratchView.setRevealListener(object : IRevealListener {
            override fun onRevealed(scratchView: ScratchView) {
              //  Toast.makeText(this@ScartchcardActivity, "Revealed!", Toast.LENGTH_SHORT).show()
                userdetails()
                apicall1()
                scratchView.visibility = View.GONE

            }

            override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {
                Log.d("Revealed", percent.toString())
            }
        })


        if(chance.equals("0")){
            binding.ScarchImg.visibility = View.GONE
            binding.image.visibility = View.VISIBLE
            DialogUtils.showCustomDialog(activity, "No More Chances")
        }
        else{
            binding.ScarchImg.visibility = View.VISIBLE
            binding.image.visibility = View.GONE
            apicall()
        }
        userdetails()


        return setContentView(binding!!.root)
    }


    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
//        Toast.makeText(this,"" + session!!.getData(Constant.USER_ID),Toast.LENGTH_SHORT).show()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



                        session!!.setData(Constant.CHANCES, jsonArray.getJSONObject(0).getString(Constant.CHANCES))
                        binding.tvChance.text =  session.getData(com.slve.ratingjob.helper.Constant.CHANCES) + " Chances left"

                        chance = session.getData(com.slve.ratingjob.helper.Constant.CHANCES)



                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        binding.tvAmount.text = jsonObject.getString("amount")
                        scratch_id = jsonObject.getString("scratch_id")

                       Toast.makeText(this@ScartchcardActivity,jsonObject.getString(Constant.MESSAGE) , Toast.LENGTH_SHORT).show()



                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.SCRATCH_CARD, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }
    private fun apicall1() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.SCRATCH_ID] = scratch_id.toString()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        Toast.makeText(this@ScartchcardActivity,jsonObject.getString(Constant.MESSAGE) , Toast.LENGTH_SHORT).show()

                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.SCRATCH_CARD, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}

