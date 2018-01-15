package CrackMd5_2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static long TOTAL_RANGE_SIZE = 0; // total line

    public static void main(String[] args) throws Exception {
        Configuration c = new Configuration();
        Path input = new Path("/user/hduser/md5-in");
        Path output = new Path("/user/hduser/md5-out");

        //arg[0] input password
        //arg[1] dictionary name
        //arg[2] number of replicas
        c.set("encryptedPassword", args[0]);
        c.setInt("numberOfSplit", Integer.parseInt(args[2]));
        c.set("filePath",input+ "/" + args[1]);  //dictionary location

        TOTAL_RANGE_SIZE = countLine(input+ "/" + args[1],c);

        //Set jobs properties
        Job j = new Job(c, "crackMd5");

        j.setJarByClass(Main.class);
        j.setMapperClass(Md5Mapper.class);
        j.setReducerClass(Md5Reducer.class);
        j.setOutputKeyClass(Text.class);
        j.setOutputValueClass(Text.class);
        j.setInputFormatClass(Md5InputFormat.class);

        FileOutputFormat.setOutputPath(j, output);
        System.exit(j.waitForCompletion(true) ? 0 : 1);
    }

    //Counting lines in dictionary
    public static long countLine(String inputPath, Configuration conf) throws IOException{
        Path pt=new Path(inputPath);
        FileSystem fs = FileSystem.get(conf);
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        long counter =0;
        String line;
        line=br.readLine();
        while (line != null) {
            counter = counter +1 ;
            line = br.readLine();
        }
        br.close();
        return counter;
    }
}
