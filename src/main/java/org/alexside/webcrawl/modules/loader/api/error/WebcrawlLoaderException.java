package org.alexside.webcrawl.modules.loader.api.error;

public class WebcrawlLoaderException extends RuntimeException {
    public WebcrawlLoaderException(Throwable e) { super(e); }
    public WebcrawlLoaderException(String msg) { super(msg); }
    public WebcrawlLoaderException(String msg, Throwable e) { super(msg, e); }
}
