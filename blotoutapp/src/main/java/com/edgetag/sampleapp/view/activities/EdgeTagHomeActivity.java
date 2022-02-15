package com.edgetag.sampleapp.view.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.edgetag.sampleapp.R;
import com.edgetag.EdgeTag;
import com.edgetag.EdgeTagConfiguration;
import com.edgetag.model.CompletionHandler;
import com.google.gson.Gson;

import java.util.HashMap;

public class EdgeTagHomeActivity extends AppCompatActivity {

  Button initPress;
  ToggleButton toggleButton;
  TextView initResponse;
  Button consent_button_press;
  EditText consent_text;
  TextView consent_response;
  Button provider_button_press;
  EditText tag_key;
  EditText provider_key;
  TextView tag_response;
  Gson gson;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edgetag);

    EdgeTagConfiguration edgetagConfiguration = new EdgeTagConfiguration();
    edgetagConfiguration.setEndPointUrl("https://sdk-demo-t.edgetag.io");

    initPress = findViewById(R.id.init_button_press);
    toggleButton = findViewById(R.id.disableConsentCheckToggleButton);
    initResponse = findViewById(R.id.init_response);
    consent_button_press = findViewById(R.id.consent_button_press);
    consent_text = findViewById(R.id.consent_text);
    consent_response = findViewById(R.id.consent_response);
    provider_button_press = findViewById(R.id.provider_button_press);
    tag_key = findViewById(R.id.tag_key);
    provider_key = findViewById(R.id.provider_key);
    tag_response = findViewById(R.id.tag_response);

    HashMap<String,Boolean> consentHashMap = new HashMap<>();
    consentHashMap.put("facebook",true);
    consentHashMap.put("smart",false);

    HashMap<String,Object> tag = new HashMap<>();
    tag.put("value","10.00");
    tag.put("currency","USD");

    HashMap<String,Boolean> provider = new HashMap<>();
    provider.put("facebook",true);
    provider.put("smart",true);

    gson = new Gson();
    consent_text.setText(gson.toJson(consentHashMap));
    tag_key.setText(gson.toJson(tag));
    provider_key.setText(gson.toJson(provider));


    initPress.setOnClickListener(v -> {
      if (toggleButton.isChecked()) {
        edgetagConfiguration.setDisableConsentCheck(true);
      } else {
        edgetagConfiguration.setDisableConsentCheck(false);
      }
      EdgeTag.INSTANCE.init(getApplication(), edgetagConfiguration, new CompletionHandler() {
        @Override
        public void onSuccess() {
          initResponse.setText("Success");
        }

        @Override
        public void onError(int code, String msg) {
          initResponse.setText(msg);
        }
      });
    });

    consent_button_press.setOnClickListener(v->{
      HashMap<String,Boolean> consentData = gson.fromJson(consent_text.getText().toString(),HashMap.class);
      EdgeTag.INSTANCE.consent(consentData, new CompletionHandler() {
        @Override
        public void onSuccess() {
          consent_response.setText("Success");
        }

        @Override
        public void onError(int code, String msg) {
          consent_response.setText(msg);
        }
      });
    });

    provider_button_press.setOnClickListener(v->{
      HashMap<String,Boolean> providerData = gson.fromJson(provider_key.getText().toString(),HashMap.class);
      HashMap<String,Object> tagData = gson.fromJson(tag_key.getText().toString(),HashMap.class);
      EdgeTag.INSTANCE.tag("EventName",tagData,providerData, new CompletionHandler() {
        @Override
        public void onSuccess() {
          tag_response.setText("Success");
        }

        @Override
        public void onError(int code, String msg) {
          tag_response.setText(msg);
        }
      });
    });

  }

}
