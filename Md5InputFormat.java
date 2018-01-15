package CrackMd5_2;

import org.apache.hadoop.mapreduce.*;

import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static CrackMd5_2.Main.TOTAL_RANGE_SIZE;

public class Md5InputFormat extends InputFormat<Text,Text>{
        private List<InputSplit> splits;

        @Override
        public RecordReader<Text, Text> createRecordReader(InputSplit split, TaskAttemptContext context)
                throws IOException, InterruptedException {
            return new Md5RecordReader();
        }

        // It generate the splits which are consist of string and return to mapper.
        @Override
        public List<InputSplit> getSplits(JobContext job) throws IOException, InterruptedException {
            splits = new ArrayList<>();

            int numberOfSplit = job.getConfiguration().getInt("numberOfSplit", 1);    //get map_count
            long subRangeSize = (TOTAL_RANGE_SIZE + numberOfSplit - 1) / numberOfSplit;

            // For each subrange store it in the InputSlip list
            for (int i = 0; i < numberOfSplit; i++) {
                long currentSubRange = i * subRangeSize;
                Md5InputSplit split = new Md5InputSplit(
                        String.valueOf(currentSubRange), subRangeSize, null);
                splits.add(split);
            }

            return splits;
        }
}
