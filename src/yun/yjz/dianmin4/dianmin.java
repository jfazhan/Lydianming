package yun.yjz.dianmin4;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class dianmin extends Activity {

	private Button dmbutton;
	private Button djbutton;
	private Button dxbutton;
	private BluetoothReceiver bluetoothReceiver ;  //�������������
	private BluetoothAdapter bluetoothAdapter ;  //����������
	private LinkedHashMap<String,String> macaddress=null;  //����Լ�����ϵ�����<��ֵString������String>
	private LinkedHashMap<String,String> haomaMap=null;
	private LinkedHashMap<String,Object> rowIdmap=null;
	private SQLiteDatabase allclass=null;
	private SQLiteDatabase chuqin=null;
	private String classId=null;
	private String classname=null;
	private ArrayList<String> weidaoList=null;
	private ArrayList<String> haoma=null;
	private  LinkedHashMap<String,String> State=null;
	private Handler handler=null;
	private ArrayList<RelativeLayout> ll=null;
	private ListView lay1list;
	private MyAdapter a1=null;
	private TextView toptext;
	private static final int dialog1 = 1;
	private static final int dialog2 = 2;
	private static final int dialog3 = 3;
	private TextView toptext2;
	private int djflag=0;
	private int djflag2=0;
	private SmsManager smsmanager=null;
	private int dmflag=0;
	private ImageView zhuye, fanhui;
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianmin);
        dmbutton=(Button)findViewById(R.id.button1);
        dmbutton.setOnClickListener(new dmListener());
        djbutton=(Button)findViewById(R.id.button2);
        djbutton.setOnClickListener(new djListener());
        dxbutton=(Button)findViewById(R.id.button3);
        dxbutton.setOnClickListener(new dxListener());
        toptext=(TextView)findViewById(R.id.textView1);  //ѧ������
        toptext2=(TextView)findViewById(R.id.textView2);  //δ��ѧ������
        zhuye = (ImageView) findViewById(R.id.dianmingzhuye);
		fanhui = (ImageView) findViewById(R.id.dianmingfanhui);
		zhuye.setOnClickListener(new buttonListener());
		fanhui.setOnClickListener(new buttonListener());
		
        bluetoothReceiver=new BluetoothReceiver();
        /*
         * ItentFilter��һ����������ֻ�з��������ĲŻᱻBroadCastReciver����
         * ����һ��IntentFilter���󣬽���action�ƶ�ΪBluetoothDevice.ACTION_FOUND
         */
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);  
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction("yun.yjz.dianmin4.chooseClass");
        //ע��㲥������
        registerReceiver(bluetoothReceiver, intentFilter);
        //�õ�BluetoothAdapter����
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lay1list=(ListView)findViewById(R.id.listView1);  //ÿ�еĳ��ڼ�¼
        classId=(String)getIntent().getStringExtra("chooseclassid");
        classname=(String)getIntent().getStringExtra("chooseclassname");
        toptext.setText("ѧ������"+"\n"+classname);
        handler = new Handler() {  //�����ͺʹ�����Ϣ������UI�̣߳���ʱ�Ĳ����������߳�
	           @Override
	           public void handleMessage(Message msg) {
	        	   super.handleMessage(msg);
	        	   switch(msg.what){
	        	   case 0:
	        		   djflag=2;
	        		   djflag2=0;
	        		   dmflag=1;
	        		   Toast.makeText(dianmin.this,R.string.smwc,Toast.LENGTH_SHORT ).show();
		        	   dmbutton.setEnabled(true); 
		        	   for(int i=0;i<weidaoList.size();i++){
		               	ll.add(i,(RelativeLayout)getLayoutInflater().inflate(R.layout.dianminlist, null));
		               	State.put(weidaoList.get(i), "wd");
		               }
		               lay1list.setAdapter(a1);
		               toptext2.setText(R.string.wdxsmd);
		               dmbutton.setEnabled(true);
		               break;
	        	   case 1:
	        		   ArrayList<RelativeLayout> l4=new ArrayList<RelativeLayout>();
	        		   MyAdapter al4=new MyAdapter(l4);
	        		   lay1list.setAdapter(al4);
	        		   toptext.setText("ѧ������"+"\n\n"+classname);
	        		   break;
	        	   case 2:
	        		   djflag=1;
	        		   toptext2.setText(R.string.zzsm);
	        		   break;
	        	   case 3:
	        		   djflag=1;
	        		   ArrayList<RelativeLayout> l5=new ArrayList<RelativeLayout>();
	        		   MyAdapter al5=new MyAdapter(l5);
	        		   lay1list.setAdapter(al5);
	        		   toptext2.setText(R.string.zzsm);
	        		   break;
	        		   
	        	   }
	           }
        };
    }
    
    /*
     * �����øûص�����ʱ��Androidϵͳ�Զ�����ÿ���Ի����״̬�������Ǻ�Activity����
     *showDialog()����onCreateDialog()����
     * 
     */
    protected Dialog onCreateDialog(int id) {
        switch(id){
        case dialog1:
            return buildDialog1(dianmin.this);
        case dialog2:
            return buildDialog2(dianmin.this);
        case dialog3:
        	return buildDialog3(dianmin.this);
        }
        return null;
    }
    
    private class dmListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			//����isEnable()�������жϵ�ǰ�����豸�Ƿ����
			if (bluetoothAdapter.isEnabled()) {	
				if(dmflag==0){
					showDialog(dialog1);
				}else{
					showDialog(dialog3);
				}
			}else{ 
			//����һ��intent���󣬸ö�����������һ��Activity����ʾ�û����������豸
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
			}
		}
	}
    
    private Dialog buildDialog1(Context context) {//������ʾ
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.jg);
        builder.setMessage(R.string.jgtextt);
        builder.setPositiveButton(R.string.ksdm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dmbutton.setEnabled(false);
				ready();
				new Thread(new Runnable() {
    	            @Override
    	            public void run() {			                			               
    	                    try {
    	                        Thread.sleep(100);		                        
    	                        Message msg = new Message();
    	                        msg.what = 2;
    	                        handler.sendMessage(msg);
    	                    } catch (InterruptedException e) {
    	                        e.printStackTrace();
    	                    }
    	                }
    	        }).start();
				//ɨ��һ������Ҫ12s,��ʼɨ��ÿɨ��һ�������豸�ͻᷢ��һ���㲥��ɨ������BroadCastReciver�㲥���������գ�
    			bluetoothAdapter.startDiscovery();
			}     	
        });
        builder.setNegativeButton(R.string.qx, null);
        return builder.create();
    }  
    private Dialog buildDialog2(Context context) {//����֪ͨ
    	/*
    	 * LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
    	 * intflater.infalte()��Layout�ļ�ת��ΪView����Android������뽫
    	 * xml�е�Layoutת��ΪView����.java�����в�����ֻ��ͨ��Infaler
    	 */
    	LayoutInflater inflater=LayoutInflater.from(this);
        final View textEntryView=inflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.qsrdxnr);
        builder.setView(textEntryView); 
        builder.setPositiveButton(R.string.fs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	EditText duanxinneirong=(EditText)textEntryView.findViewById(R.id.duanxin);
            	 for(int i=0;i<haoma.size();i++){
            		 try{
            			/*
            			 * ʵ�ֶ���֪ͨҪ��ȡĬ�ϵ���Ϣ������
            			 * ���Ͷ��ţ���һ���������ռ��˵ĵ�ַ���ڶ��������Ƕ������ĵĺ��룬nullΪĬ�����ĺ���
            			 * ����������Ϊ�������ݣ����ĸ�����Ϊ�����ѷ��͵Ĺ㲥��ͼ�����������Ϊ���ն��ŵ���ͼ
            			 * �����ŷ��ͳɹ�֮��Pi����ڲ�������intent�㲥��ȥ
            			 */ 
            			 smsmanager=SmsManager.getDefault();  
            			 PendingIntent pi = PendingIntent.getBroadcast(dianmin.this,0,new Intent(),0);
            			 smsmanager.sendTextMessage(haoma.get(i).toString(), null, duanxinneirong.getText().toString(), pi, null);
            			 Toast.makeText(dianmin.this, R.string.fscg,Toast.LENGTH_SHORT ).show();
            		}
             		catch (Exception e) {
             			e.printStackTrace();
             		}
             }
            }
        });
        builder.setNegativeButton(R.string.qx,null);
        return builder.create();
    }
    private Dialog buildDialog3(Context context) {//��������or���µ���
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.xzdmfs);
        builder.setMessage(R.string.xzdmfstext);
        builder.setPositiveButton(R.string.jxdm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dmbutton.setEnabled(false);	
            	haomaMap=new LinkedHashMap<String,String>();
        		rowIdmap=new LinkedHashMap<String,Object>();
        		weidaoList=new ArrayList<String>(20);
        		State=new LinkedHashMap<String,String>();
        		ll = new ArrayList<RelativeLayout>();
        		a1=new MyAdapter(ll);
    			bluetoothAdapter.startDiscovery();
    			new Thread(new Runnable() {
    	            @Override
    	            public void run() {			                			               
    	                    try {
    	                        Thread.sleep(100);		                        
    	                        Message msg = new Message();
    	                        msg.what = 3;
    	                        handler.sendMessage(msg);
    	                    } catch (InterruptedException e) {
    	                        e.printStackTrace();
    	                    }
    	                }
    	        }).start();
            }
        });
        builder.setNegativeButton(R.string.cxdm, new DialogInterface.OnClickListener() {//���µ���
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dmbutton.setEnabled(false);
            	ready();
    			bluetoothAdapter.startDiscovery();
    			new Thread(new Runnable() {
    	            @Override
    	            public void run() {			                			               
    	                    try {
    	                        Thread.sleep(100);		                        
    	                        Message msg = new Message();
    	                        msg.what = 3;
    	                        handler.sendMessage(msg);
    	                    } catch (InterruptedException e) {
    	                        e.printStackTrace();
    	                    }
    	                }
    	        }).start();
    			
            }
        });
        builder.setNeutralButton(R.string.qx, null);
        return builder.create();
    }  
   
    public class dxListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch(djflag){
			case 0://����δ��ʼ
				Toast.makeText(dianmin.this,R.string.dmwks,Toast.LENGTH_SHORT ).show();
			    break;
			case 1://���ڵ���
				Toast.makeText(dianmin.this,R.string.zzdmqsh,Toast.LENGTH_SHORT ).show();
			    break;
			case 2://��������
				ArrayList<String> hm=new ArrayList<String>();
				 for(int j=0;j<weidaoList.size();j++){
					 hm.add(State.get(weidaoList.get(j)));
				 }
			     haoma=new ArrayList<String>(20);
                 for(int i=0;i<hm.size();i++){
                	 if(hm.get(i).equals("wd")&&haomaMap.get(""+i).length()==11){
                		 haoma.add(haomaMap.get(""+i));
                	 }
                 }
				if(haomaMap.size()==0){  
					Toast.makeText(dianmin.this,R.string.ydq,Toast.LENGTH_SHORT ).show();
				}else if(haoma.size()==0){  //����Ϊ�գ���ʾû�е绰����
					Toast.makeText(dianmin.this,R.string.hmk,Toast.LENGTH_SHORT ).show();
				}else{
					showDialog(dialog2);
				}
			    break;
			}
		}
	}
   
    protected void onDestroy() {//��ע��㲥
		unregisterReceiver(bluetoothReceiver);
		super.onDestroy();
	}
	public class djListener implements OnClickListener {   //�Ǽ�
		@Override
		public void onClick(View v) {
			switch(djflag){
			case 0:
				Toast.makeText(dianmin.this,R.string.dmwks,Toast.LENGTH_SHORT ).show();
				break;
			case 1:
				Toast.makeText(dianmin.this,R.string.zzdmqsh,Toast.LENGTH_SHORT ).show();
				break;
			case 2:
				if(djflag2==0){
					djbutton.setClickable(false);  //���õ���������
					chuqin=dianmin.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE, null);
					ArrayList<String> rowidList=new ArrayList<String>();  //����һ����̬����
					Object[] rmapList=rowIdmap.values().toArray();  //��ȡrowIdmap�е�ֵ��Object������
					
					for(int i=0;i<rmapList.length;i++){ 
						System.out.println("rmapList[i]------------------------->"+rmapList[i]);
					}
					
					String cmd="SELECT*FROM class"+classId;
					Cursor cur=chuqin.rawQuery(cmd, null);  //ִ�в�ѯ����
					
					int a=cur.getColumnIndex("rowId");
					cur.moveToFirst();
					while(cur.moveToNext()){
						rowidList.add(cur.getInt(a)+"");   //ֻ��δ��ѧ����Ϣ
						System.out.println("cur.getInt(a)------------------------->"+cur.getInt(a)+" "+"rowId");
					}
					for(int i=0;i<rmapList.length;i++){  
						
						/**
						 * weidaoList<��̬�����б���������ѧ��
						 * rowIdmap�м�ֵ�԰�������ѧ�ţ���Ӧ�����к�
						 * State�а���ѧ�������͡�Wd��
						 */
						if(rowidList.contains(rmapList[i].toString())){  //�ж�rowidList���Ƿ����rmapList�е�charֵ
							
							if(State.get(weidaoList.get(i)).equals("wd")){  //���ش��б����ƶ�λ�õ�Ԫ��
								System.out.println("State.get------------------------->"+State.get(weidaoList.get(i)).equals("wd"));
								String cmd1="UPDATE class"+classId+" SET wdNo=wdNo+1 WHERE rowId="+rmapList[i]+";";
								System.out.println("rmapList1------------------------->"+rmapList[i]);
								chuqin.execSQL(cmd1);
							}else if(State.get(weidaoList.get(i)).equals("qj")){
								String cmd2="UPDATE class"+classId+" SET qjNo=qjNo+1 WHERE rowId="+rmapList[i]+";";
								System.out.println("rmapList2------------------------->"+rmapList[i]);
								chuqin.execSQL(cmd2);
							}
						}else{
							if(State.get(weidaoList.get(i)).equals("wd")){
								String cmd3="INSERT INTO class"+classId+" (rowId,stu,qjNo,wdNo) values("+rmapList[i]+",'"+weidaoList.get(i)+"',0,1)";
								System.out.println("δ��ͬѧ------------------------>"+rmapList[i]);
								chuqin.execSQL(cmd3);
							}else if(State.get(weidaoList.get(i)).equals("qj")){
								String cmd4="INSERT INTO class"+classId+" (rowId,stu,qjNo,wdNo) values("+rmapList[i]+",'"+weidaoList.get(i)+"',1,0)";
								System.out.println("���ͬѧ------------------------>"+rmapList[i]);
								chuqin.execSQL(cmd4);
							}
						}
					}
					cur.close();
					chuqin.close();
					Toast.makeText(dianmin.this,R.string.djywc,Toast.LENGTH_SHORT ).show();
					djbutton.setClickable(true);
					djflag2=1;
				}else if(djflag2==1){
					Toast.makeText(dianmin.this,R.string.djywcwcf,Toast.LENGTH_SHORT ).show();
				}
			}
		}
	}
    private void ready(){
    	macaddress=new LinkedHashMap<String,String>();
		haomaMap=new LinkedHashMap<String,String>();
		rowIdmap=new LinkedHashMap<String,Object>();
		weidaoList=new ArrayList<String>(20);
		State=new LinkedHashMap<String,String>();
		ll = new ArrayList<RelativeLayout>();
		a1=new MyAdapter(ll);
    }
    private class BluetoothReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
			 String action = intent.getAction();  //���intent��Action
			 /*
			  * ����ɨ��ʱ��ɨ�赽��һԶ�������豸ʱ������մ˹㲥
			  * �������ҵ�һ���豸
			  */
			 if (BluetoothDevice.ACTION_FOUND.equals(action)) {  
				 //��Intent�л�ȡ�豸�������ⲿ��ɨ��������豸����
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//��ȡ�������豸����mac��ַ������ȥ��ð�ţ������еĵ�ַ��ת��Ϊ��д
				macaddress.put(device.getName().toString(), device.getAddress().replaceAll(":", "").toUpperCase());
			}else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){//ɨ�����
				allclass=dianmin.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
				String cmd="SELECT*FROM class"+classId;
				Cursor cur10=allclass.rawQuery(cmd, null);  //���в�ѯ����
				cur10.moveToFirst();
				int a=cur10.getColumnIndex("stuNo");
	           	int b=cur10.getColumnIndex("sName");
	           	int c=cur10.getColumnIndex("Mac");
	           	int d=cur10.getColumnIndex("phone");
	           	int e=cur10.getColumnIndex("rowId");
	           	while(cur10.moveToNext()){
	           		if(!macaddress.containsValue(cur10.getString(c).toUpperCase().replaceAll(":", ""))){
	           			
						haomaMap.put(weidaoList.size()+"", cur10.getString(d));
						System.out.println("weidaoList.size(),cur10.getString(d)"+weidaoList.size()+""+cur10.getString(d));
						
						weidaoList.add(cur10.getString(a)+" "+cur10.getString(b));  //weidaoList��̬����
						System.out.println("cur10.getString(a),cur10.getString(b)"+cur10.getString(a)+" "+cur10.getString(b));
						
						State.put(cur10.getString(a)+" "+cur10.getString(b), "wd");  //put��ֵ�Ե�State 
						System.out.println("cur10.getString(a),cur10.getString(b),Wd"+cur10.getString(a)+" "+cur10.getString(b)+" "+"wd");
						
						rowIdmap.put(cur10.getString(a)+" "+cur10.getString(b), cur10.getInt(e));
						System.out.println("cur10.getString(a),cur10.getString(b), cur10.getInt(e)"+cur10.getString(a)+" "+cur10.getString(b)+" "+cur10.getInt(e));
	
					}
				}
				cur10.close();
				new Thread(new Runnable() {
		            @Override
		            public void run() {			                			               
		                    try {
		                        Thread.sleep(1000);		                        
		                        Message msg = new Message();
		                        msg.what = 0;
		                        handler.sendMessage(msg);
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }
		        }).start();
			}	
		}
    	
    }
    private class MyAdapter extends BaseAdapter {//lay2  Listview������
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
		t.setText(weidaoList.get(position));
		convertView.setClickable(true);
		final CheckBox c1 = (CheckBox)ll.get(position).findViewById(R.id.checkBox1);
		c1.setChecked(c1.isChecked());
		c1.setBackgroundColor(Color.parseColor("#ffffff"));
		final CheckBox c2 = (CheckBox)ll.get(position).findViewById(R.id.checkBox2);
		c2.setChecked(c2.isChecked());
		c2.setBackgroundColor(Color.parseColor("#ffffff"));
		final CheckBox c3 = (CheckBox)ll.get(position).findViewById(R.id.checkBox3);
		c3.setChecked(c1.isChecked());
		c3.setBackgroundColor(Color.parseColor("#ffffff"));

		c1.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					State.put(weidaoList.get(position),"yd");
					c2.setChecked(false);
					c3.setChecked(false);
				}else if(c2.isChecked())
					{
					State.put(weidaoList.get(position),"qj");
					c1.setChecked(false);
					c3.setChecked(false);}
				else if(c3.isChecked()){
						State.put(weidaoList.get(position),"wd");
						c1.setChecked(false);
						c2.setChecked(false);
					}
				}
			});
		c2.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					State.put(weidaoList.get(position), "qj");
					c1.setChecked(false);
					c3.setChecked(false);
				}else if(c1.isChecked()){					
					State.put(weidaoList.get(position), "yd");
					c2.setChecked(false);
					c3.setChecked(false);
				}else if(c3.isChecked()){
					State.put(weidaoList.get(position), "wd");
					c2.setChecked(false);
					c3.setChecked(false);
					}
				}
			}
		);
		c3.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					State.put(weidaoList.get(position), "wd");
					c1.setChecked(false);
					c2.setChecked(false);
				}else if(c1.isChecked()){
					State.put(weidaoList.get(position), "yd");
					c2.setChecked(false);
					c3.setChecked(false);
				}else if(c2.isChecked()){
					State.put(weidaoList.get(position), "qj");
					c1.setChecked(false);
					c3.setChecked(false);
					}
				}
		});		
		return convertView;
	}
	}
    private class buttonListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.dianmingfanhui) {
				Intent intent=new Intent();
				intent.setClass(dianmin.this, chooseClass.class);
				startActivity(intent);
				dianmin.this.finish();
			} else if (v.getId() == R.id.dianmingzhuye) {
				finish();
			}
		}
	}
  
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
        if (keyCode == KeyEvent.KEYCODE_BACK) {   
        	Intent intent = new Intent();
        	intent = new Intent(dianmin.this,chooseClass.class);
        	startActivity(intent);
        	dianmin.this.finish();
            return true;   
        } else  
            return super.onKeyDown(keyCode, event);   
    }  
}
