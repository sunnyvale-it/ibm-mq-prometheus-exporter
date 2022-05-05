package it.sunnyvale.ibmmqexporter.services;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class DataProvider {

    public double getQueueCurrentDepth(String host,
                                       String port,
                                       String queueManager,
                                       String channel,
                                       String username,
                                       String password,
                                       String queue){
        int depth = -1;
        try {
            MQEnvironment.hostname = host;
            MQEnvironment.port = Integer.valueOf(port).intValue();
            MQEnvironment.channel = channel;
            MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,
                    MQC.TRANSPORT_MQSERIES);
            MQQueueManager qMgr = new MQQueueManager(queueManager);
            int openOptions = MQC.MQOO_INQUIRE;
            MQQueue destQueue = qMgr.accessQueue(queue, openOptions);
            depth = destQueue.getCurrentDepth();
            destQueue.close();
            qMgr.disconnect();
        } catch (Exception err) {
            throw new RuntimeException();
        }
        return depth;
    }

}
