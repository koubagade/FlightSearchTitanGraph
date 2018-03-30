package com.thinkaurelius.titan.webexample;

import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.elasticsearch.common.joda.time.LocalDate;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;

public class AirportsAndFlights {

    public static void load(TitanGraph graph) {

        //create schema
        TitanManagement mgmt = graph.openManagement();
        final PropertyKey id = mgmt.makePropertyKey("id").dataType(Integer.class).make();
        final PropertyKey name = mgmt.makePropertyKey("name").dataType(String.class).make();
        final PropertyKey city = mgmt.makePropertyKey("city").dataType(String.class).make();
        final PropertyKey state = mgmt.makePropertyKey("state").dataType(String.class).make();
        final PropertyKey country = mgmt.makePropertyKey("country").dataType(String.class).make();

        final PropertyKey fid = mgmt.makePropertyKey("fid").dataType(Integer.class).make();
        final PropertyKey fname = mgmt.makePropertyKey("fname").dataType(String.class).make();
        final PropertyKey place = mgmt.makePropertyKey("place").dataType(Geoshape.class).make();
        final PropertyKey depTime = mgmt.makePropertyKey("depTime").dataType(String.class).make();
        final PropertyKey arrTime = mgmt.makePropertyKey("arrTime").dataType(String.class).make();
        final PropertyKey depDate = mgmt.makePropertyKey("depDate").dataType(String.class).make();
        final PropertyKey arrDate = mgmt.makePropertyKey("arrDate").dataType(String.class).make();
        final PropertyKey company = mgmt.makePropertyKey("company").dataType(String.class).make();
        final PropertyKey source = mgmt.makePropertyKey("source").dataType(String.class).make();
        final PropertyKey destination = mgmt.makePropertyKey("destination").dataType(String.class).make();

        mgmt.makeEdgeLabel("flight").make();

        mgmt.makeVertexLabel("airport").make();

        mgmt.commit();
        TitanTransaction tx = graph.newTransaction();

        //vertex
        Vertex mumbai = tx.addVertex(T.label,"airport","id","1","name","MMB","city","Mumbai","state","Maharashtra","country","India","place",Geoshape.point(19.0760, 72.8777));
        Vertex pune = tx.addVertex(T.label,"airport","id","2","name","PNQ","city","Pune","state","Maharashtra","country","India","place",Geoshape.point(18.5204, 73.8567));
        Vertex delhi = tx.addVertex(T.label,"airport","id","3","name","DEL","city","Delhi","state","Maharashtra","country","India","place",Geoshape.point(28.7041, 77.1025));
        Vertex chennai = tx.addVertex(T.label,"airport","id","4","name","CENN","city","Chennai","state","Tamil Nadu","country","India","place",Geoshape.point(13.0827, 80.2707));
        Vertex bangalore = tx.addVertex(T.label,"airport","id","5","name","BNG","city","Bangalore","state","Karnataka","country","India","place",Geoshape.point(12.9716, 77.5946));
        Vertex hyderabad = tx.addVertex(T.label,"airport","id","2","name","HYDR","city","Hyderabad","state","Telangana","country","India","place",Geoshape.point(17.3850, 78.4867));

        //edges
        mumbai.addEdge("flight",pune,"fid","01","fname","MtoP","depTime", Time.valueOf("10:30:00"),"arrTime",Time.valueOf("11:00:40"),"depDate","2018-02-12","arrDate","2018-02-12","company","Air India","source","Mumbai","destination","Pune");
        mumbai.addEdge("flight",delhi,"fid","02","fname","MtoD","depTime",Time.valueOf("23:00:15"),"arrTime",Time.valueOf("01:30:30"),"depDate","2018-02-20","arrDate","2018-02-21","company","Jet Airways","source","Mumbai","destination","Delhi");
        mumbai.addEdge("flight",chennai,"fid","03","fname","MtoC","depTime",Time.valueOf("11:00:00"),"arrTime",Time.valueOf("14:00:45"),"depDate","2018-03-10","arrDate","2018-03-10","company","SpiceJet","source","Mumbai","destination","Chennai");
        mumbai.addEdge("flight",bangalore,"fid","04","fname","MtoB","depTime",Time.valueOf("22:00:52"),"arrTime",Time.valueOf("01:15:41"),"depDate","2018-02-25","arrDate","2018-02-26","company","GoAir","source","Mumbai","destination","Bangalore");
        mumbai.addEdge("flight",hyderabad,"fid","05","fname","MtoH","depTime",Time.valueOf("15:15:25"),"arrTime",Time.valueOf("18:00:23"),"depDate","2018-02-27","arrDate","2018-02-27","company","Air India","source","Mumbai","destination","Hyderabad");

        pune.addEdge("flight",delhi,"fid","08","fname","PtoD","depTime",Time.valueOf("06:50:36"),"arrTime",Time.valueOf("10:00:20"),"depDate","2018-02-15","arrDate","2018-02-15","company","Air India","source","Pune","destination","Delhi");
        delhi.addEdge("flight",bangalore,"fid","07","fname","DtoB","depTime",Time.valueOf("19:00:52"),"arrTime",Time.valueOf("12:15:00"),"depDate","2018-02-20","arrDate","2018-02-21","company","Jet Airways","source","Delhi","destination","Bangalore");
        hyderabad.addEdge("flight",chennai,"fid","06","fname","HtoC","depTime",Time.valueOf("08:15:45"),"arrTime",Time.valueOf("10:00:42"),"depDate","2018-02-23","arrDate","2018-02-23","company","GoAir","source","Hyderabad","destination","Chennai");

        pune.addEdge("flight",mumbai,"fid","011","fname","PtoM","depTime",Time.valueOf("15:00:35"),"arrTime",Time.valueOf("17:00:00"),"depDate","2018-02-15","arrDate","2018-02-15","company","Air India","source","Pune","destination","Mumbai");
        delhi.addEdge("flight",mumbai,"fid","012","fname","DtoM","depTime",Time.valueOf("12:15:45"),"arrTime",Time.valueOf("15:00:15"),"depDate","2018-02-20","arrDate","2018-02-20","company","Jet Airways","source","Delhi","destination","Mumbai");
        chennai.addEdge("flight",mumbai,"fid","013","fname","CtoM","depTime",Time.valueOf("13:20:55"),"arrTime",Time.valueOf("17:00:25"),"depDate","2018-02-25","arrDate","2018-02-25","company","SpiceJet","source","Chennai","destination","Mumbai");
        bangalore.addEdge("flight",mumbai,"fid","014","fname","BtoM","depTime",Time.valueOf("05:00:00"),"arrTime",Time.valueOf("07:15:35"),"depDate","2018-02-28","arrDate","2018-02-28","company","GoAir","source","Bangalore","destination","Mumbai");
        hyderabad.addEdge("flight",mumbai,"fid","015","fname","HtoM","depTime",Time.valueOf("06:15:15"),"arrTime",Time.valueOf("08:30:30"),"depDate","2018-03-20","arrDate","2018-03-20","company","Air India","source","Hyderabad","destination","Mumbai");

        delhi.addEdge("flight",pune,"fid","016","fname","DtoP","depTime",Time.valueOf("22:00:00"),"arrTime",Time.valueOf("00:30:00"),"depDate","2018-04-15","arrDate","2018-04-15","company","Air India","source","Delhi","destination","Pune");
        bangalore.addEdge("flight",delhi,"fid","017","fname","BtoD","depTime",Time.valueOf("20:30:25"),"arrTime",Time.valueOf("23:30:45"),"depDate","2018-05-17","arrDate","2018-05-17","company","Jet Airways","source","Bangalore","destination","Delhi");
        chennai.addEdge("flight",hyderabad,"fid","018","fname","CtoH","depTime",Time.valueOf("18:00:35"),"arrTime",Time.valueOf("22:00:00"),"depDate","2018-10-19","arrDate","2018-10-19","company","Air India","source","Chennai","destination","Hyderabad");
        chennai.addEdge("flight",hyderabad,"fid","019","fname","CtoH","depTime",Time.valueOf("13:20:35"),"arrTime",Time.valueOf("18:00:00"),"depDate","2019-11-25","arrDate","2019-11-25","company","Jet Airways","source","Chennai","destination","Hyderabad");
        chennai.addEdge("flight",hyderabad,"fid","020","fname","CtoH","depTime",Time.valueOf("05:00:35"),"arrTime",Time.valueOf("06:30:05"),"depDate","2019-01-01","arrDate","2019-01-01","company","GoAir","source","Chennai","destination","Hyderabad");
        chennai.addEdge("flight",hyderabad,"fid","021","fname","CtoH","depTime",Time.valueOf("08:00:35"),"arrTime",Time.valueOf("10:20:00"),"depDate","2019-10-05","arrDate","2019-10-05","company","SpiceJet","source","Chennai","destination","Hyderabad");
        chennai.addEdge("flight",hyderabad,"fid","022","fname","CtoH","depTime",Time.valueOf("12:00:35"),"arrTime",Time.valueOf("14:35:00"),"depDate","2018-10-20","arrDate","2018-10-21","company","Air India","source","Chennai","destination","Hyderabad");

        tx.commit();
    }
}
