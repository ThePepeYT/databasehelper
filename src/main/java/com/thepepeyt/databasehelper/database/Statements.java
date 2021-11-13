package com.thepepeyt.databasehelper.database;

import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statements implements DatabaseConnection{


    ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Jedis jedis;

    public Jedis getJedis() {
        return jedis;
    }






    public void setData(List<String> keys, List<String> values){
            keys.forEach(x -> {
                jedis.set(keys.get(keys.indexOf(x)), values.get(keys.indexOf(x)));
            });
    }

    public void setData(String key, List<String> values){
            values.forEach(x -> {

                if (!Values(key).contains(x)) {
                    jedis.lpush(key, x);
                }

            });
    }



    public boolean KeyExists(String key){
        return Keys().contains(key);
    }

    public boolean ValueExists(String key, String name){
            return Values(key).contains(name);
    }





    public List<String> getData(String key, int start, int stop){
           return jedis.lrange(key, start, stop).stream().collect(Collectors.toList());

    }

    public List<String> getData(List<String> keys){
        return keys.stream().map(x -> jedis.get(x)).collect(Collectors.toList());
    }


    public void removeFrom(String key, List<String> values, List<Integer> counts){
            values.forEach(x -> jedis.lrem(key, counts.get(values.indexOf(x)), x));
    }

    public List<String> Keys(){
            return jedis.keys("*").stream().collect(Collectors.toList());
    }

    public List<String> Values(String key){
            return jedis.lrange(key, 0, jedis.llen(key));
    }

    public void deleteKeys(List<String> keys){
            keys.forEach(x -> {
                if(KeyExists(x)) {
                    jedis.del(x);
                }
            });
    }

    @Override
    public void connect() throws SQLException, ClassNotFoundException {}

    @Override
    public void disconnect(){
        jedis.close();
    }
}
