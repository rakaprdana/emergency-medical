package com.example.medicalinfo.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("hospital_data")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()
    private val HOSPITAL_DATA_KEY = stringPreferencesKey("hospital_data_list")

    //simpan list ke Data Store
    suspend fun saveHospitalData(list: List<MedicalInfo>) {
        val json = gson.toJson(list)
        context.dataStore.edit { preferences -> preferences[HOSPITAL_DATA_KEY] = json }
    }

    fun getHospitalData(): Flow<List<MedicalInfo>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[HOSPITAL_DATA_KEY]
            if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<MedicalInfo>>() {}.type
                gson.fromJson(json, type)
            }
        }
    }
    suspend fun clearData(){
        context.dataStore.edit { it.clear() }
    }
    suspend fun deletedData(index: Int){
        val currentList = getHospitalData().first()
        if (index in currentList.indices){
            val updatedList = currentList.toMutableList().apply {
                removeAt(index)
            }
            saveHospitalData(updatedList)
        }
    }
}
