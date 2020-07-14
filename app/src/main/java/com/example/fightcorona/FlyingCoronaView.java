package com.example.fightcorona;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static java.util.Calendar.getInstance;

public class FlyingCoronaView extends View {
    int occurance=2 ;
    int jsindex=0;
    private Bitmap Docter[] = new Bitmap[2];
    private Bitmap  Background;
    private Bitmap Corona[]= new  Bitmap[occurance];
    private  Bitmap Med[]= new Bitmap[occurance];
    private  Bitmap Mask[]= new Bitmap[occurance];
    private  Bitmap Wash[] = new Bitmap[occurance];
    private  int flagsocial=0,flagstates=0;
    int already_have=0;
    private  Bitmap Life[] = new Bitmap[2];
    private  Paint scoreBand = new Paint();
    private  Paint immunityBoard= new Paint();
    private  Paint levelBoard = new Paint();
    private  Paint casesBoard = new Paint();
    private  Paint CurretCorona = new Paint();
    TextToSpeech tts ;
    int locations_no=0;
   
    private  Bitmap State ;
    Paint stateBg= new Paint();
    Paint printName= new Paint();
    JSONObject api_opject;
    String stateName;
    int jsonstatus =-1;
    Paint immunity = new Paint();
      int DocX=10, DocY, DocSpeed, canvasHeight,canvasWidth,virusSpeed=16,score;
    int stateX,stateY;
    private  int medSpeed=12, Unispeed=16;
    int no_of_masks=0, no_of_wash=0, no_of_virus=0, no_of_med=0;
    int virusX[] = new int[occurance];
    int virusY[]= new int[occurance];
    int medX[]= new int[occurance];
    int medY[]= new int[occurance];
    int washX[]= new  int[occurance];
    int washY[]= new int[occurance];
    int maskX[]= new int[occurance];
    int maskY[]= new int [occurance];
    private  int social_frequency=20;
    private  int lifeCounter=3;
    private  int total_immunity_lost=0;
    private int total_elements=0;
     long cases;
    int caseTotal;
    int CasesDischarged;
    String jsurl;
    JSONObject Data= null;
    int  CasesDeath;
    private  boolean Touch=false;
    int immunity_gradient=20;
    int immunity_gained=0;
    int pre_no_of_element=0;
    int level=1;
    public FlyingCoronaView(Context context) {
        super(context);
        jsurl= "https://api.rootnet.in/covid19-in/stats/latest";

        Docter[0] = BitmapFactory.decodeResource(getResources(),R.drawable.police);
        Docter[1] = BitmapFactory.decodeResource(getResources(),R.drawable.doc);
        Background  = BitmapFactory.decodeResource(getResources(),R.drawable.bg23m);
        State  = BitmapFactory.decodeResource(getResources(),R.drawable.stats);
        int i;
        for(i = 0; i<1; i++) {
            Corona[i] = BitmapFactory.decodeResource(getResources(), R.drawable.corona15);
            Med[i] = BitmapFactory.decodeResource(getResources(), R.drawable.med);
            Mask[i] = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
            Wash[i] = BitmapFactory.decodeResource(getResources(), R.drawable.wash);
        }
        Wash[1]= BitmapFactory.decodeResource(getResources(),R.drawable.social);
        for( i =0;i<occurance;i++){
            maskY[i]=0;
            maskX[i]=0;
            virusY[i]=0;
            virusX[i]=0;
            washX[i]=0;
            washY[i]=0;
            medX[i]=0;
            medY[i]=0;
            washX[1]=0;
        }
        // Med = BitmapFactory.decodeResource(getResources(),R.drawable.med);
        //  Med = BitmapFactory.decodeResource(getResources(),R.drawable.med);



        scoreBand.setColor(Color.WHITE);
        scoreBand.setTextSize(50);
        scoreBand.setAntiAlias(true);
        scoreBand.setTypeface(Typeface.DEFAULT_BOLD);

        immunityBoard.setColor(Color.WHITE);
        immunityBoard.setTextSize(40);
        immunityBoard.setAntiAlias(true);
        immunityBoard.setTypeface(Typeface.DEFAULT_BOLD);

        levelBoard.setColor(Color.WHITE);
        levelBoard.setTextSize(50);
        levelBoard.setAntiAlias(true);
        levelBoard.setTypeface(Typeface.DEFAULT_BOLD);

        casesBoard.setColor(Color.WHITE);
        casesBoard.setTextSize(50);
        casesBoard.setAntiAlias(true);
        casesBoard.setTypeface(Typeface.DEFAULT_BOLD);

        printName.setColor(Color.WHITE);
        printName.setTextSize(30);
        printName.setAntiAlias(true);
        printName.setTypeface(Typeface.DEFAULT_BOLD);



        Life[0]= BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        Life[1]= BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);
        DocY=420;
        score=0;


        if(already_have==0) {
            new JsonTask().execute(jsurl);


        }



    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pre_no_of_element=total_elements;

        if(total_elements%social_frequency==0){
           // total_elements=0;
            flagsocial=1;
            flagstates=1;
        }
        canvas.drawBitmap(Background,0,0,null);

        immunity.setColor(Color.BLACK);
        immunity.setStrokeWidth(3);
        immunity.setColor(Color.GREEN);


        canvasHeight= canvas.getHeight();
        canvasWidth= canvas.getWidth();


        int minDocY= Docter[0].getHeight();
        int maxDocY= canvasHeight- Docter[0].getHeight();


        DocY= DocY+DocSpeed;
        if(DocY<minDocY){
            DocY=minDocY;
        }
        if(DocY>maxDocY){
            DocY=maxDocY;

        }

        DocSpeed+=2;
        if(Touch){
            canvas.drawBitmap(Docter[0],DocX,DocY,null);
            Touch=false;
        }
        else {
            canvas.drawBitmap(Docter[0],DocX,DocY,null);
        }
        if(100-total_immunity_lost<0)
        {

            total_immunity_lost=0;
        }





        virusSpeed= 12+score%10;


        washX[0] = washX[0 ] - Unispeed;
        for(int i=0;i<1;i++) {

            if(HItBallChecker(washX[i],washY[i])){
                score+=5;
                washX[i]=-100;
            }
        }
       if(flagsocial==1) {
           washX[1] = washX[1] - Unispeed;
           if (HItBallChecker(washX[1], washY[1])) {
               score -=20;
               Vibrator v = (android.os.Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
               v.vibrate(100);
               washX[1] = -100;
           }
       }
        maskX[0] = maskX[0] - Unispeed;
        for (int i=0;i<1;i++) {

            if(HItBallChecker(maskX[i],maskY[i])){
                score+=7;
                maskX[i]=-100;
            }

        }
        virusX[0] = virusX[0] - virusSpeed;
        for (int i=0;i<1;i++) {

            if(HItBallChecker(virusX[i],virusY[i]))
            {

                Vibrator v = (android.os.Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
                v.vibrate(400);
                score-=10;
                virusX[i]=-100;
                lifeCounter--;
                if(lifeCounter==0){
                   // String strI = String.valueOf(score);
                   Toast.makeText(getContext(),"Game Over "+score,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), GameOver.class);
                    Bundle extras = new Bundle();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    extras.putString("score",Integer.toString(score));

                    intent.putExtras(extras);
                    getContext().startActivity(intent);

                }
            }
        }


        medX[0] = medX[0] - medSpeed;
        for(int i=0;i<1;i++) {

            if(HItBallChecker(medX[i],medY[i]))
            {
                score+=10;
                total_immunity_lost-=immunity_gradient;

                medX[i]=-100;
            }
        }

        if(flagstates==1) {
            stateX= stateX-Unispeed;
            if (HItBallChecker(stateX, stateY)) {
                score += 15;
                stateX = -100;
                locations_no++;
                TextToSpeech t1;
                t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {


                    }
                });


               // Toast.makeText(getContext(), "hello",Toast.LENGTH_SHORT).show();
                t1.speak("hi", TextToSpeech.QUEUE_FLUSH, null);

                if(already_have==0) {
                    new JsonTask().execute(jsurl);


                }
                else {

                    if(already_have==0)
                    new JsonTask().execute(jsurl);

                    else
                        getAPI();
                }

            }
        }








level=getLevel(score);

        canvas.drawText("Score: "+score+" Locations: "+locations_no,20,90,scoreBand);
        canvas.drawText("Immunity"+(100-(total_immunity_lost)),20,140,immunityBoard);
        canvas.drawText("Level"+level,canvasWidth-500,160,levelBoard);





        for(int i =0;i<3;i++){
            int x=(int) (580+Life[0].getWidth()*1.5*i);
            int y =30;
            if(i<lifeCounter){
                canvas.drawBitmap(Life[0],x,y,null);
            }
            else {
                canvas.drawBitmap(Life[1 ],x,y,null);
            }
        }
        long time= (System.currentTimeMillis()/1000)%8;



            int random = new Random().nextInt((10 - 0) + 1) + 0;
            if ((virusX[0]<0)) {
                total_elements++;


                virusX[no_of_virus] = canvasWidth + 21;
                virusY[no_of_virus] = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;



            }
            if (medX[0]<0) {
                total_elements++;
                medX[no_of_med] = canvasWidth + 21;
                medY[no_of_med] = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;


            }
            if ((washX[0]<0)) {
                total_elements++;
                washX[no_of_wash] = canvasWidth + 21;
                washY[no_of_wash] = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;


            }


            if ((washX[1] < 0)) {
                flagsocial=0;
                washX[1] = canvasWidth + 21;
                washY[1] = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;


            }

            if (maskX[0]<0) {
                total_elements++;
                maskX[no_of_masks] = canvasWidth + 21;
                maskY[no_of_masks] = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;


            }
        if (stateX<0) {
            flagstates=0;
            total_elements++;
            stateX = canvasWidth + 21;
            stateY = (int) Math.floor(Math.random() * (maxDocY+50 - minDocY)) + minDocY;


        }
        canvas.drawBitmap(Med[0], medX[0], medY[0], null);
        canvas.drawBitmap(Corona[0], virusX[0], virusY[0], null);
        canvas.drawBitmap(Wash[0], washX[0], washY[0], null);


        if(flagstates==1) {
            canvas.drawBitmap(State, stateX, stateY, null);
        }
        if(flagsocial==1) {
            canvas.drawBitmap(Wash[1], washX[1], washY[1], null);


        }
        canvas.drawBitmap(Mask[0], maskX[0], maskY[0], null);

        if(100-total_immunity_lost<70)
        immunity.setColor(Color.YELLOW);

       if(100-total_immunity_lost<50)
            immunity.setColor(Color.BLUE);
        if(100-total_immunity_lost<20)
            immunity.setColor(Color.RED);

        canvas.drawRect(0, 180, (canvasWidth/100)*(100-total_immunity_lost), 200, immunity );

        canvas.drawRect(0, 300, (canvasWidth), 200, CurretCorona );
        stateBg.setColor(Color.TRANSPARENT);
        stateBg.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
    //    canvas.drawRect(0, canvasHeight, (canvasWidth), canvasHeight-200,  stateBg);



        canvas.drawText("Total: "+caseTotal+""+" Recovered "+CasesDischarged+ " Died: "+CasesDeath,40,270,casesBoard);
        total_immunity_lost+=total_elements-pre_no_of_element;
        if(100-total_immunity_lost>100)
            total_immunity_lost=0;
        if(100-total_immunity_lost<0){
            Toast.makeText(getContext(),"Game Over "+score,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), GameOver.class);
            Bundle extras = new Bundle();
            extras.putString("score",Integer.toString(score));

            intent.putExtras(extras);
            getContext().startActivity(intent);
        }
        Log.i("Counts", "M get item number " + total_elements);





    }
    public  boolean HItBallChecker(int x, int y){

        if(DocX<x && x<(DocX+Docter[0].getWidth()) && DocY<y && y<(DocY+ Docter[0].getHeight())){

            return  true;
        }
        return  false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Touch=true;
            DocSpeed=-22;



        }
        return  true;
    }
    public  int getLevel(int s){
        int level=0;

        if(s<=100){
            level=1;
            immunity_gradient=20;
            virusSpeed=16;
            Unispeed = 16;

        }
        if(s>100){
            level=2;
            immunity_gradient=10;
            virusSpeed=18;
            Unispeed = 16;

        }
        if(s>200){
            level=3;
            immunity_gradient=7;
            virusSpeed=20;
            Unispeed=20;
        }

        return  level;
    }

    void getAPI(){



        try {
            if(jsonstatus== -1){
                caseTotal= Data.getJSONObject("data").getJSONObject("summary").getInt("total");
                CasesDischarged= Data.getJSONObject("data").getJSONObject("summary").getInt("discharged");
                CasesDeath= Data.getJSONObject("data").getJSONObject("summary").getInt("deaths");
            }

            else
            {

                JSONArray jA = Data.getJSONObject("data").getJSONArray("regional");
                jsonstatus = new Random().nextInt((  jA.length() - 0) + 1) + 0;

                stateName= jA.getJSONObject(jsonstatus).getString("loc");
                Log.i("LOca", "Random " + jA.length());

                for (int i=0; i < 7; i++)
                    Toast.makeText(getContext(),"Corona cases in "+stateName+" Total: "+jA.getJSONObject(jsonstatus).getInt("totalConfirmed")+""+" Recovered "+ jA.getJSONObject(jsonstatus).getInt("discharged")+ " Died: "+jA.getJSONObject(jsonstatus).getInt("deaths"),Toast.LENGTH_LONG).show();
            }
            jsonstatus++;


        } catch (JSONException e) {
            jsonstatus--;
            e.printStackTrace();
        }
    }
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            if(!isConnectedToInternet()) {
                return "";
            }

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                Data = new JSONObject(result);
                already_have=1;
                getAPI();;
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
    public boolean isConnectedToInternet(){

        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }



}
