package yun.yjz.dianmin4;

import java.io.File;
import java.util.ArrayList;

import yun.yjz.dianmin4.R;
import jxl.Sheet;
import jxl.Workbook;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class drbj extends Activity {
	private ImageView zhuye, fanhui;
	private SQLiteDatabase banji;
	private SQLiteDatabase chuqin;
	private ListView mulu;
	private Workbook book;
    private Sheet sheet;
	private MyAdapter a1=null;
	private ArrayList<RelativeLayout> ll=null;
	private File[] files=null;
	private ArrayList<File> afile;
	private ArrayList<String> tclassflag=new ArrayList<String>();
	private ArrayList<String> tclass1=new ArrayList<String>();
	private int Rows;
	private static final int  dialog1 = 1;
	private String classpath=null;
	/*因为所设计的程序必须把excel表格放在SDcard中，所以就没有对SDcard的状态进行判断获取SDcard目录
	 * 
	 *获取SDcard目录
	 */
	private File fileflag=new File("//mnt//sdcard"); 
	private int flag=0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//可用于全屏显示
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drbj);
        zhuye = (ImageView) findViewById(R.id.bjdrzhuye);
		fanhui = (ImageView) findViewById(R.id.bjdrfanhui);
		zhuye.setOnClickListener(new buttonListener());
		fanhui.setOnClickListener(new buttonListener());  
        ll=new ArrayList<RelativeLayout>();
		a1=new MyAdapter(ll);
        mulu=(ListView)findViewById(R.id.listView1);  //导入班级目录的显示列表
        mulu.setOnItemClickListener(new muluOnItemClickListener());
        files=new File("//mnt//sdcard").listFiles();  //读取文件列表并读取
		afile=new ArrayList<File>();
		banji=this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
		for(int j=0;j<50;j++){//遍历数据库，获得已存在的班级名
			String cmd="SELECT sName FROM"+" class"+j;
			String cmd2="SELECT Mac FROM"+" class"+j;
			Cursor cur = null;
			Cursor cur1=null;
			try{
	   			cur=banji.rawQuery(cmd,null);
	   			cur1=banji.rawQuery(cmd2, null);
	   			cur.moveToFirst();
	   			cur1.moveToFirst();
	   			/*当首次导入班级时不会执行此代码，只有当我们删除了班级列表时才会执行此代码*/
	   			if(cur1.getString(0).equals("有")){ //true
	   				System.out.println("cur1.getString(0).equals()-------------------------->"+cur1.getString(0).equals("有"));
	   				tclass1.add(cur.getString(0).replaceAll(".xls", "")); //财务管理
	   				System.out.println("tclass1-------------------------->"+cur.getString(0).replaceAll(".xls", ""));
	   			}
	   			
	   			tclassflag.add(cur1.getString(0));//有
	   			System.out.println("tclassflag-------------------------->"+cur1.getString(0));
	   			cur.close();
	   			cur1.close();
	   		}catch(Exception e){
	   			break;
	   		}
		}
		try{
		   for(int i=0;i<files.length;i++){   //当首次点击"导入班级"按钮时进入根目录
			   System.out.println("文件列表的长度-------------------------->"+files.length);
			   if(ChechFile(files[i])>0){
				   System.out.println("ChechFile-------------------------->"+ChechFile(files[i]));
				   ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.drbjlist, null));
				   afile.add(files[i]);
				   flag=1;
			   }
		   }
		   mulu.setAdapter(a1);
		   }catch(Exception e){
			   flag=1;
		   }
		   banji.close();
    }
    protected Dialog onCreateDialog(int id) {
        switch(id){
        case dialog1:
        	return buildDialog1(drbj.this);
        }
        return null;
    }
    private Dialog buildDialog1(Context context) {//警告导入格式
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.qr);
        builder.setMessage(R.string.qrdrbj);
        builder.setPositiveButton(R.string.qr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	choiceExcel(classpath);  ///classpath == mnt/sdcard/网络工程.xls
            	Intent intent=new Intent();
        		intent.setClass(drbj.this, xsmd.class);
        		drbj.this.startActivity(intent);
        		drbj.this.finish();
            }
        });
        builder.setNegativeButton(R.string.qx, null);
        return builder.create();
    }
    
    private class muluOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			GointoFile(afile.get(arg2).getPath());
			
			System.out.println("GointoFile(afile.get(arg2).getPath())"+afile.get(arg2).getPath());
		}
	   }
    
	   private void GointoFile(String path) {//读取文件夹或者Excel文件
		   if(ChechFile(new File(path))==1){
			   flag=0;
			   ll=new ArrayList<RelativeLayout>();  //用来绑定一个数组，支持泛型操作
			   a1=new MyAdapter(ll);
			   files=new File(path).listFiles();
			   afile=new ArrayList<File>();
			   for(int i=0;i<files.length;i++){
				   if(ChechFile(files[i])>0){
					   ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.drbjlist, null));
					   afile.add(files[i]);
				   }
			   }
			   fileflag=new File(path).getParentFile();  //返一个新文件，从该文件的父目录下
			   mulu.setAdapter(a1);
		  }else if(ChechFile(new File(path))==2){
			   classpath=path;
			   showDialog(dialog1);
		   }
	   }
	   private void choiceExcel(String path){//选择Excel判断,path的值是当前点击的班级路径
		   File e=new File(path);  //path == /mnt/sdcard/网络工程.xls
		   if(tclass1.contains(e.getName().replaceAll(".xls", ""))){
			   Toast.makeText(drbj.this,R.string.bjycz,Toast.LENGTH_SHORT ).show();
		   }else{ //班级导入的过程
			   Toast.makeText(drbj.this,R.string.zzdr,Toast.LENGTH_SHORT ).show();
			   mulu.setClickable(false);
			   banji=this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null);
			   if(tclassflag.contains("空")){  //当班级标志为空时，执行导入操作
				   for(int i=0;i<tclassflag.size();i++){
					   if(tclassflag.get(i).equals("空")){
						   String cmd3="UPDATE class"+i+" "+"SET sName ='"+e.getName().replaceAll(".xls", "")+ "' WHERE stuNo=-1;";
						   banji.execSQL(cmd3);
						   cmd3="UPDATE class"+i+" "+"SET Mac ='有' WHERE stuNo=-1;";
						   banji.execSQL(cmd3);
						   tclassflag.set(i, "有");
						   tclass1.add(e.getName().replaceAll(".xls", ""));
						   System.out.println("e.getName().replaceAll()------------------>"+e.getName().replaceAll(".xls", ""));
						   importExcel(e,i);
						   break;
					   }
				   }
			   }else{  //首次进行班级导入时执行此代码
				   int i=tclassflag.size();
				   String cmd4="create table"+" class"+i+" "+"(rowId PRIMARY KEY,stuNo String,sName String,Mac String,phone String);";
				   banji.execSQL(cmd4);
				   System.out.println("tclassflag.size()------------------>"+tclassflag.size());
				   cmd4="INSERT INTO class"+i+" "+"(rowId,stuNo,sName,Mac,phone) values(0,-1,'"+e.getName().replaceAll(".xls", "")+"','有',0);";
				   banji.execSQL(cmd4);
				   tclass1.add(e.getName().replaceAll(".xls", ""));
				   tclassflag.add("有");
				   importExcel(e,i);
			   }
		   }
	   }
	   private void importExcel(File f,int i){//导入Excel到数据库
		   chuqin=this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE, null);
		   String cmd="create table class"+i+" (rowId PRIMARY KEY,stu String,qjNo INTEGER,wdNo INTEGER);";
		   chuqin.execSQL(cmd);
		   String cmd1="INSERT INTO class"+i+" "+"(rowId,stu,qjNo,wdNo) values(0,'"+f.getName().replaceAll(".xls", "")+"',0,0);";
		   chuqin.execSQL(cmd1);
		   chuqin.close();
		   try {
	    	book = Workbook.getWorkbook(f);  //获取一个工作表对象	
	        sheet = book.getSheet(0);  //得到第一个工作表
	        Rows = sheet.getRows();  //得到行数

	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		   for(int j=1;j<Rows;j++){
			   if(!sheet.getCell(1, j).getContents().equals("")&&!sheet.getCell(0, j).getContents().equals("")){
			   if(!sheet.getCell(2, j).getContents().equals("")){
			   String cmd5="INSERT INTO class"+i+" "+"(rowId,stuNo,sName,Mac,phone) values("+j+",'"+sheet.getCell(0, j).getContents()+"',"+"'"+sheet.getCell(1, j).getContents()+"','"+sheet.getCell(2, j).getContents()+"','"+sheet.getCell(3, j).getContents()+"');";
			   banji.execSQL(cmd5);
			   }else{
				   String cmd5="INSERT INTO class"+i+" "+"(rowId,stuNo,sName,Mac,phone) values("+j+",'"+sheet.getCell(0, j).getContents()+"',"+"'"+sheet.getCell(1, j).getContents()+"','mac','"+sheet.getCell(3, j).getContents()+"');";
			   banji.execSQL(cmd5);
			   }}
		   }
		   book.close();
		   banji.close();
		   mulu.setClickable(true);
		   Toast.makeText(drbj.this,R.string.drwc,Toast.LENGTH_SHORT ).show();
	   }
    
    private int ChechFile(File file) {//当点击"导入班级"时用于判断文件类型
		   if(!file.isFile()){
			   return 1;
		   }else if(file.getName().substring(file.getName().lastIndexOf('.')+1).equals("xls")){//取得文件夹的扩展名
			   return 2;
		   }else{
			   return 0;
		   }
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
		TextView t = (TextView)ll.get(position).findViewById(R.id.textView1);
		t.setText(afile.get(position).getName());
		ImageView i=(ImageView)ll.get(position).findViewById(R.id.tubiao);
		if(ChechFile(files[position])==1){
		i.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_contact_card));
		}else if(ChechFile(files[position])==2){
			i.setImageDrawable(getResources().getDrawable(android.R.drawable.star_off));
		}
		return convertView;
	}
	}
    private class buttonListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.bjdrfanhui) {
				finish();
			} else if (v.getId() == R.id.bjdrzhuye) {
				finish();
			}
		}
	}
   
}