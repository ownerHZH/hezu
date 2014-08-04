package com.room.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

/**
 * 异常保存
 * 
 * @author king
 * 
 */
public class RoomException implements UncaughtExceptionHandler {
	private static RoomException roomException = new RoomException();

	private RoomException() {
	};

	public static RoomException getInstance() {
		return roomException;
	}

	// 以键值对形式存储信息
	private Map<String, String> map = new HashMap<String, String>();
	private Context mcontext;
	private Thread.UncaughtExceptionHandler defaultHandler;

	public void init(Context context) {
		this.mcontext = context;
		// 获取系统异常处理机制
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		if (!handlerException(e) && defaultHandler != null) {
			defaultHandler.uncaughtException(thread, e);
		} else {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * 收集保存错误信息
	 * 
	 * @param e
	 * @return
	 */
	private boolean handlerException(Throwable e) {
		if (e == null) {
			return false;
		} else {
			collectDeviceMessage(mcontext);
			final File file = saveExceptionInfoFile(e);
			/*new Thread() {
				public void run() {
					Looper.prepare();
					sendBug(file);
					Looper.loop();
				};
			}.start();*/
			return true;
		}
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	private void collectDeviceMessage(Context context) {
		// 获得包管理器
		PackageManager packageManager = context.getPackageManager();
		try {
			// 得到应用信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				String versionName = packageInfo.versionName == null ? "null"
						: packageInfo.versionName;
				String versionCode = packageInfo.versionCode + "";
				map.put("versionName", versionName);
				map.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 反射机制
		Field[] declaredFields = Build.class.getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get("").toString());
				System.out.println(field.getName() + "---:---"
						+ field.get("").toString() + "------");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private File saveExceptionInfoFile(Throwable e) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");
		}

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = stringWriter.toString();
		sb.append(result);
		// 保存文件
		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String date = format.format(new Date());
		String filename = "crash-" + date + "-" + time + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/roomShare/");
			if (!file.exists()) {
				file.mkdirs();
			}
			File filecash = new File(file, "crash/");
			if (!filecash.exists()) {
				filecash.mkdirs();
			} else {
				delteDirectory(filecash);
				filecash.delete();
				filecash.mkdirs();
			}
			File fileName = new File(filecash, filename);
			try {
				FileOutputStream out = new FileOutputStream(fileName);
				out.write(sb.toString().getBytes());
				out.flush();
				out.close();
				return fileName;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/*private void sendBug(File file) {
		if (file != null) {
			FileInputStream input;
			try {
				input = new FileInputStream(file);
				System.out.println(file.getPath()
						+ "====================file路径");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] buffer = new byte[1024];
				try {
					while ((len = input.read(buffer)) != -1) {
						baos.write(buffer, 0, len);
					}
					input.close();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				byte[] result = baos.toByteArray();
				String temp = new String(result);
				System.out.println("++++++++++++++++" + temp.toString() + ":"
						+ "--------------111111111111111-----------------");
				AjaxParams params = new AjaxParams();
				params.put("bug", new ByteArrayInputStream(result));
				FinalHttp http = new FinalHttp();
				http.post(ConstatnValue.HOST + ConstatnValue.SAVEBUG, params,
						new AjaxCallBack<Object>() {
						});
				System.out.println("---------------发送成功----------------------");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}*/

	/**
	 * 删除文件
	 */
	private void delteDirectory(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (File fileitem : files) {
						fileitem.delete();
					}
				}
			}
		}
	}
}
