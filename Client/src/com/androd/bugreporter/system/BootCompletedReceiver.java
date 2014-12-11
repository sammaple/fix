package com.androd.bugreporter.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("Bugreporter", "recevie boot completed ... ");
		//Toast.makeText(context, "接收到开机", Toast.LENGTH_LONG).show();
		context.startService(new Intent(context, FixService.class));
	}

}
