package com.example.freemendrawwidget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View{
	Paint paint = new Paint();
	Path path = new Path();
	Bitmap mBitmap = null;
	Canvas mcanvas = new Canvas();
	Bitmap saveBitmap = null;
	Canvas savecanvas = new Canvas();
	final int partNum = 4;
	int part;
	float x, oldx;
	float y, oldy;

	public DrawingView(Context context, AttributeSet attr) {
		super(context, attr);
//		paint.setColor(Color.rgb(220, 200, 20));
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.FILL);
		x=0;y=0;

	}
	
	public void setSaveBitmap(Bitmap bitmap){
		Log.v("DrawingView", "setting the save Bitmap in method 1");
		this.saveBitmap = Bitmap.createBitmap(bitmap);
		savecanvas.setBitmap(saveBitmap);
	}
	public void setSaveBitmap(String name){
		Log.v("DrawingView", "setting the save Bitmap in method 2");
		if ("NULL".equals(name)){
			this.saveBitmap = Bitmap.createBitmap(480, 640, Config.ARGB_8888);
		}else{
			
			Log.v("DrawingView", "decode"+name);
			
	        File soFile = new File(name);
	        if (!soFile.exists()){
	        	Log.v("mainA", "create file"+name);
				this.saveBitmap = Bitmap.createBitmap(480, 640, Config.ARGB_8888);
	        }else{
	        	Log.v("mainA", "have! file"+name);
				this.saveBitmap = BitmapFactory.decodeFile(soFile.getAbsolutePath());
	        	
	        }
//			try{
//				this.saveBitmap = BitmapFactory.decodeFile(name);
//			} catch (OutOfMemoryError e){
//				Log.e("DrawView","here");
//				try{
//					BitmapFactory.Options options = new BitmapFactory.Options();
//					options.inSampleSize = 2;
//					saveBitmap = BitmapFactory.decodeFile(name, options);
//				}catch(Exception ee){
//					Log.e(name, name, ee);
//				}
//			}
			
		}
		mBitmap = Bitmap.createBitmap(saveBitmap);
		mcanvas.setBitmap(mBitmap);
	}

	public void onDraw(Canvas canvas){
//		if(!path.isEmpty())
//		mcanvas = canvas;
//		mcanvas.drawCircle(x, y, 10, paint);
//		mcanvas.drawPath(path, paint);
		mcanvas.drawLine(oldx, oldy, x, y, paint);
		
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		Log.v("DrawView onDraw", "canvas");
		super.onDraw(canvas);
			
	}
	
	public boolean onTouchEvent(MotionEvent event){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldx = x = event.getX();
			oldy = y = event.getY()-80;
			break;
		case MotionEvent.ACTION_MOVE:
//			path.moveTo(x, y);
			oldx = x;
			oldy = y;
			Log.v("DV OnTouch", "moveto"+String.valueOf(x)+"and"+String.valueOf(y));
			x = event.getX();
			y = event.getY()-80;
//			path.lineTo(x, y);
			Log.v("DV OnTouch", "lineto"+String.valueOf(x)+"and"+String.valueOf(y));
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		
		return false;
	}
	
	public void clear(){
		mBitmap = Bitmap.createBitmap(480, 640, Config.ARGB_8888);
		mcanvas.setBitmap(mBitmap);
		invalidate();
	}
	
	public void save(){	//save and next
		saveBitmap = Bitmap.createBitmap(mBitmap);//TODO:scale
		clear();
	}
	
	public ByteArrayOutputStream finishDrawing(){
		save();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		saveBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
		return bos;
	}
	
}
