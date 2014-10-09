package com.nortal.rest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Controller
@RequestMapping("/user")
public class ThumbnailResource {

    private static final Logger LOG = Logger.getLogger(ThumbnailResource.class);

    @RequestMapping(method = RequestMethod.GET, value = "/{username}", produces = "image/*")
    public String getUserThumbnail(@PathVariable String username){
        return "/assets/astro.png";
        /*try {
            //default pic
            return downloadImgFromProtectedUrl(new URL(//mingi väline url?));
        } catch (IOException e1) {
            return null;
        }*/
    }

    private byte[] downloadImgFromProtectedUrl(URL toDownload) throws IOException {
        return downloadToOutputStream(toDownload).toByteArray();
    }

    private ByteArrayOutputStream downloadToOutputStream(URL toDownload) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] chunk = new byte[4096];
        int bytesRead;
        InputStream stream = toDownload.openStream();

        while ((bytesRead = stream.read(chunk)) > 0) {
            outputStream.write(chunk, 0, bytesRead);
        }
        return outputStream;
    }

}
