package com.ndn.lucene.analyzer.filter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import com.ndn.vnm.VNMSignGenerator;

public final class VNMSignFilter extends TokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

	private final Queue<char[]> queue = new ArrayDeque<>();

	protected VNMSignFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		Object[] os = nextToken();
		if (os != null) {
			char[] nextToken = (char[]) os[1];
			boolean fromQueue = (boolean) os[0];
			emit(nextToken, fromQueue);
			return true;
		}
		return false;
	}

	private Object[] nextToken() throws IOException {
		if (!queue.isEmpty()) {
			return new Object[] { true, queue.poll() };
		}

		if (input.incrementToken()) {
			if (termAtt != null) {
				int length = termAtt.length();
				char[] termBuf = new char[length];
				System.arraycopy(termAtt.buffer(), 0, termBuf, 0, length);
				List<char[]> variants = VNMSignGenerator.genBySign(termBuf);
				for (int i = 1; i < variants.size(); i++) {
					queue.offer(variants.get(i));
				}
				return new Object[] { false, variants.get(0) };
			}
		}

		return null;
	}

	private void emit(char[] token, boolean fromQueue) {
		termAtt.copyBuffer(token, 0, token.length);

		if (posIncrAtt != null) {
			if (fromQueue) {
				posIncrAtt.setPositionIncrement(0);
			} else {
				posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement());
			}
		}
	}
}
