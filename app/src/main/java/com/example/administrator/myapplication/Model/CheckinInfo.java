package com.example.administrator.myapplication.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
/**
 * 名称     ：CheckinInfo
 * 主要内容 ：用户表
 * 创建人   ：wanzhuang
 * 创建时间 ：2018.9.16
 */

@Entity
public class CheckinInfo {
    //
    @Id(autoincrement = true)
    private Long id;
    //
    private long user_id;
    //
    private String ibeacn_sn;
    //
    private long ibeacn_id;
    //
    //status true 表示进入，false 表示离开
    private boolean status;
    //
    private long time;
    @Generated(hash = 2131975694)
    public CheckinInfo(Long id, long user_id, String ibeacn_sn, long ibeacn_id,
            boolean status, long time) {
        this.id = id;
        this.user_id = user_id;
        this.ibeacn_sn = ibeacn_sn;
        this.ibeacn_id = ibeacn_id;
        this.status = status;
        this.time = time;
    }
    @Generated(hash = 370604973)
    public CheckinInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public String getIbeacn_sn() {
        return this.ibeacn_sn;
    }
    public void setIbeacn_sn(String ibeacn_sn) {
        this.ibeacn_sn = ibeacn_sn;
    }
    public long getIbeacn_id() {
        return this.ibeacn_id;
    }
    public void setIbeacn_id(long ibeacn_id) {
        this.ibeacn_id = ibeacn_id;
    }
    public boolean getStatus() {
        return this.status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

}
