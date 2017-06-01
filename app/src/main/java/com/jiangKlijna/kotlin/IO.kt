package com.jiangKlijna.kotlin

import java.io.*

/**
 * io流工具类
 * Author: com.jiangKlijna
 */
object IO {

    private const val BYTE_ARRAY_LENGTH = 2048

    //文件相关
    @Throws(IOException::class)
    fun filec(fsrc: File, descf: File) {
        chars(FileReader(fsrc), FileWriter(descf))
    }

    @Throws(IOException::class)
    fun fileb(fsrc: File, descf: File) {
        bytes(FileInputStream(fsrc), FileOutputStream(descf))
    }

    @Throws(IOException::class)
    fun filec(src: String, descf: File) {
        str(src, FileWriter(descf))
    }

    @Throws(IOException::class)
    fun fileb(src: String, descf: File) {
        str(src, FileOutputStream(descf))
    }

    @Throws(IOException::class)
    fun filec(bytes: ByteArray, descf: File) {
        byt(bytes, FileWriter(descf))
    }

    @Throws(IOException::class)
    fun fileb(bytes: ByteArray, descf: File) {
        byt(bytes, FileOutputStream(descf))
    }

    @Throws(IOException::class)
    fun filec(descf: File): String {
        return str(FileReader(descf))
    }

    @Throws(IOException::class)
    fun fileb(descf: File): ByteArray {
        return byt(FileInputStream(descf))
    }

    //序列化对象
    @Throws(IOException::class)
    fun obj(obj: Any): ByteArray {
        val bo = ByteArrayOutputStream()
        val oo = ObjectOutputStream(bo)
        oo.writeObject(obj)
        val bytes = bo.toByteArray()
        bo.close()
        oo.close()
        return bytes
    }

    @Throws(Exception::class)
    fun <T> obj(bytes: ByteArray): T {
        val sin = ObjectInputStream(ByteArrayInputStream(bytes))
        return sin.readObject() as T
    }
    
    //字符串相关
    @Throws(IOException::class)
    fun str(string: String, writer: Writer) {
        chars(StringReader(string), writer)
    }

    @Throws(IOException::class)
    fun str(string: String, writer: OutputStream) {
        chars_bytes(StringReader(string), writer)
    }

    @Throws(IOException::class)
    fun str(reader: Reader): String {
        val stringWriter = StringWriter()
        chars(reader, stringWriter)
        return stringWriter.toString()
    }

    @Throws(IOException::class)
    fun str(reader: InputStream): String {
        val stringWriter = StringWriter()
        bytes_chars(reader, stringWriter)
        return stringWriter.toString()
    }

    //字节数组相关
    @Throws(IOException::class)
    fun byt(bytes: ByteArray, writer: Writer) {
        bytes_chars(ByteArrayInputStream(bytes), writer)
    }

    @Throws(IOException::class)
    fun byt(bytes: ByteArray, writer: OutputStream) {
        bytes(ByteArrayInputStream(bytes), writer)
    }

    @Throws(IOException::class)
    fun byt(reader: InputStream): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bytes(reader, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    @Throws(IOException::class)
    fun byt(reader: Reader): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        chars_bytes(reader, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    //字节流<->字符流
    @Throws(IOException::class)
    fun bytes_chars(`is`: InputStream, os: Writer) {
        chars(InputStreamReader(`is`), os)
    }

    @Throws(IOException::class)
    fun chars_bytes(`is`: Reader, os: OutputStream) {
        chars(`is`, OutputStreamWriter(os))
    }

    //字节流
    @Throws(IOException::class)
    fun bytes(`is`: InputStream, os: OutputStream) {
        val bis = BufferedInputStream(`is`)
        val bos = BufferedOutputStream(os)
        val buf = ByteArray(BYTE_ARRAY_LENGTH)
        var len: Int
        while (true) {
            len = bis.read()
            if (len == -1) break
            else {
                bos.write(buf, 0, len)
                bos.flush()
            }
        }
        bis.close()
        bos.close()
    }

    //字符流
    @Throws(IOException::class)
    fun chars(`is`: Reader, os: Writer) {
        val bufr = BufferedReader(`is`)
        val bufw = BufferedWriter(os)
        var line: String
        while (true) {
            line = bufr.readLine() ?: break
            bufw.write(line)
            bufw.newLine()
            bufw.flush()
        }
        bufr.close()
        bufw.close()
    }

}
