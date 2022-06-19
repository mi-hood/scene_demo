package org.example.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class LogData {
    private Long event_time;
    private String client_addr;
    private Integer log_level;
    private String log_content;
    private String related_account;
    private Integer source_id;

    public LogData() {
    }

    public LogData(Long event_time, String client_addr, Integer log_level, String log_content, String related_account, Integer source_id) {
        this.event_time = event_time;
        this.client_addr = client_addr;
        this.log_level = log_level;
        this.log_content = log_content;
        this.related_account = related_account;
        this.source_id = source_id;
    }

    public Long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Long event_time) {
        this.event_time = event_time;
    }

    public String getClient_addr() {
        return client_addr;
    }

    public void setClient_addr(String client_addr) {
        this.client_addr = client_addr;
    }

    public Integer getLog_level() {
        return log_level;
    }

    public void setLog_level(Integer log_level) {
        this.log_level = log_level;
    }

    public String getLog_content() {
        return log_content;
    }

    public void setLog_content(String log_content) {
        this.log_content = log_content;
    }

    public String getRelated_account() {
        return related_account;
    }

    public void setRelated_account(String related_account) {
        this.related_account = related_account;
    }

    public Integer getSource_id() {
        return source_id;
    }

    public void setSource_id(Integer source_id) {
        this.source_id = source_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogData logData = (LogData) o;
        return Objects.equals(getEvent_time(), logData.getEvent_time()) && Objects.equals(getClient_addr(), logData.getClient_addr()) && Objects.equals(getLog_level(), logData.getLog_level()) && Objects.equals(getLog_content(), logData.getLog_content()) && Objects.equals(getRelated_account(), logData.getRelated_account()) && Objects.equals(getSource_id(), logData.getSource_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvent_time(), getClient_addr(), getLog_level(), getLog_content(), getRelated_account(), getSource_id());
    }

    @Override
    public String toString() {
        return "LogData{" +
                "event_time=" + event_time +
                ", client_addr='" + client_addr + '\'' +
                ", log_level=" + log_level +
                ", log_content='" + log_content + '\'' +
                ", related_account='" + related_account + '\'' +
                ", source_id=" + source_id +
                '}';
    }

    public static void main(String[] args) {
        AtomicReference<List<String>> a=new AtomicReference<List<String>>();
        System.out.println(a.get());
    }
}
