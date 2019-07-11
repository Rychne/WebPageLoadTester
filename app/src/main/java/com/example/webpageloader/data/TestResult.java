package com.example.webpageloader.data;

public class TestResult {

    private String webPageName;
    private long start;
    private long end;
    private long duration;
    private long bodySize;
    private int statusCode;
    private String statusMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getWebPageName() {
        return webPageName;
    }

    public void setWebPageName(String webPageName) {
        this.webPageName = webPageName;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getBodySize() {
        return bodySize;
    }

    public void setBodySize(long bodySize) {
        this.bodySize = bodySize;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }


    @Override
    public String toString() {
        return "TestResult{" +
                "webPageName='" + webPageName + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", duration=" + duration +
                ", bodySize=" + bodySize +
                ", statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
