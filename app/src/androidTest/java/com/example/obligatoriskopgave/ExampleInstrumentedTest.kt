package com.example.obligatoriskopgave

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.obligatoriskopgave.screens.ListScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.obligatoriskopgave", appContext.packageName)
    }
    @Test
    fun listScreenTest_shouldSwitchMode(){
        //Set up UI:
        composeTestRule.setContent {
            ListScreen(
                shoppingListvar = emptyList(),
                userEmail = "test@example.com"
            )
        }
        //Find the button and assert initial text:
        val modeButton = composeTestRule.onNodeWithTag("modeButton")
        modeButton.assertIsDisplayed()
        modeButton.assertTextContains("Mode: Title")

        //Click to switch mode
        modeButton.performClick()
        //Assert the text has changed
        modeButton.assertTextContains("Mode: Price")
        //Click again (to toggle back)
        modeButton.performClick()
        modeButton.assertTextContains("Mode: Title")

    }
}