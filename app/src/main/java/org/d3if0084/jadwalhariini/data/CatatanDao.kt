package org.d3if0084.jadwalhariini.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CatatanDao {
    @Insert
    fun insertData(catatan: Catatan)

    @Query("SELECT * FROM catatan ORDER BY catatan")
    fun getData(): LiveData<List<Catatan>>

    @Query("DELETE FROM catatan WHERE id IN (:ids)")
    fun deleteData(ids: List<Int>)

}
