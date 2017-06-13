package com.madhouse.protobuf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * Created by WUJUNFENG on 2017/6/12.
 */
public class DataFileReader<T> {
    private T nextData = null;
    private Method parseFrom = null;
    private BufferedInputStream inputStream = null;

    public DataFileReader(Class<T> c) {
        try {
            this.parseFrom = c.getMethod("parseFrom", new Class[]{byte[].class});
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public boolean open(File file) {
        try {
            this.close();
            this.inputStream = new BufferedInputStream(new FileInputStream(file));
            return true;
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return false;
        }
    }

    public T next() {
        return this.nextData;
    }

    public boolean hasNext() {
        try {
            if (this.inputStream == null || this.parseFrom == null) {
                return false;
            }

            byte[] size = new byte[4];
            if (this.inputStream.read(size) >= size.length) {
                int len = size[0] | (size[1] << 8) | (size[2] << 16) | (size[3] << 24);
                if (len > 0) {
                    byte[] data = new byte[len];
                    if (this.inputStream.read(data) >= data.length) {
                        if ((this.nextData = (T)this.parseFrom.invoke(null, data)) != null) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (Exception ex) {
            System.err.println(ex.toString());
            return false;
        }
    }

    public void close() {

        try {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
}
