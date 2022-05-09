# IBM MQ Prometheus Exporter

This repo contains the source code of a Java-based Prometheus exporter for IBM MQ. 

In the Prometheus vocabulary, an exporter is an application that connects to a target system (in this case an IBM MQ Queue Manager) and "exports" all the measurements in **Prometheus exposition format 0.0.4** (or **OpenMetrics 1.0**) to be scraped by Prometheus itself.

## Disclaimer

The source code contained in this repo does not belong to and is not supported/endorsed by IBM or any of its subsidiaries.

## Compatibility

The exporter has been tested against the following software versions:

- OpenJDK v. 8.x
- IBM MQ v. 8.x
- IBM MQ Java API v. 9.2.5.0
- Apache Maven v. 3.5.4

## Available metrics

The exporter makes available to Prometheus the following metrics:

| Metric  | Type  | Description  | Labels  |
|---|---|---|---|
| ibm_mq_queue_current_depth  | Gauge  | Number of messages waiting to be read/processed in a queue  | channel, host, port, queue, queue_manager  |

More metrics en route...

## How to build the exporter

Before compiling the source code you have to download the **IBM MQ All Client** library (available at https://developer.ibm.com/articles/mq-downloads/), extract the com.ibm.mq.allclient.jar archive and install it in the local Maven repo using the following command:

```console
$ mvn install:install-file -Dfile=./com.ibm.mq.allclient.jar -DgroupId=com.ibm.mq -DartifactId=allclient -Dversion=9.2.5.0 -Dpackaging=jar
```

Then proceed with the build phase:

```console
$ mvn package
```

The command above will produce a file named **ibm-mq-prometheus-exporter-1.0-SNAPSHOT.jar** in the **target** subdir. This is a uber-jar, containing everything that is needed to run the exporter.

## How to run the exporter

After having built the exporter from the source code and before running it, copy the uber-jar in its final location (i.e. /opt/mqexporter). From now on, this folder will be named the **EXPORTER_HOME** directory for the sake of brevity.

```console
$ cp target/ibm-mq-prometheus-exporter-1.0-SNAPSHOT.jar /opt/mqexporter
```

Enter in the EXPORTER_HOME and create a **config** subfolder within:

```console
$ cd /opt/mqexporter
$ mkdir config
```

In the config subfolder, create the exporter's config file (called **application.yaml**) as showed here after.

Before running the following command, please adapt the **mq-config** section with details of your MQ installation (queue managers, host, port, username, password, channel and queues).

```console
$ cat > config/application.yaml << EOF
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    prometheus:
      enabled: 'true'
    metrics:
      enabled: 'true'
    info:
      enabled: 'true'
  metrics:
    export:
      prometheus:
        enabled: 'true'
  security:
    enabled: 'false'

mq-config:
  queue-managers:
    - name: "QM1"
      host: "my.qm1host.com"
      port: "1414"
      username: "user"
      password: "pass"
      channel: "APP.CH.SVR"
      queues:
        - "Q1"
        - "Q2"
    - name: "QM2"
      host: "my.qm2host.com"
      port: "1414"
      username: ""
      password: ""
      channel: "APP.CH.SVR"
      queues:
        - "Q1"
        - "Q2"
EOF
```

Finally, from the EXPORTER_HOME directory, launch the exporter as showed:

```console
$ java -jar ibm-mq-prometheus-exporter-1.0-SNAPSHOT.jar
```

## How to scrape metrics

By default the exporter opens the HTTP port **8080** and its metrics can be scraped at the **/actuator/prometheus** URI path.

If running on localhost, the exporter can be tested using the **curl** command such as: 

```console
$ curl -s localhost:8080/actuator/prometheus
```
