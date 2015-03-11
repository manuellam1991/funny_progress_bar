package com.example.ass1;

import java.util.Random;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

class MyHandler extends Handler {
	Ass1 context;
	public MyHandler(Ass1 c) {
		context=c;
	}
	public void handleMessage(Message msg) {
		// Calculate the score!
		context.calculateScore();
		// if not reach the expected progress yet ...
		if(context.pb01.getProgress() < context.pb01ExProgress01) {
			int delta = 
			(context.pb01ExProgress01-context.pb01.getProgress()) /
			(Math.abs((context.pb01ExProgress01-context.pb01.getProgress()))) *
			context.pb01Speed;
			
			int result = context.pb01.getProgress() + delta;
			if(delta > 0)
				result = (result > context.pb01ExProgress01) ? 
				context.pb01ExProgress01 : result;
			else
				result = (result < context.pb01ExProgress01) ? 
				context.pb01ExProgress01 : result;
			// Set the progress and secondary progress bar values
			context.pb01.setProgress(result);;
			context.pb01.setSecondaryProgress(result+3);
		}
 	}
}

class ProgressBarMover extends Thread {
	Ass1 context;
	Random rand = new Random (1010); 
	public ProgressBarMover(Ass1 c) {
		context=c;
	}
	public void run () {
		while(!context.end) {
			// Only they are in the same spot, the pb will move again!
			// This will not only modify the expected movement but also the speed
			if(context.pb01.getProgress() == context.sb01.getProgress()) {
				context.pb01ExProgress01 = rand.nextInt(100);
				context.pb01Speed = rand.nextInt(4)+1;
			}
			// Send message to the handler!!
			((Ass1)context).handler.sendMessage(new Message());
			try {
				sleep(100);
			} catch (Exception e) {}
		}
	}
}
public class Ass1 extends Activity {

	SeekBar	sb01;
	ProgressBar pb01;
	int pb01ExProgress01=100, pb01Speed=2;	// Ex = expected
	TextView tv03;
	MyHandler handler;
	ProgressBarMover mover;
	int score=0;							
	boolean end=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		pb01 = (ProgressBar)findViewById(R.id.progressBar1);
	   	tv03 = (TextView) findViewById(R.id.textView3);      
	   	sb01 = (SeekBar)findViewById(R.id.seekBar1);
	   	handler = new MyHandler(this);
	   	mover = new ProgressBarMover(this);
	   	mover.start();
	}

	void calculateScore() {
	   	int scores[] = {12,9,7,5,3,1};
	   	int delta = pb01.getProgress() - sb01.getProgress();
	   	if(Math.abs(delta) < scores.length)
	   		score += scores[Math.abs(delta)] ;
	   	tv03.setText("Score: " + score);
	  }

}
