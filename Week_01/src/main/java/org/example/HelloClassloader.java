package org.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * 自定义类加载器读取Hello.xlass
 * @author Verbier
 * @date 2021/1/15
 */
public class HelloClassloader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> aClass = new HelloClassloader().findClass("Hello");
            Object obj = aClass.newInstance();
            Method method = aClass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try (InputStream inputStream = getResourceAsStream("Hello.xlass")) {
            if (inputStream != null) {
                byte[] bytes = read(inputStream);
                decode(bytes);
                return defineClass(name, bytes, 0, bytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }

    /**
     * read io
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * decode bytes
     *
     * @param bytes
     */
    private void decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)(255 - bytes[i]);
        }
    }
}
