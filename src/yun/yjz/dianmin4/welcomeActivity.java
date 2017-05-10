package yun.yjz.dianmin4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class welcomeActivity extends Activity{
	   protected void onCreate(Bundle savedInstanceState){
		   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.welcome);
		   Handler x=new Handler();
		   x.postDelayed(new splashhandler(), 2000);
		   }
	   private class splashhandler implements Runnable{
		@Override
		public void run() {
			startActivity(new Intent(welcomeActivity.this,Dianmin4Activity.class));
			welcomeActivity.this.finish();
		}
	   }
}
