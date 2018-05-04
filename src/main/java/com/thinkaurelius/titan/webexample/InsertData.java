package com.thinkaurelius.titan.webexample;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class InsertData {
    public static void load(TitanGraph graph) throws Exception {
        TitanManagement management = graph.openManagement();

        management.makeEdgeLabel("flight");
        management.makeVertexLabel("airport");

        management.commit();
        TitanTransaction transaction = graph.newTransaction();

        BufferedReader br = new BufferedReader(new FileReader("/home/supriya/BEProject/Data/100000Flights.csv"));
        String line;
        List<String> vertexList = new ArrayList<>();
        HashMap<String, Vertex> hm = new HashMap<>();
        String date = null;

        while((line = br.readLine()) != null) {
            List<String> list = Arrays.asList(line.split(","));
            if(!vertexList.contains(list.get(6))) {
                vertexList.add(list.get(6));
                date = list.get(2) + "-" + list.get(1) + "-" + list.get(0);
                hm.put(list.get(6), transaction.addVertex(T.label, "airport", "airport_city", list.get(6)));
            }
            if (!vertexList.contains(list.get(7))) {
                vertexList.add(list.get(7));
                hm.put(list.get(7), transaction.addVertex(T.label, "airport", "airport_city", list.get(7)));
            }
            hm.get(list.get(6)).addEdge("flight", hm.get(list.get(7)), "flight_id", list.get(5), "departure_time", list.get(3), "arrival_time", list.get(4), "departure_date", date, "arrival_date", date);
        }
        //System.out.println("Number of vertices : "+vertexList.size());
        //System.out.println("Vertex List : "+vertexList);
        transaction.commit();
    }
}