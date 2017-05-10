package yun.yjz.dianmin4;


import yun.yjz.dianmin4.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class about extends Activity{
	
	private TextView about;
	private ImageView zhuye, fanhui;
	   protected void onCreate(Bundle savedInstanceState){
		   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.about);
		   about=(TextView)findViewById(R.id.textView3);
		   about.setMovementMethod(ScrollingMovementMethod.getInstance());
		   about.setText(R.string.about);
		   zhuye = (ImageView) findViewById(R.id.guanyuzhuye);
		   fanhui = (ImageView) findViewById(R.id.guanyufanhui);
		   zhuye.setOnClickListener(new buttonListener());
		   fanhui.setOnClickListener(new buttonListener());
	   }
	   private class buttonListener implements OnClickListener {
			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.guanyufanhui) {
					finish();
				} else if (v.getId() == R.id.guanyuzhuye) {
					finish();
				}
			}
		}
}
