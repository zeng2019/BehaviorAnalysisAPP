package com.example.administrator.timeRecording.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

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
    //private long user_id;
    private String email;
    //
    private String ibeacn_sn;
    //
    private long ibeacn_id;
    //
    //status true 表示进入，false 表示离开
    private boolean status;
    //
    private String position;
    private Date time;
    @Generated(hash = 1895782417)
    public CheckinInfo(Long id, String email, String ibeacn_sn, long ibeacn_id,
            boolean status, String position, Date time) {
        this.id = id;
        this.email = email;
        this.ibeacn_sn = ibeacn_sn;
        this.ibeacn_id = ibeacn_id;
        this.status = status;
        this.position = position;
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
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }


  }
