package gr.jvoyatz.afse.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val accountNumber: Int,
    val accountType: String,
    val balance: String,
    val currencyCode: String,
    val accountNickname: String?=null
)