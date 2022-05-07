#

Before compiling the source code you have to download the IBM MQ All Client library (available at https://developer.ibm.com/articles/mq-downloads/), extract the com.ibm.mq.allclient.jar archive and install it in the local Maven repo

```console
$ mvn install:install-file -Dfile=./com.ibm.mq.allclient.jar -DgroupId=com.ibm.mq -DartifactId=allclient -Dversion=9.2.5.0 -Dpackaging=jar
```