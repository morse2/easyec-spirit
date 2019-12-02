package com.googlecode.easyec.spirit.web.medias;

import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MediaResolver {

    void populate(HttpServletRequest request, HttpServletResponse response, Resource resource) throws IOException;
}
