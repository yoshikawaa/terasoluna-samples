package io.github.yoshikawaa.sample.app.common;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

public class RequestHeaderModelAttributeMethodProcessor extends ModelAttributeMethodProcessor {

    public RequestHeaderModelAttributeMethodProcessor() {
        super(true);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RequestHeader.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {

        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue("userAgent", request.getHeader("user-agent"));
        pvs.addPropertyValue("accept", request.getHeader("accept"));
        pvs.addPropertyValue("acceptLanguage", request.getHeader("accept-language"));
        pvs.addPropertyValue("acceptEncoding", request.getHeader("accept-encoding"));
        binder.bind(pvs);
    }

}
