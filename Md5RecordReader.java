package CrackMd5_2;

import PasswordCracker.CandidateRangeInputSplit;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class Md5RecordReader  extends RecordReader<Text, Text> {
    private String rangeBegin;
    private String rangeEnd;
    private boolean done = false;


    Md5RecordReader() {

    }
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        boolean tmpDone = false;
        if (done == false) {
            tmpDone = true;
            done = true;
        }

        return tmpDone;
    }
    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return new Text(rangeBegin);
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return new Text(rangeEnd);
    }

    // After creating this class, It is called with a inputSplit as a parameter. and It divides inputSplit by a record of key/value.
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
        Md5InputSplit candidataRangeSplit = (Md5InputSplit) split;

        rangeBegin = candidataRangeSplit.getInputRange();
        rangeEnd = String.valueOf(Long.valueOf(rangeBegin) + candidataRangeSplit.getLength());

    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (done) {
            return 1.0f;
        }
        return 0.0f;
    }

    @Override
    public void close() throws IOException {
    }
}
