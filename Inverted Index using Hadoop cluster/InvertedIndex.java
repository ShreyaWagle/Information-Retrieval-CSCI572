import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;
import java.util.*;
public class InvertedIndex {
        public static class Tokenmap
        extends Mapper<Object, Text, Text, Text>{
                private Text term = new Text();
                private Text id = new Text();
                public void map(Object key, Text value, Context context
                                ) throws IOException, InterruptedException {

                        StringTokenizer dummy = new StringTokenizer(value.toString());

                        id.set(dummy.nextToken());

                        while (dummy.hasMoreTokens()) {
                                term.set(dummy.nextToken());
                                context.write(term, id);
                        }
                }
        }
        public static class getsumred
        extends Reducer<Text,Text,Text,Text> {
                private Text save = new Text();
                public void reduce(Text key, Iterable<Text> values,
                                Context context
                                ) throws IOException, InterruptedException {
                        HashMap<String, Integer> hm = new HashMap<String, Integer>();
                        for (Text val : values) {
                                if(hm.containsKey(val.toString())){
                                hm.put(val.toString(),hm.get(val.toString())+1);
                        }else{
                                hm.put(val.toString(),1);
                        }
                        }
                        StringBuilder ssw = new StringBuilder();
                        for(Map.Entry<String, Integer> pair : hm.entrySet()){
                                ssw.append(pair.getKey());
                                ssw.append(":");
                                ssw.append(pair.getValue());
                                ssw.append("\t");
                        }
                        save.set(ssw.toString());
                        context.write(key, save);
                }
        
        public static void main(String[] args) throws Exception {
                Configuration conf = new Configuration();
                Job obj = Job.getInstance(conf, "word count");
                obj.setJarByClass(InvertedIndex.class);
                obj.setMapperClass(Tokenmap.class);
                obj.setReducerClass(getsumred.class);
                obj.setOutputKeyClass(Text.class);
                obj.setOutputValueClass(Text.class);
                FileInputFormat.addInputPath(obj, new Path(args[0]));
                FileOutputFormat.setOutputPath(obj, new Path(args[1]));
                System.exit(obj.waitForCompletion(true) ? 0 : 1);
        }
        }
}
