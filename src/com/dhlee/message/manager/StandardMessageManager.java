package com.dhlee.message.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardMessageUtil;
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
	
	private StandardMessage standardMessage = null;
	private InterfaceMapper mapper;
	
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
	
	public void init() {
		try {
			Properties config = loadConfigFile();
			logger.debug(config.toString());
			String path = config.getProperty(LAYOUT_FILE_PATH);
			logger.debug("{} : {}", LAYOUT_FILE_PATH, path);
			standardMessage = StandardMessageUtil.generateMessageFromCCsvFile(path);
			
			String mapperClass = config.getProperty(MAPPER_CLASS);
			logger.debug("{} : {}", MAPPER_CLASS, mapperClass);
			Class cl = null;
			try {
				cl = Class.forName(mapperClass);
				mapper = (InterfaceMapper)cl.newInstance();
				
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
			logger.debug("mapperClass : {}", mapper.getClass().getCanonicalName());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("init failed", ex);
		}
	}
	
	private Properties loadConfigFile() throws Exception {
		String configFile = getConfigFile();
		logger.debug("configFile : {}", configFile);
		Properties p = new Properties();
		try(InputStream in = new FileInputStream(configFile)) {
			p.load(in);
			return p;
		} catch(Exception e) {
			throw new Exception("StaandardMessageMangaer | Cannot load config file. - " + configFile, e);
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
