package com.example.vaxinfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdaptor(private val centerList: List<CenterRVModal>) : RecyclerView.Adapter<CenterRVAdaptor.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val centerNameTV: TextView = itemView.findViewById(R.id.CenterName)
        val centerAddrTV: TextView = itemView.findViewById(R.id.CenterLocation)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.VaccineName)
        val centerTimingTV: TextView = itemView.findViewById(R.id.CenterTimings)
        val vaccinePriceTV: TextView = itemView.findViewById(R.id.VaccinePrice)
        val ageLimitTV: TextView = itemView.findViewById(R.id.AgeLimit)
        val availableTV: TextView = itemView.findViewById(R.id.Available)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item,parent,false)
        return CenterRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerNameTV.text =  center.centerName
        holder.centerAddrTV.text = center.centerAddr
        holder.centerTimingTV.text = ("from: "+center.fromTime+"to: "+center.toTime)
        holder.vaccineNameTV.text = center.vaccine
        holder.vaccinePriceTV.text= center.price
        holder.ageLimitTV.text = ("Age Limit: "+center.agelimit.toString())
        holder.availableTV.text = ("available: "+center.availability.toString())
        
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

}