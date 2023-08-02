package org.example;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SentenceSpout extends BaseRichSpout {
    SpoutOutputCollector soc;
    List<String> sentences;

    String inputFilePath;

    public SentenceSpout(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.soc = spoutOutputCollector;
        try {
            sentences = Files.readAllLines(Paths.get(this.inputFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextTuple() {
        for(String sentence : sentences) {
            this.soc.emit(new Values(sentence));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("Sentence"));
    }
}
