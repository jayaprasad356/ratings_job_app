package com.slve.ratingjob.activity

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.slve.ratingjob.R
import com.slve.ratingjob.databinding.ActivityDataEntryBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DataEntryActivity : AppCompatActivity() {

    lateinit var binding: ActivityDataEntryBinding

    private lateinit var activity: Activity
    private lateinit var session: com.slve.ratingjob.helper.Session
    private var TOTAL_COUNT: Int = 0
    private var count = 0 // Initial count value
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entry)
        binding = ActivityDataEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)
        //tvTitle text is double color text "We" and "Agri"
        val tvTitle = "<font color='#105685'>Rating</font> " + "<font color='#f92530'>Job</font>"

         TOTAL_COUNT = session.getData(Constant.TOTAL_RATINGS).toString().toInt()


        apicall()


        binding.tvTitle.text = Html.fromHtml(tvTitle)



        binding.tvCopy.setOnClickListener(View.OnClickListener {
            val text = binding.tvName.text.toString()
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
            binding.tvPaste.visibility = View.VISIBLE

        })

        binding.tvPaste.setOnClickListener(View.OnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip
            val item = clipData!!.getItemAt(0)
            val text = item.text.toString()
            binding.edName.setText(text)
            binding.tvPaste.visibility = View.GONE
        })



        binding.tvCount.text = getString(R.string.count_text, count, TOTAL_COUNT)

        // Set OnClickListener for the submit button
        binding.btnGenerate.setOnClickListener {
            // Increment the count


            if (binding.ratingBar.rating == 0f) {
                // Display a message or handle the situation accordingly
                // For example, you can show a Toast message:
                Toast.makeText(this, "Please rate the product", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.edName.text.toString().isEmpty()) {
                // Display a message or handle the situation accordingly
                // For example, you can show a Toast message:
                Toast.makeText(this, "Please enter the Review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // is not equal edname to  tvname
            else if (binding.edName.text.toString() != binding.tvName.text.toString()) {
                Toast.makeText(this, "Please enter the Correct Review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            count++


            if (count == TOTAL_COUNT){
              //  Toast.makeText(this, "$count $TOTAL_COUNT", Toast.LENGTH_SHORT).show()

                // Reset the count if it exceeds the  total count
//                count = 1
//                binding.tvCount.text = getString(R.string.count_text, count, TOTAL_COUNT)

                // Show custom dialog

                dilogend()


            }

            else if (count <= TOTAL_COUNT) {

               // Toast.makeText(this, "$count $TOTAL_COUNT", Toast.LENGTH_SHORT).show()
                // Update the count text
                binding.tvCount.text = getString(R.string.count_text, count, TOTAL_COUNT)
                binding.ratingBar.rating = 0f
                binding.edName.setText("")

                dilog()

            }
        }


    }


    companion object {
     // Total count value
    }


    private fun dilogend() {

        apicall2()
        // handler 3 sec and move to next activity
        val handler = android.os.Handler()
        handler.postDelayed({
            val Intent = Intent(activity, HomeActivity::class.java)
            startActivity(Intent)
            finish()
        }, 3000)

       // custom Dialog  Box
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        dialog.setCancelable(false)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()




    }
    private fun dilog() {

        val dialog = Dialog(activity)
        // handler 3 sec and move to next activity
        val handler = android.os.Handler()
        handler.postDelayed({
            binding.tvCount.text = getString(R.string.count_text, count, TOTAL_COUNT)
            binding.ratingBar.rating = 0f
            binding.edName.setText("")
            dialog.dismiss()
        }, 2000)

       // custom Dialog  Box
        apicall()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        dialog.setCancelable(false)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()


    }

    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params["plan_id"] = session.getData(Constant.PLAN_ID)!!
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)



                        val id = jsonArray.getJSONObject(0).getString("id")
                        val name = jsonArray.getJSONObject(0).getString("name")
                        val image = jsonArray.getJSONObject(0).getString("image")
                        val review = jsonArray.getJSONObject(0).getString("review")


                        Glide.with(activity)
                            .load(image)
                            .placeholder(R.drawable.product_ic)
                            .error(R.drawable.product_ic)
                            .into(binding.ivProduct)

                        binding.tvProductName.text = name
                        binding.tvName.text = review






                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.PRODUCT_LIST, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }
    private fun apicall2() {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = session.getData(Constant.PLAN_ID)!!
        params["slot_id"] = session.getData(Constant.SLOT_ID)!!
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)

                   //    Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.CLAIM, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


}