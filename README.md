# EdgeTag Android SDK

[![](https://jitpack.io/v/blotoutio/edgetag-android.svg)](https://jitpack.io/#blotoutio/edgetag-android)


# Documentation

## Add Required Gradle Dependency and libraries
```kotlin
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation "com.github.blotoutio:edgetag-android:0.1.0"
```


# Basic API's

## init
The `init` method is used for initializing SDK. This prepared all required configurations .

#### Input
`EdgeTag.INSTANCE.init(this, edgeTagConfiguration, new CompletionHandler())`

|||||
|---|---|---|---|
| `applicationContext` | `Object` | Application Context |
| `edgeTagConfiguration` | `EdgeTagConfiguration` | This Model contains information related to SDK initialization |
| `comletionHandler` | `CompletionHandler`| Return callback for sdk success and failure|

## EdgeTagConfiguration

|||||
|---|---|---|---|
| `setEndPointUrl` | `String` | Required | Url where you will be sending data. |
|`disableConsentCheck` | `Boolean` | Optional | default: `false`. Additional layer of consent that the developer can program in. |


#### Example
```java
        EdgeTagConfiguration edgeTagConfiguration = new EdgetagConfiguration();
        edgeTagConfiguration.setEndPointUrl("https://sdk-demo-t.edgetag.io");
        EdgeTag.INSTANCE.init(this, edgeTagConfiguration, new CompletionHandler() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess() {

            }
        });
```

## consent
The `consent` method is used to record consent events. This allows you to send tag events to the server

#### Input
`consent( consentInfo:HashMap<String, Boolean>,comletionHandler())`

|||||
|---|---|---|---|
| `consentInfo` | `Object` | Optional | You can provide consent to this event. There is no limitation as this is just a key-value pair send to the server. |
| `comletionHandler` | `CompletionHandler`| Return callback for sdk success and failure|
#### Example
```Java
HashMap<String,Object> consentInfo = new HashMap<>();
consentInfo.put("facebook","true");
EdgeTag.INSTANCE.consent(consentInfo,comletionHandler());
```

## tag
The `tag` method is used to record events. This allows you to send tag events to the server

#### Input
`tag( eventName:String,eventInfo:HashMap<String, Any>?,providerInfo:HashMap<String, Boolean>?)`

|||||
|---|---|---|---|
| `eventName` | `String` | Required | Name of the event that you are sending |
| `eventInfo` | `Object` | Required | You can provide some additional data to this event. There is no limitation as this is just a key-value pair send to the server. |
| `providerInfo` | `Object` | Optional | You can provide consent to this event|
| `comletionHandler` | `CompletionHandler`| Return callback for sdk success and failure|

#### Example
```Java
HashMap<String,Boolean> providerInfo = new HashMap<>();
providerInfo.put("facebook","true");

HashMap<String,Boolean> eventInfo = new HashMap<>();
eventInfo.put("facebook","capture");


EdgeTag.INSTANCE.tag("EventName ",eventInfo,providerInfo);
```

