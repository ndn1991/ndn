package com.ndn.common.data.commondata.msgpack;

import java.io.IOException;
import java.util.Map.Entry;

import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.CommonObject;

public final class CommonObjectTemplate extends AbstractTemplate<CommonObject> {
	
	private static CommonObjectTemplate instance = new CommonObjectTemplate();
	
	private CommonObjectTemplate() {
		super();
	}

	public static CommonObjectTemplate getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Packer packer, CommonObject t, boolean required)
			throws IOException {
		if (t == null) {
			if (required) {
				throw new MessageTypeException("Attempted to write null");
			}
			packer.writeNil();
			return;
		}
		
		MsgPackTemplateFactory factory = MsgPackTemplateFactory.getInstance();
		packer.writeArrayBegin(t.size() * 3);
		for (Entry<String, Object> e : t) {
			packer.write(e.getKey());
			packer.write(factory.getType(e.getValue().getClass()));
			if (e.getValue() != null) {
				factory.getTemplate(factory.getType(e.getValue().getClass()))
						.write(packer, e.getValue());
			} else {
				packer.writeNil();
			}
		}
		packer.writeArrayEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonObject read(Unpacker u, CommonObject t, boolean required)
			throws IOException {
		MsgPackTemplateFactory factory = MsgPackTemplateFactory.getInstance();
		
		if (!required && u.trySkipNil()) {
			return null;
		}

		CommonObject obj = t == null ? new CommonObject() : t;
		int n = u.readArrayBegin();
		for (int i = 0; i < n; i += 3) {
			String key = u.readString();
			Byte type = u.readByte();
			Object data = null;
			if (type != MsgPackTemplateFactory.NIL) {
				data = factory.getTemplate(type).read(u, null);
			} else {
				u.readNil();
			}
			obj.put(key, data);
		}
		
		u.readArrayEnd();
		return obj;
	}

}
