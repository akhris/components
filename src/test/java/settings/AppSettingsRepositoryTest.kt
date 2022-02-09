package settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AppSettingsRepositoryTest {
    lateinit var repo: AppSettingsRepository


    @BeforeAll
    fun setup() {
        repo = AppSettingsRepository(TestScope())
    }


    @Test
    fun test_is_light_theme() = runTest {

    }
}