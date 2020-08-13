package cn.neyzoter.aiot.fddp.biz.service.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

/**
 * Hdfs工具
 * @author neyzoter
 */
public class HdfsUtil {
    public FileSystem getFiledSystem() throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        return fileSystem;
    }
    public static void cat(Configuration conf, String remoteFilePath) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        Path remotePath = new Path(remoteFilePath);
        FSDataInputStream in = fs.open(remotePath);
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = d.readLine()) != null) {
            String[] strarray = line.split(" ");
            for (int i = 0; i < strarray.length; i++) {
                System.out.print(strarray[i]);
                System.out.print(" ");

            }

            System.out.println(" ");
        }
        d.close();
        in.close();
        fs.close();
    }
    public void readHDFSFile(String filePath){
        FSDataInputStream fsDataInputStream = null;

        try {
            Path path = new Path(filePath);
            fsDataInputStream = this.getFiledSystem().open(path);
            IOUtils.copyBytes(fsDataInputStream, System.out, 4096, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fsDataInputStream != null){
                IOUtils.closeStream(fsDataInputStream);
            }
        }
    }
    public void writeHDFS(String localPath, String hdfsPath){
        FSDataOutputStream outputStream = null;
        FileInputStream fileInputStream = null;

        try {
            Path path = new Path(hdfsPath);
            outputStream = this.getFiledSystem().create(path);
            fileInputStream = new FileInputStream(new File(localPath));
            //输入流、输出流、缓冲区大小、是否关闭数据流，如果为false就在 finally里关闭
            IOUtils.copyBytes(fileInputStream, outputStream,4096, false);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                IOUtils.closeStream(fileInputStream);
            }
            if(outputStream != null){
                IOUtils.closeStream(outputStream);
            }
        }

    }
}
