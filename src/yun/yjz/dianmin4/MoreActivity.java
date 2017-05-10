package yun.yjz.dianmin4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MoreActivity extends Activity{

	private ImageView zhuye, fanhui;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		zhuye = (ImageView) findViewById(R.id.moretozhuye);
		fanhui = (ImageView) findViewById(R.id.morefanhui);
		zhuye.setOnClickListener(new buttonListener());
		fanhui.setOnClickListener(new buttonListener());
	}
	private class buttonListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.morefanhui) {
				finish();
			} else if (v.getId() == R.id.moretozhuye) {
				finish();
			}
		}
	}

}

