package com.idesk360.idesklivechatlib.view_model

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import com.idesk360.idesklivechatlib.model.IdeskChatSDKConfig
import com.idesk360.idesklivechatlib.network.ApiInterface
import com.idesk360.idesklivechatlib.network.RetrofitClient
import okhttp3.ResponseBody

class LiveChatViewModel(private val sdkConfig: IdeskChatSDKConfig) :BaseObservable() {

    val htmlData:MutableLiveData<String> = MutableLiveData()
     val isLoading:MutableLiveData<Boolean> = MutableLiveData()
     val error:MutableLiveData<String> = MutableLiveData()

    fun getHtmlForWebView() {
      htmlData.postValue("<h1>Please wait</h1>")
        getHTMlFromAPI(sdkConfig)
    }


    private fun getHTMlFromAPI(ideskChatSDKConfig: IdeskChatSDKConfig) {
        var retrofit = RetrofitClient.getInstance()
        var apiInterface = retrofit.create(ApiInterface::class.java)

        apiInterface.getHtmlForWebView(ideskChatSDKConfig).enqueue(
            object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val htmlString = response.body()?.string().toString()
                        htmlData.postValue(htmlString)
                    } else {
                        error.postValue("Something went wrong")
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    error.postValue("Something went wrong")
                }
            }
        )


    }

}