package com.thinkaurelius.titan.webexample;

import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;

/**
 * This class handles the HTTP requests.
 */
@Path("/")
@Component
public class TitanWebService {
    Logger logger = LoggerFactory.getLogger(TitanWebService.class);

    @Autowired
    GroovyGraphOp groovyOp;

    @Autowired
    JavaGraphOp javaOp;

    @PostConstruct
    private void init() {
        System.out.println("Initialized Titan Web Example Service");
    }

    @GET
    @Path("/listEdges")
    @Produces(MediaType.TEXT_PLAIN)
    public String listEdges(@Context UriInfo info) throws JSONException {
        String res = javaOp.listEdges().toString();
        return "\"" + res + "\"";
    }

    @GET
    @Path("/listVertices")
    @Produces(MediaType.TEXT_PLAIN)
    public String listVertices(@Context UriInfo info) throws JSONException {
        String res = javaOp.listVertices().toString();
        return "\"" + res + "\"";
    }

    @GET
    @Path("/getAirport")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAirport(@QueryParam(value = "airport") String airportName) {
        logger.info("Received airport name {}", airportName);
        HashMap<String, String> result = javaOp.getAirport(airportName);
        logger.info("Returning airport properties {}", result.toString());
        return "\"" + result.toString() + "\"";
    }

    @GET
    @Path("getFlights")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFlights(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received source airport name {}", destination);
        JSONObject result = javaOp.getFlights(source, destination);
        return "\"" + result + "\"";
    }

    @GET
    @Path("getAllFlights")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllFlights(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received source airport name {}", destination);
        List result = javaOp.getMultipleDirectFlights(source, destination);
        return "\"" + result + "\"";
    }
}
