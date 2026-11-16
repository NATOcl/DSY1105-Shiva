package cl.duoc.tecmundo.remote

import cl.duoc.tecmundo.model.ApplicationRequest
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.model.ProviderProfile
import cl.duoc.tecmundo.model.UserAccount
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // ==================== USER ====================
    @GET("user")
    suspend fun obtenerUsuarios(): List<UserAccount>

    @GET("user/{user_id}")
    suspend fun obtenerUsuario(@Path("user_id") id: Long): UserAccount

    @POST("user")
    suspend fun crearUsuario(@Body user: UserAccount): UserAccount

    @PATCH("user/{user_id}")
    suspend fun actualizarUsuario(
        @Path("user_id") id: Long,
        @Body user: UserAccount
    ): UserAccount

    @DELETE("user/{user_id}")
    suspend fun eliminarUsuario(@Path("user_id") id: Long)

    // ==================== PARCEL ====================
    @GET("parcel")
    suspend fun obtenerParcelas(): List<Parcel>

    @GET("parcel/{parcel_id}")
    suspend fun obtenerParcela(@Path("parcel_id") id: Long): Parcel

    @POST("parcel")
    suspend fun crearParcela(@Body parcel: Parcel): Parcel

    @PATCH("parcel/{parcel_id}")
    suspend fun actualizarParcela(
        @Path("parcel_id") id: Long,
        @Body parcel: Parcel
    ): Parcel

    @DELETE("parcel/{parcel_id}")
    suspend fun eliminarParcela(@Path("parcel_id") id: Long)

    // ==================== PROVIDER PROFILE ====================
    @GET("providerprofile")
    suspend fun obtenerPerfilesProveedor(): List<ProviderProfile>

    @GET("providerprofile/{providerprofile_id}")
    suspend fun obtenerPerfilProveedor(@Path("providerprofile_id") id: Long): ProviderProfile

    @POST("providerprofile")
    suspend fun crearPerfilProveedor(@Body profile: ProviderProfile): ProviderProfile

    @PATCH("providerprofile/{providerprofile_id}")
    suspend fun actualizarPerfilProveedor(
        @Path("providerprofile_id") id: Long,
        @Body profile: ProviderProfile
    ): ProviderProfile

    @DELETE("providerprofile/{providerprofile_id}")
    suspend fun eliminarPerfilProveedor(@Path("providerprofile_id") id: Long)

    // ==================== APPLICATION REQUEST ====================
    @GET("applicationrequest")
    suspend fun obtenerSolicitudes(): List<ApplicationRequest>

    @GET("applicationrequest/{applicationrequest_id}")
    suspend fun obtenerSolicitud(@Path("applicationrequest_id") id: Int): ApplicationRequest

    @POST("applicationrequest")
    suspend fun crearSolicitud(@Body request: ApplicationRequest): ApplicationRequest

    @PATCH("applicationrequest/{applicationrequest_id}")
    suspend fun actualizarSolicitud(
        @Path("applicationrequest_id") id: Int,
        @Body request: ApplicationRequest
    ): ApplicationRequest

    @DELETE("applicationrequest/{applicationrequest_id}")
    suspend fun eliminarSolicitud(@Path("applicationrequest_id") id: Int)
}