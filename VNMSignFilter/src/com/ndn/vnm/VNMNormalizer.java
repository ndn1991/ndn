package com.ndn.vnm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noind on 1/11/2016.
 * Chuyển đổi từ bảng mã unicode dựng sẵn và tổ hợp về cùng 1 chuẩn là unicode dựng sẵn.
 */
public class VNMNormalizer {
    private static String[] material = new String[]{
            "à a`",
            "á a'",
            "ả a?",
            "ã a~",
            "ạ a.",
            "â a^",
            "ầ à^ â`",
            "ấ á^ â'",
            "ẩ ả^ â?",
            "ẫ ã^ â~",
            "ậ ạ^ â.",
            "ă a(",
            "ằ à( ă`",
            "ắ á( ă'",
            "ẳ ả( ă?",
            "ẵ ã( ă~",
            "ặ ạ( ă.",
            "è e`",
            "é e'",
            "ẻ e?",
            "ẽ e~",
            "ẹ e.",
            "ê e^",
            "ề è^ ê`",
            "ế é^ ê'",
            "ể ẻ^ ê?",
            "ễ ẽ^ ê~",
            "ệ ẹ^ ê.",
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
            "ô o^",
            "ồ ò^ ô`",
            "ố ó^ ô'",
            "ổ ỏ^ ô?",
            "ỗ õ^ ô~",
            "ộ ọ^ ô.",
            "ơ o+",
            "ờ ò+ ơ`",
            "ớ ó+ ơ'",
            "ở ỏ+ ơ?",
            "ỡ õ+ ơ~",
            "ợ ọ+ ơ.",
            "ù u`",
            "ú u'",
            "ủ u?",
            "ũ u~",
            "ụ u.",
            "ư u+",
            "ừ ù+ ư`",
            "ứ ú+ ư'",
            "ử ủ+ ư?",
            "ữ ũ+ ư~",
            "ự ụ+ ư.",
            "ỳ y`",
            "ý y'",
            "ỷ y?",
            "ỹ y~",
            "ỵ y."
    };

    private static Map<String, Character> actionMapChar = actionMapChar();

    private static Map<String, Character> actionMapChar() {
        Map<String, Character> action = new HashMap<>();
        for (String row : material) {
            String[] es = row.split(" ");
            char target = es[0].charAt(0);
            for (int i = 1; i < es.length; i++) {
                String e = es[i];
                char base = e.charAt(0);
                int[] signs = sign(e.charAt(1));
                for (int sign : signs) {
                    action.put(new String(new char[]{(char) sign, base}), target);
                    action.put(new String(new char[]{base, (char) sign}), target);
                }
            }
        }

        return action;
    }

    private static int[] sign(char visibleChar) {
        switch (visibleChar) {
            case '\'': return new int[]{769};
            case '`': return new int[]{768};
            case '?': return new int[]{777};
            case '~': return new int[]{771, 732};
            case '.': return new int[]{803};
            case '(': return new int[]{774};
            case '^': return new int[]{770};
            case '+': return new int[]{795};
            default: throw new RuntimeException("'" + visibleChar + "' is not a vietnamese sign");
        }
    }
    
    public static char[] toBuiltinUnicode(char[] src) {
    	StringBuilder builder = new StringBuilder();
    	int length1 = src.length - 1;
    	for (int i = 0; i < src.length; i++) {
    		if (i != length1) {
	    		String pre = new String(src, i, 2);
	    		if (actionMapChar.containsKey(pre)) {
	    			builder.append(actionMapChar.get(pre));
	    			i++;
	    		} else {
	    			builder.append(src[i]);
	    		}
    		} else {
    			builder.append(src[i]);
    		}
    	}
    	char[] tmp = new char[builder.length()];
    	builder.getChars(0, builder.length(), tmp, 0);
    	return tmp;
    }
    
    public static void main(String[] args) {
        System.out.println("tưới cây");
        System.out.println(toBuiltinUnicode("tưới cây".toCharArray()));
    }
}
