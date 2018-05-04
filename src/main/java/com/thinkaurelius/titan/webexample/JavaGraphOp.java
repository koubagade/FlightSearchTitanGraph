package com.thinkaurelius.titan.webexample;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    public List<String> getAllAirports() {

        List<String> airports = new ArrayList<>();
        List<Vertex> vList;
        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            for(Vertex v: vList){
                airports.add(v.property("airport_city").value().toString());
            }
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return airports;
    }

    public JSONArray getFlights(String source, String destination) {
        logger.info("Checkpoint 1 {}", source);
        logger.info("Checkpoint 1 {}", destination);

        JSONArray array = new JSONArray();
        JSONObject temp1;
        JSONObject temp2;
        JSONObject temp3;
        List<JSONObject> tempList1 = new ArrayList<>();
        List<JSONObject> tempList2 = new ArrayList<>();
        List<Vertex> vList;
        List<Edge> edgeList1;
        List<Edge> edgeList2;
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            logger.info("Number of vertices: {}", vList.size());
            int directFlightCount = 0;
            int connectedFlightCount = 0;
            for(Vertex v1: vList) {
                if (v1.property("airport_city").value().toString().equalsIgnoreCase(source)) {
                    edgeList1 = graph.V(v1).outE().toList();
                    for (Edge e1: edgeList1) {
                        String arrT1 = e1.property("arrival_time").value().toString();
                        String arrD1 = e1.property("arrival_date").value().toString();
                        String depT1 = e1.property("departure_time").value().toString();
                        String depD1 = e1.property("departure_date").value().toString();
                        Date arrDate1 = sdf.parse(arrD1 + " " + arrT1);
                        Date depDate1 = sdf.parse(depD1 + " " + depT1);
                        DecimalFormat df = new DecimalFormat("00");
                        if (e1.inVertex().property("airport_city").value().toString().equalsIgnoreCase(destination)) {
                            //tempList1 = new ArrayList<>();
                            temp1 = new JSONObject();
                            temp2 = new JSONObject();
                            temp1.put("fid", e1.property("flight_id").value().toString());
                            temp1.put("depTime", e1.property("departure_time").value().toString());
                            temp1.put("arrTime", e1.property("arrival_time").value().toString());
                            temp1.put("depDate", e1.property("departure_date").value().toString());
                            temp1.put("arrDate", e1.property("arrival_date").value().toString());
                            temp1.put("airline", e1.property("airline_name").value().toString());
                            long diff = arrDate1.getTime() - depDate1.getTime();
                            double diffInHours = diff / ((double) 1000 * 60 * 60);
                            String duration = df.format((int) diffInHours) + ":" + df.format((diffInHours - (int) diffInHours) * 60);
                            temp2.put("duration", duration);
                            temp2.put("stops", 0);
                            temp2.put("flight", temp1);
                            tempList1.add(temp2);
                            directFlightCount++;
                        }
                        String intermediateAirport = e1.inVertex().property("airport_city").value().toString();
                        if (!intermediateAirport.equalsIgnoreCase(destination)) {
                            edgeList2 = graph.V(e1.inVertex()).outE().toList();
                            for (Edge e2 : edgeList2) {
                                String arrT2 = e2.property("arrival_time").value().toString();
                                String arrD2 = e2.property("arrival_date").value().toString();
                                String depT2 = e2.property("departure_time").value().toString();
                                String depD2 = e2.property("departure_date").value().toString();
                                Date arrDate2 = sdf.parse(arrD2 + " " + arrT2);
                                Date depDate2 = sdf.parse(depD2 + " " + depT2);
                                if (e2.inVertex().property("airport_city").value().toString().equalsIgnoreCase(destination) && depDate2.after(arrDate1)) {
                                    //tempList2 = new ArrayList<>();
                                    temp1 = new JSONObject();
                                    temp2 = new JSONObject();
                                    temp3 = new JSONObject();
                                    temp1.put("fid", e1.property("flight_id").value().toString());
                                    temp1.put("source1", source);
                                    temp1.put("destination1", intermediateAirport);
                                    temp1.put("depTime", e1.property("departure_time").value().toString());
                                    temp1.put("arrTime", e1.property("arrival_time").value().toString());
                                    temp1.put("depDate", e1.property("departure_date").value().toString());
                                    temp1.put("arrDate", e1.property("arrival_date").value().toString());
                                    temp1.put("airline", e1.property("airline_name").value().toString());
                                    temp2.put("fid", e2.property("flight_id").value().toString());
                                    temp2.put("source2", intermediateAirport);
                                    temp2.put("destination2", destination);
                                    temp2.put("depTime", e2.property("departure_time").value().toString());
                                    temp2.put("arrTime", e2.property("arrival_time").value().toString());
                                    temp2.put("depDate", e2.property("departure_date").value().toString());
                                    temp2.put("arrDate", e2.property("arrival_date").value().toString());
                                    temp2.put("airline", e2.property("airline_name").value().toString());
                                    long diff = arrDate2.getTime() - depDate1.getTime();
                                    double diffInHours = diff / ((double) 1000 * 60 * 60);
                                    String duration = df.format((int) diffInHours) + ":" + df.format((diffInHours - (int) diffInHours) * 60);
                                    temp3.put("duration", duration);
                                    temp3.put("stops", 1);
                                    temp3.put("flight1", temp1);
                                    temp3.put("flight2", temp2);
                                    tempList2.add(temp3);
                                    connectedFlightCount++;
                                }
                            }
                        }
                    }
                }
            }

            Collections.sort(tempList1, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    String format = "HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    Date time1 = null;
                    Date time2 = null;
                    try {
                        time1 = sdf.parse(o1.get("duration").toString());
                        time2 = sdf.parse(o2.get("duration").toString());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return time1.compareTo(time2);
                }
            });
            for(int i = 0; i < 5; i++) {
                //sortedList.add(tempList1.get(i));
                array.add(tempList1.get(i));
            }
            Collections.sort(tempList2, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    String format = "HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    Date time1 = null;
                    Date time2 = null;
                    try {
                        time1 = sdf.parse(o1.get("duration").toString());
                        time2 = sdf.parse(o2.get("duration").toString());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return time1.compareTo(time2);
                }
            });
            for(int i = 0; i < 5; i++) {
                //sortedList.add(tempList2.get(i));
                array.add(tempList2.get(i));
            }
            int flightCount = directFlightCount + connectedFlightCount;
            System.out.println("Total flights retrieved = " + flightCount);
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return array;
    }

    public List<Float> getAvgEdges() {
        List<Vertex> vList;
        List<Float> avg = new ArrayList<>();
        try {
            GraphTraversalSource graph = g.traversal();
            vList = graph.V().toList();
            int vertices = vList.size();
            int inCount = 0;
            int outCount = 0;
            for(Vertex v: vList){
                List<Edge> in = graph.V(v).outE().toList();
                inCount += in.size();
                List<Edge> out = graph.V(v).inE().toList();
                outCount += out.size();
            }
            avg.add(0,(float) inCount/vertices);
            avg.add(1,(float) outCount/vertices);
        } catch (Exception e){
            logger.error(e.toString());
            e.printStackTrace();
        }
        return avg;
    }

}
