package org.d3if0084.jadwalhariini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if0084.jadwalhariini.data.Catatan
import org.d3if0084.jadwalhariini.data.CatatanDao

class MainViewModel(private val db : CatatanDao) : ViewModel() {
    val data = db.getData()

    fun insertData(catatan: Catatan) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insertData(catatan)
            }
        }
    }
    fun deleteData(ids: List<Int>) {
        val newIds = ids.toList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.deleteData(newIds)
            }
        }
    }
}