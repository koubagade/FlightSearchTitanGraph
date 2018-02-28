package com.thinkaurelius.titan.webexample;

import com.sun.jndi.toolkit.url.Uri;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.Driver;

/**
 * This class handles the HTTP requests.
 */
@Path("/")
@Component
public class TitanWebService {

    @Autowired
    GroovyGraphOp groovyOp;

    @Autowired
    JavaGraphOp javaOp;

    @PostConstruct
    private void init() {
        System.out.println("Initialized Titan Web Example Service");
    }

    @GET
    @Path("/listOfEdgesJava")
    @Produces(MediaType.TEXT_PLAIN)
    public String listEdges(@Context UriInfo info) throws JSONException {
        String res = javaOp.listEdgesJava().toString();
        return "\"" + res + "\"";
    }

    @GET
    @Path("/listVertices")
    @Produces(MediaType.TEXT_PLAIN)
    public String listVertices(@Context UriInfo info) throws JSONException {
        String res = javaOp.listVertices().toString();
        return "\"" + res + "\"";
    }
    
}
