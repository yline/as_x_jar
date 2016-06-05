package com.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class CalculateService extends Service
{
    private int mIntParam1;
    
    private int mIntparam2;
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (null == intent)
        {
            intent = new Intent();
        }
        parseParams(intent);
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void parseParams(Intent intent)
    {
        mIntParam1 = intent.getIntExtra("param1", 0);
        mIntparam2 = intent.getIntExtra("param2", 0);
    }
    
    public int getResult()
    {
        return mIntParam1 + mIntparam2;
    }
    
    public static void startAction(Context context, int value1, int value2)
    {
        context.startService(
            new Intent(context, CalculateService.class).putExtra("param1", value1).putExtra("param2", value2));
    }
}
