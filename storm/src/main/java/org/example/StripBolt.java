package org.example;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Arrays;
import java.util.Map;

public class StripBolt extends BaseRichBolt {
    OutputCollector oc;
    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.oc = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String s = tuple.getString(0);
        String[] splits = s.split(" ");
        // if the sentence contains less than 4 words, ignore this sentence
        if(splits.length <= 4) return;
        String testSentence = String.join(" ", Arrays.copyOfRange(splits, 0, splits.length-3));
        String expectedWords = String.join(" ", Arrays.copyOfRange(splits, splits.length-3, splits.length));

        this.oc.emit(new Values(testSentence, expectedWords));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("TestSentence", "ExpectedWords"));
    }
}
