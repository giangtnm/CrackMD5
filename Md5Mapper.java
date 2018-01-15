package CrackMd5_2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Mapper extends Mapper<Text, Text, Text, Text>  {
    public void map(Text key, Text value, Context context)
            throws IOException, InterruptedException  {
        Configuration conf = context.getConfiguration();
        FileSystem hdfs = FileSystem.get(conf);

        long rangeBegin = Long.valueOf(key.toString());
        long rangeEnd = Long.valueOf(value.toString());

        System.out.println("B: " + rangeBegin + " E: " + rangeEnd);

        String encryptedPassword = conf.get("encryptedPassword");
        Path filePath = new Path(conf.get("filePath"));
        BufferedReader br=new BufferedReader(new InputStreamReader(hdfs.open(filePath)));
        String password = null;
        try{
            password = findPasswordInRange(rangeBegin, rangeEnd, encryptedPassword,br);
        } catch (NoSuchAlgorithmException e){
        }
        if (password != null)
            context.write(new Text(encryptedPassword), new Text(password));
    }


    public String findPasswordInRange (long rangeBegin, long rangeEnd, String encryptedPassword, BufferedReader br) throws IOException,NoSuchAlgorithmException{
        String line;
        int i = 0;
        //go to the first line in range
        for(; i<rangeBegin;i++){
            line = br.readLine();
        }
        //hash to compare
        for(; i<rangeEnd;i++ ){
            line = br.readLine();
            if(line==null) break;
            String hash = toHash(line);
            if(hash.equals(encryptedPassword)){
                br.close();
                return line;
            }
        }
        br.close();
        return null;
    }

    //hash function
    static private String toHash(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md5;
        md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(plainText.getBytes());
        byte[] digest = md5.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        return hashtext;
    }
}
