package com.example.freemendrawwidget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.MailTo;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	final String APPWIDGETID = "appWidgetId";
	final String PUSH_SPPWIDGETBUTTON = "freemen.appwidget.pushbutton";
	String dirString = null;
	File oFile;
	private int appWidgetId ;
	private DrawingView drawingView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView)findViewById(R.id.drawView);
        
        Intent intent = getIntent();
        appWidgetId = intent.getIntExtra(APPWIDGETID, 0);
        String appWidgetIdString = String.valueOf(appWidgetId);
        
        if (appWidgetId == 0){
        	
        }else{
        	((TextView)findViewById(R.id.mainText)).setText(
        			"the "+appWidgetIdString+"appWidget call me");
        }
        
        //Check the dir
        dirString = Environment.getExternalStorageDirectory() + 
        		"/DrawingView/";
        File dir = new File(dirString);
        if (!(dir.exists() && dir.isDirectory())){
        	dir.mkdir();
        	Log.v("mainActivity", "no "+dir);
        }
        
        //set the saved Bitmap
        Bitmap sBitmap = null;
        oFile = new File(dir, appWidgetIdString+".jpg");
        if (!oFile.exists()){
        	Log.v("mainA", "create file"+appWidgetIdString);
        	try {
        		oFile.createNewFile();
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        		Log.e("setFile", e.toString());
        	}
//        	sBitmap = Bitmap.createBitmap(480, 640, Config.ARGB_8888);
        	drawingView.setSaveBitmap("NULL");
        }else{			//if existed read it
//        	sBitmap = BitmapFactory.decodeFile(dirString+appWidgetIdString);
        	Log.v("mainA", "have file"+oFile.getAbsolutePath());        	
        	drawingView.setSaveBitmap(oFile.getAbsolutePath());
        }
        
        
        //buttons
        ((Button)findViewById(R.id.saveBtn)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				drawingView.save();
				
			}
		});
        ((Button)findViewById(R.id.clsBtn)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				drawingView.clear();
			}
		});
        ((Button)findViewById(R.id.okBtn)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ByteArrayOutputStream bos = drawingView.finishDrawing();
				FileOutputStream fo;
				try {
					fo = new FileOutputStream(oFile);
					fo.write(bos.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        toRefreshAppWidget();
		        MainActivity.this.finish();
			}
		});
        
        
//        //send
//        toRefreshAppWidget();
    }
/*
 * To refresh the appWidget by the appWidgetId that push in
 * 
 */
    private void toRefreshAppWidget(){
		Intent sendIntent =new Intent();
		sendIntent.setAction(PUSH_SPPWIDGETBUTTON);
		
		Bundle extras = new Bundle();
		extras.putInt(APPWIDGETID, appWidgetId);
		sendIntent.putExtras(extras);
		Log.i("MainActivity", extras.toString());
		
		sendBroadcast(sendIntent);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public boolean onTouchEvent(MotionEvent event){
		drawingView.onTouchEvent(event);
		drawingView.postInvalidate();
    	return super.onTouchEvent(event);
    }

}
