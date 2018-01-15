package CrackMd5_2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


//
public class Md5InputSplit extends InputSplit implements Writable{
    private String candidateRange;
    private long rangeBegin;
    private long rangeEnd;
    private String[] hosts;

    public Md5InputSplit(String inputRange, long inputRangeStringLength, String[] hosts) {
        this.candidateRange = inputRange;
        this.rangeBegin = 0;
        this.rangeEnd = inputRangeStringLength;
        this.hosts = hosts;
    }

    public Md5InputSplit() {

    }

    public String getInputRange() {
        return candidateRange;
    }

    public long getStart() {
        return rangeBegin;
    }

    @Override
    public long getLength() {
        return rangeEnd;
    }

    @Override
    public String toString() {
        return candidateRange + ":" + rangeBegin + "+" + rangeEnd;
    }

    @Override
    public String[] getLocations() throws IOException {
        if (this.hosts == null) {
            return new String[]{};
        } else {
            return this.hosts;
        }
    }

    public void readFields(DataInput in) throws IOException {
        candidateRange = Text.readString(in);
        rangeBegin = in.readLong();
        rangeEnd = in.readLong();
        hosts = null;
    }

    public void write(DataOutput out) throws IOException {
        Text.writeString(out, candidateRange.toString());
        out.writeLong(rangeBegin);
        out.writeLong(rangeEnd);
    }
}
