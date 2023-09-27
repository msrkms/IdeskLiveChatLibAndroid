package com.idesk360.idesklivechatlib

import android.content.Context
import android.widget.Toast
import com.idesk360.idesklivechatlib.model.IdeskChatSDKConfig

class IdeskLiveChat(private val ideskChatSDKConfig: IdeskChatSDKConfig, private val context: Context) {

    public fun OpenChatWindow(){
        if(!validateData(ideskChatSDKConfig)){
             Toast.makeText(context,"Please provide all the required data", Toast.LENGTH_SHORT).show()
        }else{
            IdeskLiveChatActivity.getStartIntent(context, ideskChatSDKConfig)
          //  LiveChatActivity.getStartIntent(context, ideskChatSDKConfig)
        }
    }

    private fun validateData(sdkConfig: IdeskChatSDKConfig):Boolean{
        return if(sdkConfig.resourceUri.isEmpty()){
            false
        }else if(sdkConfig.appUri.isEmpty()){
            false
        }else sdkConfig.pageId.isNotEmpty()
    }

}