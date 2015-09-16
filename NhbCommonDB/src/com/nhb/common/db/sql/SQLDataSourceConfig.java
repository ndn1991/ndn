package com.nhb.common.db.sql;

import java.util.Map.Entry;
import java.util.Properties;

import com.ndn.common.Loggable;
import com.ndn.common.data.commondata.CommonObject;

public class SQLDataSourceConfig implements Loggable {

	private String name;
	private CommonObject initParams;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommonObject getInitParams() {
		return initParams;
	}

	public void setInitParams(CommonObject initParams) {
		this.initParams = initParams;
	}

	public Properties getProperties() {
		if (this.initParams != null) {
			Properties props = new Properties();
			for (Entry<String, Object> entry : this.initParams) {
				props.setProperty(entry.getKey(), entry.getValue().toString());
			}
			return props;
		}
		return null;
	}
}
