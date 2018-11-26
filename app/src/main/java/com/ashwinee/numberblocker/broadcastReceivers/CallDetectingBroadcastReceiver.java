package com.ashwinee.numberblocker.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.ashwinee.numberblocker.ITelephony;
import com.ashwinee.numberblocker.modelClasses.DatabaseInteractions;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.src;

/**
 * Created by Ashwinee on 22-Nov-18.
 */

public class CallDetectingBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> numberList;
        if(!intent.getAction().equals("android.intent.action.PHONE_STATE")){
            return;
        }
        numberList=this.fetchData(context);
        String number=String.valueOf(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
        if(numberList.contains(number)){
            disconnectCall(context);
        }
    }

    private void disconnectCall(Context context){
        ITelephony telephonyInterfaceReference;
        TelephonyManager telephonyManagerObject=(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        try{
            Class c=Class.forName(telephonyManagerObject.getClass().getName());
            Method method=c.getDeclaredMethod("getITelephony");

            method.setAccessible(true);

            telephonyInterfaceReference=(ITelephony) method.invoke(telephonyManagerObject);

            telephonyInterfaceReference.endCall();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private List<String> fetchData(Context context){
       //run a service and fetch the data:
        List<String> numberList=new ArrayList<String>();
        int i=1;
        if(i!=0){
            //TestData:
            numberList.add("9454644217");
            numberList.add("8707896041");
            return numberList;
        }else{
            //Functional Data:
            DatabaseInteractions dbInteractions=new DatabaseInteractions(context);
        }
        return numberList;
    }
}
