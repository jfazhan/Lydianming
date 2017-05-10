package yun.yjz.dianmin4;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class chooseClass extends Activity {
	private MyAdapter a1=null;
	private SQLiteDatabase banji;
	private SQLiteDatabase chuqin;
	private ListView bjml;     //����չʾ�б��View��Adapter(������)����������ӳ�䵽ListView�ϵ��н�
	private ArrayList<RelativeLayout> ll=null;   
	private ArrayList<String> classname=new ArrayList<String>();
	private HashMap<String,Object> classid=null;
	private ImageButton addclass;
	private String delid=null;
	private static final int dialog1 = 1;
	private Handler handler=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xsmd);    //��ǰҳ����ص����ĸ�����
        ll=new ArrayList<RelativeLayout>();    //��̬����          
        a1=new MyAdapter(ll);
        bjml=(ListView)findViewById(R.id.listView1);
        bjml.setOnItemClickListener(new bjmlListener());
        bjml.setOnItemLongClickListener(new bjmulongListener());
        banji=this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null); //�ֶ�����������ݿ�
        classid=new HashMap<String,Object>(50);
        addclass=(ImageButton)findViewById(R.id.imageButton1);
        addclass.setOnClickListener(new addlcassListener());
        for(int i=0;i<50;i++){         //���ݿ�Ĳ�ѯ
        	String cmd="SELECT sName FROM"+" class"+i;
			String cmd1="SELECT Mac FROM"+" class"+i;
			Cursor cur = null;       //�α�
			Cursor cur1=null;
			try{
				cur=banji.rawQuery(cmd,null); //��һ������Ϊselect��䣬�ڶ�������Ϊselect�����ռλ��������ֵ
	   			cur1=banji.rawQuery(cmd1, null);
	   			cur.moveToFirst();
	   			cur1.moveToFirst();
	   			if(cur1.getString(0).equals("��")){
		   			classname.add(cur.getString(0).replaceAll(".xls", ""));
		   			classid.put(cur.getString(0).replaceAll(".xls", ""), i+"");
		   			}
	   			cur.close();
	   			cur1.close();
			}catch(Exception e){
	   			break;
	   		}
        }
        for(int i=0;i<classname.size();i++){
        	ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.drbjlist, null));  //��ȡview����
        }
        bjml.setAdapter(a1);
		banji.close();
		handler=new Handler(){  //ͨ��Handler�����������߳������ݵ���Ϣ
		public void handleMessage(Message msg) {  
	        	super.handleMessage(msg);  //ִ�и����handlerMessage����
	        	switch(msg.what){  //������Ϣ�ķ���
	        	case 0:
	        		    ll=new ArrayList<RelativeLayout>();  //�½�һ����̬�������ڴ洢�༶�б�
	        			a1=new MyAdapter(ll);  //�Զ���������
	        			classname=new ArrayList<String>();  //�½���̬���������ڴ洢
	        			banji=chooseClass.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
	        	        classid=new HashMap<String,Object>(50);
	        	        for(int i=0;i<50;i++){
	        	        	String cmd="SELECT sName FROM"+" class"+i;
	        				String cmd1="SELECT Mac FROM"+" class"+i;
	        				Cursor cur = null;
	        				Cursor cur1=null;
	        				try{
	        					cur=banji.rawQuery(cmd,null);
	        		   			cur1=banji.rawQuery(cmd1, null);
	        		   			cur.moveToFirst();
	        		   			cur1.moveToFirst();
	        		   			if(cur1.getString(0).equals("��")){
	        		   				//add�������Ը�ͬһ��keyֵ�����value���������һ��list���棬��put����ֻ�ܸ�ͬһ��key��Ψһһ��value
	        			   			classname.add(cur.getString(0).replaceAll(".xls", ""));  //���༶���Ƶĺ�׺.xlsȥ��
	        			   			classid.put(cur.getString(0).replaceAll(".xls", ""), i+"");  //
	        			   			}
	        		   			cur.close();
	        		   			cur1.close();
	        				}catch(Exception e){
	        		   			break;
	        		   		}
	        	        }
	        	        for(int i=0;i<classname.size();i++){
	        	        	//�����ҵ�Layout�ļ����µ�.XML�����ļ�������ʵ����
	        	        	ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.drbjlist, null));
	        	        }
	        	        bjml.setAdapter(a1);  //����������
	        			banji.close();
	        	   }
			 }
		};
    }
    protected Dialog onCreateDialog(int id) {
        switch(id){
        case dialog1:
            return buildDialog1(chooseClass.this);
        }
        return null;
   }
    private Dialog buildDialog1(Context context) {//ɾ���༶
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.scbj);
        builder.setMessage(R.string.scbjtext);
        builder.setPositiveButton(R.string.qr, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				banji=chooseClass.this.openOrCreateDatabase("banji.db", MODE_PRIVATE, null);
				chuqin=chooseClass.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE, null);
				String cmd="DELETE FROM class"+delid+" WHERE rowId<>0;";
				String cmd1="UPDATE class"+delid+" SET Mac='��' WHERE rowId=0;";
				banji.execSQL(cmd);
				banji.execSQL(cmd1);
				String cmd2="DROP TABLE class"+delid;
				chuqin.execSQL(cmd2);
				Toast.makeText(chooseClass.this,R.string.scwc,Toast.LENGTH_SHORT ).show();
				banji.close();
				chuqin.close();
				new Thread(new Runnable() {
		            @Override
		            public void run() {			                			               
		                    try {
		                        Thread.sleep(100);		                        
		                        Message msg = new Message();
		                        msg.what =0;
		                        handler.sendMessage(msg);
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }
		        }).start();
			}
        });
        builder.setNegativeButton(R.string.qx, null);
        return builder.create();
    }   
    private class addlcassListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent=new Intent();
    		intent.setClass(chooseClass.this, drbj.class);
    		chooseClass.this.startActivity(intent);
    		chooseClass.this.finish();
		}
	}
	private class bjmulongListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			delid=classid.get(classname.get(arg2))+"";  //��ȡ�༶�б��λ����Ϣ
			System.out.println("delid--------------------------------->"+delid);
			showDialog(dialog1);
			return false;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {   
	        if (keyCode == KeyEvent.KEYCODE_BACK) {   
	        	chooseClass.this.finish();
	            return true;   
	        } else  
	            return super.onKeyDown(keyCode, event);   
	    } 
    private class bjmlListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent1=new Intent("yun.yjz.dianmin4.chooseClass");
			intent1.putExtra("chooseclassid", classid.get(classname.get(arg2))+"");
			
			System.out.println("classid.get(classname.get(arg2))---------------->"+classid.get(classname.get(arg2))+"");
			intent1.putExtra("chooseclassname", classname.get(arg2));
			sendBroadcast(intent1);
			intent1.setClass(chooseClass.this, dianmin.class);
			startActivity(intent1);
			chooseClass.this.finish();
		}
	}
    
    private class MyAdapter extends BaseAdapter {       //�Զ���Listview������
		private ArrayList<RelativeLayout> ll;
	public MyAdapter(ArrayList<RelativeLayout> ll){
		this.ll = ll;
	}  
	@Override
	public int getCount() {
		return ll.size();
	}
	@Override
	public Object getItem(int position) {
		return ll.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = ll.get(position);
		TextView t = (TextView)ll.get(position).findViewById(R.id.textView1);
		t.setText(classname.get(position).toString());
		ImageView i=(ImageView)ll.get(position).findViewById(R.id.tubiao);
		i.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
		return convertView;
	}  
    }
}