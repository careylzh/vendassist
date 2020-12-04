package com.overseer.vendassist;

import android.content.Context;

public class Utils {
    static int findResIdByString(Context i, String name, String dataType){
        switch(dataType){
            case "image":
                //ProductInfoActivity.this.getResources().getIdentifier(productId+"_image","drawable",ProductInfoActivity.this.getPackageName());
                return i.getResources().getIdentifier(name+"_image","drawable",i.getPackageName());
            case "text":
                return i.getResources().getIdentifier(name+"_text","string",i.getPackageName());
            case "title":
                return i.getResources().getIdentifier(name+"_title","string",i.getPackageName());
            case "POPUP":
                return i.getResources().getIdentifier(name+"_POPUP","string",i.getPackageName());
            default:
                return 0;
        }
    }

}
