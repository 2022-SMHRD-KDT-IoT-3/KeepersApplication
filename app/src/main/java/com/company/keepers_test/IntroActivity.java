package com.company.keepers_test;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_activity);

        //firebase
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.w("Main", "토큰 가져오는 데 실패", task.getException());
                    return;
                }

                String newToken = task.getResult();
                Log.v("token", newToken);
            }
        });

        //GIF 파일 재생
        ImageView intro = (ImageView) findViewById(R.id.intrologo);
        Glide.with(this).load(R.raw.intrologo).into(intro);

        //핸들러 등록, 화면 옮기기
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

