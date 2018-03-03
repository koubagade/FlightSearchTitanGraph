package com.thinkaurelius.titan.webexample;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public List<HashMap<String, String>> listVertices() {
        List<HashMap<String, String>> airports = new ArrayList<HashMap<String, String>>();
        Iterator<Vertex> itty = g.vertices();
        Vertex v;
        HashMap<String, String> jsonObject = new HashMap<String, String>();

        while (itty.hasNext()) {
            v = itty.next();

            jsonObject.clear();

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

    public List<HashMap<String, String>> listEdges() {
        List<HashMap<String, String>> flights = new ArrayList<HashMap<String, String>>();
        Iterator<Edge> itty1 = g.edges();
        Edge e;

        HashMap<String, String> jsonObject = new HashMap<String, String>();

        while (itty1.hasNext()) {
            e = itty1.next();

            jsonObject.clear();

            jsonObject.put("fid",e.property("fid").value().toString());
            jsonObject.put("fname",e.property("fname").value().toString());
            jsonObject.put("arrTime",e.property("arrTime").value().toString());
            jsonObject.put("depTime",e.property("depTime").value().toString());
            jsonObject.put("arrDate",e.property("arrDate").value().toString());
            jsonObject.put("depDate",e.property("depDate").value().toString());
            jsonObject.put("company",e.property("company").value().toString());
            jsonObject.put("source",e.property("source").value().toString());
            jsonObject.put("destination",e.property("destination").value().toString());

            flights.add(jsonObject);
        }
        return flights;
    }

    public HashMap<String, String> getAirport(String airportName) {

        HashMap<String, String> airportProperties = new HashMap<>();
        logger.info("Checkpoint 1 {}", airportName);
        List<Vertex> vList = new ArrayList<Vertex> ();
        Vertex v = null;
        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex e: vList){
                logger.info("{} -  {}", e.property("id"), e.property("name"));
                if (e.property("name").value().equals(airportName)){
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
}
