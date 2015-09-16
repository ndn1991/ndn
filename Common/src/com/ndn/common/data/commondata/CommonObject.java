package com.ndn.common.data.commondata;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Created by ndn on 7/21/2015.
 */
public final class CommonObject implements CommonData, Serializable, Iterable<Entry<String, Object>> {
	private static final long serialVersionUID = 363932952614475001L;
	
	private HashMap<String, Object> datas = new HashMap<String, Object>();

	public CommonObject() {
		super();
	}

	public CommonObject(Object... objects) {
		super();
		String key = null;
		for (int i = 0; i < objects.length; i++) {
			if ((i & 1) == 0) {
				if (objects[i] instanceof String) {
					key = (String) objects[i];
				} else {
					throw new IllegalArgumentException(
							"This key is not a String: " + objects[i]);
				}
			} else {
				if (typeAccepted(objects[i])) {
					datas.put(key, objects[i]);
				} else {
					datas.put(key, objects[i].toString());
				}
			}
		}
	}

	public void setInt(String key, Integer value) {
		datas.put(key, value);
	}

	public void setLong(String key, Long value) {
		datas.put(key, value);
	}

	public void setString(String key, String value) {
		datas.put(key, value);
	}

	public void setFloat(String key, Float value) {
		datas.put(key, value);
	}

	public void setDouble(String key, Double value) {
		datas.put(key, value);
	}

	public void setBoolean(String key, Boolean value) {
		datas.put(key, value);
	}

	public void setChar(String key, Character value) {
		datas.put(key, value);
	}

	public void setCommonData(String key, CommonData value) {
		datas.put(key, value);
	}

	public Integer getInt(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getInteger(value);
	}

	public Long getLong(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getLong(value);
	}

	public Float getFloat(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getFloat(value);
	}

	public Double getDouble(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getDouble(value);
	}

	public String getString(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	public Boolean getBool(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getBool(value);
	}

	public Character getChar(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		return ObjectCommonTools.getChar(value);
	}

	public CommonData getCommonData(String key) {
		Object value = datas.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof CommonData) {
			return (CommonData) value;
		}
		return null;
	}

	static boolean typeAccepted(Object o) {
		if (o instanceof Long || o instanceof Integer || o instanceof Double
				|| o instanceof Float || o instanceof String
				|| o instanceof Boolean || o instanceof Character
				|| o instanceof CommonData) {
			return true;
		}
		return false;
	}
	
	public Object put(String key, Object value) {
		if (typeAccepted(value)) {
			return datas.put(key, value);
		} else {
			throw new IllegalArgumentException(
					"This value has type not be accepted: " + value);
		}

	}

	@Override
	public void copyFrom(CommonData other) {
		if (other instanceof CommonObject) {
			datas.clear();
			datas.putAll(((CommonObject) other).datas);
		} else {
			throw new IllegalArgumentException("input is not a CommonObject");
		}
	}

	public static void main(String[] args) {
		String s = "{\"warehouseProductItemMappingId\":2423, \"score\":43.534, \"jsonScore\":{\"0\":\"0.1236\",\"4\":\"0.0000\",\"8\":\"-60000.0000\"}, \"districtsJson\":\"[{\\\"Id\\\":4,\\\"LstDistrictId\\\":[{\\\"id\\\":7,\\\"LstWardId\\\":[{\\\"id\\\":1}]},{\\\"id\\\":9}]}\", \"updateTime\":234567898}";
		CommonData o = CommonObjectTools.fromJson(s);
		System.out.println(o.toJsonString());
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return datas.entrySet().iterator();
	}
	
	public Collection<String> names() {
		return datas.keySet();
	}
	
	public Collection<Object> values() {
		return datas.values();
	}
	
	public int size() {
		return datas.size();
	}
}
