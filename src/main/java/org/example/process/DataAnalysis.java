package org.example.process;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.collect.Iterators;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class DataAnalysis extends KeyedProcessFunction<Long, JSONObject, JSONObject> {

    private transient ListState<Float> assetsState;
    private transient ListState<Float> threatState;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ListStateDescriptor<Float> assetsDescriptor = new ListStateDescriptor<>("timer-state", Types.FLOAT);
        assetsState = getRuntimeContext().getListState(assetsDescriptor);

        ListStateDescriptor<Float> threatDescriptor = new ListStateDescriptor<>("timer-state", Types.FLOAT);
        threatState = getRuntimeContext().getListState(threatDescriptor);
    }



    @Override
    public void processElement(JSONObject value, KeyedProcessFunction<Long, JSONObject, JSONObject>.Context ctx, Collector<JSONObject> out) throws Exception {
        assetsState.add(Float.parseFloat(value.getString("value")));
        System.out.println(Iterators.size(assetsState.get().iterator()));
        out.collect(value);
    }
}
