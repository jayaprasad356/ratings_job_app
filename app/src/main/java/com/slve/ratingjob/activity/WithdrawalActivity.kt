package com.slve.ratingjob.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.slve.ratingjob.R
import com.slve.ratingjob.databinding.ActivityWithdrawalBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.StrictMath.round

class WithdrawalActivity : AppCompatActivity() {

    lateinit var binding: ActivityWithdrawalBinding
    private lateinit var activity: Activity
    private lateinit var session: com.slve.ratingjob.helper.Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)


        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvTotalEarnings.text = "Balance ₹ " +session.getData(Constant.BALANCE)



//        binding.llbank.setOnClickListener {
//            val intent = Intent(activity, com.slve.ratingjob.activity.UpdatebankActivity::class.java)
//            startActivity(intent)
//        }

        binding.btnWithdraw.setOnClickListener {
            if (binding.etAmount.text.toString().isEmpty()) {
                binding.etAmount.error = "Please enter amount"
                binding.etAmount.requestFocus()
            } else {
                withdraw()
            }
        }
//
//        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        binding.rvHistory.layoutManager = linearLayoutManager


       // swipeRefreshLayout.setOnRefreshListener { history(swipeRefreshLayout) }

      //  history(swipeRefreshLayout)
        apicall()

        setContentView(binding.root)


    }
    override fun onStart() {
        super.onStart()

        // show the keyboard if has focus
        currentFocus?.let {
            showSoftKeyboard(it)
        }
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            // here is one more tricky issue
            // imm.showSoftInputMethod doesn't work well
            // and imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0) doesn't work well for all cases too
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)

                        val withdrawal_ins = jsonArray.getJSONObject(0).getString("withdrawal_ins")

                        val withdrawalHtml = Html.fromHtml(withdrawal_ins, Html.FROM_HTML_MODE_COMPACT)
                        binding.tvWithdraw.text = withdrawalHtml


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


//    private fun history(swipeRefreshLayout:SwipeRefreshLayout) {
//    val params: MutableMap<String, String> = HashMap()
//        params["user_id"] = session.getData(Constant.USER_ID)
//        com.slve.ratingjob.helper.ApiConfig.RequestToVolley({ result, response ->
//            if (result) {
//                try {
//                    val jsonObject = JSONObject(response)
//                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
//                        val jsonArray = jsonObject.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
//
//                        val g = Gson()
//                        val withdrawal: java.util.ArrayList<Withdrawal> =
//                            java.util.ArrayList<Withdrawal>()
//                        for (i in 0 until jsonArray.length()) {
//                            val jsonObject1 = jsonArray.getJSONObject(i)
//                            if (jsonObject1 != null) {
//                                val group: Withdrawal = g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
//                                withdrawal.add(group)
//                            } else {
//                                break
//                            }
//                        }
//
//                        val adapter = WithdrawalAdapter(activity, withdrawal)
//                        binding.rvHistory.adapter = adapter
//                        swipeRefreshLayout.isRefreshing = false
//
//
//                    } else {
//                        swipeRefreshLayout.isRefreshing = false
//                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            } else {
//                Toast.makeText(activity, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
//            }
//        }, activity, com.slve.ratingjob.helper.Constant.WITHDRAWAL_LIST, params, true)
//    }

    private fun withdraw() {

        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(com.slve.ratingjob.helper.Constant.USER_ID)
        params["amount"] = binding.etAmount.text.toString()
        com.slve.ratingjob.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.slve.ratingjob.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.slve.ratingjob.helper.Constant.DATA)
                        session.setData(
                            com.slve.ratingjob.helper.Constant.BALANCE, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.BALANCE))
                        session.setData(
                            com.slve.ratingjob.helper.Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(
                                com.slve.ratingjob.helper.Constant.TOTAL_WITHDRAWAL))
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        moveToHomeActivity();
                    } else
                    {


                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.slve.ratingjob.helper.Constant.WITHDRAWALS, params, true)


    }

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setOnDismissListener {
            // This code will be executed when the dialog is dismissed
            moveToHomeActivity()
        }
        dialog.show()
    }

    private fun moveToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun calculateFivePercent(amount: Float?): Int {
        // Check if amount is not null
        val fivePercent = amount?.times(0.05f)?.toDouble() ?: 0.0
        // Round off to the nearest integer
        return round(fivePercent).toInt()
    }

    private fun showKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.etAmount, InputMethodManager.SHOW_IMPLICIT)
    }

}