package org.example.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.shaded.guava30.com.google.common.collect.Lists;

import java.util.List;

public class EsResAsset {
    private String id;
    private Long normal_indicator;
    private Long abnormal_indicator;
    private Long account_indicator;
    private Long indicator_time;
    private List<String> caz_ips;

    public List<String> getCaz_ips() {
        return caz_ips;
    }

    public void setCaz_ips(List<String> caz_ips) {
        this.caz_ips = caz_ips;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNormal_indicator() {
        return normal_indicator;
    }

    public void setNormal_indicator(Long normal_indicator) {
        this.normal_indicator = normal_indicator;
    }

    public Long getAbnormal_indicator() {
        return abnormal_indicator;
    }

    public void setAbnormal_indicator(Long abnormal_indicator) {
        this.abnormal_indicator = abnormal_indicator;
    }

    public Long getAccount_indicator() {
        return account_indicator;
    }

    public void setAccount_indicator(Long account_indicator) {
        this.account_indicator = account_indicator;
    }

    public Long getIndicator_time() {
        return indicator_time;
    }

    public void setIndicator_time(Long indicator_time) {
        this.indicator_time = indicator_time;
    }

    public static void main(String[] args) {
        System.out.println(CollectionUtils.intersection(Lists.newArrayList("a","b"),Lists.newArrayList("b","c")));
        System.out.println(CollectionUtils.union(Lists.newArrayList("a","b"),Lists.newArrayList("b","c")));

    }
}
