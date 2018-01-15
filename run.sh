ENCRYPTEDPASSWORD=$1
FILENAME=wordList.txt
SPLITS=2

hdfs dfs -rmr md5-out
hadoop jar CrackMd5.jar CrackMd5_2.Main ${ENCRYPTEDPASSWORD} ${FILENAME} ${SPLITS}
