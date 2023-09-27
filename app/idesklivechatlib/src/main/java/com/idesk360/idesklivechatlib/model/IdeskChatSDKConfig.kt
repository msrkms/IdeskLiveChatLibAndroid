package com.idesk360.idesklivechatlib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IdeskChatSDKConfig(
    @SerializedName("resource_uri")
    val resourceUri: String,
    @SerializedName("app_uri")
    val appUri: String,
    @SerializedName("page_id")
    val pageId: String,
    @SerializedName("miscellaneous")
    val miscellaneous: Map<String,Any>,
    @SerializedName("customerInfo")
    val customerInfo: Map<String,Any>,

): Serializable
