package com.example.mkawamura.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MyResult extends AppCompatActivity {

    //このActivityが起動した時に実行するメソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_result);

        //Viewを取得
        TextView resultText = (TextView) findViewById(R.id.resultText);
        //MyQuizActivityから渡されたIntentを取得
        Intent intent = getIntent();
        //Intentで渡された値をresultTextViewに設定する
        resultText.setText(intent.getStringExtra(MyQuiz.EXTRA_MYSCORE));
    }

    //結果画面の"Result?"ボタンが押された時に実行するメソッド
    public void goBack(View view) {
        finish();       //MyResultのActivityを終了させて、MyQuizActivityに遷移する
    }
}
