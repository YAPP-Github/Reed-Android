package com.ninecraft.booket.core.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ninecraft.booket.core.datastore.impl.datasource.DefaultTokenPreferencesDataSource
import com.ninecraft.booket.core.datastore.impl.security.CryptoManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.io.path.createTempDirectory

@RunWith(AndroidJUnit4::class)
class TokenPreferenceDataSourceTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataSource: DefaultTokenPreferencesDataSource
    private lateinit var cryptoManager: CryptoManager
    private lateinit var tempFile: File

    @Before
    fun setup() {
        val tempFolder = createTempDirectory().toFile()
        tempFile = File(tempFolder, "token_prefs.preferences_pb")

        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { tempFile },
        )

        cryptoManager = CryptoManager()
        dataSource = DefaultTokenPreferencesDataSource(dataStore, cryptoManager)
    }

    @After
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun 토큰은_암호화되어_저장된다() = runTest {
        // Given
        val plainToken = "plain_access_token"
        dataSource.setAccessToken(plainToken)

        // When
        val storedToken = dataStore.data.first()[stringPreferencesKey("ACCESS_TOKEN")]

        // Then
        assertNotNull(storedToken)
        assertNotEquals(plainToken, storedToken)
        assertTrue(storedToken!!.isNotEmpty())
    }

    @Test
    fun 암호화된_토큰은_복호화되어_반환된다() = runTest {
        // Given
        val plainToken = "plain_access_token"
        dataSource.setAccessToken(plainToken)

        // When
        val restoredToken = dataSource.accessToken.first()

        // Then
        assertEquals(plainToken, restoredToken)
    }
}
