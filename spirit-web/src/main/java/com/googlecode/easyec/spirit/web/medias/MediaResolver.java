package com.googlecode.easyec.spirit.web.medias;

import org.springframework.core.io.AbstractFileResolvingResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MediaResolver {

    void populate(HttpServletRequest request, HttpServletResponse response, AbstractFileResolvingResource resource) throws IOException;
}
