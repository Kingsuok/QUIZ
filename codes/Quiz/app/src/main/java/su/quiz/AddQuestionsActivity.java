package su.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.HashMap;
import java.util.HashSet;

import dbOperation.TableCompletionOperation;
import dbOperation.TableQuestionOperation;
import dbOperation.TableTakersOperation;
import exitApplication.ExitApplication;

/**
 * Created by su on 2016/9/30.
 */

public class AddQuestionsActivity extends AppCompatActivity {
    private EditText question;
    private EditText optionA;
    private EditText optionB;
    private EditText optionC;
    private EditText optionD;
    private RadioGroup radioGroup;
    private RadioButton A;
    private RadioButton B;
    private RadioButton C;
    private RadioButton D;
    private Button reset;
    private Button submit;
    private int answer = 0;
    private HashMap<Integer, EditText> answerCheck; //check whether the answer exists
    private Toast toast;
    private TableQuestionOperation tableQuestionOperation;
    private TableCompletionOperation tableCompletionOperation;
    private TableTakersOperation tableTakersOperation;
    private String[] questionInfor;
    private String[] completionInfor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit


        question = (EditText)findViewById(R.id.question);
        optionA = (EditText)findViewById(R.id.optionA);
        optionB = (EditText)findViewById(R.id.optionB);
        optionC = (EditText)findViewById(R.id.optionC);
        optionD = (EditText)findViewById(R.id.optionD);
        radioGroup = (RadioGroup)findViewById(R.id.radioGrop);
        A = (RadioButton)findViewById(R.id.A);
        B = (RadioButton)findViewById(R.id.B);
        C = (RadioButton)findViewById(R.id.C);
        D = (RadioButton)findViewById(R.id.D);
        reset = (Button)findViewById(R.id.reset);
        submit = (Button)findViewById(R.id.submit);
        answerCheck = new HashMap<Integer, EditText>();
        answerCheck.put(1,optionA);
        answerCheck.put(2,optionB);
        answerCheck.put(3,optionC);
        answerCheck.put(4,optionD);
        tableQuestionOperation = new TableQuestionOperation(AddQuestionsActivity.this, "quizDatabase");
        tableCompletionOperation = new TableCompletionOperation(AddQuestionsActivity.this, "quizDatabase");
        tableTakersOperation = new TableTakersOperation(AddQuestionsActivity.this, "quizDatabase");
        questionInfor = new String[6];
        completionInfor = new String[2];

        RadioListener radioListener = new RadioListener();
        radioGroup.setOnCheckedChangeListener(radioListener);

        ButtonListener buttonListener = new ButtonListener();
        submit.setOnClickListener(buttonListener);
        reset.setOnClickListener(buttonListener);




    }

    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.reset){
                resetInput();
            }else{
                String questionWithoutBlank = question.getText().toString().replace(" ","");
                if (!questionWithoutBlank.equals("")){
                    if (answer == 0){
                        toast = Toast.makeText(getApplicationContext(),"Answer is not set", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        String answerWithoutBlank = answerCheck.get(answer).getText().toString().replace(" ","");
                        if (answerWithoutBlank.equals("")){
                            toast = Toast.makeText(getApplicationContext(),"Answer should not be empty", Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            questionInfor[0] = question.getText().toString().trim();
                            questionInfor[1] = optionA.getText().toString().trim();
                            questionInfor[2] = optionB.getText().toString().trim();
                            questionInfor[3] = optionC.getText().toString().trim();
                            questionInfor[4] = optionD.getText().toString().trim();
                            questionInfor[5] = Integer.toString(answer);
                            HashSet<String> takerSet = tableTakersOperation.IDSet();
                            boolean success = tableQuestionOperation.add(questionInfor);
                            if (success == true){
                                resetInput();
                                toast = Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT);
                                toast.show();
                                completionInfor[1] = questionInfor[0];
                                // every taker should also add the new question in the completion
                                for (String taker : takerSet){
                                    completionInfor[0] = taker;
                                    tableCompletionOperation.add(completionInfor);
                                }
                            }else {
                                toast = Toast.makeText(getApplicationContext(),"Failure:same question", Toast.LENGTH_SHORT);
                                toast.show();
                                resetInput();
                            }
                        }
                    }

                }else{
                    toast = Toast.makeText(getApplicationContext(),"Question should not be empty", Toast.LENGTH_SHORT);
                    toast.show();
                    resetInput();
                }

            }


        }
        public void resetInput() {
            question.setText("");
            optionA.setText("");
            optionB.setText("");
            optionC.setText("");
            optionD.setText("");
            radioGroup.clearCheck();
            answer = 0;
        }
    }

    class RadioListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.A){
                answer = 1;
            }else if (checkedId == R.id.B){
                answer = 2;
            }else if(checkedId == R.id.C){
                answer = 3;
            }else {
                answer = 4;
            }
        }
    }


}
