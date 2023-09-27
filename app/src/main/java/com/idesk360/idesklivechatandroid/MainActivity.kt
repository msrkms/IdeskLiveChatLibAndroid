package com.idesk360.idesklivechatandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.idesk360.idesklivechatlib.IdeskLiveChat
import com.idesk360.idesklivechatlib.model.IdeskChatSDKConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnOpenChat)

        button.setOnClickListener {
            val sdkConfig = IdeskChatSDKConfig(
                resourceUri = "xxxx.idesk360.com",//required
                appUri = "xxx.idesk360.com",//required
                pageId = "xxxxxxx",//required
                miscellaneous = mapOf("float" to 0),//optional,
                customerInfo = mapOf("name" to "xxx", "rmn" to "xxxxx")//optional
            )

            IdeskLiveChat(sdkConfig, this).OpenChatWindow();

        }
    }
}