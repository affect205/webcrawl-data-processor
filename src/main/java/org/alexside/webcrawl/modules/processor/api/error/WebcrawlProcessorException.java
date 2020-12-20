package org.alexside.webcrawl.modules.processor.api.error;

public class WebcrawlProcessorException extends RuntimeException {
    public WebcrawlProcessorException(Throwable e) { super(e); }
    public WebcrawlProcessorException(String msg) { super(msg); }
    public WebcrawlProcessorException(String msg, Throwable e) { super(msg, e); }
}