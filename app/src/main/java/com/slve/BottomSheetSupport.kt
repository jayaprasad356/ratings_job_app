package com.slve

import android.R
import android.content.Intent
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
import javax.annotation.Nullable


class BottomSheetSupport : BottomSheetDialogFragment() {


    lateinit var session: com.slve.ratingjob.helper.Session
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.slve.ratingjob.R.layout.support_bottom_sheet_layout, container, false)

        session = com.slve.ratingjob.helper.Session(activity)

        val binding = com.slve.ratingjob.databinding.SupportBottomSheetLayoutBinding.bind(v)

        val llWhatsapp = binding.llWhatsapp

        llWhatsapp.setOnClickListener {

            val whatsapplink = session.getData(com.slve.ratingjob.helper.Constant.WHATSAPPLINK)
            openWhatsApp(whatsapplink)

        }

        val llTelegram = binding.llTelegram

        llTelegram.setOnClickListener {

            val telegram = session.getData(com.slve.ratingjob.helper.Constant.TELEGRAMLINK)
            openTelegram(telegram)

        }




        return v
    }



    private fun openTelegram(telegram: String) {
        val url = telegram;
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun openWhatsApp(whatsapplink: String) {
        val url = whatsapplink
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)

    }
}