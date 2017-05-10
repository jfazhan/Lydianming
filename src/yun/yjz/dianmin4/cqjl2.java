package yun.yjz.dianmin4;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class cqjl2 extends Activity {
	
	private MyAdapter a1=null;
	private SQLiteDatabase banji;
	private ListView bjml;
	private ArrayList<RelativeLayout> ll=null;
	private ArrayList<HashMap<String,Object>> item=null;
	private Handler handler=null;
	private String classname;
	private TextView chuqinjl;
	private String classid;
	private static final int dialog4 = 4;
	private static final int dialog1 = 1;
	private ImageButton delete;
	private Button dcbutton;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cqjl);
        bjml=(ListView)findViewById(R.id.listView1);
        chuqinjl=(TextView)findViewById(R.id.textView1);
        delete=(ImageButton)findViewById(R.id.imageButton1);
        delete.setOnClickListener(new deleteListener());
        dcbutton=(Button)findViewById(R.id.button1);
        dcbutton.setOnClickListener(new dcListener());
        classname=(String)getIntent().getStringExtra("cqjlclassname");
        classid=(String)getIntent().getStringExtra("cqjlclassid");
        chuqinjl.setText("出勤记录"+"\n"+classname);
        updata(classid);
        handler=new Handler(){
        	@Override
	           public void handleMessage(Message msg) {
	        	   super.handleMessage(msg);
	        	   switch(msg.what){
	        	   case 0:
	        		   for(int i=0;i<item.size();i++){
	        			   ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.cqjllist, null));
	        		   }
	        		   bjml.setAdapter(a1);
	        		   break;
	        	   }
               }
        };
    }
    protected Dialog onCreateDialog(int id) {
        switch(id){
        case dialog1:
        	return buildDialog1(cqjl2.this);
        case dialog4:
        	return buildDialog4(cqjl2.this);
        }
        return null;
    }
    private Dialog buildDialog1(Context context) {//导出完成
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.dcwc);
        builder.setMessage(R.string.dcwctext);
        builder.setPositiveButton(R.string.qr, null);
        return builder.create();
    } 
    private Dialog buildDialog4(Context context) {//删除记录
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.jg);
        builder.setMessage(R.string.jgtext);
        builder.setPositiveButton(R.string.qr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	banji=cqjl2.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE,null);
            	String cmd="delete from class"+classid+" WHERE rowId<>0;";  
            	banji.execSQL(cmd);    
            	updata(classid);
            	banji.close();

            }
        });
        builder.setNegativeButton(R.string.qx,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	banji.close();
            }
        });
        return builder.create();
    }
    private class deleteListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			showDialog(dialog4);
		}
	}
    public class dcListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(item.size()!=0){
    			banji=cqjl2.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE, null);
    			String cmd="SELECT*FROM class"+classid;
    			Cursor cur=banji.rawQuery(cmd, null);
    			int a=cur.getColumnIndex("stu");
    			int b=cur.getColumnIndex("qjNo");
    			int c=cur.getColumnIndex("wdNo");
    			int x=1;
    			int d=1;
    			cur.moveToFirst();
    			File file=new File("//mnt//sdcard//出勤记录");
    			if(!file.exists()){
    				file.mkdir();
    			}
    			while(new File("//mnt//sdcard//出勤记录//"+classname+x+".xls").exists()){
    				x=x+1;
    			}
    			try {
    				//创建工作薄
    				WritableWorkbook book=Workbook.createWorkbook(new File("//mnt//sdcard//出勤记录//"+classname+x+".xls"));
    				WritableSheet sheet1=book.createSheet("sheet1", 0);  //创建工作表
    				Label label1=new Label(0,0,"学号姓名");  //创建表单，第一个参数表示列，第二个参数表示行
    				sheet1.addCell(label1);
    				label1=new Label(1,0,"请假次数");
    				sheet1.addCell(label1);
    				label1=new Label(2,0,"未到次数");
    				sheet1.addCell(label1);  //添加标签到工作表
    				while(cur.moveToNext()){
    					Label l=new Label(0,d,cur.getString(a));
    					sheet1.addCell(l);
    					jxl.write.Number number=new jxl.write.Number(1,d,cur.getInt(b));
    					sheet1.addCell(number);
    					number=new jxl.write.Number(2,d,cur.getInt(c));
    					sheet1.addCell(number);
    					d=d+1;
    				}
    				book.write();   //从内存写入到文件
    				book.close();
    				showDialog(dialog1);
    			} catch (Exception e) {
    				
    			}
    			cur.close();
    			banji.close();
    			}
		}
	}
    private void updata(String id){
    	item=new ArrayList<HashMap<String,Object>>(40);
    	banji=this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE,null);
    	ll=new ArrayList<RelativeLayout>();
		a1=new MyAdapter(ll);
    	String cmd="SELECT*FROM class"+id;
    	Cursor cur=banji.rawQuery(cmd, null);
    	int a=cur.getColumnIndex("stu");
       	int b=cur.getColumnIndex("qjNo");
       	int c=cur.getColumnIndex("wdNo");
    	cur.moveToFirst();
    	while(cur.moveToNext()){
    		HashMap<String,Object> map=new HashMap<String,Object>();
			   map.put("stu", cur.getString(a));	        			   
			   map.put("qjNo", cur.getString(b));
			   map.put("wdNo", cur.getString(c));
			   item.add(map);
    	}
    	new Thread(new Runnable() {
            @Override
            public void run() {			                			               
                    try {
                        Thread.sleep(10);		                        
                        Message msg = new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }).start();
    	cur.close();
    	banji.close();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
        if (keyCode == KeyEvent.KEYCODE_BACK) {   
        	Intent intent = new Intent();
        	intent = new Intent(cqjl2.this,cqjl.class);
        	startActivity(intent);
        	cqjl2.this.finish();
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);   
    } 
    
  
    private class MyAdapter extends BaseAdapter {//Listview适配器
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
		TextView stu = (TextView)ll.get(position).findViewById(R.id.xmxh);
		TextView wdNo = (TextView)ll.get(position).findViewById(R.id.wdcs);
		TextView qjNo = (TextView)ll.get(position).findViewById(R.id.qjcs);
		//语句将listview的每项的tag都标注上
		stu.setText(item.get(position).get("stu")+"");
		wdNo.setText(item.get(position).get("wdNo")+"");
		qjNo.setText(item.get(position).get("qjNo")+"");
		return convertView;
	}
	}
    
   
}