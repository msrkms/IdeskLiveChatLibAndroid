package com.idesk360.idesklivechatlib.network

import com.idesk360.idesklivechatlib.model.IdeskChatSDKConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/init-iDesk-live-chat")
    fun getHtmlForWebView(
        @Body ideskChatSDKConfig: IdeskChatSDKConfig

    ): Call<ResponseBody>
}