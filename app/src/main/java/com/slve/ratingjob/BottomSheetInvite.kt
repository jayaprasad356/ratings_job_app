package com.slve.ratingjob

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.graphics.drawable.toBitmap
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.slve.ratingjob.helper.Constant
import javax.annotation.Nullable


class BottomSheetInvite : BottomSheetDialogFragment() {


    lateinit var session: com.slve.ratingjob.helper.Session
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.slve.ratingjob.R.layout.invite_bottom_sheet_layout, container, false)

        session = com.slve.ratingjob.helper.Session(activity)



        val refferalCode = session.getData(com.slve.ratingjob.helper.Constant.REFER_CODE)





        val binding = com.slve.ratingjob.databinding.InviteBottomSheetLayoutBinding.bind(v)





        binding.tvCopy.text = session.getData(com.slve.ratingjob.helper.Constant.REFER_CODE)

        binding.rlCopy.setOnClickListener {
            val clipboard = android.content.Context.CLIPBOARD_SERVICE
            val clip = android.content.ClipData.newPlainText("label", session.getData(com.slve.ratingjob.helper.Constant.REFER_CODE))
            val clipboardManager = requireActivity().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboardManager.setPrimaryClip(clip)
            android.widget.Toast.makeText(activity, "Copied", android.widget.Toast.LENGTH_SHORT).show()
        }
//
//        binding.btnInvite.setOnClickListener {
//
//            val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
//                longLink = dynamicLinkUri
//            }.addOnSuccessListener { (shortLink, flowChartLink) ->
//                // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
//                // com.google.firebase.dynamiclinks.ktx.component2
//
//                val shareBody = "" + shortLink
//                val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
//                sharingIntent.type = "text/plain"
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
//                startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))
//            }.addOnFailureListener {
//                // Error
//                Log.d("Error", it.message.toString())
//                // ...
//            }
//        }


         binding.btnInvite.setOnClickListener {

             val app_link = session.getData(Constant.REFER_LINK)

                val shareBody = "$app_link$refferalCode"
                val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))

         }

        return v
    }
}