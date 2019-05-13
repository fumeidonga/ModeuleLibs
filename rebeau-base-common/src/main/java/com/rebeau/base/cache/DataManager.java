/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.rebeau.base.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.rebeau.base.config.Constants;


/**数据工具类
 * @author Lemon
 */
public class DataManager {
	private final String TAG = "DataManager";

	private Context context;

	public DataManager() {
	}

	public void init(Context context) {
		this.context = context;
	}

	private static DataManager instance;
	public static DataManager getInstance() {
		if (instance == null) {
			synchronized (DataManager.class) {
				if (instance == null) {
					instance = new DataManager();
				}
			}
		}
		return instance;
	}

	/**
	 * put string preferences
	 *
	 * @param key The name of the preference to modify
	 * @param value The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public boolean putString(String key, String value) {
		SharedPreferences settings = context.getSharedPreferences("cn_pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 *
	 * @param key The name of the preference to retrieve
	 * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
	 *         name that is not a string
	 * @see #getString(String, String)
	 */
	public String getString(String key) {
		return getString(key, null);
	}
	/**
	 * get string preferences
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 *         this name that is not a string
	 */
	public String getString(String key, String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences("cn_pref", Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	public int getInt(String key) {
		SharedPreferences settings = context.getSharedPreferences("cn_pref", Context.MODE_PRIVATE);
		return settings.getInt(key, 0);
	}

	public boolean putLong(String key, long value) {
		SharedPreferences settings = context.getSharedPreferences("cn_pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public long getLong(String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences("cn_pref", Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * 进入后台的时间差来比较
	 * @return
	 */
	public boolean isSplashTimeOut(){
        long showTime = getLong(Constants.SPLASH_ACTIVITY_AD_TIME, System.currentTimeMillis());
        long intervalTime = getLong(Constants.SPLASH_ACTIVITY_AD_INVATE_TIME, 10L);
        if(showTime > 0) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = Math.abs(currentTime - showTime);
            if(timeDifference < intervalTime * 1000 * 60) {
                return true;
            }
        }
		return false;
	}

}
