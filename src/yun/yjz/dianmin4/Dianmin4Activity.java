package yun.yjz.dianmin4;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class Dianmin4Activity extends Activity {
    
	/** Called when the activity is first created. */
    private Button xsdmbutton; 
    private Button xsmdbutton;
    private Button cqjlbutton;
    private Button bjdrbutton;
    private Button bzbutton;
    private Button gybutton;
    private Button moreButton;
    private ImageButton helpImageView;
    
    private DatePicker datePicker;
	private TextView dateView;
	protected static final int DATE_DIALOG_ID = 0;
	private int year;
	private int month;
	private int day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        xsdmbutton=(Button)findViewById(R.id.xsdm);
        xsdmbutton.setOnClickListener(new xsdmListener());
        xsmdbutton=(Button)findViewById(R.id.xsmd);
        xsmdbutton.setOnClickListener(new xsmdListener());
        cqjlbutton=(Button)findViewById(R.id.cqjl);
        cqjlbutton.setOnClickListener(new cqjlListener());
        bjdrbutton=(Button)findViewById(R.id.bjdr);
        bjdrbutton.setOnClickListener(new bjdrListener());
        bzbutton=(Button)findViewById(R.id.bz);
        bzbutton.setOnClickListener(new bzListener());
        gybutton=(Button)findViewById(R.id.gy);
        gybutton.setOnClickListener(new gyListener());  
        moreButton=(Button)findViewById(R.id.moreButton);
        moreButton.setOnClickListener(new mbListener());
        helpImageView=(ImageButton) findViewById(R.id.helpImageView);
        helpImageView.setOnClickListener(new hpListener());
        
        datePicker = (DatePicker) findViewById(R.id.datePicker1);
		dateView = (TextView) findViewById(R.id.textView1);
		Calendar c = Calendar.getInstance();
		year = c.get(c.YEAR);
		month = c.get(c.MONTH)+1;
		day = c.get(c.DATE);
		dateView.setText(year + "-" + month + "-" + day);
		updateDisplay();
		dateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});
	}

	private void updateDisplay() {
		dateView.setText(new StringBuilder().append(year).append("-")
				.append(month).append("-").append(day));

	}

	private DatePickerDialog.OnDateSetListener date = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			year = years;
			month = monthOfYear;
			day = dayOfMonth;
			updateDisplay();
		}
	};
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, date, year, month, day);
		}
		return null;
	}

    private class xsdmListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, chooseClass.class);
			startActivity(intent1);
		}
    }
    private class xsmdListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, xsmd.class);
			startActivity(intent1);
		}
    }
    private class cqjlListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, cqjl.class);
			startActivity(intent1);
		}
    }
    private class bjdrListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, drbj.class);
			startActivity(intent1);
		}
    }
    
    
    public class gyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, about.class);
			startActivity(intent1);
		}
	}
	public class bzListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, helpActivity.class);
			startActivity(intent1);
		}
	}
	
	public class mbListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, MoreActivity.class);
			startActivity(intent1);
		}
    	
    }
	
	public class hpListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent1=new Intent();
			intent1.setClass(Dianmin4Activity.this, helpActivity.class);
			startActivity(intent1);
		}
    	
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (keyCode == KeyEvent.KEYCODE_BACK
				) {

			builder.setTitle("提示:");
			builder.setIcon(android.R.drawable.ic_dialog_info);

			builder.setMessage("您确定您想要退出?")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									System.exit(0);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

		}

		AlertDialog alert = builder.create();
		alert.show();
		return super.onKeyDown(keyCode, event);
	}
}