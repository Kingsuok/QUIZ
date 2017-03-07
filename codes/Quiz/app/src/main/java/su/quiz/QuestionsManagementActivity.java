package su.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dbOperation.TableCompletionOperation;
import dbOperation.TableQuestionOperation;
import exitApplication.ExitApplication;

/**
 * Created by su on 2016/10/2.
 */

public class QuestionsManagementActivity extends AppCompatActivity {

    private Button addQue;
    private Button deleteQue;
    private Button checkQue;
    private Button clearAll;
    private EditText titleDele;
    private EditText titleChec;
    private Toast toast;
    private TableQuestionOperation tableQuestionOperation;
    private TableCompletionOperation tableCompletionOperation;
    private String[] deleteTitleDetail;
    private String[] checkTitleDetail;
    private String[] checkResult;
    private String[] titleToDelete;
    private HashMap<String, String> answerCheck;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_management);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        addQue = (Button)findViewById(R.id.addQue);
        deleteQue = (Button)findViewById(R.id.deleteQue);
        checkQue = (Button)findViewById(R.id.checkQue);
        clearAll = (Button)findViewById(R.id.clear);
        titleDele = (EditText)findViewById(R.id.titleDele);
        titleChec = (EditText)findViewById(R.id.titleCheck);
        intent = new Intent();
        tableQuestionOperation = new TableQuestionOperation(QuestionsManagementActivity.this,"quizDatabase");
        tableCompletionOperation = new TableCompletionOperation(QuestionsManagementActivity.this,"quizDatabase");
        deleteTitleDetail = new String[1];
        checkTitleDetail = new String[1];
        titleToDelete = new String[1];
        checkResult = new String[6];
        answerCheck = new HashMap<String,String>();
        answerCheck.put("1","A");
        answerCheck.put("2","B");
        answerCheck.put("3","C");
        answerCheck.put("4","D");

        ClickListener clickListener = new ClickListener();
        addQue.setOnClickListener(clickListener);
        deleteQue.setOnClickListener(clickListener);
        checkQue.setOnClickListener(clickListener);
        clearAll.setOnClickListener(clickListener);
        
    }

    class ClickListener implements View.OnClickListener{
        //Intent intent = new Intent();
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.addQue){
                intent.setClass(QuestionsManagementActivity.this, AddQuestionsActivity.class);
                startActivity(intent);

            }else if(v.getId() == R.id.deleteQue){


                String title = titleDele.getText().toString().replaceAll(" ","");
                if (title.equals("")){
                    toast = Toast.makeText(getApplicationContext(),"Title should not be empty", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    //HashSet<String> titleSet = tableQuestionOperation.titleSet();
                    //String titleValue = titleDele.getText().toString().trim();
                   // if (!titleSet.contains(titleValue)){
                     //   toast = Toast.makeText(getApplicationContext(),"Title does not exist", Toast.LENGTH_SHORT);
                    //    toast.show();
                   // }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsManagementActivity.this);
                        builder.setIcon(R.drawable.quiz);
                        builder.setTitle("Alert");
                        builder.setMessage("Delete the question ?");
                        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteTitleDetail[0] = titleDele.getText().toString().trim();
                                boolean success = tableQuestionOperation.delete(deleteTitleDetail);
                                titleToDelete[0] = deleteTitleDetail[0];
                                if (success){
                                    toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                                    titleDele.setText("");
                                    tableCompletionOperation.deleteBasedOnTitle(titleToDelete);
                                }else {
                                    toast = Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT);
                                    titleDele.setText("");
                                }
                                toast.show();

                            }
                        });


                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create();
                        builder.show();
                   // }
                }



            }else if(v.getId() == R.id.checkQue){
                String title = titleChec.getText().toString().replaceAll(" ","");

                if (title.equals("")){
                    toast = Toast.makeText(getApplicationContext(),"Title should not be empty", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    String realTitle = titleChec.getText().toString().trim();

                    checkTitleDetail[0] = realTitle;
                    HashMap<String,String> result = tableQuestionOperation.viewOne(checkTitleDetail);
                    if (result.isEmpty()){
                        toast = Toast.makeText(getApplicationContext(), "Title doesn't exist", Toast.LENGTH_SHORT);
                        titleChec.setText("");
                        toast.show();
                    }else {
                        titleChec.setText("");
                        checkResult[0] = "Ques: " + realTitle;
                        checkResult[1] = "A. " + result.get("A");
                        checkResult[2] = "B. " + result.get("B");
                        checkResult[3] = "C. " + result.get("C");
                        checkResult[4] = "D. " + result.get("D");
                        checkResult[5] = "Answer: " + answerCheck.get(result.get("answer"));
                        intent.setClass(QuestionsManagementActivity.this, CheckQuestionActivity.class);
                        intent.putExtra("com.a655.su.quiz.checkResult", checkResult);
                        startActivity(intent);
                    }

                }

            }else {

                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsManagementActivity.this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Delete all ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean success = tableQuestionOperation.emptyTable();

                        if (success == true){
                            toast = Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT);
                            tableCompletionOperation.emptyTable();
                        }else {
                            toast = Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_SHORT);
                        }
                        toast.show();
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
    }
}
