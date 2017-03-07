package su.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dbOperation.TableSetTimeOperation;
import exitApplication.ExitApplication;

/**
 * Created by su on 2016/9/30.
 */

public class QMActivity extends AppCompatActivity {
    private Button questionManagement;
    private Button takerManagement;
    private Button statisticManagement;
    private Button setTime;
    private EditText period;
    private Toast toast;
    private TableSetTimeOperation tableSetTimeOperation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qm);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        questionManagement = (Button)findViewById(R.id.questionManagement);
        takerManagement = (Button)findViewById(R.id.takerManagement);
        statisticManagement = (Button)findViewById(R.id.statisticManagement);
        setTime = (Button)findViewById(R.id.setTime);
        period = (EditText)findViewById(R.id.period);
        tableSetTimeOperation = new TableSetTimeOperation(QMActivity.this,"quizDatabase");
        ButtonClickListenser listenser = new ButtonClickListenser();
        questionManagement.setOnClickListener(listenser);
        takerManagement.setOnClickListener(listenser);
        statisticManagement.setOnClickListener(listenser);
        setTime.setOnClickListener(listenser);
    }

    class ButtonClickListenser implements View.OnClickListener{
        Intent intent = new Intent();
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.questionManagement){
                intent.setClass(QMActivity.this, QuestionsManagementActivity.class);
                startActivity(intent);
            }else if (v.getId() == R.id.takerManagement){
                intent.setClass(QMActivity.this, TakerManagementActivity.class);
                startActivity(intent);
            }else if (v.getId() == R.id.statisticManagement){
                intent.setClass(QMActivity.this, StatisticManagementActivity.class);
                startActivity(intent);
            }else {
                String time = period.getText().toString().replaceAll(" ","");
                if (time.equals("")){
                    toast = Toast.makeText(getBaseContext(),"Time should not be empty",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    time = period.getText().toString().trim();
                    int timeValue = Integer.parseInt(time);
                    if (timeValue > 30 || timeValue < 10){
                        toast = Toast.makeText(getBaseContext(),"Time should be 10 ~ 30",Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        boolean success = tableSetTimeOperation.update(new String[]{time});
                        if (success == true){
                            toast = Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            toast = Toast.makeText(getBaseContext(),"Failure",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }
            }

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to exit ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        ExitApplication.getInstance().exit(); // exit the application
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
        return false;
    }

}
