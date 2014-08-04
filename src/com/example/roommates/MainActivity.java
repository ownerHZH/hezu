package com.example.roommates;


import java.util.List;

import com.example.roommates.CircleLayout.OnItemClickListener;
import com.example.roommates.CircleLayout.OnItemLongClickListener;
import com.example.roommates.CircleLayout.OnItemSelectedListener;
import com.room.sqlutils.Person;
import com.room.sqlutils.SQLManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener, OnItemClickListener ,OnItemLongClickListener{

	TextView selectedTextView;
	CircleLayout circleMenu;
	Context context=MainActivity.this;
	SQLManager sqlManager;
	static int personTotal=0;//总的成员数
	private Button detail1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sqlManager=new SQLManager(context);
		detail1=(Button) findViewById(R.id.detail1);
		
		circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);
		circleMenu.setOnItemLongClickListener(this);
		//初始化一个添加菜单选择项
		createMenuItem("添加","0",Color.GRAY);
		List<Person> persons=sqlManager.getPersons("");
		if(persons!=null&&persons.size()>0)
		{
			personTotal=persons.size();
			for(Person p:persons)
			{
				int color=Integer.parseInt(p.getId());
				color=color%8;
				createMenuItem(p.getName(),p.getId(),getResources().getColor(R.color.color0+color));
			}		
		}

		selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
		
		detail1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(MainActivity.this ,Settlement.class);
				startActivity(i);
			}
		});
	}

	//生成一个菜单项
	private void createMenuItem(String name,String id,int color) {
		CircleImageView circleImageView=new CircleImageView(MainActivity.this);
		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		circleImageView.setLayoutParams(params);
		circleImageView.setName(name);
		circleImageView.setId(Integer.parseInt(id));
		
		circleImageView.setImageBitmap(createBitmap(name,color));
		circleMenu.addView(circleImageView);
	}

	//根据名字生成一张图片
	private Bitmap createBitmap(String name,int color) {
		int W=150;//图片宽高
		int H=150;
		int textSize=30;//文字大小
		int px=(W-(name.length()*textSize))/2;//文字起始点x
		int py=(H-textSize)/2+textSize;//文字起始点y
		Bitmap bitmap = Bitmap.createBitmap(W,H, Config.ARGB_8888);//创建一个宽度和高度都是400、32位ARGB图
		Canvas canvas =new Canvas(bitmap);//初始化画布绘制的图像到icon上
		//canvas.drawColor(Color.GRAY);//图层的背景色
		
		RectF rectf = new RectF(0F, 0F, W, H);
        Paint p=new Paint(1);
        p.setColor(color);
		canvas.drawOval(rectf, p);
		
		Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);//创建画笔
		paint.setTextSize(textSize);//设置文字的大小
		paint.setTypeface(Typeface.DEFAULT_BOLD);//文字的样式(加粗)
		paint.setColor(Color.BLACK);//文字的颜色
		canvas.drawText(name,px, py, paint);//将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
		canvas.save(canvas.ALL_SAVE_FLAG);//保存所有图层
		canvas.restore();
		return bitmap;
	}
	//生成圆角图片
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);					
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());		
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 200;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);		
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));				
	
			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			
			canvas.drawBitmap(bitmap, src, rect, paint);	
			return output;
		} catch (Exception e) {			
			return bitmap;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(View view, int position, long id, String name) {		
		//Toast.makeText(getApplicationContext(), " " + name, Toast.LENGTH_SHORT).show();
		if(name.equals("添加"))
		{
			if(personTotal>8)
			{
				Toast.makeText(getApplicationContext(), "人数已达上限，不能再添加", Toast.LENGTH_SHORT).show();
			}else
			{
				final EditText editText=new EditText(context);
				new AlertDialog.Builder(context)
				.setTitle("请输入成员名称")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(editText)
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String n=editText.getText().toString().trim();
						if(!n.isEmpty())
						{
							long l=sqlManager.addPerson(n);
							createMenuItem(n,l+"",getResources().getColor(R.color.color0+(int)(l%8)));						
							personTotal++;
						}else{
							Toast.makeText(getApplicationContext(), "名称不能为空！", Toast.LENGTH_SHORT).show();	
						}			
					}
				})
				.setNegativeButton("取消", null)
				.show();
			}		
		}else
		{
			//跳转
			Intent i=new Intent(context, ShowDetail.class);
			i.putExtra("searchName", name);
			startActivity(i);
		}
		
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		selectedTextView.setText(name);	
	}

	@Override
	public void onItemLongClick(View view, int position, long id, String name) {
		if(!name.equals("添加"))
		{
			circleMenu.removeView((CircleImageView) (view));
			sqlManager.removePerson(id+"");
			Toast.makeText(getApplicationContext(), "删除  " + name, Toast.LENGTH_SHORT).show();
		}
		
	}

}
