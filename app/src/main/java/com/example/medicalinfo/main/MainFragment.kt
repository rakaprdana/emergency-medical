package com.example.medicalinfo.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalinfo.R
import com.example.medicalinfo.common.DataStoreManager
import com.example.medicalinfo.common.MedicalInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class MainFragment: Fragment(), MedicalInfoListener {

    //sample untuk test UI
//    private val rvMedicalInfo by lazy { view?.findViewById<RecyclerView>(R.id.rv_medical_info) }
//    private val fabAddData by lazy { view?.findViewById<FloatingActionButton>(R.id.fab_add_data) }
//    private val adapter = MedicalInfoAdapter()

    //definisi variabel ketika terdapat newDataEntry
    private lateinit var rvMedicalInfo: RecyclerView
    private lateinit var fabAddData: FloatingActionButton
    private lateinit var adapter: MedicalInfoAdapter
    private lateinit var dataStoreManager: DataStoreManager
    private val medicalInfoData = mutableListOf<MedicalInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initMedicalInfoList()
        getDataFromInputFragment()
        dataStoreManager= DataStoreManager(requireContext())
        fabAddData?.setOnClickListener {
            val bundle = bundleOf("medicalInfoData" to adapter.currentList.toTypedArray())
            findNavController().navigate(R.id.action_mainFragment_to_inputFragment, bundle)
        }
        observeData()
    }

    private fun initViews() {
        view?.let {
            rvMedicalInfo = it.findViewById(R.id.rv_medical_info)
            fabAddData = it.findViewById(R.id.fab_add_data)
            adapter = MedicalInfoAdapter(this)
        }
    }
    private fun observeData(){
        lifecycleScope.launch {
            dataStoreManager.getHospitalData().collect { list ->
                if (list.isNotEmpty()){
                    medicalInfoData.addAll(list)
                    adapter.setData(list.toList())
                } else{
                    adapter.setData(emptyList())
                }
            }
        }
    }
    private fun initMedicalInfoList() {
        rvMedicalInfo?.layoutManager = LinearLayoutManager(context)
        rvMedicalInfo?.adapter = adapter
//        adapter.setData(generateDummyData())
    }

    private fun getDataFromInputFragment() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<MedicalInfo>>(
            "resultKey"
        )?.observe(viewLifecycleOwner) { result -> adapter.setData(result) }
    }
   override fun onDeleteClick(position: Int){
        lifecycleScope.launch {
            val updatedList = adapter.currentList.toMutableList().apply {
                removeAt(position)
            }
            dataStoreManager.saveHospitalData(updatedList)
            adapter.setData(updatedList)
        }
    }
//    private fun generateDummyData(): List<MedicalInfo> {
//        return listOf(
//            MedicalInfo(),
//            MedicalInfo(
//                hospitalName = "Hospital A",
//                hospitalAddress = "Jln Mangga dua",
//                hospitalPhone = "08124133524524545"
//            )
//        )
//    }

    override fun onPhoneNumberClickes(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${phoneNumber}")
        startActivity(intent)
    }

}