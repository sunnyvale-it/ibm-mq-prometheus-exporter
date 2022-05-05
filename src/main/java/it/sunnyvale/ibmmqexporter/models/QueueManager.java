package it.sunnyvale.ibmmqexporter.models;

import lombok.Data;

import java.util.List;

@Data
public class QueueManager {
    String name;
    String host;
    String port;
    String username;
    String password;
    String channel;
    List<String> queues;
}
