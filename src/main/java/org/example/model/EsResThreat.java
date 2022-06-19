package org.example.model;

import java.util.List;

public class EsResThreat {
    private String id;
    private Long abnormal_aggregation;
    private Long host_aggregation;
    private Long network_abs;
    private Long indicator_time;

    private List<String> caz_ips;

    public List<String> getCaz_ips() {
        return caz_ips;
    }

    public void setCaz_ips(List<String> caz_ips) {
        this.caz_ips = caz_ips;
    }

    public Long getAbnormal_aggregation() {
        return abnormal_aggregation;
    }

    public void setAbnormal_aggregation(Long abnormal_aggregation) {
        this.abnormal_aggregation = abnormal_aggregation;
    }

    public Long getHost_aggregation() {
        return host_aggregation;
    }

    public void setHost_aggregation(Long host_aggregation) {
        this.host_aggregation = host_aggregation;
    }

    public Long getNetwork_abs() {
        return network_abs;
    }

    public void setNetwork_abs(Long network_abs) {
        this.network_abs = network_abs;
    }

    public Long getIndicator_time() {
        return indicator_time;
    }

    public void setIndicator_time(Long indicator_time) {
        this.indicator_time = indicator_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
