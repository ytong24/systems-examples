package org.example;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.TopologyBuilder;

public class MainTopology extends ConfigurableTopology {
    public static void main(String[] args) throws Exception {
        // Create the topology builder
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("sentenceSpout", new SentenceSpout(), 1);
        builder.setBolt("StripBolt", new StripBolt(), 1).shuffleGrouping("sentenceSpout");

        // Set configurations
        Config conf = new Config();
        conf.setDebug(true);
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

    @Override
    protected int run(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("sentenceSpout", new SentenceSpout(), 1);
        builder.setBolt("StripBolt", new StripBolt(), 1).shuffleGrouping("sentenceSpout");
//        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("exclaim1");

        conf.setDebug(true);

        String topologyName = "test";

        conf.setNumWorkers(1);
//        conf.setEnvironment();

        if (args != null && args.length > 0) {
            topologyName = args[0];
        }

        return submit(topologyName, conf, builder);
    }
}