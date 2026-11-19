package cl.duoc.tecmundo.model

import com.google.gson.annotations.SerializedName

data class UserAccount(
    @SerializedName("profile_type") val profile: ProfileType,
    @SerializedName("name") val fullName: String,
    val rut: String,
    val email: String,
    val phone: String,
    @SerializedName("farm_name") val farmName: String,
    @SerializedName("profile_image") val profileImage: String? = null,
    @SerializedName("password_hash") val passwordHash: String? = null,
    @SerializedName("password_salt") val passwordSalt: String? = null,
    val id: Long = 0
)