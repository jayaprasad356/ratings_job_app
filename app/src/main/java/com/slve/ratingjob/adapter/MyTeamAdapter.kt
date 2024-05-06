package com.slve.ratingjob.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.slve.ratingjob.model.MyTeam
import com.slve.ratingjob.R

class MyTeamAdapter(
    val activity: Activity,
    myTeam : ArrayList<MyTeam>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myTeam: ArrayList<MyTeam>
    val activitys: Activity

    init {
        this.myTeam = myTeam
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_myteam, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyTeam = myTeam[position]


      //  holder.rlMain.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim1))

        holder.tvViewMore.setOnClickListener {
           holder.llInviteDetails.visibility = if(holder.llInviteDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        }


//        holder.tvName.text = report.name
        holder.tvMobile.text = report.mobile
        holder.tvJoinTime.text = report.registered_date
        holder.tvReferrer.text = report.valid_team
        holder.tvAssets.text = report.total_assets


    }


    override fun getItemCount(): Int {
        return myTeam.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView
        val tvMobile: TextView
        val rlMain: RelativeLayout
        val tvJoinTime: TextView
        val tvReferrer: TextView
        val tvAssets: TextView
        val tvViewMore: TextView
        val llInviteDetails:LinearLayout



        init {
//            tvName = itemView.findViewById(R.id.tvName)
            tvMobile = itemView.findViewById(R.id.tvMobile)
            rlMain = itemView.findViewById(R.id.rlMain)
            tvJoinTime = itemView.findViewById(R.id.tvJoinTime)
            tvReferrer = itemView.findViewById(R.id.tvReferrer)
            tvAssets = itemView.findViewById(R.id.tvAssets)
            tvViewMore = itemView.findViewById(R.id.tvViewMore)
            llInviteDetails = itemView.findViewById(R.id.llInviteDetails)


        }
    }
}