package org.d3if0084.jadwalhariini.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val catatan: String,
    val jam: String
)