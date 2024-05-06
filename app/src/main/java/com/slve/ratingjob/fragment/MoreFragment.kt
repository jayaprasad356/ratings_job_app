package com.slve.ratingjob.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.anupkumarpanwar.scratchview.ScratchView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.slve.BottomSheetSupport
import com.slve.ratingjob.BottomSheetInvite
import com.slve.ratingjob.R
import com.slve.ratingjob.activity.HomeActivity
import com.slve.ratingjob.activity.MyProductionActivity
import com.slve.ratingjob.activity.PaymentActivity
import com.slve.ratingjob.activity.RechargeActivity
import com.slve.ratingjob.activity.TransactionActivity
import com.slve.ratingjob.activity.UpdateProfileActivity
import com.slve.ratingjob.activity.WithdrawalActivity
import com.slve.ratingjob.databinding.FragmentMoreBinding
import com.slve.ratingjob.helper.ApiConfig
import com.slve.ratingjob.helper.Constant
import com.slve.ratingjob.utils.DialogUtils
import com.zoho.salesiqembed.ZohoSalesIQ
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MoreFragment : Fragment() {


    lateinit var binding: FragmentMoreBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session

    lateinit var scratch_id:String
    lateinit var chance:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)




        activity = getActivity() as Activity
        session = com.slve.ratingjob.helper.Session(activity)

        chance = session.getData(com.slve.ratingjob.helper.Constant.CHANCES)
        Glide.with(activity).load(session.getData(Constant.PROFILE)).placeholder(com.slve.ratingjob.R.drawable.avatar).into(binding.ivProfile)


        (activity as HomeActivity).binding.rlToolbar.visibility = View.GONE
        userdetails()

        binding.llInvite.setOnClickListener {
      //    startActivity(Intent(activity, InviteActivity::class.java))

            // open custom bottom sheet dialog

            val bottomSheet = BottomSheetInvite()
            bottomSheet.show(
                requireActivity().getSupportFragmentManager(),
                "ModalBottomSheet"
            )




        }

        binding.llScartchCard.setOnClickListener {
          // startActivity(Intent(activity, com.slve.ratingjob.activity.ScartchcardActivity::class.java))

            // show custom dialog box
            scartchdialog()
        }


        binding.tvMobile.text = session.getData(com.slve.ratingjob.helper.Constant.MOBILE)
        binding.tvName.text = session.getData(com.slve.ratingjob.helper.Constant.NAME)

        binding.cvWithdraw.setOnClickListener {
            startActivity(Intent(activity, WithdrawalActivity::class.java))
        }

        binding.rlmyBank.setOnClickListener {
            startActivity(Intent(activity, com.slve.ratingjob.activity.UpdatebankActivity::class.java))
        }

        binding.llMyProduction.setOnClickListener{
            startActivity(Intent(activity,MyProductionActivity::class.java))
        }

        binding.llServices.setOnClickListener{
            val bottomSheet = BottomSheetSupport()
            bottomSheet.show(
                requireActivity().getSupportFragmentManager(),
                "ModalBottomSheet"
            )
        }

        binding.cvRecharge.setOnClickListener {

            if (session.getData(Constant.PAYGATEWAY).equals("1")) {
                startActivity(Intent(activity, RechargeActivity::class.java))
            } else if (session.getData(Constant.PAYGATEWAY).equals("0")) {
                startActivity(Intent(activity, PaymentActivity::class.java))

            }
         //  startActivity(Intent(activity, PaymentActivity::class.java))
      //   startActivity(Intent(activity, RechargeActivity::class.java))
        }


        binding.rlChangepassword.setOnClickListener {
            startActivity(Intent(activity, com.slve.ratingjob.activity.ChangepasswordActivity::class.java))
        }

        binding.rlhistory.setOnClickListener {
            startActivity(Intent(activity, TransactionActivity::class.java))
        }

        binding.rlUpdateprofile.setOnClickListener{
            startActivity(Intent(activity, UpdateProfileActivity::class.java))
        }

        binding.rlwithdrawhistory.setOnClickListener{
            startActivity(Intent(activity, com.slve.ratingjob.activity.WithdrawalStatusActivity::class.java))
        }

        binding.tvChat.setOnClickListener{

            ZohoSalesIQ.Chat.show()


        }


        binding.rlLogout.setOnClickListener{
            session.logoutUser(activity)
        }

        binding.tvTotalIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_INCOME)
        binding.tvTotalRecharge.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.RECHARGE)
        binding.tvTodayIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TODAY_INCOME)
        binding.tvTotalAssests.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_ASSETS)
        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_WITHDRAWAL)
        binding.tvTeamIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TEAM_INCOME)



        return binding.root
    }

    private fun scartchdialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_scratchcard)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()


        val scratchView = dialog.findViewById<ScratchView>(R.id.scratchView)
        val tvChance = dialog.findViewById<TextView>(R.id.tvChance)
        val ScarchImg = dialog.findViewById<CardView>(R.id.Scarch_img)
        val image = dialog.findViewById<CardView>(R.id.image)
        val tvAmount = dialog.findViewById<TextView>(R.id.tvAmount)


        tvChance.text =  session.getData(com.slve.ratingjob.helper.Constant.CHANCES) + " Chances left"

       scratchView.setRevealListener(object : ScratchView.IRevealListener {
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
            ScarchImg.visibility = View.GONE
           image.visibility = View.VISIBLE
            DialogUtils.showCustomDialog(activity, "No More Chances")
            dialog.dismiss()
        }
        else{
            ScarchImg.visibility = View.VISIBLE
            image.visibility = View.GONE
            apicall(tvAmount)
        }
        userdetails()

    }

    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)

                        session!!.setData(Constant.CHANCES, jsonArray.getJSONObject(0).getString(Constant.CHANCES))


                        session!!.setData(
                            Constant.ACCOUNT_NUM,
                            jsonArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM)
                        )
                        session!!.setData(
                            Constant.HOLDER_NAME,
                            jsonArray.getJSONObject(0).getString(Constant.HOLDER_NAME)
                        )
                        session!!.setData(
                            Constant.BANK,
                            jsonArray.getJSONObject(0).getString(Constant.BANK)
                        )
                        session!!.setData(
                            Constant.BRANCH,
                            jsonArray.getJSONObject(0).getString(Constant.BRANCH)
                        )
                        session!!.setData(
                            Constant.IFSC,
                            jsonArray.getJSONObject(0).getString(Constant.IFSC)
                        )
                        session!!.setData(
                            Constant.AGE,
                            jsonArray.getJSONObject(0).getString(Constant.AGE)
                        )
                        session!!.setData(
                            Constant.CITY,
                            jsonArray.getJSONObject(0).getString(Constant.CITY)
                        )
                        session!!.setData(
                            Constant.STATE,
                            jsonArray.getJSONObject(0).getString(Constant.STATE)
                        )
                        session!!.setData(
                            Constant.DEVICE_ID,
                            jsonArray.getJSONObject(0).getString(Constant.DEVICE_ID)
                        )
                        session!!.setData(
                            Constant.RECHARGE, jsonArray.getJSONObject(0).getString(
                                Constant.RECHARGE))
                        session!!.setData(
                            Constant.TODAY_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TODAY_INCOME))
                        session!!.setData(
                            Constant.TOTAL_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_INCOME))
                        session!!.setData(
                            Constant.BALANCE, jsonArray.getJSONObject(0).getString(
                                Constant.BALANCE))
                        session!!.setData(
                            Constant.WITHDRAWAL_STATUS, jsonArray.getJSONObject(0).getString(
                                Constant.WITHDRAWAL_STATUS))
                        session!!.setData(
                            Constant.TEAM_SIZE, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_SIZE))
                        session!!.setData(
                            Constant.VALID_TEAM, jsonArray.getJSONObject(0).getString(
                                Constant.VALID_TEAM))
                        session!!.setData(
                            Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_WITHDRAWAL))
                        session!!.setData(
                            Constant.TEAM_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_INCOME))
                        session!!.setData(
                            Constant.TOTAL_ASSETS, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_ASSETS))
                        session!!.setData(
                            Constant.BANK, jsonArray.getJSONObject(0).getString(
                                Constant.BANK))
                        session!!.setData(
                            Constant.BRANCH, jsonArray.getJSONObject(0).getString(
                                Constant.BRANCH))
                        session!!.setData(Constant.IFSC, jsonArray.getJSONObject(0).getString(
                                Constant.IFSC))
                           session!!.setData(Constant.ACCOUNT_NUM, jsonArray.getJSONObject(0).getString(
                                Constant.ACCOUNT_NUM))
                        session!!.setData(Constant.HOLDER_NAME, jsonArray.getJSONObject(0).getString(
                                Constant.HOLDER_NAME))



                        binding.tvTotalIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_INCOME)
                        binding.tvTotalRecharge.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.RECHARGE)
                        binding.tvTodayIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TODAY_INCOME)
                        binding.tvTotalAssests.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_ASSETS)
                        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TOTAL_WITHDRAWAL)
                        binding.tvTeamIncome.text = "₹ " + session.getData(com.slve.ratingjob.helper.Constant.TEAM_INCOME)

                        session!!.setData(Constant.CHANCES, jsonArray.getJSONObject(0).getString(Constant.CHANCES))

                        val chance = session.getData(com.slve.ratingjob.helper.Constant.CHANCES)


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

    private fun apicall1() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.SCRATCH_ID] = scratch_id.toString()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        Toast.makeText(getActivity(),jsonObject.getString(Constant.MESSAGE) , Toast.LENGTH_SHORT).show()

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


    private fun apicall(tvAmount: TextView) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        tvAmount.text = jsonObject.getString("amount")
                        scratch_id = jsonObject.getString("scratch_id")

                        Toast.makeText(getActivity(),jsonObject.getString(Constant.MESSAGE) , Toast.LENGTH_SHORT).show()



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