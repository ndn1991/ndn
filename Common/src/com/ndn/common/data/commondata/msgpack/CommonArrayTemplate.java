package com.ndn.common.data.commondata.msgpack;

import java.io.IOException;

import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.CommonArray;

public final class CommonArrayTemplate extends AbstractTemplate<CommonArray> {
	
	public static CommonArrayTemplate getInstance() {
		return null;
	}

	@Override
	public void write(Packer paramPacker, CommonArray paramT,
			boolean paramBoolean) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommonArray read(Unpacker paramUnpacker, CommonArray paramT,
			boolean paramBoolean) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
