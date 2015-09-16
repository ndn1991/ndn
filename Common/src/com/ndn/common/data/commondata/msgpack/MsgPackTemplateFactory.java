package com.ndn.common.data.commondata.msgpack;

import java.util.HashMap;
import java.util.Map;

import org.msgpack.template.AbstractTemplate;
import org.msgpack.template.BooleanTemplate;
import org.msgpack.template.ByteTemplate;
import org.msgpack.template.DoubleTemplate;
import org.msgpack.template.FloatTemplate;
import org.msgpack.template.IntegerTemplate;
import org.msgpack.template.LongTemplate;
import org.msgpack.template.ShortTemplate;
import org.msgpack.template.StringTemplate;

import com.ndn.common.data.commondata.CommonArray;
import com.ndn.common.data.commondata.CommonObject;

public final class MsgPackTemplateFactory {
	private static MsgPackTemplateFactory instance = null;
	private Map<Byte, AbstractTemplate<?>> templates = new HashMap<Byte, AbstractTemplate<?>>(11);
	private Map<Class<?>, Byte> types = new HashMap<Class<?>, Byte>(11);
	private CommonDataTemplate commonDataTemplate = CommonDataTemplate.getInstance();
	
	private MsgPackTemplateFactory() {
		super();
	}

	public static MsgPackTemplateFactory getInstance() {
		if (instance == null) {
			instance = new MsgPackTemplateFactory();
			instance.put(NIL, null);
			instance.put(BOOLEAN, BooleanTemplate.getInstance());
			instance.put(BYTE, ByteTemplate.getInstance());
			instance.put(SHORT, ShortTemplate.getInstance());
			instance.put(INTEGER, IntegerTemplate.getInstance());
			instance.put(LONG, LongTemplate.getInstance());
			instance.put(FLOAT, FloatTemplate.getInstance());
			instance.put(DOUBLE, DoubleTemplate.getInstance());
			instance.put(STRING, StringTemplate.getInstance());
			instance.put(COMMON_OBJECT, CommonObjectTemplate.getInstance());
			instance.put(COMMON_ARRAY, CommonArrayTemplate.getInstance());

			instance.putType(NIL, null);
			instance.putType(BOOLEAN, Boolean.class);
			instance.putType(BYTE, Byte.class);
			instance.putType(SHORT, Short.class);
			instance.putType(INTEGER, Integer.class);
			instance.putType(LONG, Long.class);
			instance.putType(FLOAT, Float.class);
			instance.putType(DOUBLE, Double.class);
			instance.putType(STRING, String.class);
			instance.putType(COMMON_OBJECT, CommonObject.class);
			instance.putType(COMMON_ARRAY, CommonArray.class);

		}

		return instance;
	}

	private void put(Byte type, AbstractTemplate<?> template) {
		templates.put(type, template);
	}

	private void putType(Byte type, Class<?> clazz) {
		types.put(clazz, type);
	}

	@SuppressWarnings("rawtypes")
	public AbstractTemplate getTemplate(byte type) {
		if (templates.containsKey(type)) {
			return templates.get(type);
		}
		throw new RuntimeException("Do not support this type: " + type);
	}

	public Byte getType(Class<?> clazz) {
		if (types.containsKey(clazz)) {
			return types.get(clazz);
		}
		throw new RuntimeException("Do not support this class: " + clazz);
	}

	public static final byte NIL = 0;
	public static final byte BOOLEAN = 1;
	public static final byte BYTE = 2;
	public static final byte SHORT = 3;
	public static final byte INTEGER = 4;
	public static final byte LONG = 5;
	public static final byte FLOAT = 6;
	public static final byte DOUBLE = 7;
	public static final byte STRING = 8;
	public static final byte COMMON_OBJECT = 9;
	public static final byte COMMON_ARRAY = 10;

	public CommonDataTemplate getCommonDataTemplate() {
		return commonDataTemplate;
	}
}
