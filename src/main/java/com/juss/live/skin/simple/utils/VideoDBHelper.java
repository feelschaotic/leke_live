package com.juss.live.skin.simple.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lecloud.dispatcher.play.entity.Config;
import com.lecloud.leutils.LeLog;

/**
 * 数据库工具类
 */
public class VideoDBHelper {

    private static final String TAG = "DBHelper";
    /**
     * 续播表字段
     */
    public interface UserSchema {
        String TABLE_NAME = "sdkPlayer_table";// 表名字
        String UU = "m_uu";// uu
        String VU = "m_vu";// vu
        String POS = "m_position";// 播放时间
        String DEF = "m_defination";// 播放码率
    }


    private static VideoDBHelper dbHelper;

    // 续播时间
    private static LastTimeDBConnection helper;
    /**
     * 单例
     * 
     * @return
     */
    public static VideoDBHelper getInstance(Context ctx) {
        if (dbHelper == null) {
            dbHelper = new VideoDBHelper(ctx);
        }
        return dbHelper;
    }

    private VideoDBHelper(Context ctx) {
        helper = new LastTimeDBConnection(ctx);
    }


    /**
     * 加入数据
     * 
     * @param values
     */
    public void add(SQLiteOpenHelper helper, String tableName, ContentValues values) {
    	if(helper!=null){
    		SQLiteDatabase db = null;
    		try {
    			db = helper.getWritableDatabase();
    			db.insert(tableName, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (db != null) {
					db.close();
				}
			}
    	}
    }


    /**
     * 更新数据
     * 
     * @param values
     * @param where
     * @param whereArgs
     */
    public void update(SQLiteOpenHelper helper, String tableName, ContentValues values, String where, String[] whereArgs) {
    	if(helper!=null){
    		SQLiteDatabase db = null;
    		try {
    			db = helper.getWritableDatabase();
    			db.update(tableName, values, where, whereArgs);
			} catch (Exception e) {
			}finally{
				if(db != null){
					db.close();
				}
			}
    	}
    }

    /**
     * 删除数据
     * 
     * @param where
     * @param whereArgs
     */
    public void delete(SQLiteOpenHelper helper, String tableName, String where, String[] whereArgs) {
    	if(helper!=null){
    		SQLiteDatabase db = null;
    		try{
	    		db = helper.getWritableDatabase();
	    		db.delete(tableName, where, whereArgs);
    		}catch(Exception e){}finally{
    			if (db != null) {
    				db.close();
				}
    		}
    	}
    }


    /**
     * 续播表
     */
    public static class LastTimeDBConnection extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "LetvSDKPlayer.db";
        private static final int DATABASE_VERSION = 1;

        private LastTimeDBConnection(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * 创建数据表
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sb = new StringBuilder();

            sb.append("CREATE TABLE " + UserSchema.TABLE_NAME);
            sb.append(" (");
            sb.append("m_uu varchar(50),");
            sb.append("m_vu varchar(50),");
            sb.append("m_position integer,");
            sb.append("m_defination varchar(50),");
            sb.append("primary key (m_uu,m_vu)");
            sb.append(" )");
            String create_sql = sb.toString();
            db.execSQL(create_sql);
        }

        /**
         * 更新数据库
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    
    /**
     * 记录和保存点播数据到数据库
     * 
     * @param uu
     * @param vu
     * @param position
     * @param defination
     */
    public void setVideoConfig(String uu, String vu, int position, String defination) {
        if (helper != null) {
            ContentValues values = new ContentValues();
            values.put(UserSchema.UU, uu);
            values.put(UserSchema.VU, vu);
            values.put(UserSchema.POS, position);
            values.put(UserSchema.DEF, defination);
            LeLog.d(TAG, "[DBHelper][setVideoConfig]保存的数据有:uu="+uu+";vu="+vu+";position="+position+";defination="+defination);
            if(helper!=null){
        		SQLiteDatabase db = null;
        		try {
        			db = helper.getWritableDatabase();
        			long a; 
        			String wheres = UserSchema.UU + "=? and " + UserSchema.VU + "=?";
        			a = db.update(UserSchema.TABLE_NAME, values, wheres, new String[]{(String) values.get(UserSchema.UU),(String) values.get(UserSchema.VU )});
        			if (a <= 0) {
        				db.insert(UserSchema.TABLE_NAME, null, values);
        			}
    			} catch (Exception e) {
    				LeLog.ePrint(TAG,"[setVideoConfig]"+e.getMessage());
    			}finally{
    				if (db != null) {
    					db.close();
    				}
    			}
        	}
        }
    }
    
	public Config getVideoConfig(String uu, String vu) {
		Config configTemp = null;
		/**
		 * 查询是否存在
		 */
		String wheres = UserSchema.UU + "=? and " + UserSchema.VU + "=?";

		Cursor c = null;
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			c = db.query(UserSchema.TABLE_NAME, null, wheres, new String[] {
					uu, vu }, null, null, null, null);
			if (c != null && c.getCount() > 0) {
				configTemp = new Config();
				c.moveToFirst();
				configTemp.setSeek(c.getInt(c.getColumnIndex(UserSchema.POS)));
				configTemp.setDefination(c.getString(c.getColumnIndex(UserSchema.DEF)));
			}
		} catch (Exception e) {
			LeLog.ePrint(TAG,"[getVideoConfig]"+e.getMessage());
		} finally {
			if (c != null)
				c.close();
			if (db != null)
				db.close();
		}

		return configTemp;
	}

}
