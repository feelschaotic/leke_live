package com.juss.mediaplay;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by lenovo on 2016/7/22.
 */
public class SchoollistDialog extends Dialog {
    private Context context;
    public SchoollistDialog(Context context) {

        super(context);
    }
    public SchoollistDialog(Context context,int theme){
        super(context,theme);
    }

    public static class Builder{

    }


}
