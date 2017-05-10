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
		   help.setText("*Ϊȷ������������ȷ������������ȷ���չ涨�ĸ�ʽ����ѧ��������"+"\n\t\t"
		   +"1.ѧ����������Ϊxls��ʽ������excelʱѡ�񱣴�����Ϊexcel97-2003���������ɣ�"+"\n\t\t"
		   +"2.���뽫ѧ�������ļ�����SD���в��ܱ�ʶ��"+"\n\t\t"
		   +"3.excel�ļ��У��ڵڶ��еĵ�һ�п�ʼдѧ�ţ��ڶ���д��Ӧѧ��������������д��Ӧѧ���ֻ�Mac��ַ��������д��Ӧѧ�����ֻ����루��¼��ѧ���ֻ����뽫�޷�ʹ�÷��Ͷ��Ź��ܣ�����Ҫʹ�ô˹��ܵĿ��Բ���д����"+"\n\n"
		   +"**************************"+"\n"
		   +"*����ֻ�����MAC��ַ��"+"\n\t\t"
		   +"1.Android�ֻ��ڴ���������£���ϵͳ����-�����ֻ�-״̬��Ϣ���ҵ�������ַ��"+"\n\t\t"
		   +"2.ŵ�����ֻ��ڴ����������롰*#2820#�����ɲ鿴������ַ"+"\n\t\t"
		   +"3.ƻ���ֻ�������-ͨ��-�����ֻ����ҵ�������ַ"+"\n\n"
		   +"**************************"+"\n"
		   +"*��ʼ����ǰ��������ѧ�����ֻ�������ȷ��ѧ���Լ��������������Լ���ѧ�ŷ����޷���ȷ��������Android�ֻ���Ҫ�������ɼ��ԣ�ƻ���ֻ��뽫����ͣ��������-ͨ��-�����ϣ���������Ϊ�ɱ�����״̬�����ɣ���"+"\n\n"
		   +"**************************"+"\n"
		   +"*�������󼰽���취��"+"\n\t\t"
		   +"���⣺ʹ�ù����г������"+"\n\t\t"
		   +"�����ȷ���ֻ��Բ���SD������������ҪSD����֧�֣�û��SD�������ֲ���Ԥ�ϵĴ���"+"\n\n\t\t"
		   +"���⣺�����������޷���ʾδ��ѧ����Ϣ"+"\n\t\t"
		   +"�����ȷ�ϰ༶������ʽ�Ƿ���ȷ���������½��ļ����ж�Ӧ�������ļ�ɾ��֮�����·��ã���ȷ����ȷ�γ��ֻ�USB��"+"\n\n\t\t"
		   +"���⣺��ѡ��ѧ������ʱ���޷��ҵ��ѵ����ѧ��������"+"\n\t\t"
		   +"�������鵼���ѧ�������Ƿ�Ϊxls��ʽ����excel97-2003�����������������޷�����xlsx��ʽ��excel�ļ��������������xlsx��ʽ��excel�ļ����Ϊexcel97-2003��������"+"\n\n\t\t");
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
