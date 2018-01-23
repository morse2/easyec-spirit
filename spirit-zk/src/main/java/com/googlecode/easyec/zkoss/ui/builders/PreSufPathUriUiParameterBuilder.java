package com.googlecode.easyec.zkoss.ui.builders;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PreSufPathUriUiParameterBuilder extends UriUiParameterBuilder {

    private String prefix;
    private String suffix;

    protected PreSufPathUriUiParameterBuilder() { }

    public static PreSufPathUriUiParameterBuilder create() {
        return new PreSufPathUriUiParameterBuilder();
    }

    public PreSufPathUriUiParameterBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public PreSufPathUriUiParameterBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public PreSufPathUriUiParameterBuilder setUri(String uri) {
        if (isNotBlank(uri)) {
            if (uri.startsWith("~./")
                || isBlank(prefix)
                || isBlank(suffix)
                || uri.startsWith(prefix)) {
                super.setUri(uri);
            } else {
                super.setUri(mergeUri(uri, prefix, suffix));
            }
        }

        return this;
    }

    protected String mergeUri(String uri, String prefix, String suffix) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        return new StringBuffer()
            .append(prefix)
            .append(uri)
            .append(suffix)
            .toString();
    }
}
