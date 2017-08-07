package com.madhouse.protobuf;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * Created by WUJUNFENG on 2017/6/12.
 */
public class DataFileReader<T> {
    private T nextData = null;
    private Method parseFrom = null;
    private DataInputStream dataInputStream = null;

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
            this.dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
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
            if (this.dataInputStream == null || this.parseFrom == null) {
                return false;
            }

            int len = 0;
            if ((len = this.dataInputStream.readInt()) > 0) {
                byte[] data = new byte[len];
                if (this.dataInputStream.read(data) >= data.length) {
                    if ((this.nextData = (T)this.parseFrom.invoke(null, data)) != null) {
                        return true;
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
            if (this.dataInputStream != null) {
                this.dataInputStream.close();
                this.dataInputStream = null;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
}
