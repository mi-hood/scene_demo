package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch7SinkBuilder;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.shaded.guava30.com.google.common.collect.Collections2;
import org.apache.flink.shaded.guava30.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.example.model.*;
import org.example.process.DataAnalysis;
import org.example.source.FakeSource;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Main {
    private static Integer WindowSizeMinute=10;
    private static class LogDataProcess implements AggregateFunction<LogData,List<LogData>, EsResAsset>{

        @Override
        public List<LogData> createAccumulator() {
            return Lists.newLinkedList();
        }

        @Override
        public List<LogData> add(LogData value, List<LogData> accumulator) {
            accumulator.add(value);
            return accumulator;
        }

        @Override
        public EsResAsset getResult(List<LogData> accumulator) {
            EsResAsset res=new EsResAsset();
            res.setId("asset-"+getKeySuffix(accumulator.size()>0 ? accumulator.get(0).getEvent_time() : System.currentTimeMillis(),WindowSizeMinute));
            res.setNormal_indicator(accumulator.stream().filter(e->e.getLog_level()>4).count());
            res.setAbnormal_indicator(accumulator.stream().filter(e->e.getLog_level()<=4).count());
            res.setAccount_indicator(accumulator.stream().filter(e-> !StringUtils.isNullOrWhitespaceOnly(e.getRelated_account())).count());
            res.setCaz_ips(accumulator.stream().map(LogData::getClient_addr).distinct().collect(Collectors.toList()));
            res.setIndicator_time(accumulator.size()>0 ? accumulator.get(0).getEvent_time() : System.currentTimeMillis());
            return res;
        }

        @Override
        public List<LogData> merge(List<LogData> a, List<LogData> b) {
            a.addAll(b);
            return a;
        }
    }

    private static class EventDataProcess implements AggregateFunction<EventData,List<EventData>, EsResThreat>{

        @Override
        public List<EventData> createAccumulator() {
            return Lists.newLinkedList();
        }

        @Override
        public List<EventData> add(EventData value, List<EventData> accumulator) {
            accumulator.add(value);
            return accumulator;
        }

        @Override
        public EsResThreat getResult(List<EventData> accumulator) {
            EsResThreat res = new EsResThreat();
            res.setId("threat-"+getKeySuffix(accumulator.size()>0 ? accumulator.get(0).getEvent_time() : System.currentTimeMillis(),WindowSizeMinute));
            res.setAbnormal_aggregation(accumulator.stream().map(EventData::getClasstype).distinct().count());
            res.setHost_aggregation(accumulator.stream().map(EventData::getSrc_ip).distinct().count());
            res.setNetwork_abs((long) accumulator.size());
            res.setIndicator_time(accumulator.size()>0 ? accumulator.get(0).getEvent_time() : System.currentTimeMillis());
            res.setCaz_ips(accumulator.stream().map(EventData::getSrc_ip).distinct().collect(Collectors.toList()));
            return res;
        }

        @Override
        public List<EventData> merge(List<EventData> a, List<EventData> b) {
            a.addAll(b);
            return a;
        }
    }
    private static String getKeySuffix(Long timestamp,Integer windowsMinuteSize){
        Calendar s = Calendar.getInstance();
        s.setTime(new Date(timestamp));
        return String.format("%d%d%d%d",s.get(Calendar.YEAR),s.get(Calendar.DAY_OF_YEAR),s.get(Calendar.HOUR_OF_DAY),s.get(Calendar.MINUTE)/windowsMinuteSize);
    }
    private static IndexRequest indexAssetElement(EsResAsset element) {
        return Requests.indexRequest()
                .index("asset_index")
                .id(element.getId())
                .source(element);
    }

    private static IndexRequest indexThreatElement(EsResThreat element) {
        return Requests.indexRequest()
                .index("threat_index")
                .id(element.getId())
                .source(element);
    }

    private static IndexRequest indexGlobalElement(EsResGlobal element) {
        return Requests.indexRequest()
                .index("global_index")
                .id(element.getId())
                .source(element);
    }
    private static Float calIpsOverlap(List<String> ips_a,List<String> ips_b){
        if(CollectionUtils.isEmpty(ips_a) || CollectionUtils.isEmpty(ips_b)){
            return 0f;
        }else{
            return (CollectionUtils.intersection(ips_a,ips_b).size()+0f)/CollectionUtils.union(ips_a,ips_b).size();
        }
    }

    private static Float calAssetIndex(EsResAsset res){
        return (res.getAbnormal_indicator()+ res.getAccount_indicator()+0f)/(res.getAbnormal_indicator()+res.getNormal_indicator());
    }
    private static Float calThreatIndex(EsResThreat res){
        return (res.getAbnormal_aggregation()+res.getHost_aggregation()+0f)/(res.getNetwork_abs());
    }
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> event_source = KafkaSource.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("event_data")
                .setGroupId("csm")
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        KafkaSource<String> log_source = KafkaSource.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("log_data")
                .setGroupId("csm")
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStream<EsResThreat> event_stream= env.fromSource(event_source,WatermarkStrategy.noWatermarks(), "kafka_event_source")
                .map(json-> JSON.toJavaObject(JSONObject.parseObject(json),EventData.class))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<EventData>forBoundedOutOfOrderness(Duration.ofSeconds(5)).withTimestampAssigner((event,timestamp)->event.getEvent_time()))
                .keyBy(EventData::getSource_id)
                .window(TumblingEventTimeWindows.of(Time.minutes(WindowSizeMinute)))
                .aggregate(new EventDataProcess());
        event_stream.sinkTo(new Elasticsearch7SinkBuilder<EsResThreat>()
                        .setBulkFlushMaxActions(1) // Instructs the sink to emit after every element, otherwise they would be buffered
                        .setHosts(new HttpHost("elastic", 9200, "http"))
                        .setEmitter((element, context, indexer) ->indexer.add(indexThreatElement(element)))
                        .build())
                .name("event_process");
        DataStream<EsResAsset> log_stream= env.fromSource(log_source,WatermarkStrategy.noWatermarks(), "kafka_log_source")
                .map(json-> JSON.toJavaObject(JSONObject.parseObject(json),LogData.class))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<LogData>forBoundedOutOfOrderness(Duration.ofSeconds(5)).withTimestampAssigner((event,timestamp)->event.getEvent_time()))
                .keyBy(LogData::getSource_id)
                .window(TumblingEventTimeWindows.of(Time.minutes(WindowSizeMinute)))
                .aggregate(new LogDataProcess());

        log_stream.sinkTo(new Elasticsearch7SinkBuilder<EsResAsset>()
                        .setBulkFlushMaxActions(1) // Instructs the sink to emit after every element, otherwise they would be buffered
                        .setHosts(new HttpHost("elastic", 9200, "http"))
                        .setEmitter(
                                (element, context, indexer) ->
                                        indexer.add(indexAssetElement(element)))
                        .build())
                .name("log_process");
        event_stream.connect(log_stream).keyBy(event->event.getId().split("-")[1],log->log.getId().split("-")[1]).map(new CoMapFunction<EsResThreat, EsResAsset, EsResGlobal>() {
            AtomicReference<EsResGlobal> res=new AtomicReference<>(new EsResGlobal());
            AtomicReference<List<String>> ips=new AtomicReference<>();
            @Override
            public EsResGlobal map1(EsResThreat value) {
                res.get().setId("global-"+value.getId().split("-")[1]);
                res.get().setIndicator_time(value.getIndicator_time());
                res.get().setSecurity_indicator(calThreatIndex(value));
                if(CollectionUtils.isNotEmpty(ips.get())){
                    Float ip_overlap=calIpsOverlap(value.getCaz_ips(),ips.get());
                    Float sitution_indicator = ip_overlap*res.get().getService_indicator()+(1+ip_overlap)*res.get().getSecurity_indicator();
                    res.get().setSituation_indicator(sitution_indicator);
                }else{
                    ips.set(value.getCaz_ips());
                }
                return res.get();
            }

            @Override
            public EsResGlobal map2(EsResAsset value) {
                res.get().setService_indicator(calAssetIndex(value));
                if(CollectionUtils.isNotEmpty(ips.get())){
                    Float ip_overlap=calIpsOverlap(value.getCaz_ips(),ips.get());
                    Float sitution_indicator = ip_overlap*res.get().getService_indicator()+(1+ip_overlap)*res.get().getSecurity_indicator();
                    res.get().setSituation_indicator(sitution_indicator);
                }else{
                    ips.set(value.getCaz_ips());
                }
                return res.get();
            }
        }).sinkTo(new Elasticsearch7SinkBuilder<EsResGlobal>()
                        .setBulkFlushMaxActions(1) // Instructs the sink to emit after every element, otherwise they would be buffered
                        .setHosts(new HttpHost("elastic", 9200, "http"))
                        .setEmitter(
                                (element, context, indexer) ->
                                        indexer.add(indexGlobalElement(element)))
                        .build())
                .name("global_process");

        env.execute("demo");
    }
}