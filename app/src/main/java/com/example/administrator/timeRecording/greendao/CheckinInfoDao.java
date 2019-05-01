package com.example.administrator.timeRecording.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.administrator.timeRecording.Model.CheckinInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHECKIN_INFO".
*/
public class CheckinInfoDao extends AbstractDao<CheckinInfo, Long> {

    public static final String TABLENAME = "CHECKIN_INFO";

    /**
     * Properties of entity CheckinInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Email = new Property(1, String.class, "email", false, "EMAIL");
        public final static Property Ibeacn_sn = new Property(2, String.class, "ibeacn_sn", false, "IBEACN_SN");
        public final static Property Ibeacn_id = new Property(3, long.class, "ibeacn_id", false, "IBEACN_ID");
        public final static Property Status = new Property(4, boolean.class, "status", false, "STATUS");
        public final static Property Position = new Property(5, String.class, "position", false, "POSITION");
        public final static Property Time = new Property(6, java.util.Date.class, "time", false, "TIME");
    }


    public CheckinInfoDao(DaoConfig config) {
        super(config);
    }
    
    public CheckinInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHECKIN_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EMAIL\" TEXT," + // 1: email
                "\"IBEACN_SN\" TEXT," + // 2: ibeacn_sn
                "\"IBEACN_ID\" INTEGER NOT NULL ," + // 3: ibeacn_id
                "\"STATUS\" INTEGER NOT NULL ," + // 4: status
                "\"POSITION\" TEXT," + // 5: position
                "\"TIME\" INTEGER);"); // 6: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHECKIN_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CheckinInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String ibeacn_sn = entity.getIbeacn_sn();
        if (ibeacn_sn != null) {
            stmt.bindString(3, ibeacn_sn);
        }
        stmt.bindLong(4, entity.getIbeacn_id());
        stmt.bindLong(5, entity.getStatus() ? 1L: 0L);
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(6, position);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(7, time.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CheckinInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String ibeacn_sn = entity.getIbeacn_sn();
        if (ibeacn_sn != null) {
            stmt.bindString(3, ibeacn_sn);
        }
        stmt.bindLong(4, entity.getIbeacn_id());
        stmt.bindLong(5, entity.getStatus() ? 1L: 0L);
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(6, position);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(7, time.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CheckinInfo readEntity(Cursor cursor, int offset) {
        CheckinInfo entity = new CheckinInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ibeacn_sn
            cursor.getLong(offset + 3), // ibeacn_id
            cursor.getShort(offset + 4) != 0, // status
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // position
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CheckinInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIbeacn_sn(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIbeacn_id(cursor.getLong(offset + 3));
        entity.setStatus(cursor.getShort(offset + 4) != 0);
        entity.setPosition(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CheckinInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CheckinInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CheckinInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
