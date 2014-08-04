package com.room.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class RoomApplication extends Application{
	private static List<Activity> list_activitys;
	private static RoomApplication application;
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		RoomException instance = RoomException.getInstance();
		instance.init(this);
	}

	public static RoomApplication getInstance() {
		return application;
	}
		
	/**
	 * add Activity 只添加了MainActivity
	 */
	public void addActivity(Activity activity) {
		if (list_activitys == null) {
			list_activitys = new ArrayList<Activity>();
		}
		list_activitys.add(activity);
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0; i < list_activitys.size(); i++) {
			if (null != list_activitys.get(i)) {
				list_activitys.get(i).finish();
			}
		}
		list_activitys.clear();
	}
}
