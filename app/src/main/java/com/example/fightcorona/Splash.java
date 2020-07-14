package com.example.fightcorona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Splash extends AppCompatActivity {
    int tapcount=0;
    RelativeLayout RL;
    Button B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
            RL=(RelativeLayout)findViewById(R.id.rl1);
            B= (Button) findViewById(R.id.btn);

            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tapcount++;
                    if (tapcount==1){
                        B.setText("Play Now");
                        RL.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.sp2m));

                    }
                    if(tapcount==2){
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        Bundle extras = new Bundle();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);


                        intent.putExtras(extras);
                        startActivity(intent);
                    }

                }
            });
    }



    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {





        }
        return  true;
    }
}
