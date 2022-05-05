package it.sunnyvale.ibmmqexporter.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "mq-config")
public class MQConfig {
    List<QueueManager> queueManagers;
}
