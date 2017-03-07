package su.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbOperation.TableQuestionOperation;
import dbOperation.TableTakersOperation;
import exitApplication.ExitApplication;

public class LoginActivity extends AppCompatActivity {
    //Quize master : Username: 0; password:0
    private String master = "0";
    private String masterPassword = "0";

    private EditText userName;
    private EditText password;
    private String userNameValue;
    private String passwordValue;
    private Button login;
    private TableTakersOperation tableTakersOperation;
    private TableQuestionOperation tableQuestionOperation;
    private Toast toast;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        tableTakersOperation = new TableTakersOperation(LoginActivity.this,"quizDatabase");
        tableQuestionOperation = new TableQuestionOperation(LoginActivity.this,"quizDatabase");
        intent = new Intent();
        login = (Button)findViewById(R.id.login);
        ButtonListener buttonListener = new ButtonListener();
        login.setOnClickListener(buttonListener);



    }

    // build button on click listener
    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            boolean isempty = isempty(userName, password);
            if (isempty == true){
                toast = Toast.makeText(getApplicationContext(), "ID and password should not be empty",Toast.LENGTH_SHORT);
                toast.show();
            }else {
                userNameValue = userName.getText().toString().trim();
                passwordValue = password.getText().toString().trim();
                if (userNameValue.equals(master) && passwordValue.equals(masterPassword)){
                    //reset();
                    intent.setClass(LoginActivity.this,QMActivity.class);
                    startActivity(intent);
                    // }else if() {


                }else{

                    HashMap<String,String> result = tableTakersOperation.viewOne(new String[]{userNameValue});
                    if (result.isEmpty()){
                        toast = Toast.makeText(getApplicationContext(), "ID is wrong",Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        Map<String, String> questionSet = tableQuestionOperation.oneQuestion(new String[]{userNameValue});
                        if (passwordValue.equals(result.get("password"))){
                            if (questionSet.isEmpty()){
                                toast = Toast.makeText(getApplicationContext(), "No questions now",Toast.LENGTH_LONG);
                                toast.show();
                            }else {

                                intent.setClass(LoginActivity.this, QTActivity.class);
                                intent.putExtra("su.quiz.takerID",userNameValue);
                                startActivity(intent);


                            }

                        }else {
                            toast = Toast.makeText(getApplicationContext(), "Password is wrong",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }
            }

        }

        public boolean isempty(EditText e1, EditText e2){
            String nameValue = e1.getText().toString().trim();
            String passwordValue = e2.getText().toString().trim();
            String name = e1.getText().toString().replaceAll(" ","");
            String password = e2.getText().toString().replaceAll(" ","");
            if (name.equals("") || password.equals("") || nameValue.length()!= name.length() || passwordValue.length()!= password.length()){
                return true;
            } else {
                return false;
            }

         }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event ){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alter");
                builder.setMessage("Do you want to exit ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
    //public void reset(){
     //   userName.setText("");
      //  password.setText("");
   // }
}
