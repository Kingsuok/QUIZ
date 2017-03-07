package su.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbOperation.TableCompletionOperation;
import dbOperation.TableQuestionOperation;
import dbOperation.TableSetTimeOperation;
import dbOperation.TableTakersOperation;
import exitApplication.ExitApplication;

import static android.R.attr.key;
import static android.R.attr.subMenuArrow;

/**
 * Created by su on 2016/9/30.
 */

public class QTActivity extends AppCompatActivity{
    private TextView timeCount;
    private TextView takerQuestion;
    private RadioButton takerOptionA;
    private RadioButton takerOptionB;
    private RadioButton takerOptionC;
    private RadioButton takerOptionD;
    private RadioGroup takerAnswers;
    private TextView takerName;
    private TextView correctRate;
    private TableTakersOperation tableTakersOperation;
    private TableQuestionOperation tableQuestionOperation;
    private TableSetTimeOperation tableSetTimeOperation;
    private TableCompletionOperation tableCompletionOperation;
    private boolean exchangeQuestion;
    private int finishNum; // count number of finishing question (5 is a cycle)
    private boolean allFinish; // flag for finishing all question
    private Toast toast;
    private int timeLimit;
    private List<Map<String, String>> questionList;
    private HashMap<String, String> oneQuestion;
    private int takerAnswer; // taker's answer, default value is 0
    private int correctAnswer;
    private HashMap<Integer, RadioButton> answerCheck;
    private int currentCorrectNumber; // correct number when a taker is answering questions
    private int currentIncorrectNumber; // incorrect number when a taker is answering questions
    private int correctNumber; // correct number before the taker answers questions
    private int incorrectNumber;// incorrect number before the taker answers questions
    private int loginNumber; // login number before the taker login
    private String takerID;
    private boolean cycleFinish; // flag : whether 5 questions (a cycle) is finished or not
    private boolean backExitDialogLive; // flag : whether the dialog from pressing back key still appear or not
    private int cycleIndex;
    private TimeCount timeCountObject;
    private Button next;
    private Vibrator vibrator;
    private long[] pattern;
    private String settingTime;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qt);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit



        timeCount = (TextView)findViewById(R.id.timeCount);
        takerQuestion = (TextView)findViewById(R.id.takerQuestion);
        takerOptionA = (RadioButton)findViewById(R.id.takerOptionA);
        takerOptionB = (RadioButton)findViewById(R.id.takerOptionB);
        takerOptionC = (RadioButton)findViewById(R.id.takerOptionC);
        takerOptionD = (RadioButton)findViewById(R.id.takerOptionD);
        takerAnswers = (RadioGroup)findViewById(R.id.takerAnswers);
        takerName = (TextView)findViewById(R.id.takerNameDisplayQT);
        correctRate = (TextView)findViewById(R.id.correctRateDisplayQT);
        tableQuestionOperation = new TableQuestionOperation(QTActivity.this,"quizDatabase");
        tableTakersOperation = new TableTakersOperation(QTActivity.this,"quizDatabase");
        tableSetTimeOperation = new TableSetTimeOperation(QTActivity.this,"quizDatabase");
        tableCompletionOperation = new TableCompletionOperation(QTActivity.this,"quizDatabase");
        settingTime = tableSetTimeOperation.getTime();
        timeLimit = Integer.parseInt(settingTime);// get the time to exam
        timeCountObject = new TimeCount(timeLimit*1000, 1000);
        next = (Button)findViewById(R.id.next);

        //timeCountObject = new TimeCount(3000, 1000);
        //takerID = new String();
        exchangeQuestion = false;
        allFinish = false;
        cycleFinish = false;
        backExitDialogLive = false;
        finishNum = 0;
        takerAnswer = 0;
        currentCorrectNumber = 0;
        currentIncorrectNumber = 0;
        answerCheck = new HashMap<Integer, RadioButton>();
        answerCheck.put(1,takerOptionA);
        answerCheck.put(2,takerOptionB);
        answerCheck.put(3,takerOptionC);
        answerCheck.put(4,takerOptionD);
        cycleIndex = 0;


        Intent intent = getIntent();
        takerID = intent.getStringExtra("su.quiz.takerID");
        HashMap<String, String> takerInfor = tableTakersOperation.viewOne(new String[]{takerID});
        correctNumber = Integer.parseInt(takerInfor.get("correctNum"));
        incorrectNumber = Integer.parseInt(takerInfor.get("incorrectNum"));
        loginNumber = Integer.parseInt(takerInfor.get("loginNum"));
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //loginNumber += 1;// login number increases 1


        takerName.setText(takerID + ": ");
        RadioListener radioListener = new RadioListener();
        takerAnswers.setOnCheckedChangeListener(radioListener);
        ClickListener listener = new ClickListener();
        next.setOnClickListener(listener);


        oneQuestion = tableQuestionOperation.oneQuestion(new String[]{takerID});
        if (oneQuestion.isEmpty()){
            toast = Toast.makeText(getApplicationContext(),"Questions have been all finished",Toast.LENGTH_LONG);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ExitApplication.getInstance().exit();
                }
            },2000);
            ExitApplication.getInstance().exit();
        }else {

                updateQuestion(cycleIndex); // update new question
        }
    }

    // button "next"
    class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QTActivity.this);
            builder.setIcon(R.drawable.quiz);
            builder.setTitle("Alert");
            builder.setMessage("Next ?");
            builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timeCountObject.onFinish();// if next, then this question is finished by the taker.
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        }
    }

// this function : deal with the back key, when taker press the back key on the phone, then we should cope with it,
    // if the taker does not want to continue, the qpp will exist and save the statistic before the app exists.
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                backExitDialogLive = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to Exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cycleFinish == false) {// judge whether the question the taker is taking is right or wrong
                            if (takerAnswer != correctAnswer) {
                                currentIncorrectNumber++;
                            } else {
                                currentCorrectNumber++;
                            }
                        }
                        updateTakerInfor(); // save and update the taker's performance
                        //QTActivity.this.finish();// exit and back to the upper activity
                        ExitApplication.getInstance().exit(); // exit the application
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backExitDialogLive = false; // if NO, the taker wants to continue quiz
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show(); // show the dialog
                break;
            }
        return false;
        }

// one CycleAlert : after the taker finished 5 questions, the app will inquire continue ? if no, save and update statistic
    public void oneCycleAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QTActivity.this);
        builder.setIcon(R.drawable.quiz);
        builder.setTitle("Alert");
        builder.setMessage("Continue to quiz or exit ?");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                cycleFinish = false;
                oneQuestion = tableQuestionOperation.oneQuestion(new String[]{takerID});
                if (oneQuestion.isEmpty()){
                    toast = Toast.makeText(getApplicationContext(),"Questions have been all finished",Toast.LENGTH_LONG);
                    toast.show();
                    updateTakerInfor();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ExitApplication.getInstance().exit();
                        }
                    },2000);

                }else {

                    updateQuestion(cycleIndex);
                }
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // store all the statistic
                updateTakerInfor();
                ExitApplication.getInstance().exit(); // exit the application
                //QTActivity.this.finish();// exit and back to the upper activity
            }
        });

        AlertDialog alertDialog = builder.create();

        // these codes may be a test for back key, so if the back key does not effect the function, delete them

        //while (backExitDialogLive == true){ // wait reaction from pressing  the back key

       // }
        alertDialog.show();
    }
// save and update the taker's performance
    public void updateTakerInfor(){
        String numberLogin = Integer.toString(loginNumber + 1);
        String numberCorrect = Integer.toString(correctNumber + currentCorrectNumber);
        String numberIncorrect = Integer.toString(incorrectNumber + currentIncorrectNumber);
        String[] takerScore = new String[]{numberLogin, numberCorrect, numberIncorrect,takerID};
        tableTakersOperation.updateTakerScore(takerScore);
    }
//Judge whether this is a cycle
    public void cycleJudge(){
        takerAnswers.clearCheck();
        cycleIndex++;
        if (cycleIndex % 5 == 0 ){// a cycle has finished
            cycleFinish = true;
            timeCount.setText("0s");
            timeCountObject.cancel();
            oneCycleAlert();
        } else {
            oneQuestion = tableQuestionOperation.oneQuestion(new String[]{takerID});
            if (oneQuestion.isEmpty()){
                toast = Toast.makeText(getApplicationContext(),"Questions have been all finished",Toast.LENGTH_LONG);
                toast.show();
                updateTakerInfor();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ExitApplication.getInstance().exit();
                    }
                },2000);
            }else {
                updateQuestion(cycleIndex);
            }
        }
    }

    // update a question
    public void updateQuestion(int questionNumber){
        //timeCount.setText(settingTime + "s");
        String title = questionNumber + 1 + ". " + oneQuestion.get("title");
        String A = oneQuestion.get("A");
        String B = oneQuestion.get("B");
        String C = oneQuestion.get("C");
        String D = oneQuestion.get("D");
        String answer = oneQuestion.get("answer");
        correctAnswer = Integer.parseInt(answer);
        takerQuestion.setText(title);
        takerOptionA.setText(A);
        takerOptionB.setText(B);
        takerOptionC.setText(C);
        takerOptionD.setText(D);

        timeCountObject.start(); // start timer
        tableCompletionOperation.delete(new String[]{takerID,oneQuestion.get("title")}); // update table completion
        // }
    }


    class RadioListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.takerOptionA){
                takerAnswer = 1;
            }else if (checkedId == R.id.takerOptionB){
                takerAnswer = 2;
            }else if (checkedId == R.id.takerOptionC){
                takerAnswer = 3;
            }else {
                takerAnswer = 4;
            }
        }
    }


// time count
    // CountDown timer class
    class TimeCount extends CountDownTimer{

        public TimeCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);// total length, count interval
        }

        @Override
        public void onTick(long millisUntilFinished) {  // display the countDown time
            timeCount.setText(millisUntilFinished / 1000 + "s");
        }



        public void onFinish() { // finishing will implement this method


            if (takerAnswer != correctAnswer){
                timeCount.setText("0s");
                vibrator.vibrate(200);// vibrate to give a attention
                currentIncorrectNumber ++;
                toast = Toast.makeText(getApplicationContext(),"The answer is wrong", Toast.LENGTH_SHORT);
                toast.show();
                String tempCorrectRate = currentCorrectNumber + "/" + (currentCorrectNumber + currentIncorrectNumber);
                //System.out.println("++++" + currentCorrectNumber + "/" + (currentCorrectNumber + currentIncorrectNumber));
                correctRate.setText(tempCorrectRate);
                answerCheck.get(correctAnswer).setTextColor(Color.RED);
                new Handler().post(new Runnable() { //suspend the timeCount to show the right answer
                    @Override
                    public void run() {
                        timeCountObject.cancel();
                    }
                });


                new Handler().postDelayed(new Runnable() { //after showing the right answer, start the timeCount again
                    public void run()
                    { //
                        answerCheck.get(correctAnswer).setTextColor(Color.BLACK);
                        //timeCountObject.start();
                        cycleJudge();
                    }
                }, 2000);

            }else {
                //timeCount.setText("10s");
                currentCorrectNumber ++;
                String tempCorrectRate = currentCorrectNumber + "/" + (currentCorrectNumber + currentIncorrectNumber);
                correctRate.setText(tempCorrectRate);
                cycleJudge();
            }

        }

    }
}
