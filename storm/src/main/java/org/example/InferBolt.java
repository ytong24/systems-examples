package org.example;

import org.apache.storm.task.ShellBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;

import java.util.Map;

public class InferBolt extends ShellBolt implements IRichBolt {
    public InferBolt() {
        super("python3", "infersentence.py");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("expectedWords", "createdWords"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
