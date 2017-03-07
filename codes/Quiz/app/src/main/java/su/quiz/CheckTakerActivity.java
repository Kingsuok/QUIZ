package su.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import exitApplication.ExitApplication;

/**
 * Created by su on 2016/10/3.
 */

public class CheckTakerActivity extends AppCompatActivity{

    private TextView IDDisplay;
    private TextView passwordDisplay;
    private TextView loginNumber;
    private TextView correctNumber;
    private TextView wrongNumber;
    private String[] displayValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_taker);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        IDDisplay = (TextView)findViewById(R.id.IDDisplay);
        passwordDisplay = (TextView)findViewById(R.id.passwordDisplay);
        loginNumber = (TextView)findViewById(R.id.loginNumberDisplay);
        correctNumber = (TextView)findViewById(R.id.correctDisplay);
        wrongNumber = (TextView)findViewById(R.id.wrongDisplay);

        Intent intent = getIntent();
        displayValue = intent.getStringArrayExtra("com.a655.su.quiz.taker");
        IDDisplay.setText(displayValue[0]);
        passwordDisplay.setText(displayValue[1]);
        loginNumber.setText(displayValue[2]);
        correctNumber.setText(displayValue[3]);
        wrongNumber.setText(displayValue[4]);
    }
}
