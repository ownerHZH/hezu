package com.example.roommates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.room.sqlutils.Bill;
import com.room.sqlutils.DetailItem;
import com.room.sqlutils.Person;
import com.room.sqlutils.SQLManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settlement extends Activity {

	private ListView detail;
	List<DetailItem> items=new ArrayList<DetailItem>();
	private Context context;
	private ListViewAdapter adapter;
	private Spinner month_choice;
	private Button back;
	SQLManager sqlManager;
	Double totalMoney=0.0;
	Double perMoney=0.0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settlement);	
		context=Settlement.this;
		sqlManager=new SQLManager(context);
		detail=(ListView) findViewById(R.id.listViewDetail);
		month_choice=(Spinner) findViewById(R.id.spinnerMonth);
		back=(Button) findViewById(R.id.back);
		
		ArrayAdapter<CharSequence> adapterEnvironment = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
		adapterEnvironment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		month_choice.setAdapter(adapterEnvironment);
				
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		 
		int month = c.get(Calendar.MONTH)+1;
		//Log.e("月份----", month+"");
		month_choice.setSelection(month-1);
		adapter=new ListViewAdapter();
		detail.setAdapter(adapter);
		//refreshDataByMonth(month);//根据月份更新数据	
		
		month_choice.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				refreshDataByMonth(position+1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private void refreshDataByMonth(int month) {
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		 
		int year = c.get(Calendar.YEAR); 
		int z=month;
		totalMoney=0.0;
		perMoney=0.0;
		String searchDate="";
		if((z+"").length()==1)
		{
			searchDate=year+"-"+"0"+z;
		}else
		{
			searchDate=year+"-"+z;
		}
		items.clear();
		//获取所有的人员
		List<Person> persons=sqlManager.getPersons("");
		//所有的数据项
		List<Bill> totalList=sqlManager.getBillsByDate(searchDate);
		for(int i=0;i<totalList.size();i++)
		{
			totalMoney+=Double.valueOf(totalList.get(i).getMoney());
		}
		perMoney=totalMoney/persons.size();//平均每个人的钱数
		
		DetailItem di=new DetailItem();
		di.setName("总共花费的钱");
		di.setMoney(totalMoney+"");
		items.add(di);
		
		di=new DetailItem();
		di.setName("平均每人的钱");
		di.setMoney(perMoney+"");
		items.add(di);
		
		for(int j=0;j<persons.size();j++)
		{
			Double m=0.0;
			for(int i=0;i<totalList.size();i++)
			{
				if(persons.get(j).getName().equals(totalList.get(i).getName()))
				{
					m+=Double.valueOf(totalList.get(i).getMoney());
				}
			}
			//persons.get(j).setMoney(m);//每个人已经支付的钱数
			//persons.get(j).setMoney(perMoney-m);//每个人应该支付的钱 整数代表需要给出的 负数代表进账的
			Double sj=perMoney-m;
			DetailItem ddi=new DetailItem();
			if(sj<0)
			{
				ddi.setName(persons.get(j).getName()+"  需进账");
				ddi.setMoney((0-sj)+"");
			}else
			{
				ddi.setName(persons.get(j).getName()+"  需出钱");
				ddi.setMoney(sj+"");
			}
			
			items.add(ddi);
		}
		adapter.notifyDataSetChanged();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settlement, menu);
		return true;
	}*/
	
	private class ListViewAdapter extends BaseAdapter
	{
		private LayoutInflater inflater=null;
		private ListViewAdapter()
		{
			inflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public DetailItem getItem(int arg0) {
			// TODO Auto-generated method stub
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.detail_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.textViewName);
				holder.money = (TextView) convertView.findViewById(R.id.textViewMoney);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(position==0||position==1)
			{
				holder.name.setText(items.get(position).getName());
				holder.money.setText(items.get(position).getMoney());	
			}else
			{
				holder.name.setText("       ┗    "+items.get(position).getName());
				holder.money.setText(items.get(position).getMoney());
			}
			   
			return convertView;
		}				
	}
	private static class ViewHolder{
		TextView name;
		TextView money;
	}
}
