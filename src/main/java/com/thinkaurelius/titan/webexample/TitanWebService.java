package com.thinkaurelius.titan.webexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class handles the HTTP requests.
 */
@Path("/")
@Component
public class TitanWebService {
    Logger logger = LoggerFactory.getLogger(TitanWebService.class);

    @Autowired
    JavaGraphOp javaOp;

    @PostConstruct
    public void init() {
        System.out.println("Initialized Titan Web Example Service");
    }

    //only for checking if it working or not
    @GET
    @Path("/getAirport")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirport(@QueryParam(value = "airport") String airportName) {
        logger.info("Received airport name {}", airportName);
        String result = javaOp.getAllAirports().toString();
        System.out.println(result);
        return Response
            .status(200)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Credentials", "true")
            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .header("Access-Control-Max-Age", "1209600")
            .entity(result)
            .build();
    }

    @GET
    @Path("getFlights")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlights(@QueryParam("source") String source, @QueryParam("destination") String destination) {
        logger.info("Received source airport name {}", source);
        logger.info("Received destination airport name {}", destination);
        String result = javaOp.getFlights(source, destination).toString();
        //List<Float> list = javaOp.getAvgEdges();
        //System.out.println("inAvg = " + list.get(0) + "outAvg = " + list.get(1));
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .entity(result)
                .build();
    }
}
