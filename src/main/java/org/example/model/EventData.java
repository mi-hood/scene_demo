package org.example.model;

import java.util.Objects;

public class EventData {
    private Long event_time;
    private Long event_weight;
    private Integer serverity;
    private String classtype;
    private String dst_ip;
    private String src_ip;
    private Integer dst_port;
    private String source_id;

    public EventData() {
    }

    public EventData(Long event_time, Long event_weight, Integer serverity, String classtype, String dst_ip, String src_ip, Integer dst_port, String source_id) {
        this.event_time = event_time;
        this.event_weight = event_weight;
        this.serverity = serverity;
        this.classtype = classtype;
        this.dst_ip = dst_ip;
        this.src_ip = src_ip;
        this.dst_port = dst_port;
        this.source_id = source_id;
    }

    public Long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Long event_time) {
        this.event_time = event_time;
    }

    public Long getEvent_weight() {
        return event_weight;
    }

    public void setEvent_weight(Long event_weight) {
        this.event_weight = event_weight;
    }

    public Integer getServerity() {
        return serverity;
    }

    public void setServerity(Integer serverity) {
        this.serverity = serverity;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getDst_ip() {
        return dst_ip;
    }

    public void setDst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public Integer getDst_port() {
        return dst_port;
    }

    public void setDst_port(Integer dst_port) {
        this.dst_port = dst_port;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventData eventData = (EventData) o;
        return Objects.equals(getEvent_time(), eventData.getEvent_time()) && Objects.equals(getEvent_weight(), eventData.getEvent_weight()) && Objects.equals(getServerity(), eventData.getServerity()) && Objects.equals(getClasstype(), eventData.getClasstype()) && Objects.equals(getDst_ip(), eventData.getDst_ip()) && Objects.equals(getSrc_ip(), eventData.getSrc_ip()) && Objects.equals(getDst_port(), eventData.getDst_port()) && Objects.equals(getSource_id(), eventData.getSource_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvent_time(), getEvent_weight(), getServerity(), getClasstype(), getDst_ip(), getSrc_ip(), getDst_port(), getSource_id());
    }

    @Override
    public String toString() {
        return "EventData{" +
                "event_time=" + event_time +
                ", event_weight=" + event_weight +
                ", serverity=" + serverity +
                ", classtype='" + classtype + '\'' +
                ", dst_ip='" + dst_ip + '\'' +
                ", src_ip='" + src_ip + '\'' +
                ", dst_port=" + dst_port +
                ", source_id='" + source_id + '\'' +
                '}';
    }
}
