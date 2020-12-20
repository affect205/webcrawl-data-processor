package org.alexside.webcrawl.modules.store.api.error;

public class WebcrawlStoreException extends RuntimeException {
    public WebcrawlStoreException(Throwable e) { super(e); }
    public WebcrawlStoreException(String msg) { super(msg); }
    public WebcrawlStoreException(String msg, Throwable e) { super(msg, e); }
}
