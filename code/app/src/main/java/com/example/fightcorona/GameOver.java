package com.example.fightcorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOver extends AppCompatActivity {
    String scoretotal="";
    SQLiteDatabase highScoreDB;
    String nameString="Unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Button replay = (Button)findViewById(R.id.replay);
        Button exit = (Button)findViewById(R.id.exit);
        EditText editName =(EditText)findViewById(R.id.name);
        TextView score = (TextView) findViewById(R.id.score);
        TextView printscore = (TextView) findViewById(R.id.dbtext);
        Bundle extras = getIntent().getExtras();
        scoretotal= extras.getString("score");
        score.append(scoretotal);
        nameString=editName.getText().toString();



        highScoreDB.execSQL("CREATE TABLE IF NOT EXISTS TutorialsPoint(Username VARCHAR,Password VARCHAR);");
        highScoreDB.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");



        printscore.setText(tableToString(highScoreDB,"Score"));





        //Toast.makeText(GameOver.this,"Game Over"+scoretotal,Toast.LENGTH_LONG).show();
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent repalyIntent = new Intent(getApplicationContext(),MainActivity.class);
                repalyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(repalyIntent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highScoreDB.execSQL("INSERT INTO Scores VALUES('\"+nameString+\"','admin');");
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
    }
    public String tableToString(SQLiteDatabase highScoreDB, String tableName) {
        Log.d("","tableToString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = highScoreDB.rawQuery("SELECT * FROM " + tableName, null);
        tableString += cursorToString(allRows);
        return tableString;
    }
    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }
}
