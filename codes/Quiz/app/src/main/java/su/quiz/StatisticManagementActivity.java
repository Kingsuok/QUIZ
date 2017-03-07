package su.quiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashSet;

import dbOperation.TableCompletionOperation;
import dbOperation.TableQuestionOperation;
import dbOperation.TableTakersOperation;
import exitApplication.ExitApplication;

/**
 * Created by su on 2016/9/30.
 */

public class StatisticManagementActivity extends AppCompatActivity {
    private EditText deleteStatistic1;
    private EditText deleteStatistic2;
    private EditText deleteStatistic3;
    private EditText deleteStatistic4;
    private EditText deleteStatistic5;
    private Button clearDeleteStatistic;
    private Button deleteStatistic;
    private Button deleteAllStatistic;
    private TableCompletionOperation tableCompletionOperation;
    private TableTakersOperation tableTakersOperation;
    private TableQuestionOperation tableQuestionOperation;
    private Toast toast;
    private Toast toast1;
    private Toast toast2;
    private String[] IDToDelete;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_management);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        deleteStatistic1 = (EditText)findViewById(R.id.deleteStatistic1);
        deleteStatistic2 = (EditText)findViewById(R.id.deleteStatistic2);
        deleteStatistic3 = (EditText)findViewById(R.id.deleteStatistic3);
        deleteStatistic4 = (EditText)findViewById(R.id.deleteStatistic4);
        deleteStatistic5 = (EditText)findViewById(R.id.deleteStatistic5);
        clearDeleteStatistic = (Button)findViewById(R.id.clearDeleteStatistic);
        deleteStatistic = (Button)findViewById(R.id.deleteStatistic);
        deleteAllStatistic = (Button)findViewById(R.id.deleteAllStatistic);
        tableCompletionOperation = new TableCompletionOperation(StatisticManagementActivity.this, "quizDatabase");
        tableTakersOperation = new TableTakersOperation(StatisticManagementActivity.this, "quizDatabase");
        tableQuestionOperation = new TableQuestionOperation(StatisticManagementActivity.this, "quizDatabase");
        IDToDelete = new String[1];
        toast1 = Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT);
        toast2 = Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_SHORT);

        Listener listener = new Listener();
        clearDeleteStatistic.setOnClickListener(listener);
        deleteStatistic.setOnClickListener(listener);
        deleteAllStatistic.setOnClickListener(listener);

    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.clearDeleteStatistic){
                reset();
            }else if (v.getId() == R.id.deleteStatistic){

                AlertDialog.Builder builder = new AlertDialog.Builder(StatisticManagementActivity.this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Delete these statistic ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean input1 = inputIsempty(deleteStatistic1);
                        boolean input2 = inputIsempty(deleteStatistic2);
                        boolean input3 = inputIsempty(deleteStatistic3);
                        boolean input4 = inputIsempty(deleteStatistic4);
                        boolean input5 = inputIsempty(deleteStatistic5);
                        if (input1 && input2 && input3 && input4 && input5){
                            toast = Toast.makeText(getApplicationContext(), "All should not be empty", Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            HashSet<String> titleSet = tableQuestionOperation.titleSet();
                            databaseProcess(input1, deleteStatistic1, titleSet);
                            databaseProcess(input2, deleteStatistic2, titleSet);
                            databaseProcess(input3, deleteStatistic3, titleSet);
                            databaseProcess(input4, deleteStatistic4, titleSet);
                            databaseProcess(input5, deleteStatistic5, titleSet);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();


            }else {

                AlertDialog.Builder builder = new AlertDialog.Builder(StatisticManagementActivity.this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Delete all statistic ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean success = tableTakersOperation.resetAll();
                        if (success == true){
                            toast1.show();
                        }else {
                            toast2.show();
                        }
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

        public void databaseProcess(boolean input, EditText e, HashSet<String> titleSet){
            if (input == false){
                IDToDelete[0] = e.getText().toString().trim();
                boolean success = tableTakersOperation.update(IDToDelete);
                if (success == true){
                    toast1.show();
                    tableCompletionOperation.deleteBasedOnId(IDToDelete);
                    addToCompletion(titleSet, e);
                }else{
                    toast2.show();
                }
            }
        }
        public void addToCompletion(HashSet<String> titleSet, EditText v){
            String[] completionInfor = new String[2];
            completionInfor[0] = v.getText().toString().trim();
            for (String title : titleSet){
                completionInfor[1] = title;
                tableCompletionOperation.add(completionInfor);
            }

        }

        public void reset(){
            deleteStatistic1.setText("");
            deleteStatistic2.setText("");
            deleteStatistic3.setText("");
            deleteStatistic4.setText("");
            deleteStatistic5.setText("");
        }

        public boolean inputIsempty(EditText e){
            String input = e.getText().toString().replaceAll(" ", "");
            if (input.equals("")){
                return true;
            }else {
                return false;
            }
        }

    }
}
