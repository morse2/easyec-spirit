package com.googlecode.easyec.spirit.web.medias;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.DigestUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

public class DefaultMediaResolver implements MediaResolver {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void populate(HttpServletRequest request, HttpServletResponse response, AbstractFileResolvingResource resource) throws IOException {
        String eTag = createETag(resource);
        String mediaType = guessMediaType(resource);
        logger.debug("Resource: [{}] with etag: [{}], media type: [{}].", resource.getURL().toString(), eTag, mediaType);

        response.setDateHeader("Last-Modified", resource.lastModified());
        response.setHeader("ETag", eTag);
        response.setContentType(mediaType);

        String range = request.getHeader("range");
        if (isNotBlank(range)) {
            writeRangeData(response, resource, range);
        } else writeAllData(response, resource);
    }

    protected String createETag(AbstractFileResolvingResource resource) throws IOException {
        MediaEntity ent = new MediaEntity();
        ent.setLastModified(resource.lastModified());
        ent.setPath(resource.getURI().getPath());
        ent.setLength(resource.contentLength());
        ent.setName(resource.getFilename());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        getObjectMapper().writeValue(bos, ent);

        return DigestUtils.md5DigestAsHex(bos.toByteArray());
    }

    protected String guessMediaType(AbstractFileResolvingResource resource) {
        return MediaTypeFactory
            .getMediaType(resource)
            .orElse(APPLICATION_OCTET_STREAM)
            .toString();
    }

    protected void writeAllData(HttpServletResponse response, AbstractFileResolvingResource resource) throws IOException {
        response.setContentLengthLong(resource.contentLength());
        ServletOutputStream out = response.getOutputStream();
        InputStream stream = resource.getInputStream();
        IOUtils.copyLarge(stream, out);
        response.setStatus(200);  // ok
    }

    protected void writeRangeData(HttpServletResponse response, AbstractFileResolvingResource resource, String range) throws IOException {
        logger.debug("Range: [{}].", range);

        if (equalsIgnoreCase("bytes=0-", range)) {
            writeAllData(response, resource);

            return;
        }

        String[] parts = replaceIgnoreCase(range, "bytes=", "").split("-", 2);
        long length = resource.contentLength();
        long start = Integer.parseInt(parts[0]);
        long end;

        try {
            end = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            end = length - 1;
        }

        response.setContentLengthLong(end - start + 1);
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
        response.setHeader("Accept-Ranges", "bytes");

        InputStream stream = resource.getInputStream();
        ServletOutputStream out = response.getOutputStream();
        long l = IOUtils.copyLarge(stream, out, start, end - start + 1);
        logger.debug("Length of copy data. [{}], range: [{}].", l, range);

        response.setStatus(206);  // partial content
    }
}
