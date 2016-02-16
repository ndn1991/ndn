package com.ndn.lucene.analyzer.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.ndn.vnm.VNMNormalizer;

public final class VNMNormalizerFilter extends TokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	
	protected VNMNormalizerFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}
		int length = termAtt.length();
		char[] termBuf = new char[length];
		System.arraycopy(termAtt.buffer(), 0, termBuf, 0, length);
		char[] token = VNMNormalizer.toBuiltinUnicode(termBuf);
		termAtt.copyBuffer(token, 0, token.length);
		return true;
	}
}
