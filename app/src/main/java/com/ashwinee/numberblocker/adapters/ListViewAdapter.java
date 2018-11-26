package com.ashwinee.numberblocker.adapters;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwinee.numberblocker.R;
import com.ashwinee.numberblocker.interfacesForHandling.DeleteReflector;
import com.ashwinee.numberblocker.modelClasses.DatabaseInteractions;
import com.ashwinee.numberblocker.pojo_classes.NumberDetails;

import java.sql.SQLException;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Ashwinee on 13-Nov-18.
 */

public class ListViewAdapter extends BaseAdapter{

    private List<NumberDetails> data;
    public  DeleteReflector reference=null;
    private final String TABLE_NAME="number_blocker";

    public ListViewAdapter(List<NumberDetails> data,DeleteReflector reference){
        super();
        this.data=data;
        this.reference=reference;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getItemData(int position) {
        return data.get(position).getNumber();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_custom_layout,parent,false);
        TextView numberTextView=(TextView)view.findViewById(R.id.number);
        numberTextView.setText(data.get(position).getNumber());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView number=(TextView)v.findViewById(R.id.number);
                Toast.makeText(v.getContext(),"Item selected is : "+number.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton deleteButton=(ImageButton) view.findViewById(R.id.put_to_trash);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUpdate(parent.getContext(), position);
                reference.showUpdatedList();
            }
        });
        return view;
    }


    private void databaseUpdate(Context context,int position){
        String result="Unsuccessfull.Please try again";
        DatabaseInteractions dbInteractions=new DatabaseInteractions(context);
        SQLiteDatabase sqlDatabase=dbInteractions.getWritableDatabase();

        try{
            final String number=this.getItemData(position);

            ContentValues set=new ContentValues();
            set.put(" FLAG ",0);
            final String whereClause=" NUMBERS_BLOCKED = '"+number+"';";

            result=sqlDatabase.update(TABLE_NAME,
                                        set,
                                        whereClause,
                                        null)>0?
                                        "successfully updated":"Unsuccessfull.Please try again";
        }finally{
            sqlDatabase.close();
            dbInteractions.close();
            Toast.makeText(context, result+"!", Toast.LENGTH_SHORT).show();
        }
    }
}
