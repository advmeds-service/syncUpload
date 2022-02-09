package com.advmeds.uploadDemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.advmeds.uploadDemo.ui.theme.UploadTheme
import com.advmeds.uploadmodule.controller.RemoteController
import com.advmeds.uploadmodule.model.HttpFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RemoteController.init(application)
        setContent {
            UploadTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Column() {
        Button(onClick = {
            val httpFormat = HttpFormat(
                    requestType = HttpFormat.GET,
            baseUrl = "https://www.google.com"
            )
            httpFormat.headers = mutableMapOf(
                "title0" to "value0",
                "title1" to "value1",
                "title2" to "value2"
            )
            httpFormat.body = "body test".toByteArray()
            httpFormat.propertyMap = mutableMapOf(
                "property0" to "value0",
                "property1" to "value1",
                "property2" to "value2",
            )
            RemoteController.request(httpFormat)
        }) {
            Text(text = "Get")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UploadTheme {
        Greeting()
    }
}