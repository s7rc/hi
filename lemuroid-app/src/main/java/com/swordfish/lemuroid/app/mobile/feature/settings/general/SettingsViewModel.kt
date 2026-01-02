package com.swordfish.lemuroid.app.mobile.feature.settings.general

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.library.PendingOperationsMonitor
import com.swordfish.lemuroid.app.shared.settings.SettingsInteractor
import com.swordfish.lemuroid.lib.savesync.SaveSyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    context: Context,
    private val settingsInteractor: SettingsInteractor,
    saveSyncManager: SaveSyncManager,
    sharedPreferences: FlowSharedPreferences,
) : ViewModel() {
    class Factory(
        private val context: Context,
        private val settingsInteractor: SettingsInteractor,
        private val saveSyncManager: SaveSyncManager,
        private val sharedPreferences: FlowSharedPreferences,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                context,
                settingsInteractor,
                saveSyncManager,
                sharedPreferences,
            ) as T
        }
    }

    data class State(
        val directories: List<String> = emptyList(),
        val isSaveSyncSupported: Boolean = false,
    )

    val indexingInProgress = PendingOperationsMonitor(context).anyLibraryOperationInProgress()

    val directoryScanInProgress = PendingOperationsMonitor(context).isDirectoryScanInProgress()

    private val legacyPrefs = com.swordfish.lemuroid.lib.preferences.SharedPreferencesHelper.getLegacySharedPreferences(context)
    private val legacyFlowPrefs = com.fredporciuncula.flow.preferences.FlowSharedPreferences(legacyPrefs)

    private val singleFolderPref = legacyFlowPrefs.getString(context.getString(com.swordfish.lemuroid.lib.R.string.pref_key_extenral_folder), "")
    private val multiFoldersPref = legacyFlowPrefs.getStringSet(context.getString(com.swordfish.lemuroid.lib.R.string.pref_key_external_folders), emptySet())

    val uiState =
        kotlinx.coroutines.flow.combine(singleFolderPref.asFlow(), multiFoldersPref.asFlow()) { single, multi ->
            if (multi.isNotEmpty()) multi.toList() else if (single.isNotEmpty()) listOf(single) else emptyList()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
            .map { State(it, saveSyncManager.isSupported()) }

    fun changeLocalStorageFolder() {
        settingsInteractor.changeLocalStorageFolder()
    }

    fun clearLocalStorageFolders() {
        singleFolderPref.delete()
        multiFoldersPref.delete()
    }
}
