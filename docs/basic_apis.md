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

## user
The `user` method is  quite important for conversion API's. This API allows you to build you ID graph on the edge, which allows you to persist data.
Note: User consent needs to be implement to protect users.

#### Input
`user(key: String, value: String, completionHandler: CompletionHandler)`

|||||
|---|---|---|---|
| `key` | `String` | Required | Which user info are you sending.Allowed values: email, phone, firstName, lastName, gender, dateOfBirth, country, state, city, zip |
| `value` | `String` | Required | Value that you would like to associate with user key. |
| `comletionHandler` | `CompletionHandler`| Return callback for sdk success and failure|

#### Example
```Java
EdgeTag.INSTANCE.user("email","me@domain.com", new CompletionHandler() {
@Override
public void onSuccess() {
        user_response.setText("Success");
        }

@Override
public void onError(int code, String msg) {
        user_response.setText(msg);
        }
        });
```


