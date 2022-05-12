package it.sunnyvale.ibmmqexporter.prometheus;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import it.sunnyvale.ibmmqexporter.models.MQConfig;
import it.sunnyvale.ibmmqexporter.services.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PrometheusExporter {

    @Autowired
    private MQConfig mqConfig;

    @Autowired
    private DataProvider dataProvider;

    @PostConstruct
    private void init(){
        mqConfig
                .getQueueManagers()
                .stream()
                .forEach(queueManager -> queueManager
                        .getQueues()
                        .stream()
                        .forEach(queue -> {
                                    this.registerQueueCurrentDepthMetric(
                                            queueManager.getHost(),
                                            queueManager.getPort(),
                                            queueManager.getName(),
                                            queueManager.getUsername(),
                                            queueManager.getPassword(),
                                            queueManager.getChannel(),
                                            queue
                                        );
                                    this.registerQueueMaxDepthMetric(
                                            queueManager.getHost(),
                                            queueManager.getPort(),
                                            queueManager.getName(),
                                            queueManager.getUsername(),
                                            queueManager.getPassword(),
                                            queueManager.getChannel(),
                                            queue
                                    );
                            }
                        )
                );
    }

    private void registerQueueMaxDepthMetric(
            String host,
            String port,
            String queueManager,
            String username,
            String password,
            String channel,
            String queue
    ){

        Gauge gauge = Gauge
                .builder("ibm_mq_queue_max_depth",
                        dataProvider,
                        value -> value.getQueueMaxDepth(
                                host,
                                port,
                                queueManager,
                                channel,
                                username,
                                password,
                                queue
                        )
                )
                .description("The max queue capacity")
                .tags("host",host)
                .tags("port",port)
                .tags("channel",channel)
                .tags("queue-manager",queueManager)
                .tags("queue",queue)
                .register(Metrics.globalRegistry);
    }

    private void registerQueueCurrentDepthMetric(
            String host,
            String port,
            String queueManager,
            String username,
            String password,
            String channel,
            String queue
    ){

        Gauge gauge = Gauge
                .builder("ibm_mq_queue_current_depth",
                        dataProvider,
                        value -> value.getQueueCurrentDepth(
                                host,
                                port,
                                queueManager,
                                channel,
                                username,
                                password,
                                queue
                        )
                )
                .description("Number of messages waiting to be read/processed in a queue")
                .tags("host",host)
                .tags("port",port)
                .tags("channel",channel)
                .tags("queue-manager",queueManager)
                .tags("queue",queue)
                .register(Metrics.globalRegistry);
    }

}
