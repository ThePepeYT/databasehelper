package com.thepepeyt.databasehelper.database;

import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Statements implements DatabaseConnection{


    ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Jedis jedis;

    public Jedis getJedis() {
        return jedis;
    }






    public void setData(List<String> keys, List<String> values){
        executor.execute(()-> {
            keys.forEach(x -> {
                jedis.set(keys.get(keys.indexOf(x)), values.get(keys.indexOf(x)));
            });
        });
    }

    public void setData(String key, List<String> values){
        executor.execute(()-> {
            values.forEach(x -> {
                if (!jedis.lrange(key, 0, jedis.llen(key)).contains(x)) {
                    jedis.lpush(key, x);
                }

            });
        });
    }



    public CompletableFuture<Boolean> KeyExists(String key){
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(jedis.keys("*").stream().collect(Collectors.toList()).contains(key));
        });

        return completableFuture;
    }

    public CompletableFuture<Boolean> ValueExists(String key, String name){
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(jedis.lrange(key, 0, jedis.llen(key)).contains(name));
        });
        return completableFuture;
    }





    public CompletableFuture<List<String>> getData(String key, int start, int stop){
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(jedis.lrange(key, start, stop).stream().collect(Collectors.toList()));
        });

        return completableFuture;

    }

    public CompletableFuture<List<String>> getData(List<String> keys){
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(keys.stream().map(x -> jedis.get(x)).collect(Collectors.toList()));
        });
        return completableFuture;
    }


    public void removeFrom(String key, List<String> values, List<Integer> counts){
        executor.execute(()-> {
            values.forEach(x -> jedis.lrem(key, counts.get(values.indexOf(x)), x));
        });
    }

    public CompletableFuture<List<String>> Keys(){
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(jedis.keys("*").stream().collect(Collectors.toList()));
        });
        return completableFuture;
    }

    public CompletableFuture<List<String>> Values(String key){
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        executor.submit(() -> {
            completableFuture.complete(jedis.lrange(key, 0, jedis.llen(key)));
        });
        return completableFuture;
    }

    public void deleteKeys(List<String> keys){
        executor.execute(()-> {
            keys.forEach(x -> {
                if (jedis.keys("*").stream().collect(Collectors.toList()).contains(x)) {
                    jedis.del(x);
                }
            });
        });
    }


    @Override
    public void connect() throws SQLException, ClassNotFoundException {}

    @Override
    public void disconnect(){
        jedis.close();
    }
}