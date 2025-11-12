package com.example.medicalinfo.input

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicalinfo.R
import com.example.medicalinfo.common.DataStoreManager
import com.example.medicalinfo.common.MedicalInfo
import com.example.medicalinfo.main.MedicalInfoAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class InputFragment : Fragment() {
    private val btnBack by lazy { view?.findViewById<ImageView>(R.id.iv_back_arrow) }
    private val inputNameField by lazy { view?.findViewById<TextInputEditText>(R.id.input_hospital_name) }
    private val inputAddressField by lazy { view?.findViewById<TextInputEditText>(R.id.input_hospital_address) }
    private val inputPhoneField by lazy { view?.findViewById<TextInputEditText>(R.id.input_hospital_phone) }
    private val btnDiscard by lazy { view?.findViewById<MaterialButton>(R.id.btn_discard) }
    private val btnSave by lazy { view?.findViewById<MaterialButton>(R.id.btn_save) }
    private val medicalInfoData =
        mutableListOf<MedicalInfo>() //menampung data yang terkirim dari fragment sebelumnya
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        medicalInfoData.addAll(
            arguments?.getParcelableArray("medicalInfoData")
                ?.toMutableList() as MutableList<MedicalInfo>
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreManager = DataStoreManager(requireContext())
        btnDiscard?.setOnClickListener {
            discardData()
        }
        btnSave?.setOnClickListener {
            if (isEntryValid()) {
                saveData()
            } else {
                showToast("Mohon lengkapi data terlebih dahulu")
            }
        }
        lifecycleScope.launch {
            dataStoreManager.getHospitalData().collect { list ->
                medicalInfoData.clear()
                medicalInfoData.addAll(list)
            }
        }

        handleOnBackPressed()
        btnBack?.setOnClickListener {
            backToMainFragment()
        }
    }

    private fun discardData() {
        inputNameField?.setText("")
        inputAddressField?.setText("") //Fungsi yg sama
        inputPhoneField?.text?.clear() //Fungsi yg sama
    }

    private fun saveData() {
        val newDataEntry = MedicalInfo(
            hospitalName = inputNameField?.text.toString(),
            hospitalAddress = inputAddressField?.text.toString(),
            hospitalPhone = inputPhoneField?.text.toString()
        )
        lifecycleScope.launch {
            try {
                medicalInfoData.add(newDataEntry)
                dataStoreManager.saveHospitalData(medicalInfoData)
                discardData()
                showToast("Data berhasil tersimpan")
            } catch (e: Exception) {
                showToast("Data gagal tersimpan!")
            }
        }
    }

    //proses validasi input
    private fun isEntryValid(): Boolean {
        return !(inputNameField?.text.toString().isBlank() || inputAddressField?.text.toString()
            .isBlank() || inputPhoneField?.text.toString().isBlank())
    }

    //function untuk menampilkan sukses input / error input
    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    //function kembali ke main fragment
    private fun backToMainFragment() {
        val resultData = medicalInfoData
        findNavController().previousBackStackEntry?.savedStateHandle?.set("resultKey", resultData)
        findNavController().navigateUp()
    }

    //function kembali dengan button pressed
    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backToMainFragment()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }
}