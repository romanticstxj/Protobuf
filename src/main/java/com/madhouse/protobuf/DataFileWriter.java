package com.madhouse.protobuf;

import com.google.protobuf.GeneratedMessage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by WUJUNFENG on 2017/6/12.
 */
public class DataFileWriter<T> {
    private BufferedOutputStream outputStream;

    public boolean write(T message) {
        try {
            if (this.outputStream == null || message == null) {
                return false;
            }

            GeneratedMessage msg = (GeneratedMessage)(message);

            byte[] size = new byte[4];
            byte[] data = msg.toByteArray();

            size[0] = (byte)(data.length & 0xff);
            size[1] = (byte)((data.length >> 8) & 0xff);
            size[2] = (byte)((data.length >> 16) & 0xff);
            size[3] = (byte)((data.length >> 24) & 0xff);

            this.outputStream.write(size);
            this.outputStream.write(data);
            this.outputStream.flush();

            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public void close() {

        try {
            if (this.outputStream != null) {
                this.outputStream.flush();
                this.outputStream.close();
                this.outputStream = null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public boolean create(File file, boolean append) {
        try {
            this.close();
            this.outputStream = new BufferedOutputStream(new FileOutputStream(file, append));
            return true;

        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
}
