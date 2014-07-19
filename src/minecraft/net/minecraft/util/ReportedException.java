package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
    private static final String __OBFID = "CL_00001579";
    private final CrashReport theReportedExceptionCrashReport;

    public ReportedException(CrashReport p_i1356_1_) {
        this.theReportedExceptionCrashReport = p_i1356_1_;
    }

    public CrashReport getCrashReport() {
        return this.theReportedExceptionCrashReport;
    }

    public Throwable getCause() {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }

    public String getMessage() {
        return this.theReportedExceptionCrashReport.getDescription();
    }
}
