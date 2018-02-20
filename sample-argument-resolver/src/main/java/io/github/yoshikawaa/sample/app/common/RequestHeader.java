package io.github.yoshikawaa.sample.app.common;

import io.github.yoshikawaa.sample.app.common.constraint.SupportedBrowser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestHeader {
    @SupportedBrowser("FireFox")
    private String userAgent;
    private String accept;
    private String acceptLanguage;
    private String acceptEncoding;
}
