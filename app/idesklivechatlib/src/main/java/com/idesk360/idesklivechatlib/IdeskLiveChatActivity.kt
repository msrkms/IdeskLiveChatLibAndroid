package com.idesk360.idesklivechatlib

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.idesk360.idesklivechatlib.databinding.ActivityIdeskLiveChatBinding
import com.idesk360.idesklivechatlib.model.IdeskChatSDKConfig
import com.idesk360.idesklivechatlib.utils.ConnectivityReceiver
import com.idesk360.idesklivechatlib.view_model.LiveChatViewModel
import java.net.HttpURLConnection
import java.net.URL

class IdeskLiveChatActivity : AppCompatActivity() {

    private val TAG = "IdeskLiveChatActivity"
    lateinit var dataBinding: ActivityIdeskLiveChatBinding
    lateinit var viewModel: LiveChatViewModel
    val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var downloadManager: DownloadManager

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null

    private val fileUploadActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val results = result.data?.let { WebChromeClient.FileChooserParams.parseResult(result.resultCode, it) }
            fileUploadCallback?.onReceiveValue(results)
        } else {
            fileUploadCallback?.onReceiveValue(null)
        }
        fileUploadCallback = null
    }



    companion object {

        lateinit var ideskChatSDKConfig: IdeskChatSDKConfig
        private var chatSDKConfigKey: String="chatSDKConfig"


        fun getStartIntent(context: Context, ideskChatSDKConfig: IdeskChatSDKConfig) {
            val intent = Intent(context, IdeskLiveChatActivity::class.java)
            intent.putExtra(chatSDKConfigKey, ideskChatSDKConfig)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding= ActivityIdeskLiveChatBinding.inflate(layoutInflater)

        ideskChatSDKConfig = intent.getSerializableExtra(chatSDKConfigKey) as IdeskChatSDKConfig
        viewModel = LiveChatViewModel(ideskChatSDKConfig)
        setContentView(dataBinding.root)

        liveDataObservers()

        initializeDownloadManager()


        dataBinding.webview.settings.javaScriptEnabled = true
        dataBinding.webview.settings.domStorageEnabled = true
        dataBinding.webview.settings.allowFileAccess = true
        dataBinding.webview.settings.allowContentAccess = true

        dataBinding.webview.settings.allowFileAccessFromFileURLs = true
        dataBinding.webview.settings.allowUniversalAccessFromFileURLs = true


        dataBinding.webview.webChromeClient = object : WebChromeClient() {



            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                fileUploadCallback = filePathCallback ?: return false
                val intent = fileChooserParams?.createIntent()
                fileUploadActivityResultLauncher.launch(intent)
                return true
            }


            //enable download file
            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                if (message?.startsWith("download") == true) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(message.substring(8))
                    startActivity(intent)
                    result?.confirm()
                    return true
                }
                return super.onJsConfirm(view, url, message, result)
            }






        }

        dataBinding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Handle page navigation within the WebView
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                // Handle downloads from the WebView
                val url = request?.url.toString()
                if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif")) {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream = connection.inputStream
                    return WebResourceResponse("image/*", "UTF-8", inputStream)
                }
                return super.shouldInterceptRequest(view, request)
            }
        }

        dataBinding.webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Toast.makeText(this,"Downloading...",Toast.LENGTH_LONG).show()
            download(url)
        }


        if (checkConnection()) {
            viewModel.getHtmlForWebView()
        } else {
            connectionAlert();
        }



    }


    private fun liveDataObservers(){
        viewModel.htmlData.observe(this) {
            dataBinding.webview.visibility = View.VISIBLE

            dataBinding.webview.loadDataWithBaseURL(
                "https://app.idesk360.com/init-iDesk-live-chat",
                it,
                "text/html",
                "UTF-8",
                null
            )
        }
    }



    private fun initializeDownloadManager() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }



    fun download(url:String){
        try{
            var filename=url.split("/")

            val request =
                DownloadManager.Request(Uri.parse("$url"))
            request.setTitle("${filename[filename.lastIndex]}")
                .setDescription("File is downloading...")
                .setDestinationInExternalFilesDir(
                    this,
                    Environment.DIRECTORY_DOWNLOADS, "$filename"
                )
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            var downloadID=   downloadManager.enqueue(request);

        }catch (e:Exception){
            Toast.makeText(this,"Error ${e.toString()}",Toast.LENGTH_LONG).show()
        }



    }



    private fun connectionAlert() {
        dataBinding.webview.visibility = View.GONE
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet")
        builder.setMessage("Connection To Internet !")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Retry"
        ) { dialog, which ->
            if (checkConnection()) {
                if (dataBinding.webview.url.equals("")) {
                    if (checkConnection()) {
                        viewModel.getHtmlForWebView()
                    } else {
                        connectionAlert();
                    }

                } else {
                    if (checkConnection()) {
                        viewModel.getHtmlForWebView()
                    } else {
                        connectionAlert();
                    }
                }
            } else {
                dataBinding.webview.visibility = View.VISIBLE
                if (dataBinding.webview.url == null) {
                    if (checkConnection()) {
                        viewModel.getHtmlForWebView()
                    } else {
                        connectionAlert();
                    }
                } else {
                    if (checkConnection()) {
                        viewModel.getHtmlForWebView()
                    } else {
                        connectionAlert();
                    }
                }
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> finish() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun checkConnection(): Boolean {
        return ConnectivityReceiver.isConnected()
    }
}