package cl.duoc.tecmundo.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cl.duoc.tecmundo.ui.theme.ParcelDetailScreen
import cl.duoc.tecmundo.ui.theme.NewRequestSheet
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import cl.duoc.tecmundo.model.ProfileType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tecmundo.ui.theme.AdminHomeScreen
import cl.duoc.tecmundo.ui.theme.ApplicatorHomeScreen
import cl.duoc.tecmundo.ui.theme.ProducerHomeScreen
import cl.duoc.tecmundo.viewmodel.SessionViewModel
import cl.duoc.tecmundo.ui.theme.NewParcelScreen
import cl.duoc.tecmundo.viewmodel.ParcelListViewModel
import cl.duoc.tecmundo.viewmodel.ProviderProfileViewModel
import cl.duoc.tecmundo.viewmodel.RequestListViewModel
import cl.duoc.tecmundo.ui.theme.ProviderProfileScreen

private enum class AppScreen { WELCOME, REGISTER, LOGIN }

@Composable
fun ShivaApp(
    session: SessionViewModel = viewModel(),
    parcelList: ParcelListViewModel = viewModel(),
    providerProfile: ProviderProfileViewModel = viewModel(),
    requestList: RequestListViewModel = viewModel()
) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
    var loginProfile by rememberSaveable { mutableStateOf(ProfileType.PRODUCTOR) }
    var adminMode by rememberSaveable { mutableStateOf(false) }

    val activeUser by session.activeUser.collectAsStateWithLifecycle()
    val user = activeUser
    var addingParcel by rememberSaveable { mutableStateOf(false) }
    var selectedParcelId by rememberSaveable { mutableStateOf<Long?>(null) }
    var requestParcelId by rememberSaveable { mutableStateOf<Long?>(null) }
    var requestedIds by remember { mutableStateOf(setOf<Long>()) }
    var editingProfile by rememberSaveable { mutableStateOf(false) }

    val savedProfile by providerProfile.savedProfile.collectAsStateWithLifecycle()
    val parcels by parcelList.parcels.collectAsStateWithLifecycle()
    val requests by requestList.requests.collectAsStateWithLifecycle()

    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            user != null || adminMode || currentScreen != AppScreen.WELCOME
    }
    if (adminMode) {
        AdminHomeScreen(onLogOut = { adminMode = false })
    } else if (user != null) {
        when (user.profile) {
            ProfileType.PRODUCTOR -> {
                val selectedParcel = parcels.firstOrNull { it.id == selectedParcelId }
                val requestParcel = parcels.firstOrNull { it.id == requestParcelId }

                if (addingParcel) {
                    BackHandler { addingParcel = false }
                    NewParcelScreen(
                        ownerRut = user.rut,
                        onBack = { addingParcel = false },
                        onSaved = {
                            addingParcel = false
                            parcelList.refresh()
                        }
                    )
                } else if (selectedParcel != null) {
                    BackHandler { selectedParcelId = null }
                    ParcelDetailScreen(
                        parcel = selectedParcel,
                        hasRequest = selectedParcel.id in requestedIds,
                        onBack = { selectedParcelId = null },
                        onDelete = {
                            parcelList.deleteParcel(selectedParcel.id)
                            selectedParcelId = null
                        }
                    )
                } else {
                    ProducerHomeScreen(
                        userName = user.fullName,
                        parcels = parcels,
                        onAddParcel = { addingParcel = true },
                        onParcelClick = { selectedParcelId = it.id },
                        onRequestClick = { requestParcelId = it.id },
                        onLogOut = { session.logOut() }
                    )
                }

                if (requestParcel != null) {
                    NewRequestSheet(
                        parcel = requestParcel,
                        onDismiss = { requestParcelId = null },
                        onPublish = { nuevaSolicitud ->
                            requestList.crearSolicitud(nuevaSolicitud) {
                                requestedIds = requestedIds + requestParcel.id
                            }
                            requestParcelId = null
                        }
                    )
                }
            }
            ProfileType.APLICADOR -> {
                if (editingProfile) {
                    BackHandler { editingProfile = false }
                    ProviderProfileScreen(
                        ownerRut = user.rut,
                        onBack = { editingProfile = false },
                        onSaved = { editingProfile = false }
                    )
                } else {
                    ApplicatorHomeScreen(
                        userName = user.fullName,
                        requests = requests,
                        hasProfile = savedProfile != null,
                        onEditProfile = { editingProfile = true },
                        onLogOut = { session.logOut() }
                    )
                }
            }
        }
    } else {
        when (currentScreen) {
            AppScreen.WELCOME -> WelcomeScreen(
                onCreateAccount = { currentScreen = AppScreen.REGISTER },
                onAdmin = { adminMode = true },
                onLogin = { profile ->
                    loginProfile = profile
                    currentScreen = AppScreen.LOGIN
                }
            )

            AppScreen.REGISTER -> {
                BackHandler { currentScreen = AppScreen.WELCOME }
                RegisterScreen(
                    onBack = { currentScreen = AppScreen.WELCOME },
                    onRegistered = { currentScreen = AppScreen.WELCOME }
                )
            }

            AppScreen.LOGIN -> {
                BackHandler { currentScreen = AppScreen.WELCOME }
                LoginScreen(
                    profile = loginProfile,
                    onBack = { currentScreen = AppScreen.WELCOME },
                    onLoggedIn = { currentScreen = AppScreen.WELCOME }
                )
            }
        }
    }
}