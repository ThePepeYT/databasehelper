package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractDictionaryDatabase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;


public class Redis extends AbstractDictionaryDatabase {

    public Redis(String host, int port, String password, int timeout) {
        super(host, port, timeout, password);

    }

    @Override
    public void connect() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(13);
        poolConfig.setJmxEnabled(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        poolConfig.setNumTestsPerEvictionRun(-1);
        jedis = new JedisPool(poolConfig, host, port, timeout).getResource();
        jedis.connect();

    }




}
