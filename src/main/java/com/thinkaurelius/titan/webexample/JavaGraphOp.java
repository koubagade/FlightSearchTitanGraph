package com.thinkaurelius.titan.webexample;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class JavaGraphOp {
    // Autowired via setter. I leave this as a blueprints.Graph unless I have to do Titan specific stuff.
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
        JSONObject jsonObject;

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

        JSONObject jsonObject;

        while (itty1.hasNext()) {
            e = itty1.next();

            jsonObject = new JSONObject();

            jsonObject.put("fid",e.property("fid").value());
            jsonObject.put("fname",e.property("fname").value());
            jsonObject.put("arrTime",e.property("arrTime").value());
            jsonObject.put("depTime",e.property("depTime").value());
            jsonObject.put("arrDate",e.property("arrDate").value());
            jsonObject.put("depDate",e.property("depDate").value());
            jsonObject.put("company",e.property("company").value());
            jsonObject.put("source",e.property("source").value());
            jsonObject.put("destination",e.property("destination").value());

            flights.add(jsonObject);
        }
        return flights;
    }
}
