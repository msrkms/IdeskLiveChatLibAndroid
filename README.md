

# IdeskLiveChat Android SDK

The IdeskLiveChat Android SDK allows you to easily integrate live chat functionality into your Android application. This README provides an overview of how to use the library and get started with live chat in your app.

## Installation

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```


To use the IdeskLiveChat Android SDK, you'll need to add it as a dependency in your Android project. You can do this by adding the following lines to your app's `build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.Codeware-Ltd:IdeskLiveChatAndroid:1.0.0'
}
```


## Usage

To open the chat window when a button is clicked, follow these steps:

1. Import the necessary classes at the top of your Kotlin file:

```kotlin
import com.idesk360.livechatsdk.IdeskLiveChat
import com.idesk360.livechatsdk.model.IdeskChatSDKConfig
```

2. Inside your button's click listener, create an `IdeskChatSDKConfig` object with the required parameters:

```kotlin
val sdkConfig = IdeskChatSDKConfig(
    resourceUri = "xxxx.idesk360.com",
    appUri = "xxx.idesk360.com",
    pageId = "xxxxxxx",
    miscellaneous = mapOf("float" to 0),
    customerInfo = mapOf("name" to "xxx", "rmn" to "xxxxx")
)
```

Replace the values with your actual configuration provided by [Idesk360](https://www.idesk360.com/).


3. Initialize the `IdeskLiveChat` object with the `IdeskChatSDKConfig` and the current activity, and then open the chat window:

```kotlin
IdeskLiveChat(sdkConfig, context).OpenChatWindow()
```

## Additional Configuration

You can further customize the chat window and behavior by modifying the `IdeskChatSDKConfig` object with additional options as needed.

## License
This SDK is licensed under the [MIT License.](https://opensource.org/license/mit/)



## Support

If you encounter any issues or have questions, [please contact our support team.](https://www.idesk360.com/contact/)
