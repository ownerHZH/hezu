package com.example.roommates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.room.sqlutils.Bill;
import com.room.sqlutils.Data;
import com.room.sqlutils.SQLManager;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ShowDetail extends Activity {

	private ExpandableListView expandlistView;
	private Context context;
	private List<Data> datas=new ArrayList<Data>();
	private SQLManager sqlManager;
	ExpandableListViewAdapter adapter;
	Button back,add;
	AlertDialog alertDialog=null;
	String searchName=null;
	public static final String[] months=new String[]{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		searchName=getIntent().getStringExtra("searchName");
		
		setContentView(R.layout.activity_show_detail);
				
		context=ShowDetail.this;
		back=(Button) findViewById(R.id.back);
		add=(Button) findViewById(R.id.add);
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		sqlManager=new SQLManager(context);
		back.setOnClickListener(l);
		add.setOnClickListener(l);
		
		/*sqlManager.addBill("hu", "买菜", 30.0, new Date());
		sqlManager.addBill("hu", "买菜1", 30.0, new Date());
		sqlManager.addBill("hu", "买菜2", 30.0, new Date());*/					
		getDatasFromDataBase();
		adapter=new ExpandableListViewAdapter(context, datas);
		expandlistView.setAdapter(adapter);
		expandlistView.setGroupIndicator(null); // 去掉默认带的箭头

		// 遍历所有group,将所有项设置成默认展开
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);
		}
		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				return true;
			}			
		});

		expandlistView.setOnChildClickListener(onChildClickListener);
	}
	
	private OnChildClickListener onChildClickListener=new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
				int childPosition, long id) {
			 final int g=groupPosition;
			 final int c=childPosition;
			Dialog alertDialog = new AlertDialog.Builder(context).
				    setTitle("确定删除？").
				    setMessage("您确定删除该条信息吗？").
				    setIcon(R.drawable.ic_launcher).
				    setPositiveButton("删除", new DialogInterface.OnClickListener() {
				     
				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				    	   
							Bill bill=datas.get(g).getBills().remove(c);		
							adapter.notifyDataSetChanged();
							sqlManager.removeBill(bill.getId());
				     }
				    }).
				    setNegativeButton("放弃", new DialogInterface.OnClickListener() {
				     
				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				      // TODO Auto-generated method stub
				     }
				    }).
				    /*setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
				     
				     @Override
				     public void onClick(DialogInterface dialog, int which) {
				      // TODO Auto-generated method stub
				     }
				    }).*/
				    create();
				  alertDialog.show();
									
			return false;
		}
	};

	//从数据库查询所有的数据
	private void getDatasFromDataBase() {
		for(int i=0;i<months.length;i++)
		{
			Data data=new Data();
			data.setTitle(months[i]);
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		 
			int year = c.get(Calendar.YEAR); 
			int z=i+1;
			String searchDate="";
			if((z+"").length()==1)
			{
				searchDate=year+"-"+"0"+z;
			}else
			{
				searchDate=year+"-"+z;
			}
			
			List<Bill> bills=sqlManager.getBills(searchName,searchDate);
			data.setBills(bills);
			
			datas.add(data);
		}
	}
	
	public void alertShow() {
		LayoutInflater inflater=LayoutInflater.from(context);
		View v=inflater.inflate(R.layout.add_iten, null);
		final EditText item=(EditText) v.findViewById(R.id.editTextItem);
		final EditText money=(EditText) v.findViewById(R.id.editTextMoney);
		Button in=(Button) v.findViewById(R.id.buttonIn);
		Button out=(Button) v.findViewById(R.id.buttonOut);
		
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("请输入详细信息");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(v);		
		alertDialog=builder.show();
		
		
		in.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String item1=item.getText().toString().trim();
				Double money1=Double.valueOf(money.getText().toString());
				long result=0;
				if(!item1.isEmpty()&&money1!=0)
				{
					Date d=new Date();
					result=sqlManager.addBill(searchName, item1, money1, d);
					if(result!=0)
					{
						/*Data data=new Data();
						Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		 
						int m = c.get(Calendar.MONTH);
						data.setTitle(months[m-1]);
						List<Bill> bills=new ArrayList<Bill>();
						Bill arg0=new Bill();
						arg0.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
						arg0.setItem(item1);
						arg0.setMoney(money1+"");
						arg0.setName(searchName);						
						bills.add(arg0);
						data.setBills(bills);
						datas.add(data);*/
						datas.clear();
						getDatasFromDataBase();
						adapter.notifyDataSetChanged();
						
						if(alertDialog!=null)
						{
							alertDialog.cancel();
							alertDialog=null;
						}
					}
				}else
				{
					Toast.makeText(getApplicationContext(), "请详细填写数据...", Toast.LENGTH_SHORT).show();
				}								
			}
		});
		        	
		out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(alertDialog!=null)
				{
					alertDialog.cancel();
					alertDialog=null;
				}
			}
		});
	}
	
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.back:
				finish();
				break;
            case R.id.add:
            	alertShow();
                
				break;

			default:
				break;
			}
		}		
	};
	
	class ExpandableListViewAdapter extends BaseExpandableListAdapter
	{
		private LayoutInflater inflater = null;
		private List<Data> datas;
		private Context context;
			
		public ExpandableListViewAdapter(Context context, List<Data> datas) {
			this.datas = datas;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if(datas.get(groupPosition).getBills() == null){
				return 0;
			}else{
				return datas.get(groupPosition).getBills().size();
			}
		}

		@Override
		public Data getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return datas.get(groupPosition);
		}

		@Override
		public Bill getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return datas.get(groupPosition).getBills().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			
			GroupViewHolder holder = new GroupViewHolder();
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.one_status_item, null);
			}
			holder.groupName = (TextView) convertView.findViewById(R.id.one_status_name);
			//holder.group_tiao = (TextView) convertView.findViewById(R.id.group_tiao);
			
			holder.groupName.setText(datas.get(groupPosition).getTitle());
			
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildViewHolder viewHolder = null;
			Bill entity = getChild(groupPosition, childPosition);
			if (convertView != null) {
				viewHolder = (ChildViewHolder) convertView.getTag();
			} else {
				viewHolder = new ChildViewHolder();
				convertView = inflater.inflate(R.layout.two_status_item, null);
				viewHolder.childName = (TextView) convertView.findViewById(R.id.two_status_name);
				viewHolder.twoStatusTime = (TextView) convertView.findViewById(R.id.two_complete_time);
				//viewHolder.tiao = (TextView) convertView.findViewById(R.id.tiao);																
			}
			viewHolder.childName.setText(entity.getItem());
			viewHolder.twoStatusTime.setText(entity.getMoney());			
			
			convertView.setTag(viewHolder);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
		private class GroupViewHolder {
			TextView groupName;
			//public TextView group_tiao;
		}
		
		private class ChildViewHolder {
			public TextView childName;
			public TextView twoStatusTime;
			//public TextView tiao;
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_detail, menu);
		return false;
	}

}
