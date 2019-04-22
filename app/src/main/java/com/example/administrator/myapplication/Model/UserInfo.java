package com.example.administrator.myapplication.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * 名称     ：userInfo
 * 主要内容 ：用户表
 * 创建人   ：wanzhuang
 * 创建时间 ：2018.9.16
 */
@Entity
public class UserInfo{

        //
        @Id(autoincrement = true)
        private Long id;
        //
        private String username;
        //
        private String password;
        @Index(unique = true)
        private String email;
        //
        private long telnumber;
        @Generated(hash = 1526098230)
        public UserInfo(Long id, String username, String password, String email,
                long telnumber) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.telnumber = telnumber;
        }
        @Generated(hash = 1279772520)
        public UserInfo() {
        }
        public Long getId() {
            return this.id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getUsername() {
            return this.username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getEmail() {
            return this.email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public long getTelnumber() {
            return this.telnumber;
        }
        public void setTelnumber(long telnumber) {
            this.telnumber = telnumber;
        }
}