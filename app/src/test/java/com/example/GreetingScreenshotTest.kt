package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.CallLogEntity
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleLogs = listOf(
        CallLogEntity(
            id = 1,
            contactName = "John Doe",
            phoneNumber = "+1555019283",
            timestamp = 1716217800000L, // Static timestamp for consistent tests
            duration = "2m 14s",
            callType = "Incoming"
        ),
        CallLogEntity(
            id = 2,
            contactName = "Alice Smith",
            phoneNumber = "9876543210",
            timestamp = 1716210600000L, // Static timestamp for consistent tests
            duration = "0m 45s",
            callType = "Outgoing"
        )
    )
    composeTestRule.setContent { 
      MyApplicationTheme { 
         CallsScreen(
             callLogs = sampleLogs,
             hasPermission = true,
             onRequestPermission = {},
             onSyncDeviceLogs = {},
             onPromote = {},
             onReject = {},
             onGenerateSample = {}
         )
      } 
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
