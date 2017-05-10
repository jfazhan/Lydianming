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

public class helpActivity extends Activity{
	
	private TextView help;
	private ImageView zhuye, fanhui;
	   protected void onCreate(Bundle savedInstanceState){
		   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.help);
		   zhuye = (ImageView) findViewById(R.id.helpzhuye);
		   fanhui = (ImageView) findViewById(R.id.helpfanhui);
		   zhuye.setOnClickListener(new buttonListener());
		   fanhui.setOnClickListener(new buttonListener());
		   help=(TextView)findViewById(R.id.textView2);
		   help.setMovementMethod(ScrollingMovementMethod.getInstance());
		   help.setText("*为确保点名程序正确工作，必须正确按照规定的格式导入学生名单："+"\n\t\t"
		   +"1.学生名单必须为xls格式（保存excel时选择保存类型为excel97-2003工作簿即可）"+"\n\t\t"
		   +"2.必须将学生名单文件放入SD卡中才能被识别"+"\n\t\t"
		   +"3.excel文件中，在第二行的第一列开始写学号，第二列写对应学生姓名，第三列写对应学生手机Mac地址，第四列写对应学生的手机号码（不录入学生手机号码将无法使用发送短信功能，不需要使用此功能的可以不用写）。"+"\n\n"
		   +"**************************"+"\n"
		   +"*获得手机蓝牙MAC地址："+"\n\t\t"
		   +"1.Android手机在打开蓝牙情况下，在系统设置-关于手机-状态消息中找到蓝牙地址。"+"\n\t\t"
		   +"2.诺基亚手机在待机界面输入“*#2820#”即可查看蓝牙地址"+"\n\t\t"
		   +"3.苹果手机在设置-通用-关于手机中找到蓝牙地址"+"\n\n"
		   +"**************************"+"\n"
		   +"*开始点名前，请先让学生打开手机蓝牙并确保学生自己的蓝牙名称是自己的学号否则无法正确点名，（Android手机还要打开蓝牙可见性，苹果手机请将界面停留在设置-通用-蓝牙上（看见“现为可被发现状态”即可））"+"\n\n"
		   +"**************************"+"\n"
		   +"*常见错误及解决办法："+"\n\t\t"
		   +"问题：使用过程中程序出错"+"\n\t\t"
		   +"解决：确保手机以插入SD卡，本程序需要SD卡的支持，没有SD卡将出现不可预料的错误"+"\n\n\t\t"
		   +"问题：点名结束后无法显示未到学生信息"+"\n\t\t"
		   +"解决：确认班级名单格式是否正确，或者重新将文件夹中对应的名单文件删除之后重新放置，并确定正确拔出手机USB。"+"\n\n\t\t"
		   +"问题：在选择学生名单时，无法找到已导入的学生名单。"+"\n\t\t"
		   +"解决：检查导入的学生名单是否为xls格式（即excel97-2003工作簿），本程序无法读出xlsx格式的excel文件。解决方法，将xlsx格式的excel文件另存为excel97-2003工作簿。"+"\n\n\t\t");
	   }
	   private class buttonListener implements OnClickListener {
			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.helpfanhui) {
					finish();
				} else if (v.getId() == R.id.helpzhuye) {
					finish();
				}
			}
		}
}
