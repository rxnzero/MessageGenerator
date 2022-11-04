package com.dhlee.message.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardMessageUtil;
import com.dhlee.message.parser.StandardReader;
import com.dhlee.service.InterfaceMapper;

public class StandardMessageManager {
	static Logger logger = LoggerFactory.getLogger(StandardMessageManager.class);
	
	private static StandardMessageManager instance;
	/*
	layout.file.type=CSV
	layout.file.path=./resources/standard-layout.csv
	mapper.class=com.dhlee.test.TestInterfaceMapper
	*/
	public static String STANDARD_MESSAGE_CONFIG = "standard.message.config";
	private String DEFAULT_CONFIG_PATH = "./resources/standard-message-config.properties";
	
	private String LAYOUT_FILE_TYPE = "layout.file.type";
	private String LAYOUT_FILE_PATH = "layout.file.path";
	private String MAPPER_CLASS = "mapper.class";
	private String MAPPER_DEFINITION = "mapper.definition";
	private String READER_PREFIX = "reader.";
	
	private StandardMessage standardMessage = null;
	private InterfaceMapper mapper;
	
	private ConcurrentHashMap<String, StandardReader> readerMap = new ConcurrentHashMap<String, StandardReader>(); 
	public StandardMessageManager() {
		
	}
	
	public StandardMessage getStandardMessage() {
		return (StandardMessage)SerializationUtils.clone(standardMessage);
	}
	
	public InterfaceMapper getMapper() {
		return mapper;
	}

	public void setMapper(InterfaceMapper mapper) {
		this.mapper = mapper;
	}

	public static synchronized StandardMessageManager getInstance() {
		if ( instance == null ) {
			instance = new StandardMessageManager();
			instance.init();
			return instance;
		} else {
			return instance;
		}
    }
	
	private void initReaderFactory(Properties config) {
		Class cl = null;
		StandardReader reader = null;
		
		Set<Object> keys = config.keySet();
		
		for(Object key: keys) {
			String keyStr = (String)key;
			if(keyStr.startsWith(READER_PREFIX)) {
				String name = keyStr.substring(READER_PREFIX.length());
				String readerClassName = config.getProperty(keyStr);
								
				try {
					cl = Class.forName(readerClassName);
					reader = (StandardReader)cl.newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					logger.warn("Invalid Class path : {} {}", keyStr, readerClassName, e);
					continue;
				}				
				readerMap.put(name, reader);
				logger.warn("# Add Reader : {} {}", keyStr, readerClassName);
			}
		}
	}
	
	public StandardReader getReader(String messageType) {
		return readerMap.get(messageType);
	}
	
	public void init() {
		try {
			Properties config = loadConfigFile();
			logger.debug(config.toString());
			String path = config.getProperty(LAYOUT_FILE_PATH);
			logger.debug("{} : {}", LAYOUT_FILE_PATH, path);
			standardMessage = StandardMessageUtil.generateMessageFromCCsvFile(path);
			
			String mapperClass = config.getProperty(MAPPER_CLASS);
			String mapperFilePath = config.getProperty(MAPPER_DEFINITION);
			logger.debug("{} : {}", MAPPER_CLASS, mapperClass);
			logger.debug("{} : {}", MAPPER_DEFINITION, mapperFilePath);
			initReaderFactory(config);
			
			Class cl = null;
			try {
				cl = Class.forName(mapperClass);
				mapper = (InterfaceMapper)cl.newInstance();
				Properties mapperMapPropties = loadFileProperties(mapperFilePath);
				HashMap<String, String> map = propertyToMap(mapperMapPropties);				
				mapper.initPathMap(map);
				
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				logger.error("configure {} : {}", mapperClass, MAPPER_DEFINITION, e);
				return;
			}
			logger.debug("mapperClass : {}", mapper.getClass().getCanonicalName());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("init failed", ex);
		}
	}
	
	private HashMap<String, String> propertyToMap(Properties prop) {
	    HashMap<String, String> retMap = new HashMap<>();
	    for (Map.Entry<Object, Object> entry : prop.entrySet()) {
	        retMap.put(String.valueOf(entry.getKey()).trim(), String.valueOf(entry.getValue()).trim());
	    }
	    return retMap;
	}
	
	private Properties loadConfigFile() throws Exception {
		String configFile = getConfigFile();
		return loadFileProperties(configFile);
	}
	
	private Properties loadFileProperties(String filePath) throws Exception {
		logger.debug("configFile : ", filePath);
		Properties p = new Properties();
		try(InputStream in = new FileInputStream(filePath)) {
			p.load(in);
			return p;
		} catch(Exception e) {
			throw new Exception("StandardMessageMangaer | Cannot load config file. - " + filePath, e);
		}
	}
	
	private String getConfigFile() {
		String configFilePath = System.getProperty(STANDARD_MESSAGE_CONFIG);
		if (StringUtils.isEmpty(configFilePath)) {
			return DEFAULT_CONFIG_PATH; 
		} else {
			return configFilePath;
		}
	}
}
