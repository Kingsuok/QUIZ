package su.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import exitApplication.ExitApplication;

/**
 * Created by su on 2016/10/2.
 */

public class CheckQuestionActivity extends AppCompatActivity {

    private TextView checkTitle;
    private TextView checkA;
    private TextView checkB;
    private TextView checkC;
    private TextView checkD;
    private TextView checkAnswer;
    private String[] checkResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_question);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        checkTitle = (TextView)findViewById(R.id.checkTitle);
        checkA = (TextView)findViewById(R.id.checkA);
        checkB = (TextView)findViewById(R.id.checkB);
        checkC = (TextView)findViewById(R.id.checkC);
        checkD = (TextView)findViewById(R.id.checkD);
        checkAnswer = (TextView)findViewById(R.id.checkAnswer);

        Intent intent = getIntent();
        checkResult = intent.getStringArrayExtra("com.a655.su.quiz.checkResult");
        checkTitle.setText(checkResult[0]);
        checkA.setText(checkResult[1]);
        checkB.setText(checkResult[2]);
        checkC.setText(checkResult[3]);
        checkD.setText(checkResult[4]);
        checkAnswer.setText(checkResult[5]);
    }
}
