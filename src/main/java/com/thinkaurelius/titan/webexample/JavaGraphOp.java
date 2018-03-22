package com.thinkaurelius.titan.webexample;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JavaGraphOp {
    // Autowired via setter. I leave this as a blueprints.Graph unless I have to do Titan specific stuff.
    Logger logger = LoggerFactory.getLogger(JavaGraphOp.class);

    private Graph g;

    @Autowired
    // I like to autowire via setters because it makes it easy to write spring-free unit tests.
    public void setGraph(TitanGraphFactory gf) {
        this.g = gf.getGraph();
    }

    public List<JSONObject> listVertices() {
        List<JSONObject> airports = new ArrayList<JSONObject>();
        Iterator<Vertex> itty = g.vertices();
        Vertex v;
        JSONObject jsonObject = new JSONObject();

        while (itty.hasNext()) {
            v = itty.next();

            jsonObject = new JSONObject();

            jsonObject.put("id",v.property("id").value().toString());
            jsonObject.put("name",v.property("name").value().toString());
            jsonObject.put("city",v.property("city").value().toString());
            jsonObject.put("state",v.property("state").value().toString());
            jsonObject.put("country",v.property("country").value().toString());
            jsonObject.put("place",v.property("place").value().toString());

            airports.add(jsonObject);

        }
        return airports;
    }

    public List<JSONObject> listEdges() {
        List<JSONObject> flights = new ArrayList<JSONObject>();
        Iterator<Edge> itty1 = g.edges();
        Edge e;

        JSONObject jsonObject = new JSONObject();

        while (itty1.hasNext()) {
            e = itty1.next();

            jsonObject = new JSONObject();

            jsonObject.put("fid",e.property("fid").value().toString());
            jsonObject.put("fname",e.property("fname").value().toString());
            jsonObject.put("depTime",e.property("depTime").value().toString());
            jsonObject.put("arrTime",e.property("arrTime").value().toString());
            jsonObject.put("depDate",e.property("depDate").value().toString());
            jsonObject.put("retDate",e.property("retDate").value().toString());
            jsonObject.put("company",e.property("company").value().toString());
            jsonObject.put("source",e.property("source").value().toString());
            jsonObject.put("destination",e.property("destination").value().toString());

            flights.add(jsonObject);
        }
        return flights;
    }

    public JSONObject getAirport(String sourceAirport) {

        JSONObject airportProperties = new JSONObject();
        logger.info("Checkpoint 1 {}", sourceAirport);
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex e: vList){
                logger.info("{} -  {}", e.property("id"), e.property("name"));
                if (e.property("name").value().equals(sourceAirport)){
                    airportProperties.put("id",e.property("id").value().toString());
                    airportProperties.put("name",e.property("name").value().toString());
                    airportProperties.put("city",e.property("city").value().toString());
                    airportProperties.put("state",e.property("state").value().toString());
                    airportProperties.put("country",e.property("country").value().toString());
                    airportProperties.put("place",e.property("place").value().toString());
                    break;
                }

            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }

        logger.info("Checkpoint 2");
        return airportProperties;
    }

    public JSONObject getFlights(String source, String destination) {
        logger.info("Checkpoint 1 {}", source);
        logger.info("Checkpoint 1 {}", destination);

        JSONObject availableFlights = new JSONObject();
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        List<Edge> edgeList = null;

        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex v1: vList){
                if (v1.property("name").value().equals(source)){
                    edgeList = graph.V(v1).outE().toList();
                    for (Edge e: edgeList) {
                        logger.info("{} -  {}", e.property("fid"), e.property("fname"));
                        if (e.inVertex().property("name").value().equals(destination)) {
                            availableFlights.put("fid",e.property("fid").value().toString());
                            availableFlights.put("fname",e.property("fname").value().toString());
                            availableFlights.put("depTime",e.property("depTime").value().toString());
                            availableFlights.put("arrTime",e.property("arrTime").value().toString());
                            availableFlights.put("depDate",e.property("depDate").value().toString());
                            availableFlights.put("retDate",e.property("retDate").value().toString());
                            availableFlights.put("company",e.property("company").value().toString());
                        }
                    }
                    break;
                }

            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return availableFlights;
    }

    public List<JSONObject> getMultipleDirectFlights(String source, String destination) {
        logger.info("Checkpoint 1 {}", source);
        logger.info("Checkpoint 1 {}", destination);

        List<JSONObject> availableFlights = new ArrayList<JSONObject>();
        JSONObject temp;
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        List<Edge> edgeList = null;

        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex v1: vList){
                if (v1.property("name").value().equals(source)){
                    edgeList = graph.V(v1).outE().toList();
                    for (Edge e: edgeList) {
                        logger.info("{} -  {}", e.property("fid"), e.property("fname"));
                        if (e.inVertex().property("name").value().equals(destination)) {

                            temp = new JSONObject();
                            temp.put("fid",e.property("fid").value().toString());
                            temp.put("fname",e.property("fname").value().toString());
                            temp.put("depTime",e.property("depTime").value().toString());
                            temp.put("arrTime",e.property("arrTime").value().toString());
                            temp.put("depDate",e.property("depDate").value().toString());
                            temp.put("retDate",e.property("retDate").value().toString());
                            temp.put("company",e.property("company").value().toString());
                            availableFlights.add(temp);
                        }
                    }

                    break;
                }
            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return availableFlights;
    }

    public List<JSONObject> getFlightsWithDates(String source, String destination, String depDate, String retDate) {
        logger.info("Checkpoint 1 {}", source);
        logger.info("Checkpoint 1 {}", destination);
        logger.info("Checkpoint 1 {}", depDate);

        List<JSONObject> availableFlights = new ArrayList<JSONObject>();
        JSONObject temp;
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        List<Edge> edgeList = null;

        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex v1: vList){
                if (v1.property("name").value().equals(source)){
                    edgeList = graph.V(v1).outE().toList();
                    for (Edge e: edgeList) {
                        logger.info("{} -  {}", e.property("fid"), e.property("fname"));
                        if (e.inVertex().property("name").value().equals(destination)) {
                            logger.info("Database depDate {}", e.property("depDate"));
                            logger.info("Database retDate {}", e.property("retDate"));
                            if(e.property("depDate").value().equals(depDate) && e.property("retDate").value().equals(retDate)) {
                                temp = new JSONObject();
                                temp.put("fid",e.property("fid").value().toString());
                                temp.put("fname",e.property("fname").value().toString());
                                temp.put("depTime",e.property("depTime").value().toString());
                                temp.put("arrTime",e.property("arrTime").value().toString());
                                temp.put("company",e.property("company").value().toString());
                                availableFlights.add(temp);
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return availableFlights;
    }

    public List<JSONObject> getFlightsHavingSingleStop(String source, String stop,String destination) {
        logger.info("Checkpoint 1 {}", source);
        logger.info("Checkpoint 1 {}", stop);
        logger.info("Checkpoint 1 {}", destination);

        List<JSONObject> availableFlights = new ArrayList<JSONObject>();
        JSONObject temp1,temp2;
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        List<Edge> edgeList1 = null;
        List<Edge> edgeList2 = null;

        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex v1: vList){
                if (v1.property("name").value().equals(source)){
                    edgeList1 = graph.V(v1).outE().toList();
                    for (Edge e: edgeList1) {
                        if (e.inVertex().property("name").value().equals(stop)) {
                            temp1 = new JSONObject();
                            temp1.put("fid", e.property("fid").value().toString());
                            temp1.put("fname", e.property("fname").value().toString());
                            temp1.put("depTime", e.property("depTime").value().toString());
                            temp1.put("arrTime", e.property("arrTime").value().toString());
                            temp1.put("depDate", e.property("depDate").value().toString());
                            temp1.put("retDate", e.property("retDate").value().toString());
                            temp1.put("company", e.property("company").value().toString());
                            availableFlights.add(temp1);
                            for (Vertex v2: vList){
                                if (v2.property("name").value().equals(stop)){
                                    edgeList2 = graph.V(v2).outE().toList();
                                    for (Edge edge: edgeList2){
                                        if (edge.inVertex().property("name").value().equals(destination)){
                                            temp2 = new JSONObject();
                                            temp2.put("fid", edge.property("fid").value().toString());
                                            temp2.put("fname", edge.property("fname").value().toString());
                                            temp2.put("depTime", edge.property("depTime").value().toString());
                                            temp2.put("arrTime", edge.property("arrTime").value().toString());
                                            temp2.put("depDate", edge.property("depDate").value().toString());
                                            temp2.put("retDate", edge.property("retDate").value().toString());
                                            temp2.put("company", edge.property("company").value().toString());

                                            availableFlights.add(temp2);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return availableFlights;
    }
}
