package CrackMd5_2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

//  It write both original password and encrypted password in the file.

public class Md5Reducer extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text encrypted, Iterable<Text> password, Context context)
            throws IOException, InterruptedException {
        for(Text item : password){
            //write result
            context.write(encrypted, item);
        }
    }
}
