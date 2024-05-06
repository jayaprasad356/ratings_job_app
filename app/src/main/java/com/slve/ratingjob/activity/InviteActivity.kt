package com.slve.ratingjob.activity

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.slve.ratingjob.databinding.ActivityInviteBinding
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.component1
import com.google.firebase.dynamiclinks.ktx.component2
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase

class InviteActivity : AppCompatActivity() {

    lateinit var binding: ActivityInviteBinding
    lateinit var activity: Activity
    lateinit var session: com.slve.ratingjob.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInviteBinding.inflate(layoutInflater)
        activity = this
        session = com.slve.ratingjob.helper.Session(activity)



        return setContentView(binding!!.root)
    }
}