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
                resourceUri = "toolstatic.idesk360.com",
                appUri = "tool.idesk360.com",
                pageId = "1694592792000",
                miscellaneous = mapOf("float" to 0),
                customerInfo = mapOf("name" to "kamal", "rmn" to "019667653443")
            )

            IdeskLiveChat(sdkConfig, this).OpenChatWindow();

        }
    }
}