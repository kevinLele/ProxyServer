package com.hq.CloudPlatform.ProxyServer.utils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 10/16/2017.
 */
public class MyPrintStream extends PrintStream {
    /**
     * Creates a new print stream.  This stream will not flush automatically.
     *
     * @param out The output stream to which values and objects will be
     *            printed
     * @see PrintWriter#PrintWriter(OutputStream)
     */
    public MyPrintStream(OutputStream out) {
        super(out);
    }

    /**
     * Prints a string.  If the argument is <code>null</code> then the string
     * <code>"null"</code> is printed.  Otherwise, the string's characters are
     * converted into bytes according to the platform's default character
     * encoding, and these bytes are written in exactly the manner of the
     * <code>{@link #write(int)}</code> method.
     *
     * @param s The <code>String</code> to be printed
     */
    @Override
    public void print(String s) {
        String lineEnter = System.getProperty("line.separator");
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        StringBuffer sbf = new StringBuffer();
        sbf.append("StackTrace:").append(lineEnter);

        if (null != st && st.length > 3) {
            for (int i = 3; i < st.length; i++) {
                StackTraceElement e = st[i];

                if (i > 3) {
                    sbf.append(" <- ");
                    sbf.append(lineEnter);
                }

                sbf.append(java.text.MessageFormat.format("    {0}.{1}() {2}"
                        , e.getClassName()
                        , e.getMethodName()
                        , e.getLineNumber()));
            }
        }

        sbf.append(lineEnter);

        super.print(sbf.toString() + s);
    }
}
