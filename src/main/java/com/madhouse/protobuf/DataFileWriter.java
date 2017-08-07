package com.madhouse.protobuf;

import com.google.protobuf.GeneratedMessage;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by WUJUNFENG on 2017/6/12.
 */
public class DataFileWriter<T> {
    private DataOutputStream dataOutputStream;

    public boolean write(T message) {
        try {
            if (this.dataOutputStream == null || message == null) {
                return false;
            }

            GeneratedMessage msg = (GeneratedMessage)(message);
            byte[] data = msg.toByteArray();

            this.dataOutputStream.writeInt(data.length);
            this.dataOutputStream.write(data);
            this.dataOutputStream.flush();

            return true;
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return false;
        }
    }

    public void close() {

        try {
            if (this.dataOutputStream != null) {
                this.dataOutputStream.flush();
                this.dataOutputStream.close();
                this.dataOutputStream = null;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

    }

    public boolean create(File file) {
        return this.create(file, false);
    }

    public boolean create(File file, boolean append) {
        try {
            this.close();
            this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file, append)));
            return true;

        } catch (Exception ex) {
            System.err.println(ex.toString());
            return false;
        }
    }
}
