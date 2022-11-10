package com.example.signupandloginin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "uid") var uid: String? = "",
    @ColumnInfo(name = "email") var email: String? = "",
    @ColumnInfo(name = "password") var password: String? = "",
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "aboutMe") var aboutMe: String? = "",
)
