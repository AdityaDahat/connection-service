package com.connection.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionStackTrace {

    private ExceptionStackTrace() {
        // This class has only one static method. Meant for printing the stack trace of
        // exception to logs.
    }

    public static String getStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
