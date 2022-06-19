package org.example.source;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.guava30.com.google.common.collect.ImmutableMap;
import org.apache.flink.shaded.guava30.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public class FakeSource implements ParallelSourceFunction<Tuple2<String, Long>> {


    private Random random=new Random();

    private boolean isRunning = true;

    private Map<Integer,String> mapping = ImmutableMap.of(0,"event",1,"log");

    /**
     * 主要的方法
     * 启动一个source
     * 大部分情况下，都需要在这个run方法中实现一个循环，这样就可以循环产生数据了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void run(SourceFunction.SourceContext<Tuple2<String, Long>> ctx) throws Exception {
        while(isRunning){
//            ctx.collectWithTimestamp(new Tuple2<>(mapping.getOrDefault(random.nextInt()%2,"log"),Math.abs(random.nextLong()%100)),System.currentTimeMillis());
            ctx.collect(new Tuple2<>(mapping.getOrDefault(random.nextInt()%2,"log"),Math.abs(random.nextLong()%100)));
            Thread.sleep(20);
        }
    }

    /**
     * 取消一个cancel的时候会调用的方法
     *
     */
    @Override
    public void cancel() {
        isRunning = false;
    }
}
