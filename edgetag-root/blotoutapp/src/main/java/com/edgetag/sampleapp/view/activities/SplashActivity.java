

package com.edgetag.sampleapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.edgetag.EdgeTag;
import com.edgetag.sampleapp.R;
import com.edgetag.model.CompletionHandler;

import java.util.HashMap;


public class SplashActivity extends FragmentActivity {

    private Animation animation;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo_img);
        if (savedInstanceState == null) {
            flyIn();
        }
    }

    private void flyIn() {
        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(animation);
    }



    public void launchBlotoutApp(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Launch Blotout app",0);
        EdgeTag.INSTANCE.tag("custom event",eventInfo,null, new CompletionHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });
        Intent intent = new Intent(getApplicationContext(), ECartHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void getDemo(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Get Blotout Demo",0);
        EdgeTag.INSTANCE.tag("custom",eventInfo,null, new CompletionHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, "sales@blotout.io");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Get Demo");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Select email"));
    }

    public void joinSlack(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Join Blotout Slack",0);

        HashMap<String,Boolean> provider = new HashMap<>();
        provider.put("Slack",true);

        EdgeTag.INSTANCE.tag("custom",eventInfo,provider, new CompletionHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });
    }

}
