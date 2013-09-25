package ru.vkuptime;

import com.mongodb.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    static void write(String uid){

    }

    static void update(String uid){

    }

    static String get(String uid) throws IOException {
        String s= new Scanner(new URL("https://api.vkontakte.ru/method/getProfiles?uids="+uid+"&fields=online").openStream()).nextLine();
        JSONObject jo=new JSONObject(s);
        JSONArray ja= (JSONArray) jo.get("response");
        JSONObject jo2=((JSONObject)ja.get(0)).put("date", new Date().getTime());
         return  ja.get(0).toString();
    }
     static BasicDBObject dbFromString(String json){
                  BasicDBObject result=new BasicDBObject();
           JSONObject jo=new JSONObject(json);
         for(Object s: jo.keySet()){
                result.put((String)s,jo.get((String)s));
         }
         return result;
     }
    static DBObject last(String uid) throws IOException {      //TODO
        Mongo con=new Mongo();
        DBCollection col=con.getDB("vkuptime").getCollection("sessions");
        if(col.find(new BasicDBObject("uid",Integer.parseInt(uid))).sort(new BasicDBObject("_id",-1)).limit(1).hasNext())
       return  col.find(new BasicDBObject("uid",Integer.parseInt(uid))).sort(new BasicDBObject("_id",-1)).limit(1).next();
        else return null;
        }

    static List<String> getIds(){

        try {
            Mongo con=new Mongo();
            DBCollection col=con.getDB("vkuptime").getCollection("uids");
            List<DBObject> s=col.find().toArray();

             List<String> res=new ArrayList<String>(s.size());
            for(DBObject s1: s){
                   res.add(s1.get("_id").toString());

            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return new ArrayList<String>(0);
        }

        }

    static boolean isOn(String uid){
        try {
            String s=get(uid);
            JSONObject jo=new JSONObject(s);
            return  (Integer)jo.get("online")==0?false:true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean isNear(String uid) throws InterruptedException, IOException {
        if(last(uid)==null) return false;
        long a=(Long) last(uid).get("date");
        long b=(Long) dbFromString(get(uid)).get("date");
         System.out.println(b-a);
        return (b-a)>180000?false:true;}

    public static void main(String[] args) throws InterruptedException, IOException {
//
//        try {
//            last("64571597");
//            Thread.sleep(60000);
//            last("64571597");
//
//            System.out.println(last("64571597"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        isNear("3");
//
//        for(String s: getIds()){
//            System.out.println(s);
//        }

//        while (true){
//            Thread.sleep(59000);
//            //https://api.vkontakte.ru/method/getProfiles?uids=64571597,1&fields=online
//            System.out.println("Hello World!");
//            for(String uid : getIds()){
//                if(isOn(uid))
//                if(isNear(uid)){
//                    write(uid);
//                }else {
//                    update(uid);
//                }
//            }
//        }
    }
}

