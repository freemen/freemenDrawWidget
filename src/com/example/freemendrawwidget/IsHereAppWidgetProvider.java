package com.example.freemendrawwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.Toast;

public class IsHereAppWidgetProvider extends AppWidgetProvider {

	final String PUSH_SPPWIDGETBUTTON = "freemen.appwidget.pushbutton";
	final String APPWIDGETID = "appWidgetId";
	private int pushNum = 0;
	
	public void onUpdate(Context context, AppWidgetManager awm,
			int[] appWidgetIds){
		Toast.makeText(context, "appWidgetProvider!I am on Update!", Toast.LENGTH_SHORT).show();
		for (int appWidgetId : appWidgetIds) {
			Log.i("updateLog", "this is ["+appWidgetId+"] onUpdate");
			
			Intent intent = new Intent(context, MainActivity.class);
//			Intent intent =new Intent();
//			intent.setAction(PUSH_SPPWIDGETBUTTON);
			
			Bundle extras = new Bundle();
			extras.putInt(APPWIDGETID, appWidgetId);
			intent.putExtras(extras);
			Log.i("updateLog", extras.toString());
			
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			RemoteViews remoteViews =
					new RemoteViews(context.getPackageName(), R.layout.appwidgetishere);
			
			remoteViews.setOnClickPendingIntent(R.id.imageButton1, pendingIntent);
			
			awm.updateAppWidget(appWidgetId, remoteViews);
			
		}
		
		super.onUpdate(context, awm, appWidgetIds);
	}
	
	public void onReceive(Context context, Intent intent){
		Log.i("onReceive", "begin");
		
		String action = intent.getAction();
		if (PUSH_SPPWIDGETBUTTON.equals(action)){
			Log.i("onReceive","AppWidgetButton");
			RemoteViews remoteViews =
					new RemoteViews(context.getPackageName(), R.layout.appwidgetishere);
			pushNum++;
			int appWidgetId = intent.getIntExtra(APPWIDGETID, 0);
			String s = String.valueOf(appWidgetId);
			Log.i("onReceive", "the pushNum is "+String.valueOf(pushNum));
			Log.i("onReceive", "push the "+s);
			remoteViews.setTextViewText(R.id.textView1, "push "+s);
			
			String file = Environment.getExternalStorageDirectory() + 
        			"/DrawingView/"+s;
			Log.i("onReceive","want to draw"+file);
			Bitmap oBitmap = BitmapFactory.decodeFile(file);
			remoteViews.setImageViewBitmap(R.id.showImageView, oBitmap);
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			
			ComponentName componentName = new ComponentName(context, IsHereAppWidgetProvider.class);
			
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}else{
			super.onReceive(context, intent);
		}
		
//		super.onReceive(context, intent);

	}
	
	public void onDeleted(Context context, int[] appWidgetIds){
		final int N = appWidgetIds.length;
		for (int appWidgetId : appWidgetIds) {
			Log.i("delLog", "this is ["+appWidgetId+"] onDeleted");
		}
	}
}
