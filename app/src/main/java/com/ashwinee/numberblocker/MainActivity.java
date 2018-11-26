package com.ashwinee.numberblocker;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ashwinee.numberblocker.adapters.ListViewAdapter;
import com.ashwinee.numberblocker.interfacesForHandling.DeleteReflector;
import com.ashwinee.numberblocker.modelClasses.DatabaseInteractions;
import com.ashwinee.numberblocker.pojo_classes.NumberDetails;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DeleteReflector{

    private List<NumberDetails> dataForAdapter=null;
    private ListView numberListView;
    private ListViewAdapter numberListViewAdapter;
    private final String TABLE_NAME="number_blocker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Loading data from the Database into the String array:
        try{
            dataForAdapter=fetchData();
        }
        catch(Exception e){
            Toast.makeText(this, "Exception occured!", Toast.LENGTH_SHORT).show();
        }

        if((dataForAdapter!=null)&&(dataForAdapter.size()!=0)){
            showListItems();
            //populate the List view:
            numberListView=(ListView) findViewById(R.id.numbers_list_view);
            numberListViewAdapter=new ListViewAdapter(dataForAdapter,MainActivity.this);
            numberListView.setAdapter(numberListViewAdapter);
        }else{
            showEmptyMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.on_create_options_menu_for_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleted:
                startActivity(new Intent(this,DeletedNumbersActivity.class));
                return true;
            case R.id.search:
                return true;
            case R.id.settings:
                return true;
            case R.id.add:
                Bundle bundle=new Bundle();
                bundle.putSerializable("dataForAdapter",(Serializable) dataForAdapter);
                Intent intent=new Intent(this,AddNumberActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEmptyMessage(){
        findViewById(R.id.data_layout).setVisibility(View.GONE);
        findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
    }

    private void showListItems(){
        findViewById(R.id.empty_message).setVisibility(View.GONE);
        findViewById(R.id.data_layout).setVisibility(View.VISIBLE);
    }

    private List<NumberDetails> fetchData() throws SQLException {
        String fetchAllQuery="SELECT * FROM "+TABLE_NAME+" WHERE FLAG = 1;";
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

    @Override
    public void showUpdatedList() {
        try{
            numberListViewAdapter=new ListViewAdapter(fetchData(),MainActivity.this);
        }
        catch(Exception e){
//            Toast.makeText(this, "Exception occured!", Toast.LENGTH_SHORT).show();
        }
        numberListView.setAdapter(numberListViewAdapter);
    }
}
