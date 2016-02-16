package com.ndn.vnm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sinh ra các biến thể của từ đơn do đặt sai dấu
 * ví dụ hỏa => [hỏa hoả]
 * @author noind
 *
 */
public class VNMSignGenerator {
	private static Logger logger = LoggerFactory.getLogger(VNMSignGenerator.class);
	
	private static String[] material = new String[]{
            "à a`",
            "á a'",
            "ả a?",
            "ã a~",
            "ạ a.",

            "ầ â`",
            "ấ â'",
            "ẩ â?",
            "ẫ â~",
            "ậ â.",

            "ằ ă`",
            "ắ ă'",
            "ẳ ă?",
            "ẵ ă~",
            "ặ ă.",
            
            "è e`",
            "é e'",
            "ẻ e?",
            "ẽ e~",
            "ẹ e.",

            "ề ê`",
            "ế ê'",
            "ể ê?",
            "ễ ê~",
            "ệ ê.",
            
            "ì i`",
            "í i'",
            "ỉ i?",
            "ĩ i~",
            "ị i.",
            
            "ò o`",
            "ó o'",
            "ỏ o?",
            "õ o~",
            "ọ o.",

            "ồ ô`",
            "ố ô'",
            "ổ ô?",
            "ỗ ô~",
            "ộ ô.",

            "ờ ơ`",
            "ớ ơ'",
            "ở ơ?",
            "ỡ ơ~",
            "ợ ơ.",
            
            "ù u`",
            "ú u'",
            "ủ u?",
            "ũ u~",
            "ụ u.",

            "ừ ư`",
            "ứ ư'",
            "ử ư?",
            "ữ ư~",
            "ự ư.",
            
            "ỳ y`",
            "ý y'",
            "ỷ y?",
            "ỹ y~",
            "ỵ y."
    };
	
	private static Set<Character> vowelSet = new HashSet<>(Arrays.asList('a', 'ă', 'â', 'e', 'ê', 'i', 'o', 'ô', 'ơ', 'u', 'ư', 'y'));
	
	private static Map<Character, String> char2Sign() {
		Map<Character, String> map = new HashMap<>();
		for (String s : material) {
			String[] es = s.split(" ");
			char c = es[0].charAt(0);
			map.put(c, es[1]);
		}
		return map;
	}
	
	private static Map<String, Character> sign2Char() {
		Map<String, Character> map = new HashMap<>();
		for (String s : material) {
			String[] es = s.split(" ");
			char c = es[0].charAt(0);
			map.put(es[1], c);
		}
		return map;
	}
	
	private static Map<Character, String> char2Sign = char2Sign();
	private static Map<String, Character> sign2Char = sign2Char();
	
	public static List<char[]> genBySign(char[] term) {
		List<char[]> rs = new ArrayList<>();
		rs.add(term);
		
		try {
			boolean[] isVowel = new boolean[term.length];
			Character signChar = null;
			int iSignChar = -1;
			for (int i = 0; i < term.length; i++) {
				if (vowelSet.contains(term[i])) {
					isVowel[i] = true;
				} else {
					isVowel[i] = false;
				}
				
				if (char2Sign.containsKey(term[i])) {
					signChar = term[i];
					iSignChar = i;
				}
			}
				
			if (signChar != null) {
				String charAndSign = char2Sign.get(signChar);
				char sign = charAndSign.charAt(1);
				char base = charAndSign.charAt(0);
				for (int i = 0; i < term.length; i++) {
					if (isVowel[i]) {
						String signPhrase = new String(new char[]{term[i], sign});
						char newChar = sign2Char.get(signPhrase);
						char[] newTerm = new char[term.length];
						System.arraycopy(term, 0, newTerm, 0, term.length);
						newTerm[i] = newChar;
						newTerm[iSignChar] = base;
						rs.add(newTerm);
					}
				}
				char[] newTerm = new char[term.length];
				System.arraycopy(term, 0, newTerm, 0, term.length);
				newTerm[iSignChar] = base;
				rs.add(newTerm);
			}
		} catch (Exception e) {
			logger.warn("error when generate variants from a term: {}", new String(term));
		}
		return rs;
	}
	
	public static void main(String[] args) {
		String term = "hè";
		System.out.println(genBySign(term.toCharArray()));
	}
}
