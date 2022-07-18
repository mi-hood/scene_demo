package org.example.model;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EsResGlobal implements Serializable {
    private String id;
    private Float security_indicator;
    private Float service_indicator;
    private Float situation_indicator;
    private Long indicator_time;

    public EsResGlobal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getSecurity_indicator() {
        return security_indicator;
    }

    public void setSecurity_indicator(Float security_indicator) {
        this.security_indicator = security_indicator;
    }

    public Float getService_indicator() {
        return service_indicator;
    }

    public void setService_indicator(Float service_indicator) {
        this.service_indicator = service_indicator;
    }

    public Float getSituation_indicator() {
        return situation_indicator;
    }

    public void setSituation_indicator(Float situation_indicator) {
        this.situation_indicator = situation_indicator;
    }

    public Long getIndicator_time() {
        return indicator_time;
    }

    public void setIndicator_time(Long indicator_time) {
        this.indicator_time = indicator_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsResGlobal that = (EsResGlobal) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getSecurity_indicator(), that.getSecurity_indicator()) && Objects.equals(getService_indicator(), that.getService_indicator()) && Objects.equals(getSituation_indicator(), that.getSituation_indicator()) && Objects.equals(getIndicator_time(), that.getIndicator_time());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSecurity_indicator(), getService_indicator(), getSituation_indicator(), getIndicator_time());
    }

    @Override
    public String toString() {
        return "EsResGlobal{" +
                "id='" + id + '\'' +
                ", security_indicator=" + security_indicator +
                ", service_indicator=" + service_indicator +
                ", situation_indicator=" + situation_indicator +
                ", indicator_time=" + indicator_time +
                '}';
    }
}
