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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class xsmd2 extends Activity {
	
	private MyAdapter a1=null;
	private SQLiteDatabase banji;
	private ListView bjml;
	private ArrayList<RelativeLayout> ll=null;
	private ArrayList<HashMap<String,Object>> item=null;
	private Handler handler=null;
	private String classname;
	private TextView classn;
	private int stuid;
	private String classid;
	private EditText xgxm;
	private EditText xgxh;
	private EditText xgmac;
	private EditText xghm;
	private EditText zjxm;
	private EditText zjxh;
	private EditText zjmac;
	private EditText zjhm;
	private static final int dialog0 = 0;
	private static final int dialog1 = 1;
	private static final int dialog2 = 2;
	private static final int dialog3 = 3;
	private static final int dialog4 = 4;
	private ImageButton addstu;
	private int a;
	
	private int longClickIndex=0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xsmd2);
        ll=new ArrayList<RelativeLayout>();
		a1=new MyAdapter(this,ll);
        bjml=(ListView)findViewById(R.id.listView1);
        bjml.setOnItemClickListener(new bjmlListener());
        bjml.setOnItemLongClickListener(new bjmulongListener());
        classn=(TextView)findViewById(R.id.textView1);
        addstu=(ImageButton)findViewById(R.id.imageButton1);
        addstu.setOnClickListener(new addstuListener());
        classid=(String)getIntent().getStringExtra("xsmdclassid");
        classname=(String)getIntent().getStringExtra("xsmdclassname");
        classn.setText("学生名单"+"\n"+classname);
        banji=xsmd2.this.openOrCreateDatabase("banji.db", MODE_PRIVATE, null);	
        updata(classid);
        handler=new Handler(){
        	@Override
	           public void handleMessage(Message msg) {
	        	   super.handleMessage(msg);
	        	   switch(msg.what){
	        	   case 0:
	        		   for(int i=0;i<item.size();i++){
	        			   ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.xsmd2list, null));
	        		   }
	        		   bjml.setAdapter(a1);
	        		   break;
	        	   case 1:
//	        		   banji=xsmd2.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
	       			   String cmd="SELECT*FROM class"+classid;
	       			   Cursor cur=banji.rawQuery(cmd, null);
	       			   cur.moveToPosition(stuid+1);
	       			   int xh=cur.getColumnIndex("stuNo");
	       			   int xm=cur.getColumnIndex("sName");
	       			   int mac=cur.getColumnIndex("Mac");
	       			   int phone=cur.getColumnIndex("phone");
	       			   try{
	        		   xgxm.setText(cur.getString(xm));
	        		   xgxh.setText(cur.getString(xh));
	        		   xgmac.setText(cur.getString(mac));
	        		   xghm.setText(cur.getString(phone));}catch(Exception e){}
	        		   cur.close();
	        		   break;
	        	   case 2:
//	        		   banji=xsmd2.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
	        		   item=new ArrayList<HashMap<String,Object>>(100);
	        		   ll=new ArrayList<RelativeLayout>();
//	        		   a1=new MyAdapter(this,ll);
	        		   a1.setLl(ll);
	        		   String cmd1="SELECT*FROM class"+classid;
	        		   Cursor cur1=banji.rawQuery(cmd1, null);
	        	       int a=cur1.getColumnIndex("stuNo");
	        	       int b=cur1.getColumnIndex("sName");
	        	       int c=cur1.getColumnIndex("Mac");
	        	       int d=cur1.getColumnIndex("phone");
	        	       cur1.moveToFirst();
	        	       while(cur1.moveToNext()){
	        	    		HashMap<String,Object> map=new HashMap<String,Object>();
	        				  map.put("numb", cur1.getString(a));	        			   
	        				  map.put("name", cur1.getString(b));
	        				  map.put("mac", cur1.getString(c));
	        				  map.put("phone", cur1.getString(d));
	        				  item.add(map);
	        				  ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.xsmd2list, null));
	        	    	}
	        	       bjml.setAdapter(a1);
	        	       cur1.close();
//	        	       banji.close();
	        		   break;
	        	   }
               }
        };
    }
    protected Dialog onCreateDialog(int id) {
        switch(id){
        case dialog0:
        	return buildDialog(xsmd2.this);
        case dialog1:
        	return buildDialog1(xsmd2.this);
        case dialog2:
        	return buildDialog2(xsmd2.this);	
        case dialog3:
        	return buildDialog3(xsmd2.this);
        case dialog4:
        	return buildDialog4(xsmd2.this);
        }
        return null;
    }
    private Dialog buildDialog1(Context context) {//修改学生信息
    	/*
    	 * LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
    	 * intflater.infalte()将Layout文件转换为View，在Android中如何想将
    	 * xml中的Layout转换为View放入.java代码中操作，只能通过Infaler
    	 */
    	 LayoutInflater inflater=LayoutInflater.from(this);
    	 final View lay1xgView=inflater.inflate(R.layout.lay1xg, null);
         xgxm=(EditText)lay1xgView.findViewById(R.id.editText2);
         xgxh=(EditText)lay1xgView.findViewById(R.id.editText1);
         xgmac=(EditText)lay1xgView.findViewById(R.id.editText3);
         xghm=(EditText)lay1xgView.findViewById(R.id.editText4);
         AlertDialog.Builder builder=new AlertDialog.Builder(context);
         builder.setTitle(R.string.xgxsxx);
         builder.setView(lay1xgView); 
         builder.setPositiveButton(R.string.bc, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
           int i=stuid+1;
           if(!xgxh.getText().toString().equals("")&&!xgxm.getText().toString().equals("")){
       		if(xgmac.getText().toString().replaceAll(":", "").toUpperCase().length()==12){
       			String cmd11="UPDATE class"+classid+" SET stuNo='"+xgxh.getText()+"',sName='"+xgxm.getText()+"',Mac='"+xgmac.getText()+"',phone='"+xghm.getText()+"' WHERE rowId="+i+";";
            	banji.execSQL(cmd11);
       		}else{
       			String cmd11="UPDATE class"+classid+" SET stuNo='"+xgxh.getText()+"',sName='"+xgxm.getText()+"',Mac='mac',phone='"+xghm.getText()+"' WHERE rowId="+i+";";
            	banji.execSQL(cmd11);
       		}
       	   }else{
       		   showDialog(dialog2);
       	   }
            	              new Thread(new Runnable() {
            	  	            @Override
            	  	            public void run() {			                			               
            	  	                    try {
            	  	                        Thread.sleep(100);		                        
            	  	                        Message msg = new Message();
            	  	                        msg.what=2;
            	  	                        handler.sendMessage(msg);
            	  	                    } catch (InterruptedException e) {
            	  	                        e.printStackTrace();
            	  	                    }
            	  	                }
            	  	        }).start();
            }
        });
        builder.setNegativeButton(R.string.rhmac,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//            	banji.close();
            	showDialog(dialog3);
            }
        });
        builder.setNegativeButton(R.string.qx,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//            	banji.close();
            }
        });
        return builder.create();
    }
    private Dialog buildDialog2(Context context) {//警告导入格式
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.jg);
        builder.setMessage(R.string.jgtexttt);
        builder.setPositiveButton(R.string.qr, null);
        return builder.create();
    } 
    private Dialog buildDialog3(Context context) {//提示获得蓝牙MAC地址
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.hdmac);
        builder.setMessage(R.string.andriodMAC);
        builder.setPositiveButton(R.string.qr, null);
        return builder.create();
    }
    
    
    private class bjmulongListener implements OnItemLongClickListener {
		
    	@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {

			//item.remove(arg2);
			Log.v("Test","index"+arg2);
			longClickIndex=arg2;
			showDialog(dialog0);
			return false;
		}
	}
    
    private Dialog buildDialog(Context context) {//删除学生
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.cancelxs);
        builder.setMessage(R.string.cancelxstext);
        builder.setPositiveButton(R.string.qr, new DialogInterface.OnClickListener(){
			@Override
			
			public void onClick(DialogInterface dialog, int which) {
				//int i=stuid+1;
				try {
	        	
					
					
					int index=longClickIndex;
					Log.v("Test",""+index);
					String stuNo=(String) item.get(index).get("numb");
					Log.v("Test",stuNo);
					String cmd="DELETE FROM class"+classid+" WHERE stuNo="+stuNo+";";
					
					banji.execSQL(cmd);
					
					item.clear();
					ll.clear();
					a1.notifyDataSetChanged();
					updata(classid);
					System.out.println("-----------------ssuuuuss----");
					
					Toast.makeText(xsmd2.this,R.string.scwc,Toast.LENGTH_SHORT ).show();	
					
					System.out.println("-----------------ssss----");
					
					
                } catch (SQLException e) {
                    e.printStackTrace();
                }
				
				
				
				new Thread(new Runnable() {
		            @Override
		            public void run() {			                			               
		                    try {
		                        Thread.sleep(100);		                        
		                        Message msg = new Message();
		                        msg.what =2;
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

    
    void jumptonowpage(){
    	Intent intent;
    	intent = new Intent(xsmd2.this,xsmd2.class);
    	startActivity(intent);
    	
    	
    }
    
    private Dialog buildDialog4(Context context) {//增加学生信息
    	LayoutInflater inflater=LayoutInflater.from(this);
    	final View lay1xgView=inflater.inflate(R.layout.lay1xg, null);
        zjxm=(EditText)lay1xgView.findViewById(R.id.editText2);
        zjxh=(EditText)lay1xgView.findViewById(R.id.editText1);
        zjmac=(EditText)lay1xgView.findViewById(R.id.editText3);
        zjhm=(EditText)lay1xgView.findViewById(R.id.editText4);
        String cmd3="SELECT*FROM class"+classid;
        Cursor cur3=banji.rawQuery(cmd3, null);
        int a3=cur3.getColumnIndex("rowId");
        System.out.println("a3--------------------------------------->"+a3);
        cur3.moveToLast();
        a=cur3.getInt(a3)+1;
        System.out.println("a--------------------------------------->"+a);
        cur3.close();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.tjxs);
        builder.setView(lay1xgView); 
       /* zjxm.setText("111111");
	    zjxh.setText("2222");
	    zjmac.setText("hhh");
	    zjhm.setText("jjj");*/
        builder.setPositiveButton(R.string.bc, new DialogInterface.OnClickListener() {
        	
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String cmd1;
            	if(!zjxh.getText().toString().equals("")&&!zjxm.getText().toString().equals("")){
            		if(zjmac.getText().toString().replaceAll(":", "").toUpperCase().length()==12){
            			cmd1="INSERT INTO class"+classid+" "+"(stuNo,sName,Mac,phone) values('"+zjxh.getText()+"',"+"'"+zjxm.getText()+"','"+zjmac.getText().toString().replaceAll(":", "").toUpperCase()+"','"+zjhm.getText()+"');";	
            			banji.execSQL(cmd1);
            			
            	    }else{
            	    	cmd1="INSERT INTO class"+classid+" "+"(stuNo,sName,Mac,phone) values('"+zjxh.getText()+"',"+"'"+zjxm.getText()+"','mac','"+zjhm.getText()+"');"; 
            	    	banji.execSQL(cmd1);
            	    }	
            		new Thread(new Runnable() {
            	  	            @Override
            	  	            public void run() {			                			               
            	  	                    try {
            	  	                        Thread.sleep(100);		                        
            	  	                        Message msg = new Message();
            	  	                        msg.what =2;
            	  	                        handler.sendMessage(msg);
            	  	                    } catch (InterruptedException e) {
            	  	                        e.printStackTrace();
            	  	                    }
            	  	                }
            	  	        }).start();
            	}else{
//            		banji.close();
            		showDialog(dialog2);
            	}
            }
        });
       
        builder.setNeutralButton(R.string.rhmac, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				banji.close();
				showDialog(dialog3);
			}
        });
        builder.setNegativeButton(R.string.qx,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//            	banji.close();
            }
        });
        return builder.create();
    }
    private class addstuListener implements OnClickListener {
		@Override
		public void onClick(View v) {
//			banji=xsmd2.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
			showDialog(dialog4);
			zjxm.setText("");
		    zjxh.setText("");
		    zjmac.setText("");
		    zjhm.setText("");
			
		}

	}
    private class bjmlListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			stuid=arg2;
			new Thread(new Runnable() {
	            @Override
	            public void run() {			                			               
	                    try {
	                        Thread.sleep(100);		                        
	                        Message msg = new Message();
	                        msg.what =1;
	                        handler.sendMessage(msg);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	        }).start();
			showDialog(dialog1);
		}
	}
    private void updata(String id){
    	item=new ArrayList<HashMap<String,Object>>(100);
//    	banji=this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
    	String cmd="SELECT*FROM class"+id;
    	Cursor cur=banji.rawQuery(cmd, null);
    	int a=cur.getColumnIndex("stuNo");
       	int b=cur.getColumnIndex("sName");
       	int c=cur.getColumnIndex("Mac");
       	int d=cur.getColumnIndex("phone");
    	cur.moveToFirst();
    	while(cur.moveToNext()){
    		HashMap<String,Object> map=new HashMap<String,Object>();
			   map.put("numb", cur.getString(a));	        			   
			   map.put("name", cur.getString(b));
			   map.put("mac", cur.getString(c));
			   map.put("phone", cur.getString(d));
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
//    	banji.close();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
        if (keyCode == KeyEvent.KEYCODE_BACK) {   
        	Intent intent = new Intent();
        	intent = new Intent(xsmd2.this,xsmd.class);
        	startActivity(intent);
        	xsmd2.this.finish();
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);   
    }
   
    private class MyAdapter extends BaseAdapter {//Listview适配器
    	private Context context;
	private ArrayList<RelativeLayout> ll;
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public ArrayList<RelativeLayout> getLl() {
		return ll;
	}
	public void setLl(ArrayList<RelativeLayout> ll) {
		this.ll = ll;
	}
	public MyAdapter(Context context,ArrayList<RelativeLayout> ll){
		this.ll = ll;
		this.context=context;
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
		TextView stuno;
		TextView name;
		TextView mac;
		TextView phoneno;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.xsmd2list, null);
		}
		stuno=(TextView)convertView.findViewById(R.id.stuno);
		name=(TextView)convertView.findViewById(R.id.stuname);
		mac=(TextView)convertView.findViewById(R.id.mac);
		phoneno=(TextView)convertView.findViewById(R.id.phone);
		
		if(position<item.size()&&position>-1){
			stuno.setText(item.get(position).get("numb")+"");
			name.setText(item.get(position).get("name")+"");
			mac.setText(item.get(position).get("mac")+"");
			phoneno.setText(item.get(position).get("phone")+"");
		}
		return convertView;
	}
	}
   
}
