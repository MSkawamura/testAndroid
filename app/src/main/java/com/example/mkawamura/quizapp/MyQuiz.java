package com.example.mkawamura.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MyQuiz extends AppCompatActivity {

    //intentで渡す定数を定義
    public final static String EXTRA_MYSCORE = "com.example.mkawamura.quizapp.MYSCORE";
    //Quiz.txtファイルのデータを格納する変数
    private ArrayList<String[]> quizSet = new ArrayList<String[]>();    //配列型のデータ？

    //Viewの使用を宣言する
    private TextView scoreText;     //スコア表示View
    private TextView qText;         //質問テキスト表示View
    private Button a0Button;        //回答ボタンa0View
    private Button a1Button;        //回答ボタンa1View
    private Button a2Button;        //回答ボタンa2View
    private Button nextButton;      //次の質問へ移動するボタンView

    //何問目の質問かを管理する変数
    private int currentQuiz = 0;
    //正答数を保持する変数
    private int score = 0;

    //アプリ起動時に実行するメソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);

        //Quiz.txtファイルを読み込むメソッド
        loadQuizSet();

        //Viewを取得するメソッド
        getViews();

        //質問文と回答文をViewに設定するメソッド
        setQuiz();
    }

    //正答数を表示する
    private void showScore() {
        scoreText.setText("score: " + score + " / " + quizSet.size());  //  正答数のテキスト表示をViewに設定する

    }

    //質問文と回答文を設定するメソッド？
    private void setQuiz() {
        //質問文を設定する
        qText.setText(quizSet.get(currentQuiz)[0]);

        //回答文を設定する
        ArrayList<String> answers = new ArrayList<String>();    //回答文を格納する配列を宣言(インスタンス化)
        for (int i = 1; i <= 3; i++) {                          //回答文を格納する
            answers.add(quizSet.get(currentQuiz)[i]);
        }
        Collections.shuffle(answers);                           //回答文の並び順をランダムにする

        a0Button.setText(answers.get(0));       //１番目のボタンを設定する
        a1Button.setText(answers.get(1));       //２番目のボタンを設定する
        a2Button.setText(answers.get(2));       //３番目のボタンを設定する

        //回答ボタンを有効にする(次の質問へ移った際、無効化を解除する)
        a0Button.setEnabled(true);
        a1Button.setEnabled(true);
        a2Button.setEnabled(true);
        //次の質問へのボタンを無効化する(回答後,有効化する)
        nextButton.setEnabled(false);

        //正答数を表示するメソッド
        showScore();
    }

    //回答ボタンをタップした時に実行するメソッド
    public void checkAnswer(View view) {
        //answer?(タップされたボタンの回答文を取得する)
        Button clickedButton = (Button) view;       //タップされたボタンを取得する
        String clickedAnswer = clickedButton.getText().toString();      //取得したボタンに記述された回答文を文字列型で取得する

        //judge(取得した回答文が正解がどうか判定する)
        if(clickedAnswer.equals(quizSet.get(currentQuiz)[1])) {
            clickedButton.setText("○ " + clickedAnswer);    //正解の場合,ボタンに○を表示する
            score++;        //正答数を保持する変数の値を1増やす
        } else {
            clickedButton.setText("× " + clickedAnswer);    //不正解の場合,ボタンに×を表示する
        }
        showScore();    //正答数を表示するメソッド

        //button(回答後,回答ボタンを押せないように、無効化する)
        a0Button.setEnabled(false);
        a1Button.setEnabled(false);
        a2Button.setEnabled(false);
        nextButton.setEnabled(true);

        //next quiz(currentQuiz変数を1増やす)
        currentQuiz++;

        //全質問数と現在の質問数(currentQuiz)が同じ場合、nextButtonのテキストを切り替える
        if (currentQuiz == quizSet.size()) {
            nextButton.setText("Check result");
        }
    }

    //NEXTボタンが押された時、実行するメソッド(次の質問へ移動する)
    public void goNext(View view) {
        //全質問数と現在の質問数(currentQuiz)が同じ値かどうか(結果画面か、次の質問か分岐する処理)
        if (currentQuiz == quizSet.size()) {
            //結果画面へ移動する
            Intent intent = new Intent(this, MyResult.class);
            intent.putExtra(EXTRA_MYSCORE, score + " / " + quizSet.size());
            startActivity(intent);
        } else {
            //次の質問へ移動する
            setQuiz();      //質問文と回答文をViewに設定するメソッド(次の質問を設定する)
        }
    }

    //このActivityが再度アクティブになった時に実行するメソッド
    @Override
    public void onResume() {
        super.onResume();
        nextButton.setText("Next");     //nextButtonViewのテキストを"Next"に切り替える
        currentQuiz = 0;        //現在の質問数を0にする
        score = 0;
        setQuiz();              //質問文と回答文を設定する
    }

    //Viewを取得するメソッド
    private void  getViews() {
        scoreText = (TextView) findViewById(R.id.scoreText);    //スコア表示View
        qText = (TextView) findViewById(R.id.qText);            //質問テキスト表示View
        a0Button = (Button) findViewById(R.id.a0Button);        //回答ボタンa0View
        a1Button = (Button) findViewById(R.id.a1Button);        //回答ボタンa1View
        a2Button = (Button) findViewById(R.id.a2Button);        //回答ボタンa2View
        nextButton = (Button) findViewById(R.id.nextButton);    //次の質問へ移動するボタンView
    }

    //全体的に何してるかわからないメソッド。
    //quiz.txtのデータを１行ずつ読み込み、String型の配列にして、quizSet変数に格納するメソッド
    private void loadQuizSet() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        //例外処理？
        try {
            inputStream = getAssets().open("quiz.txt");     //quiz.txtから読み込む準備をする？
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));    //bufferedReaderのインスタンス化
            String s;   //変数の宣言(quiz.txtから読み込んだ文字列を入れる変数)
            while ((s = bufferedReader.readLine()) != null) {  //quiz.txtのデータを1行ずつ読み込む
                quizSet.add(s.split("\t"));     //１行ごとに取得したquiz.txtのデータを配列にしてquizSetに格納する
            }
            //?
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


