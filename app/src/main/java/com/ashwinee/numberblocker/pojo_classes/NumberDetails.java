package com.ashwinee.numberblocker.pojo_classes;


import java.io.Serializable;

/**
 * Created by Ashwinee on 14-Nov-18.
 */

public class NumberDetails implements Serializable{

    int u_id;
    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }



    String number;
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }



    boolean flag;
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    int count;
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
