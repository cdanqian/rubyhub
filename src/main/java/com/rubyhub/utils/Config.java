package com.rubyhub.utils;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

public class Config {

    private static Config config;
    private static HashMap<String, Object> currentConfig = new HashMap<>();


    public static final String database = "rubyhub";
    public static final int dbPort = 27017;
    public static final String dbHost = "localhost";
    public static String verion = "0.1.1";
    public static String logFile = "/var/log/app.log";
    public static String logLevel = "ERROR";
    public static String logName = "AppLog";




}
