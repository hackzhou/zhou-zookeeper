package com.zk.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import java.util.Properties;

/**
 * 动态加载数据库配置Properties文件（方式二）
 * 方式一：<context:property-placeholder location="classpath:db.properties"/>
 * 方式二：继承PropertyPlaceholderConfigurer类，实现processProperties方法
 */
public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private ZookeeperCentralConfigurer zkCentralConfigurer;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, zkCentralConfigurer.getProps());
	}

	public void setZkCentralConfigurer(ZookeeperCentralConfigurer zkCentralConfigurer) {
		this.zkCentralConfigurer = zkCentralConfigurer;
	}
}
