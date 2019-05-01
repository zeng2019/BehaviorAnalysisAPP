package com.example.administrator.timeRecording.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 名称     ：nodeInfo
 * 主要内容 ：节点信息表
 * 创建人   ：曾波
 * 创建时间 ：2019.04.21
 */

@Entity
public class nodeInfo {
    @Id(autoincrement = true)
    private Long id;
    private String nodeName; //节点名
    private String nodeID;   //节点ID号
    private String nodeSN; //节点SN号
    private String position; //节点位置
    private Double longitude; //节点经度
    private Double latitude; //节点纬度
    private String description; //节点描述
    @Generated(hash = 1468562288)
    public nodeInfo(Long id, String nodeName, String nodeID, String nodeSN,
            String position, Double longitude, Double latitude,
            String description) {
        this.id = id;
        this.nodeName = nodeName;
        this.nodeID = nodeID;
        this.nodeSN = nodeSN;
        this.position = position;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }
    @Generated(hash = 685774445)
    public nodeInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNodeName() {
        return this.nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getNodeID() {
        return this.nodeID;
    }
    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getNodeSN() {
        return this.nodeSN;
    }
    public void setNodeSN(String nodeSN) {
        this.nodeSN = nodeSN;
    }

}
