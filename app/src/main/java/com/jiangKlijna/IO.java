package com.jiangKlijna;

import java.io.*;

/**
 * io流工具类
 * Author: com.jiangKlijna
 */
public class IO {
    private IO() {
    }

    public static void copyFile_char(final File fsrc, final File descf) throws IOException {
        io(new FileReader(fsrc), new FileWriter(descf));
    }

    public static void copyFile_byte(final File fsrc, final File descf) throws IOException {
        io(new FileInputStream(fsrc), new FileOutputStream(descf));
    }

    public static void copyFile_char(final String src, final File descf) throws IOException {
        io(src, new FileWriter(descf));
    }

    public static void copyFile_byte(final String src, final File descf) throws IOException {
        io(src, new FileOutputStream(descf));
    }

    public static void copyFile_char(final byte[] bytes, final File descf) throws IOException {
        io(bytes, new FileWriter(descf));
    }

    public static void copyFile_byte(final byte[] bytes, final File descf) throws IOException {
        io(bytes, new FileOutputStream(descf));
    }

    public static String io(final Reader reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        io(reader, stringWriter);
        return stringWriter.toString();
    }

    public static String io(final InputStream reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        io(reader, stringWriter);
        return stringWriter.toString();
    }

    public static void io(final byte[] bytes, final Writer writer) throws IOException {
        io(new ByteArrayInputStream(bytes), writer);
    }

    public static void io(final byte[] bytes, final OutputStream writer) throws IOException {
        io(new ByteArrayInputStream(bytes), writer);
    }

    public static void io(final String string, final Writer writer) throws IOException {
        io(new StringReader(string), writer);
    }

    public static void io(final String string, final OutputStream writer) throws IOException {
        io(new StringReader(string), writer);
    }

    //字节流
    public static void io(final InputStream is, final OutputStream os) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buf = new byte[2048];
        int len;
        while ((len = bis.read(buf)) != -1) {
            bos.write(buf, 0, len);
            bos.flush();
        }
        bis.close();
        bos.close();
    }

    public static void io(final InputStream is, final Writer os) throws IOException {
        io(new InputStreamReader(is), os);
    }

    public static void io(final Reader is, final OutputStream os) throws IOException {
        io(is, new OutputStreamWriter(os));
    }

    public static void io(final Reader is, final Writer os) throws IOException {
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
