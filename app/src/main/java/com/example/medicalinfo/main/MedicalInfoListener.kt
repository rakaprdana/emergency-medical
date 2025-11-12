package com.example.medicalinfo.main

interface MedicalInfoListener {
    fun onPhoneNumberClickes(phoneNumber: String){}
    fun onDeleteClick(position: Int){}
}