package org.example.model;

public class EsResGlobal {
    private String id;
    private Float security_indicator;
    private Float service_indicator;
    private Float situation_indicator;
    private Long indicator_time;

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
}
