package it.sunnyvale.ibmmqexporter.services;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@Scope("singleton")
public class DataProvider {

    private HashMap<String,MQQueueManager> queueManagerHashMap;

    @PostConstruct
    public void init(){
        queueManagerHashMap = new HashMap<>();
    }


    private MQQueueManager createQueueManager(String host,
                                              String port,
                                              String queueManagerName,
                                              String channel,
                                              String username,
                                              String password
    ) {
        try{
            MQEnvironment.hostname = host;
            MQEnvironment.port = Integer.valueOf(port).intValue();
            MQEnvironment.channel = channel;
            MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,
                    MQC.TRANSPORT_MQSERIES);
            MQQueueManager queueManager = new MQQueueManager(queueManagerName);
            queueManagerHashMap.put(queueManagerName,queueManager);
            return queueManager;
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }

    private MQQueueManager getOrCreateQueueManager(String host,
                                                   String port,
                                                   String queueManagerName,
                                                   String channel,
                                                   String username,
                                                   String password
                            ){
        if (queueManagerHashMap.containsKey(queueManagerName)){
            MQQueueManager queueManager = queueManagerHashMap.get(queueManagerName);
            if( !queueManager.isOpen() || !queueManager.isConnected()){
                queueManager = createQueueManager(
                        host,
                        port,
                        queueManagerName,
                        channel,
                        username,
                        password
                );
                queueManagerHashMap.put(queueManagerName,queueManager);
            }
            return queueManager;
        }else{
            return createQueueManager(host,
                        port,
                        queueManagerName,
                        channel,
                        username,
                        password
                    );
        }
    }

    public int getQueueWriters(String host,
                                    String port,
                                    String queueManagerName,
                                    String channel,
                                    String username,
                                    String password,
                                    String queue){
        int value = -1;
        try {
            MQQueueManager queueManager = getOrCreateQueueManager(host,
                    port,
                    queueManagerName,
                    channel,
                    username,
                    password
            );
            int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE;
            MQQueue destQueue = queueManager.accessQueue(queue, openOptions);
            value = destQueue.getOpenOutputCount();
            destQueue.close();
        } catch (Exception err) {
            err.printStackTrace();
            throw new RuntimeException(err);
        }
        return value;
    }

    public int getQueueReaders(String host,
                                    String port,
                                    String queueManagerName,
                                    String channel,
                                    String username,
                                    String password,
                                    String queue){
        int value = -1;
        try {
            MQQueueManager queueManager = getOrCreateQueueManager(host,
                    port,
                    queueManagerName,
                    channel,
                    username,
                    password
            );
            int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE;
            MQQueue destQueue = queueManager.accessQueue(queue, openOptions);
            value = destQueue.getOpenInputCount() - 1;
            destQueue.close();
        } catch (Exception err) {
            err.printStackTrace();
            throw new RuntimeException(err);
        }
        return value;
    }

    public int getQueueCurrentDepth(String host,
                                       String port,
                                       String queueManagerName,
                                       String channel,
                                       String username,
                                       String password,
                                       String queue){
        int value = -1;
        try {
            MQQueueManager queueManager = getOrCreateQueueManager(host,
                        port,
                        queueManagerName,
                        channel,
                        username,
                        password
                    );
            int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE;
            MQQueue destQueue = queueManager.accessQueue(queue, openOptions);
            value = destQueue.getCurrentDepth();
            destQueue.close();
        } catch (Exception err) {
            err.printStackTrace();
            throw new RuntimeException(err);
        }
        return value;
    }

    public int getQueueMaxDepth(String host,
                                       String port,
                                       String queueManagerName,
                                       String channel,
                                       String username,
                                       String password,
                                       String queue){
        int value = -1;
        try {
            MQQueueManager queueManager = getOrCreateQueueManager(host,
                    port,
                    queueManagerName,
                    channel,
                    username,
                    password
            );
            int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_INQUIRE;
            MQQueue destQueue = queueManager.accessQueue(queue, openOptions);
            value = destQueue.getMaximumDepth();
            destQueue.close();
        } catch (Exception err) {
            err.printStackTrace();
            throw new RuntimeException(err);
        }
        return value;
    }

}
