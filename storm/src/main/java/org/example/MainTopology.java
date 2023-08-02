package org.example;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Map;

public class MainTopology {
    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            System.out.println("Please pass the file path");
            return;
        }
        String filePath = args[0];

        // Create the topology builder
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("sentenceSpout", new SentenceSpout(filePath), 1);
        builder.setBolt("StripBolt", new StripBolt(), 1).shuffleGrouping("sentenceSpout");
        builder.setBolt("InferBolt", new InferBolt(), 1).shuffleGrouping("StripBolt");

        // Set configurations
        Config conf = new Config();
        conf.setDebug(true);
//        conf.setEnvironment(Map.of("filePath", filePath));
//        conf.setMaxTaskParallelism(3);

        // Run in local mode
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());

        // Run the topology for 10 seconds, then shut it down
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cluster.shutdown();
        }
    }
}