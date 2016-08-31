package com.enuke.smartapp.installappdetails;

import java.util.Date;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.blinkawards.utility.Utility;

class PInfo {
    public  String appname = "";
    public  String pname = "";
    public  String versionName = "";
    public  int versionCode = 0;
    public  Drawable icon;
    public void prettyPrint(Context mContext) {
        ////System.out.println(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        InstallAppDetailsProvider.insertReferences(mContext,new AppDetailsBean(pname, versionName, appname, Utility.DateToString(new Date()), "INSTALLED"));
        
    }
}