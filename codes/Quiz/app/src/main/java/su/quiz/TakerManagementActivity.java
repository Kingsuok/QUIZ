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

import dbOperation.TableCompletionOperation;
import dbOperation.TableTakersOperation;
import exitApplication.ExitApplication;

/**
 * Created by su on 2016/9/30.
 */

public class TakerManagementActivity extends AppCompatActivity {

    private Button addTakers;
    private EditText takerCheck;
    private Button checkTaker;
    private EditText takerDelete;
    private Button deleteTaker;
    private Button clearTakers;
    private Intent intent;
    private Toast toast;
    private String[] deleteTakerDetail;
    private TableCompletionOperation tableCompletionOperation;
    private TableTakersOperation tableTakersOperation;
    private String[] takerToDelete;
    private String[] checkTitleDetail;
    private String[] checkTakerDetail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker_management);
        ExitApplication.getInstance().addActivity(this);// close all activities and exit

        addTakers = (Button)findViewById(R.id.addTakers);
        takerCheck = (EditText)findViewById(R.id.takerCheck);
        checkTaker = (Button)findViewById(R.id.checkTaker);
        takerDelete = (EditText)findViewById(R.id.takerDelete);
        deleteTaker = (Button)findViewById(R.id.deleteTaker);
        clearTakers = (Button)findViewById(R.id.clearTakers);
        intent = new Intent();
        deleteTakerDetail = new String[1];
        takerToDelete = new String[1];
        checkTitleDetail = new String[1];
        checkTakerDetail = new String[5];
        tableCompletionOperation = new TableCompletionOperation(TakerManagementActivity.this, "quizDatabase");
        tableTakersOperation = new TableTakersOperation(TakerManagementActivity.this, "quizDatabase");

        ClickListener listener = new ClickListener();
        addTakers.setOnClickListener(listener);
        checkTaker.setOnClickListener(listener);
        deleteTaker.setOnClickListener(listener);
        clearTakers.setOnClickListener(listener);
    }

    class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.addTakers){
                intent.setClass(TakerManagementActivity.this, AddTakersActivity.class);
                startActivity(intent);
            }else if (v.getId() == R.id.checkTaker){
                String takerID = takerCheck.getText().toString().replaceAll(" ","");
                if (takerID.equals("")){
                    toast = Toast.makeText(getApplicationContext(),"ID should not be empty", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    String realTakerID = takerCheck.getText().toString().trim();
                    checkTitleDetail[0] = realTakerID;
                    HashMap<String,String> result = tableTakersOperation.viewOne(checkTitleDetail);
                    if (result.isEmpty()){
                        toast = Toast.makeText(getApplicationContext(), "ID doesn't exist", Toast.LENGTH_SHORT);
                        takerCheck.setText("");
                        toast.show();
                    }else {
                        takerCheck.setText("");
                        checkTakerDetail[0] = realTakerID;
                        checkTakerDetail[1] = result.get("password");
                        checkTakerDetail[2] = result.get("loginNum");
                        checkTakerDetail[3] = result.get("correctNum");
                        checkTakerDetail[4] = result.get("incorrectNum");
                        intent.setClass(TakerManagementActivity.this,CheckTakerActivity.class);
                        intent.putExtra("com.a655.su.quiz.taker", checkTakerDetail);
                        startActivity(intent);
                    }

                }

            }else if (v.getId() == R.id.deleteTaker){

                String takerID = takerDelete.getText().toString().replaceAll(" ","");
                if (takerID.equals("")){
                    toast = Toast.makeText(getApplicationContext(),"ID should not be empty", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    //String takerValue = takerDelete.getText().toString().trim();
                   // HashSet<String> takerSet = tableTakersOperation.IDSet();
                    //if (!takerSet.contains(takerValue)) {
                     //   toast = Toast.makeText(getApplicationContext(),"ID does not exist", Toast.LENGTH_SHORT);
                     //   toast.show();
                    //}else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TakerManagementActivity.this);
                        builder.setIcon(R.drawable.quiz);
                        builder.setTitle("Alert");
                        builder.setMessage("Delete these takers ?");
                        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                deleteTakerDetail[0] = takerDelete.getText().toString().trim();
                                boolean success = tableTakersOperation.delete(deleteTakerDetail);
                                takerToDelete[0] = deleteTakerDetail[0];
                                if (success){
                                    toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                                    takerDelete.setText("");
                                    tableCompletionOperation.deleteBasedOnTitle(takerToDelete);
                                }else {
                                    toast = Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT);
                                    takerDelete.setText("");
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
                //}

            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(TakerManagementActivity.this);
                builder.setIcon(R.drawable.quiz);
                builder.setTitle("Alert");
                builder.setMessage("Delete all takers ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean success = tableTakersOperation.emptyTable();
                        if (success){
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
