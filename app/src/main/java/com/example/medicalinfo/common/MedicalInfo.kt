package com.example.medicalinfo.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class MedicalInfo(
    val id: String = UUID.randomUUID().toString(),
    val hospitalName: String = "Hospital name",
    val hospitalAddress: String = "Jln. Papaya no 50, RT5/RW22, Kel. Papaya, Kec. Buah, Kota Mangga dua",
    val hospitalPhone: String ="0812-345-678-901"
): Parcelable
