package com.busleep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * 数据库帮助类;
 * @author Render;
 *
 */
public class DBHelper extends SQLiteOpenHelper{

	private static final String DB_NAME="busleep";
	private static final int DB_VERSION=2;
	
	private SQLiteDatabase db;
	
	private static DBHelper mdbHelper;
	
	public static DBHelper getInstance(Context context){
		
		if(mdbHelper==null){
			mdbHelper=new DBHelper(context);
		}
		return mdbHelper;
	}
	
	private DBHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
	}
	
	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db=db;
		operateTable(db, "");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if(newVersion==oldVersion){
			return;
		}
		operateTable(db, "DROP TABLE IF EXISTS ");
		onCreate(db);
	}
	
	public void operateTable(SQLiteDatabase db,String actionString){
		
		Class<DatabaseColumn>[] columnsClasses=DatabaseColumn.getSubClasses();
		DatabaseColumn columns=null;
		for(int i=0;i<columnsClasses.length;i++){
			try {
				columns=columnsClasses[i].newInstance();
				if("".equals(actionString)||actionString==null){
					db.execSQL(columns.getTableCreateor());
				}else {
					db.execSQL(actionString+columns.getTableName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 插入数据;
	 * @param Table_Name
	 * @param values
	 * @return
	 */
	public long insert(String Table_Name,ContentValues values){
		
		if(db==null){
			db=getWritableDatabase();
		}
		return db.insert(Table_Name, null, values);
	}
	
	/**
	 * 根据Id来删除数据;
	 * @param Table_Name
	 * @param id
	 * @return
	 */
	public int delete(String Table_Name,int id){
		if(db==null){
			db=getWritableDatabase();
		}
		return db.delete(Table_Name, BaseColumns._ID+"=?", 
				new String[] {String.valueOf(id)});
	}
	
	/**
	 * 更新数据库中的数据;
	 * @param Table_Name
	 * @param values
	 * @param whereClause
	 * @param wherwArgs
	 * @return
	 */
	public int update(String Table_Name,ContentValues values,
			String whereClause,String[] whereArgs){
		if(db==null){
			db=getWritableDatabase();
		}
		return db.update(Table_Name, values, whereClause, whereArgs);
	}
	
	/**
	 * 查询数据库;
	 * @param Table_Name
	 * @param columns
	 * @param whereStr
	 * @param whereArgs
	 * @return
	 */
	public Cursor query(String Table_Name,String[] columns,
			String whereStr,String[] whereArgs){
		if(db==null){
			db=getReadableDatabase();
		}
		return db.query(Table_Name, columns, whereStr, whereArgs,null,null,null);
	}
	
	public Cursor rawQuery(String sql,String[] args){
		if(db==null){
			db=getReadableDatabase();
		}
		return db.rawQuery(sql, args);
	}
	
	public void ExecSQL(String sql){
		if(db==null){
			db=getWritableDatabase();
		}
		db.execSQL(sql);
	}
	
	public void closeDb(){
		if(db!=null){
			db.close();
			db=null;
		}
	}
}
