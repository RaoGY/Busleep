package com.busleep.utils;

/**
 * @date 2014-8-26
 * TODO Log工具类，设置开关，防止发布版本时log信息泄露
 */

public class LogUtils {
 
		public static boolean isDebug = false;

		public static void v(String tag, String msg) {
			if (isDebug)
				android.util.Log.v(tag, msg);
		}

		public static void v(String tag, String msg, Throwable t) {
			if (isDebug)
				android.util.Log.v(tag, msg, t);
		}

		public static void d(String tag, String msg) {
			if (isDebug)
				android.util.Log.d(tag, msg);
		}

		public static void d(String tag, String msg, Throwable t) {
			if (isDebug)
				android.util.Log.d(tag, msg, t);
		}

		public static void i(String tag, String msg) {
			if (isDebug)
				android.util.Log.i(tag, msg);
		}

		public static void i(String tag, String msg, Throwable t) {
			if (isDebug)
				android.util.Log.i(tag, msg, t);
		}

		public static void w(String tag, String msg) {
			if (isDebug)
				android.util.Log.w(tag, msg);
		}

		public static void w(String tag, String msg, Throwable t) {
			if (isDebug)
				android.util.Log.w(tag, msg, t);
		}

		public static void e(String tag, String msg) {
			if (isDebug)
				android.util.Log.e(tag, msg);
		}

		public static void e(String tag, String msg, Throwable t) {
			if (isDebug)
				android.util.Log.e(tag, msg, t);
		}

}
