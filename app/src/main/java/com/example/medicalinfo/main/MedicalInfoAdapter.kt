package com.example.medicalinfo.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalinfo.R
import com.example.medicalinfo.common.DataStoreManager
import com.example.medicalinfo.common.MedicalInfo
import com.google.android.material.button.MaterialButton

    class MedicalInfoAdapter(val listener: MedicalInfoListener) :
        ListAdapter<MedicalInfo, MedicalInfoAdapter.MedicalInfoViewHolder>(DiffUtilCalllback()) {
        inner class MedicalInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MedicalInfo, position: Int) {
                val hospitalNameView = itemView.findViewById<TextView>(R.id.tv_hospital_name)
                val hospitalAddressView = itemView.findViewById<TextView>(R.id.tv_hospital_address)
                val hospitalPhoneView = itemView.findViewById<TextView>(R.id.tv_hospital_phone)
                val btnDelete = itemView.findViewById<MaterialButton>(R.id.btn_delete)

                hospitalNameView.text = item.hospitalName
                hospitalAddressView.text = item.hospitalAddress
                hospitalPhoneView.text = item.hospitalPhone
                hospitalPhoneView.setOnClickListener {
                    listener.onPhoneNumberClickes(hospitalPhoneView?.text.toString())
                }
                btnDelete.setOnClickListener {
                    listener.onDeleteClick(position)
                }
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
            holder.bind(getItem(position), position)
        }

        fun setData(itemList: List<MedicalInfo>){
            submitList(itemList)
        }

        fun addItems(newItems: List<MedicalInfo>){
            submitList(currentList + newItems)
        }
    }