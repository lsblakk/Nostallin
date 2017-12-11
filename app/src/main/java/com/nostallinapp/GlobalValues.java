package com.nostallinapp;

import android.support.annotation.Keep;

/**
 * Created by Niyati on 7/17/2017.
 */

@Keep
public class GlobalValues {

    public  String id;
    public  String no_of_stalls;
    public  String name;
    public String add;
   public String no_of_floor;
   public int total_hits;
   public int all_stalls;
    public int atleast_one_stall;
    public int no_stalls;
    public int gn_yes;
    public int gn_no;


    public GlobalValues(String id,String name, String add, String no_of_stalls,String no_of_floor,int total_hits,int atleast_one_stall,int all_stalls,int no_stalls, int gn_yes, int gn_no){

    this.id=id;
    this.name=name;
    this.add = add;
    this.no_of_stalls=no_of_stalls;
    this.no_of_floor=no_of_floor;
    this.total_hits=total_hits;
    this.atleast_one_stall=atleast_one_stall;
        this.all_stalls=all_stalls;
        this.no_stalls=no_stalls;
        this.gn_yes = gn_yes;
        this.gn_no = gn_no;
}
}
