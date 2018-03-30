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
import java.util.Date;
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

    //only for checking if it working or not
    @GET
    @Path("/getAirport")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAirport(@QueryParam(value = "airport") String airportName) {
        logger.info("Received airport name {}", airportName);
        HashMap<String, String> result = javaOp.getAirport(airportName);
        logger.info("Returning airport properties {}", result.toString());
        return "\"" + result.toString() + "\"";
    }

    //get single flight only for source and destination
    @GET
    @Path("getFlights")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFlights(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received destination airport name {}", destination);
        JSONObject result = javaOp.getFlights(source, destination);
        return "\"" + result + "\"";
    }

    //get multiple direct flights only for source and destination
    @GET
    @Path("getMultipleDirectFlights")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMultipleDirectFlights(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received destination airport name {}", destination);
        List result = javaOp.getMultipleDirectFlights(source, destination);
        if (result.isEmpty()){
            return "Not Found";
        }
        else{
            return "\"" + result + "\"";
        }
    }

    //get multiple flights according to source, destination, depDate, retDate
    @GET
    @Path("getFlightsWithDates")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFlightsWithDates(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination, @QueryParam(value = "depDate") String depDate, @QueryParam(value="retDate") String retDate) {
        logger.info("Received source airport name {}", source);
        logger.info("Received destination airport name {}", destination);
        logger.info("Received depart date {}", depDate);
        logger.info("Received return date {}", retDate);
        List result = javaOp.getFlightsWithDates(source, destination, depDate, retDate);
        if (result.isEmpty()){
            return "Not Found";
        }
        else{
            return "\"" + result + "\"";
        }
    }

    //get flights having single stop
    @GET
    @Path("getFlightsHavingSingleStop")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFlightsHavingSingleStop(@QueryParam(value = "source") String source, @QueryParam(value = "stop") String stop, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received stop airport name {}", stop);
        logger.info("Received destination airport name {}", destination);

        List result = javaOp.getFlightsHavingSingleStop(source, stop, destination);
        if (result.isEmpty()){
            return "Not Found";
        }
        else{
            return "\"" + result + "\"";
        }
    }

    //get connected flights only for source and destination
    @GET
    @Path("getConnectedFlights")
    @Produces(MediaType.TEXT_PLAIN)
    public String getConnectedFlights(@QueryParam(value = "source") String source, @QueryParam(value = "destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received destination airport name {}", destination);
        List result = javaOp.getConnectedFlights(source, destination);
        if (result.isEmpty()){
            return "Not Found";
        }
        else{
            return "\"" + result + "\"";
        }
    }
}
