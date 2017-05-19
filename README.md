# Lydianming
1、 支持从Excel文件中导入学生信息；
2、 实现蓝牙点名功能。教师端的手机获取周围开启的蓝牙设备列表，自动与数据库中存储的学生数据比对，将点名结果存入数据库；
3、 实现出勤信息的统计。统计学生的出勤情况，能够将出勤信息导出至Excel文件；



一、导入班级

导入班级是使用该软件的第一步，是实现蓝牙点名的基础。首先要进入手机的根目录并选择目标班级，然后再导入到数据库，实现导入班级功能的核心代码如下：
File fileflag=new File("//mnt//sdcard");  //导入班级，首先要获取手机的SDcard的目录
private void GointoFile(String path)   //读取SDcard目录下的Excel文件
ll=new ArrayList<RelativeLayout>();  //新建一个动态数组用于存储班级列表
a1=new MyAdapter(ll);  //新建一个自定义适配器对象用来存放班级动态数组
files=new File(path).listFiles();  //列出文件夹下的所有文件和文件夹名
afile=new ArrayList<File>();  //新建一个文件夹的动态数组对象用于存储路径列表
//用来找到Layout文件夹下的.XML布局文件，并且实例化，显示在手机列表中
ll.add((RelativeLayout)getLayoutInflater().inflate(R.layout.drbjlist, null));
afile.add(files[i]);  //添加文件目录到列表	





二、删除班级

当班级的信息变更较大时，或者说导入错误班级时就需要实现班级删除功能，实现删除班级功能的核心代码如下：
//首先要打开文件系统中位于绝对路径banji数据库
banji=chooseClass.this.openOrCreateDatabase("banji.db", MODE_PRIVATE, null);
//根据班班级号从班级列表中删除对应的班级
String cmd="DELETE FROM class"+delid+" WHERE rowId<>0;";
banji.execSQL(cmd);  //执行该SQLite语句，实现对班级的删除





三、学生管理模块

学生管理主要是实现对学生个人信息的增、删、改、查，主要使用的是INSERT INTO、 SELECT、 DELETE 、UPDATE等SQLite语句实现对数据库的操作。实现核心代码如下：

1、插入学生信息

//使用SQLite中的insetinto语句实现对学生的学号、姓名、Mac地址、电话号码的插入
cmd1="INSERT INTO class"+classid+" "+"(rowId,stuNo,sName,Mac,phone) values
("+a+",'"+zjxh.getText()+"',"+"'"+zjxm.getText()+"','"+zjMac.getText().toString().replaceAll(":", "").toUpperCase()+"','"+zjhm.getText()+"');";
banji.execSQL(cmd1);   //在班级表中执行插入操作

2、查询学生信息

String cmd="SELECT*FROM banji"+classid;
//第一个参数为select语句，第二个参数为select语句中占位符参数的值
Cursor cur=banji.rawQuery(cmd, null);
//获取学生姓名和学号
int a=cur.getColumnIndex("stu");
//获取学生出勤的次数
int b=cur.getColumnIndex("qjNo");
//获取为出勤的次数
int c=cur.getColumnIndex("wdNo");

3、删除学生信息

//删除学生的序号和对应的数据
String cmd="DELETE FROM class"+delid+" WHERE rowId<>0;";
class.execSQL(cmd);//下发执行命令
class.close();//关闭资源

4、更新学生信息

//使用SQLite中的update语句实现对学生的学号、姓名、Mac地址、电话号码的根新
cmd1=" UPDATE class"+classid+" "+"(rowId,stuNo,sName,Mac,phone) values  
("+a+",'"+zjxh.getText()+"',"+"'"+zjxm.getText()+"','"+zjMac.getText().toString().replaceAll(":", "").toUpperCase()+"','"+zjhm.getText()+"');";
banji.execSQL(cmd1);   //在班级表中执行更新操作





四、学生点名模块

学生点名主要使用的蓝牙设备发现机制，通过蓝扫描功能实现蓝牙的Mac地址匹配进行点名，学生点名模块主要包括点名、短信通知、点名记录登记功能。

1、蓝牙点名

蓝牙点名主要是通过蓝牙的扫描和Mac地址认证实现蓝牙的点名，首先通过调用系统的API打开蓝牙，在AndroidManifest中对蓝牙设备发现、连接和配对进行权限设置，然后通过Mac地址认证，如果两个相同则该学生出勤了，如果未能认证则将未到信息加到未到学生列表中，蓝牙实现的核心代码如下：
	//调用系统的API打开蓝牙
Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//创建一个BluetoothReceiver对象来接收蓝牙设备的消息
BluetoothReceiver bluetoothReceiver=new BluetoothReceiver(); 
if (BluetoothDevice.ACTION_FOUND.equals(action)) {  //如果发现找到一个蓝牙设备就会从//intent对象中获取蓝牙设备
	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	//获取蓝牙的设备名和Mac地址，并且去掉冒号，把所有的地址都转化为大写
	Macaddress.put(device.getName().toString(), device.getAddress().replaceAll(":", "").
toUpperCase());   //把获得蓝牙设备的Mac地址全部转化为大写
}else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){//当蓝牙扫描设备结//束后打开班级表与班级表中的Mac地址和手机号码进行匹配
allclass=dianmin.this.openOrCreateDatabase("banji.db", MODE_PRIVATE,null); 
//从数据库中查询Mac地址和扫描的进行对比
if(!Macaddress.containsValue(cur10.getString(c).toUpperCase().replaceAll(":", ""))){
	//从数据库中查询学号和扫描的学号进行对比
		if(!Macaddress.containsKey(cur10.getString(a))){
			//将未到学生学号和学生姓名加入到未到学生列表中
			weidaoList.add(cur10.getString(a)+" "+cur10.getString(b));
}
}
在AndroidManifest.xml文件中的注册信息如下：
<!--允许程序连接到已配对的蓝牙设备 -->
<uses-permission android:name="android.permission.BLUETOOTH" /> 
<!--允许程序发现和配对蓝牙设备 -->
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />


2、 短信通知

当学生没有来上课时，老师可以使用该功能提醒学生要按时来上课，要在程序中实现此功能首先要在AndroidManifest中注册发送短信的权限，实例化一个SmsManager对象，通过getDefault()方法来获取默认消息管理器，再通过sendTextMessage()方法发送短信，实现该功能的核心代码如下：
SmsManager smsmanager=SmsManager.getDefault();  //实现短信通知要获得默认的消息管理器
//当短信发出成功后，pi会把内部描述的intent广播出去
PendingIntent pi = PendingIntent.getBroadcast(dianmin.this,0,new Intent(),0);
smsmanager.sendTextMessage(haoma.get(i).toString(), null, duanxinneirong.getText().toString(), pi, null);  //短息发送，第一个参数是收件人地址，第二个参数是短信中心号码，null为默认中心号码，第三个参数为短信内容，第四个参数为短信已发送的广播意图，最后一个参数对方接受短信的意图
在AndroidManifest.xml文件中的注册信息如下：
<!-- 发送短信的权限 -->
<uses-permission android:name="android.permission.SEND_SMS"/>


3、 登记记录

当点名结束之后，老师要对未到学生的信心进行登记，通过创建数库，更新数据库，信息插入数据库来实现对未到学生的登记，首先使用openOrCreateDatabase()方法来创建数据库，然后通过SQLite进本增加、修改、更新等语句来实现。其实现的核心代码如下：
//首先创建出勤表，用来记录出勤记录
chuqin=dianmin.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE, null);
//通过UPDAT语句实现对出勤的次数的更新
String cmd1="UPDATE class"+classId+" SET wdNo=wdNo+1 WHERE rowId="+rmapList[i]+";";
chuqin.execSQL(cmd1);   //执行此命令实现对出勤记录内容的更新
//通过INSERT INTO语句将出勤学生姓名，出勤次数，未到次数插入到数据库
String cmd3="INSERT INTO class"+classId+" (rowId,stu,qjNo,wdNo) values("+rmapList[i]+",'"
+weidaoList.get(i)+"',0,1)";
chuqin.execSQL(cmd3);   //通过此命令实现对出勤记录内容的插入


4、 出勤记录管理模块

出勤记录管理模块主包括出勤记录的删除和出勤记录的导出。
4.5.1 删除记录
该模块主要是对SQLite数据库的操作，首先使用openOrCreateDatabase()方法来打开数据库，然后在使用DELETE语句执行对数据库的删除操作，其实现的核心代码如下：
//首先要打开数据，发现数据库的信息，然后数据库执行删除操作
banji=cqjl2.this.openOrCreateDatabase("chuqin.db", MODE_PRIVATE,null);
//通过delete语句实现对出勤表的删除
String cmd="delete from class"+classid+" WHERE rowId<>0;";
//执行删除操作
banji.execSQL(cmd);   


5、导出记录

导出出勤记录首先要在AndroidManifest.xml文件中注册对SDcard的操作权限，然后通过File进入到SDcard中，通过mkdir()方法创建文件夹来存储导入的记录。实现的核心代码如下：
//在导出出勤记录前，首先要获取SDcard的目录
File file=new File("//mnt//sdcard//出勤记录");
//通过mkdir()方法创建出勤记录文件夹
file.mkdir();
//构建只的Workbook对象，实现在出勤记录文件夹下创建Excel工作薄
WritableWorkbook book=Workbook.createWorkbook(new File("//mnt//sdcard//出勤记录//"+
classname".xls"));
//然后生成第一列的工作表，参数0代表第一列，方面实现对每列的插入
WritableSheet sheet1=book.createSheet("sheet1", 0);
在AndroidManifest.xml文件中的注册信息如下：
<!--在SDcard中创建与删除文件的权限-->
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/> 
<!-- 往SDcard中写入数据的权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>





