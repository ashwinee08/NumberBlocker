package com.ashwinee.numberblocker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinee.numberblocker.modelClasses.DatabaseInteractions;
import com.ashwinee.numberblocker.pojo_classes.NumberDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddNumberActivity extends AppCompatActivity {

    private static final String TABLE_NAME="number_blocker";
    private  TextView alreadyExistsTextView=null;
    private  TextView removedTextView=null;
    private EditText toBeBlockedEditText=null;
    private List<NumberDetails> numberDetailsList=null;
    private List<NumberDetails> numberDeletedList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        getHold();
        Bundle bundle=getIntent().getExtras();
        numberDetailsList=bundle.isEmpty()?null:(List<NumberDetails>)bundle.getSerializable("dataForAdapter");
        try {
            numberDeletedList = fetchData();
        }catch(SQLException sqlE){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

            toBeBlockedEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(numberDetailsList!=null) {
                        for (NumberDetails individualDetails : numberDetailsList) {
                            if (individualDetails.getNumber().contains(s.toString())) {
                                alreadyExistsTextView.setText("\n" + individualDetails.getNumber());
                            }else{
                                alreadyExistsTextView.setText("");
                            }
                        }
                    }
                    if(numberDeletedList!=null) {
                        for (NumberDetails individualDetails : numberDeletedList) {
                            if (individualDetails.getNumber().contains(s.toString())) {
                                removedTextView.setText("\n" + individualDetails.getNumber());
                            }else{
                                removedTextView.setText("");
                            }
                        }
                    }
                }
            });

    }



    public void onButtonClick(View view){
        try{
            addData(toBeBlockedEditText.getText().toString());
        }
        catch(Exception e){
            Toast.makeText(this, "Number could not be added, please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addData(String NUMBER) throws SQLException{
        DatabaseInteractions dbInteractions=new DatabaseInteractions(AddNumberActivity.this);
        SQLiteDatabase sqlDb=dbInteractions.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("NUMBERS_BLOCKED",NUMBER);
        long returnValue=sqlDb.insert(TABLE_NAME,null,values);
        sqlDb.close();

        if(returnValue>0){
            Toast.makeText(this, "Number added to blocked list successfully!", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Number could not be added, please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<NumberDetails> fetchData() throws SQLException {
        String fetchAllQuery="SELECT * FROM "+TABLE_NAME+" WHERE FLAG = 0;";
        DatabaseInteractions dbInteractions=new DatabaseInteractions(this);
        SQLiteDatabase slqDb=dbInteractions.getReadableDatabase();

        List<NumberDetails> listOfNumberDetails=new ArrayList<>();

        Cursor cursor=slqDb.rawQuery(fetchAllQuery,null);

        NumberDetails numDetails;
        while(cursor.moveToNext()){
            numDetails=new NumberDetails();

            numDetails.setNumber(cursor.getString(0));
            numDetails.setFlag(cursor.getInt(1)==1);
            numDetails.setCount(cursor.getInt(2));

            listOfNumberDetails.add(numDetails);
        }
        cursor.close();
        slqDb.close();
        return listOfNumberDetails;
    }

    private final void getHold(){
        alreadyExistsTextView=(TextView) findViewById(R.id.already_exists_text_view);
        removedTextView=(TextView) findViewById(R.id.removed_text_view);
        toBeBlockedEditText= (EditText) findViewById(R.id.to_be_blocked_edit_text);
    }
}
