package su.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by su on 2016/10/3.
 */

public class AddTakersActivity extends AddQuestionsActivity {

    private Button clear;
    private Button submit;
    private EditText ID1;
    private EditText password1;
    private EditText ID2;
    private EditText password2;
    private EditText ID3;
    private EditText password3;
    private EditText ID4;
    private EditText password4;
    //private EditText ID5;
    //private EditText password5;
    private Toast toast;
    private TableTakersOperation tableTakersOperation;
    private TableCompletionOperation tableCompletionOperation;
    private TableQuestionOperation tableQuestionOperation;
    private String[] takerInfor;
    private String[] inputTakers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_takers);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit


        clear = (Button)findViewById(R.id.clearTakers);
        submit = (Button)findViewById(R.id.submitTakers);
        ID1 = (EditText)findViewById(R.id.id1);
        password1 = (EditText)findViewById(R.id.password1);
        ID2 = (EditText)findViewById(R.id.id2);
        password2 = (EditText)findViewById(R.id.password2);
        ID3 = (EditText)findViewById(R.id.id3);
        password3 = (EditText)findViewById(R.id.password3);
        ID4 = (EditText)findViewById(R.id.id4);
        password4 = (EditText)findViewById(R.id.password4);
        //ID5 = (EditText)findViewById(R.id.id5);
        //password5 = (EditText)findViewById(R.id.password5);
        tableTakersOperation = new TableTakersOperation(AddTakersActivity.this, "quizDatabase");
        tableCompletionOperation = new TableCompletionOperation(AddTakersActivity.this, "quizDatabase");
        tableQuestionOperation = new TableQuestionOperation(AddTakersActivity.this, "quizDatabase");
        takerInfor = new String[5];
        takerInfor[2] = "0";
        takerInfor[3] = "0";
        takerInfor[4] = "0";
        inputTakers = new String[4];

        ClickListener listener = new ClickListener();

        clear.setOnClickListener(listener);
        submit.setOnClickListener(listener);
    }

    class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.clearTakers){
                clear();
            }else {
                boolean isWrong1 = isWrong(ID1, password1);
                boolean isWrong2 = isWrong(ID2, password2);
                boolean isWrong3 = isWrong(ID3, password3);
                boolean isWrong4 = isWrong(ID4, password4);
                //boolean isWrong5 = isWrong(ID5, password5);
                if (isWrong1 || isWrong2 || isWrong3 || isWrong4 ){
                    toast = Toast.makeText(getApplicationContext(), "ID and password should be empty or not empty", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    boolean isEmpty1 = isEmpty(ID1, password1);
                    boolean isEmpty2 = isEmpty(ID2, password2);
                    boolean isEmpty3 = isEmpty(ID3, password3);
                    boolean isEmpty4 = isEmpty(ID4, password4);
                    //boolean isEmpty5 = isEmpty(ID5, password5);
                    if (isEmpty1 && isEmpty2 && isEmpty3 && isEmpty4 ){
                        toast = Toast.makeText(getApplicationContext(), "All should be not empty", Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        int num = 0;
                        if (isEmpty1 == false){
                            inputTakers[num] = ID1.getText().toString().trim();
                            num++;
                        }

                        if (isEmpty2 == false){
                            inputTakers[num] = ID2.getText().toString().trim();
                            num++;
                        }

                        if (isEmpty3 == false){
                            inputTakers[num] = ID3.getText().toString().trim();
                            num++;
                        }
                        if (isEmpty4 == false){
                            inputTakers[num] = ID4.getText().toString().trim();
                            num++;
                        }

                        boolean equal = judgeEqueal(inputTakers, num);
                        if (equal == true){
                            toast = Toast.makeText(getApplicationContext(), "All IDS should be different", Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            String repeat = judgeRepeat(inputTakers, num);
                            if (repeat != null){
                                toast = Toast.makeText(getApplicationContext(), "Failure: " + repeat + "group(s) existed", Toast.LENGTH_LONG);
                                toast.show();
                            }else {
                                HashSet<String> titleSet = tableQuestionOperation.titleSet();
                                if (isEmpty1 == false){
                                    if (addToTakers(ID1, password1)){
                                        addToCompletion(titleSet, ID1);
                                    }

                                }

                                if (isEmpty2 == false){
                                    if (addToTakers(ID2, password2)){
                                        addToCompletion(titleSet, ID2);
                                    }

                                }

                                if (isEmpty3 == false){
                                    if (addToTakers(ID3, password3)){
                                        addToCompletion(titleSet, ID3);
                                    }

                                }
                                if (isEmpty4 == false){
                                    if (addToTakers(ID4, password4)){
                                        addToCompletion(titleSet, ID4);
                                    }


                                }
                                //if (isEmpty5 == false){
                                //    if (addToTakers(ID5, password5)){
                                //        addToCompletion(titleSet, ID5);
                                //    };

                                //}

                                    toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                                    toast.show();

                                    clear();
                            }

                        }

                    }
                }

            }
        }

        public String judgeRepeat(String[] inputTakers, int num){
            String repeat = null;
            HashSet<String> IDSet = tableTakersOperation.IDSet();
            for (int i = 0; i < num; i ++){
                if (IDSet.contains(inputTakers[i])){
                    if (repeat == null){
                        repeat = i+1 +" ";
                    }else {
                        repeat = repeat + i+1 +" ";
                    }

                }
            }
            return repeat;
        }

        private boolean judgeEqueal(String[] inputTakers, int num){
            for (int i = 0; i < num - 1; i++){
                for (int j = i +1 ; j < num; j++){
                    if (inputTakers[i].equals(inputTakers[j])){
                        return true;
                    }
                }
            }
            return false;
        }

        public void addToCompletion(HashSet<String> titleSet, EditText v){
            String[] completionInfor = new String[2];
            completionInfor[0] = v.getText().toString().trim();
            for (String title : titleSet){
                completionInfor[1] = title;
                tableCompletionOperation.add(completionInfor);
            }

        }

        public boolean addToTakers(EditText v1, EditText v2){

            takerInfor[0] = v1.getText().toString().trim();
            takerInfor[1] = v2.getText().toString().trim();
            return tableTakersOperation.add(takerInfor);
        }

        public void clear(){
            ID1.setText("");
            password1.setText("");
            ID2.setText("");
            password2.setText("");
            ID3.setText("");
            password3.setText("");
            ID4.setText("");
            password4.setText("");
            //ID5.setText("");
            //password5.setText("");
        }
        public boolean isWrong(EditText v1, EditText v2){
            boolean flag = false;
            if ((v1.getText().toString().trim().equals("") && !v2.getText().toString().trim().equals("")) ||
                    (!v1.getText().toString().trim().equals("") && v2.getText().toString().trim().equals(""))){
                flag = true;
            }
            return  flag;
        }
        public boolean isEmpty(EditText v1, EditText v2){
            boolean flag = false;
            if (v1.getText().toString().trim().equals("") && v2.getText().toString().trim().equals("")){
                flag = true;
            }
            return  flag;
        }


    }
}
