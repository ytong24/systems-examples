package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Map;
import java.util.Properties;

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

        // Create KafkaBolt properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Create KafkaBolt
        KafkaBolt<String, String> kafkaBolt = new KafkaBolt<String, String>()
                .withProducerProperties(props)
                .withTopicSelector(new DefaultTopicSelector("infer"))
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<String, String>());

        // Add KafkaBolt to the topology
        builder.setBolt("kafka-bolt", kafkaBolt, 1).shuffleGrouping("InferBolt");

        // Set configurations
        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
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