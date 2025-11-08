package com.example.medicalinfo.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalinfo.R
import com.example.medicalinfo.common.MedicalInfo

class MedicalInfoAdapter :
    ListAdapter<MedicalInfo, MedicalInfoAdapter.MedicalInfoViewHolder>(DiffUtilCalllback()) {
    inner class MedicalInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MedicalInfo) {
            val hospitalNameView = itemView.findViewById<TextView>(R.id.tv_hospital_name)
            val hospitalAddressView = itemView.findViewById<TextView>(R.id.tv_hospital_address)
            val hospitalPhone = itemView.findViewById<TextView>(R.id.tv_hospital_phone)

            hospitalNameView.text = item.hospitalName
            hospitalAddressView.text = item.hospitalAddress
            hospitalPhone.text = item.hospitalPhone
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicalInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_medical_info, parent, false)
        return MedicalInfoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MedicalInfoViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    fun setData(itemList: List<MedicalInfo>){
        submitList(itemList)
    }

    fun addItems(newItems: List<MedicalInfo>){
        submitList(currentList + newItems)
    }
}