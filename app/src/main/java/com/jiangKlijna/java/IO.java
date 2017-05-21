package com.jiangKlijna.java;

import java.io.*;

/**
 * io流工具类
 * Author: com.jiangKlijna
 */
public class IO {

    private static final int BYTE_ARRAY_LENGTH = 2048;

    private IO() {
    }

    //文件相关
    public static final void filec(final File fsrc, final File descf) throws IOException {
        chars(new FileReader(fsrc), new FileWriter(descf));
    }

    public static final void fileb(final File fsrc, final File descf) throws IOException {
        bytes(new FileInputStream(fsrc), new FileOutputStream(descf));
    }

    public static final void filec(final String src, final File descf) throws IOException {
        str(src, new FileWriter(descf));
    }

    public static final void fileb(final String src, final File descf) throws IOException {
        str(src, new FileOutputStream(descf));
    }

    public static final void filec(final byte[] bytes, final File descf) throws IOException {
        byt(bytes, new FileWriter(descf));
    }

    public static final void fileb(final byte[] bytes, final File descf) throws IOException {
        byt(bytes, new FileOutputStream(descf));
    }

    public static final String filec(final File descf) throws IOException {
        return str(new FileReader(descf));
    }

    public static final byte[] fileb(final File descf) throws IOException {
        return byt(new FileInputStream(descf));
    }

    //字符串相关
    public static final void str(final String string, final Writer writer) throws IOException {
        chars(new StringReader(string), writer);
    }

    public static final void str(final String string, final OutputStream writer) throws IOException {
        chars_bytes(new StringReader(string), writer);
    }

    public static final String str(final Reader reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        chars(reader, stringWriter);
        return stringWriter.toString();
    }

    public static final String str(final InputStream reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        bytes_chars(reader, stringWriter);
        return stringWriter.toString();
    }

    //字节数组相关
    public static final void byt(final byte[] bytes, final Writer writer) throws IOException {
        bytes_chars(new ByteArrayInputStream(bytes), writer);
    }

    public static final void byt(final byte[] bytes, final OutputStream writer) throws IOException {
        bytes(new ByteArrayInputStream(bytes), writer);
    }

    public static final byte[] byt(final InputStream reader) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bytes(reader, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static final byte[] byt(final Reader reader) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        chars_bytes(reader, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //字节流<->字符流
    public static final void bytes_chars(final InputStream is, final Writer os) throws IOException {
        chars(new InputStreamReader(is), os);
    }

    public static final void chars_bytes(final Reader is, final OutputStream os) throws IOException {
        chars(is, new OutputStreamWriter(os));
    }

    //字节流
    public static final void bytes(final InputStream is, final OutputStream os) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buf = new byte[BYTE_ARRAY_LENGTH];
        int len;
        while ((len = bis.read(buf)) != -1) {
            bos.write(buf, 0, len);
            bos.flush();
        }
        bis.close();
        bos.close();
    }

    //字符流
    public static final void chars(final Reader is, final Writer os) throws IOException {
        BufferedReader bufr = new BufferedReader(is);
        BufferedWriter bufw = new BufferedWriter(os);
        String line;
        while ((line = bufr.readLine()) != null) {
            bufw.write(line);
            bufw.newLine();
            bufw.flush();
        }
        bufr.close();
        bufw.close();
    }

}
