rm -r classes/*
sudo javac -classpath $($HADOOP_HOME/bin/hadoop classpath) -d classes Main.java Md5InputFormat.java Md5InputSplit.java Md5Mapper.java Md5RecordReader.java Md5Reducer.java
jar -cvf CrackMd5.jar -C classes .
