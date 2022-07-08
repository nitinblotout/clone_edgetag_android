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
| `comletionHandler` | `CompletionHandler`| Required |Return callback for sdk success and failure|

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
| `comletionHandler` | `CompletionHandler`| Required |Return callback for sdk success and failure|
#### Example
```Java
HashMap<String,Object> consentInfo = new HashMap<>();
consentInfo.put("facebook","true");
EdgeTag.INSTANCE.consent(consentInfo,comletionHandler());
```

## tag
The `tag` method is used to record events. This allows you to send tag events to the server

#### Input
`tag( eventName:String,eventInfo:HashMap<String, Any>?,providerInfo:HashMap<String, Boolean>?,comletionHandler())`

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

## User
User data is quite important for conversion APIs. This API allows you to build your ID graph on the edge, which allows you to persist data.
Note: User consent needs to be implemented to protect users.

#### Input
`user(key: String, value: String, completionHandler: CompletionHandler)`

|||||
|---|---|---|---|
| `key` | `String` | Required | Which user info are you sending.
Allowed values: email, phone, firstName, lastName, gender, dateOfBirth, country, state, city, zip, address |
| `value` | `String` | Required | Value that you would like to associate with the user key. |
| `comletionHandler` | `CompletionHandler`| Required |Return callback for API success and failure|

#### Example
```Java
EdgeTag.INSTANCE.user("email", "me@domain.com", new CompletionHandler() {
@Override
public void onSuccess() {
        }

@Override
public void onError(int code, String msg) {
        }
        });
```

## Data
Sometimes you would like to send more than one thing or store data that you would like to persist for the user's next visit.
You can use this API if you would like to save multiple user records at the same time.

#### Input
`postData(data: HashMap<String, Any>, onComplete: OnComplete)`

|||||
|---|---|---|---|
| `data` | `HashMap<String, String> ` | Required | Data that you would like to persist on the edge. |
| `onComplete` | `OnComplete`| Required |Return callback for API success response and failure|

#### Example
```Java
HashMap<String, Object> data = new HashMap<>();
        data.put("email", "me@abckl.ij");
        data.put("value1", "1000");
        data.put("currency", "USD");
        data.put("newuser", "false");
        EdgeTag.INSTANCE.postData(data, new OnComplete() {
@Override
public void onSuccess(@NonNull Object msg) {
        
        }

@Override
public void onError(int code, @NonNull String msg) {
        }
        });
```

## Get Data
This API allows you to retrieve data from the edge storage that you send via user or data API.

#### Input
` getData(keys: ArrayList<String>, onComplete: OnComplete)`

|||||
|---|---|---|---|
| `keys` | `ArrayList` | Required | Keys that you would like to get value of from the edge. |
| `onComplete` | `OnComplete`|  Required | Return callback for API success response and failure|

#### Example
```Java
ArrayList<String> key = new ArrayList<>();
        key.add("email");
        EdgeTag.INSTANCE.getData(key, new OnComplete() {
@Override
public void onSuccess(@NonNull Object msg) {
        //return HashMap<String,String> or Empty Hashmap
        }

@Override
public void onError(int code, @NonNull String msg) {
        }
        });
```

## Keys
If you maybe forgot which keys you already sent to the edge to persist for the user, you can use this API to retrieve them.

#### Input
` getKeys(onComplete: OnComplete)`

|||||
|---|---|---|---|
| `onComplete` | `OnComplete`|  Required | Return callback for API success response and failure|

#### Example
```Java
EdgeTag.INSTANCE.getKeys(new OnComplete() {
@Override
public void onSuccess(@NonNull Object msg) {
        //return JSON string or Empty Json
        }

@Override
public void onError(int code, @NonNull String msg) {
        }
        });
```

