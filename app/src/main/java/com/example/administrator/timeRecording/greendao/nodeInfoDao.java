package com.example.administrator.timeRecording.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.administrator.timeRecording.Model.nodeInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NODE_INFO".
*/
public class nodeInfoDao extends AbstractDao<nodeInfo, Long> {

    public static final String TABLENAME = "NODE_INFO";

    /**
     * Properties of entity nodeInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NodeName = new Property(1, String.class, "nodeName", false, "NODE_NAME");
        public final static Property NodeID = new Property(2, String.class, "nodeID", false, "NODE_ID");
        public final static Property NodeSN = new Property(3, String.class, "nodeSN", false, "NODE_SN");
        public final static Property Position = new Property(4, String.class, "position", false, "POSITION");
        public final static Property Longitude = new Property(5, Double.class, "longitude", false, "LONGITUDE");
        public final static Property Latitude = new Property(6, Double.class, "latitude", false, "LATITUDE");
        public final static Property Description = new Property(7, String.class, "description", false, "DESCRIPTION");
    }


    public nodeInfoDao(DaoConfig config) {
        super(config);
    }
    
    public nodeInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NODE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NODE_NAME\" TEXT," + // 1: nodeName
                "\"NODE_ID\" TEXT," + // 2: nodeID
                "\"NODE_SN\" TEXT," + // 3: nodeSN
                "\"POSITION\" TEXT," + // 4: position
                "\"LONGITUDE\" REAL," + // 5: longitude
                "\"LATITUDE\" REAL," + // 6: latitude
                "\"DESCRIPTION\" TEXT);"); // 7: description
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NODE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, nodeInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nodeName = entity.getNodeName();
        if (nodeName != null) {
            stmt.bindString(2, nodeName);
        }
 
        String nodeID = entity.getNodeID();
        if (nodeID != null) {
            stmt.bindString(3, nodeID);
        }
 
        String nodeSN = entity.getNodeSN();
        if (nodeSN != null) {
            stmt.bindString(4, nodeSN);
        }
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(5, position);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(6, longitude);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(7, latitude);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(8, description);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, nodeInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nodeName = entity.getNodeName();
        if (nodeName != null) {
            stmt.bindString(2, nodeName);
        }
 
        String nodeID = entity.getNodeID();
        if (nodeID != null) {
            stmt.bindString(3, nodeID);
        }
 
        String nodeSN = entity.getNodeSN();
        if (nodeSN != null) {
            stmt.bindString(4, nodeSN);
        }
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(5, position);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(6, longitude);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(7, latitude);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(8, description);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public nodeInfo readEntity(Cursor cursor, int offset) {
        nodeInfo entity = new nodeInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nodeName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nodeID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nodeSN
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // position
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // longitude
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // latitude
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // description
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, nodeInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNodeName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNodeID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNodeSN(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPosition(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLongitude(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setLatitude(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setDescription(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(nodeInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(nodeInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(nodeInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
