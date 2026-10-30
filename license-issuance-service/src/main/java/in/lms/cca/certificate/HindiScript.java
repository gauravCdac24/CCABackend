package in.lms.cca.certificate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fills the Hindi paragraph of the licence with Hindi text.
 * Known words are translated; other English words are transliterated.
 */
final class HindiScript {

	private static final Pattern TOKEN = Pattern.compile("[A-Za-z]+|[^A-Za-z]+");
	private static final List<String[]> PHRASES = new ArrayList<>();
	private static final Map<String, String> WORDS = new HashMap<>();

	static {
		phrase("dadra and nagar haveli and daman and diu", "दादरा और नगर हवेली और दमण और दीव");
		phrase("andaman and nicobar islands", "अंडमान और निकोबार द्वीप समूह");
		phrase("andaman and nicobar", "अंडमान और निकोबार");
		phrase("dadra and nagar haveli", "दादरा और नगर हवेली");
		phrase("jammu and kashmir", "जम्मू और कश्मीर");
		phrase("jammu & kashmir", "जम्मू और कश्मीर");
		phrase("daman and diu", "दमण और दीव");
		phrase("andhra pradesh", "आंध्र प्रदेश");
		phrase("arunachal pradesh", "अरुणाचल प्रदेश");
		phrase("himachal pradesh", "हिमाचल प्रदेश");
		phrase("madhya pradesh", "मध्य प्रदेश");
		phrase("uttar pradesh", "उत्तर प्रदेश");
		phrase("west bengal", "पश्चिम बंगाल");
		phrase("tamil nadu", "तमिलनाडु");
		phrase("nct of delhi", "दिल्ली");
		phrase("new delhi", "नई दिल्ली");
		phrase("south delhi", "दक्षिण दिल्ली");
		phrase("north delhi", "उत्तर दिल्ली");
		phrase("east delhi", "पूर्व दिल्ली");
		phrase("west delhi", "पश्चिम दिल्ली");
		phrase("central delhi", "मध्य दिल्ली");
		phrase("greater noida", "ग्रेटर नोएडा");
		phrase("navi mumbai", "नवी मुंबई");
		phrase("connaught place", "कनॉट प्लेस");
		phrase("karol bagh", "करोल बाग");
		phrase("nehru place", "नेहरू प्लेस");
		phrase("lajpat nagar", "लाजपत नगर");
		phrase("vasant kunj", "वसंत कुंज");
		phrase("vasant vihar", "वसंत विहार");
		phrase("greater kailash", "ग्रेटर कैलाश");
		phrase("sarita vihar", "सरिता विहार");
		phrase("laxmi nagar", "लक्ष्मी नगर");
		phrase("lakshmi nagar", "लक्ष्मी नगर");
		phrase("model town", "मॉडल टाउन");
		phrase("civil lines", "सिविल लाइंस");
		phrase("hauz khas", "हौज़ खास");
		phrase("patel nagar", "पटेल नगर");
		phrase("rajendra place", "राजेंद्र प्लेस");
		phrase("rajendra nagar", "राजेंद्र नगर");
		phrase("of india", "भारत का");
		phrase("post office", "डाकघर");
		PHRASES.sort(Comparator.comparingInt((String[] row) -> row[0].length()).reversed());

		word("india", "भारत");
		word("indian", "भारतीय");
		word("bharat", "भारत");
		word("delhi", "दिल्ली");
		word("mumbai", "मुंबई");
		word("bombay", "मुंबई");
		word("kolkata", "कोलकाता");
		word("calcutta", "कोलकाता");
		word("chennai", "चेन्नई");
		word("madras", "चेन्नई");
		word("bengaluru", "बेंगलुरु");
		word("bangalore", "बेंगलुरु");
		word("hyderabad", "हैदराबाद");
		word("ahmedabad", "अहमदाबाद");
		word("pune", "पुणे");
		word("jaipur", "जयपुर");
		word("lucknow", "लखनऊ");
		word("kanpur", "कानपुर");
		word("nagpur", "नागपुर");
		word("indore", "इंदौर");
		word("bhopal", "भोपाल");
		word("patna", "पटना");
		word("noida", "नोएडा");
		word("ghaziabad", "गाजियाबाद");
		word("gurugram", "गुरुग्राम");
		word("gurgaon", "गुरुग्राम");
		word("faridabad", "फरीदाबाद");
		word("chandigarh", "चंडीगढ़");
		word("dehradun", "देहरादून");
		word("ranchi", "रांची");
		word("raipur", "रायपुर");
		word("bhubaneswar", "भुवनेश्वर");
		word("guwahati", "गुवाहाटी");
		word("srinagar", "श्रीनगर");
		word("jammu", "जम्मू");
		word("shimla", "शिमला");
		word("amritsar", "अमृतसर");
		word("ludhiana", "लुधियाना");
		word("varanasi", "वाराणसी");
		word("prayagraj", "प्रयागराज");
		word("allahabad", "इलाहाबाद");
		word("agra", "आगरा");
		word("meerut", "मेरठ");
		word("jodhpur", "जोधपुर");
		word("udaipur", "उदयपुर");
		word("surat", "सूरत");
		word("vadodara", "वडोदरा");
		word("baroda", "वडोदरा");
		word("rajkot", "राजकोट");
		word("nashik", "नाशिक");
		word("thane", "ठाणे");
		word("visakhapatnam", "विशाखापट्टनम");
		word("vizag", "विशाखापट्टनम");
		word("vijayawada", "विजयवाड़ा");
		word("kochi", "कोच्चि");
		word("cochin", "कोच्चि");
		word("thiruvananthapuram", "तिरुवनंतपुरम");
		word("trivandrum", "तिरुवनंतपुरम");
		word("madurai", "मदुरै");
		word("coimbatore", "कोयंबटूर");
		word("mysuru", "मैसूरु");
		word("mysore", "मैसूरु");
		word("mangaluru", "मंगलुरु");
		word("mangalore", "मंगलुरु");
		word("panaji", "पणजी");
		word("panjim", "पणजी");
		word("imphal", "इम्फाल");
		word("shillong", "शिलांग");
		word("aizawl", "आइजोल");
		word("kohima", "कोहिमा");
		word("agartala", "अगरतला");
		word("itanagar", "ईटानगर");
		word("gangtok", "गंगटोक");
		word("siliguri", "सिलीगुड़ी");
		word("dhanbad", "धनबाद");
		word("jamshedpur", "जमशेदपुर");
		word("howrah", "हावड़ा");
		word("cuttack", "कटक");
		word("rourkela", "राउरकेला");
		word("gandhinagar", "गांधीनगर");
		word("haridwar", "हरिद्वार");
		word("rishikesh", "ऋषिकेश");
		word("nainital", "नैनीताल");
		word("ajmer", "अजमेर");
		word("ujjain", "उज्जैन");
		word("gwalior", "ग्वालियर");
		word("jabalpur", "जबलपुर");
		word("jhansi", "झांसी");
		word("aligarh", "अलीगढ़");
		word("bareilly", "बरेली");
		word("gorakhpur", "गोरखपुर");
		word("jalandhar", "जालंधर");
		word("patiala", "पटियाला");
		word("ambala", "अंबाला");
		word("karnal", "करनाल");
		word("panipat", "पानीपत");
		word("rohtak", "रोहतक");
		word("sonipat", "सोनीपत");
		word("bhiwadi", "भिवाड़ी");
		word("jasola", "जसोला");
		word("okhla", "ओखला");
		word("saket", "साकेत");
		word("dwarka", "द्वारका");
		word("rohini", "रोहिणी");
		word("kalkaji", "कालकाजी");
		word("janakpuri", "जनकपुरी");
		word("pitampura", "पीतमपुरा");
		word("chanakyapuri", "चाणक्यपुरी");
		word("mehrauli", "महरौली");
		word("badarpur", "बदरपुर");
		word("shahdara", "शाहदरा");
		word("assam", "असम");
		word("bihar", "बिहार");
		word("chhattisgarh", "छत्तीसगढ़");
		word("chattisgarh", "छत्तीसगढ़");
		word("goa", "गोवा");
		word("gujarat", "गुजरात");
		word("haryana", "हरियाणा");
		word("jharkhand", "झारखंड");
		word("karnataka", "कर्नाटक");
		word("kerala", "केरल");
		word("maharashtra", "महाराष्ट्र");
		word("manipur", "मणिपुर");
		word("meghalaya", "मेघालय");
		word("mizoram", "मिजोरम");
		word("nagaland", "नागालैंड");
		word("odisha", "ओडिशा");
		word("orissa", "ओडिशा");
		word("punjab", "पंजाब");
		word("rajasthan", "राजस्थान");
		word("sikkim", "सिक्किम");
		word("telangana", "तेलंगाना");
		word("tripura", "त्रिपुरा");
		word("uttarakhand", "उत्तराखंड");
		word("uttaranchal", "उत्तराखंड");
		word("ladakh", "लद्दाख");
		word("puducherry", "पुदुच्चेरी");
		word("pondicherry", "पुदुच्चेरी");
		word("lakshadweep", "लक्षद्वीप");
		word("kashmir", "कश्मीर");
		word("andaman", "अंडमान");
		word("nicobar", "निकोबार");
		word("daman", "दमण");
		word("diu", "दीव");
		word("dadra", "दादरा");
		word("pradesh", "प्रदेश");
		word("bengal", "बंगाल");
		word("block", "ब्लॉक");
		word("plot", "प्लॉट");
		word("sector", "सेक्टर");
		word("phase", "फेज");
		word("floor", "मंजिल");
		word("road", "रोड");
		word("street", "स्ट्रीट");
		word("lane", "लेन");
		word("marg", "मार्ग");
		word("nagar", "नगर");
		word("colony", "कॉलोनी");
		word("vihar", "विहार");
		word("enclave", "एन्क्लेव");
		word("park", "पार्क");
		word("place", "प्लेस");
		word("connaught", "कनॉट");
		word("extension", "एक्सटेंशन");
		word("extn", "एक्सटेंशन");
		word("near", "निकट");
		word("opposite", "सामने");
		word("opp", "सामने");
		word("east", "पूर्व");
		word("west", "पश्चिम");
		word("north", "उत्तर");
		word("south", "दक्षिण");
		word("central", "मध्य");
		word("district", "जिला");
		word("village", "गाँव");
		word("office", "कार्यालय");
		word("city", "शहर");
		word("state", "राज्य");
		word("pin", "पिन");
		word("and", "और");
		word("of", "का");
		word("the", "");
		word("in", "में");
		word("at", "पर");
		word("certifying", "प्रमाणन");
		word("authority", "प्राधिकारी");
		word("authorities", "प्राधिकारी");
		word("private", "प्राइवेट");
		word("pvt", "प्राइवेट");
		word("ltd", "लिमिटेड");
		word("limited", "लिमिटेड");
		word("llp", "एलएलपी");
		word("company", "कंपनी");
		word("co", "कंपनी");
		word("corporation", "कॉर्पोरेशन");
		word("corp", "कॉर्पोरेशन");
		word("solutions", "सॉल्यूशंस");
		word("solution", "सॉल्यूशन");
		word("services", "सर्विसेज");
		word("service", "सर्विस");
		word("technology", "टेक्नोलॉजी");
		word("technologies", "टेक्नोलॉजीज");
		word("systems", "सिस्टम्स");
		word("system", "सिस्टम");
		word("digital", "डिजिटल");
		word("enterprise", "एंटरप्राइज");
		word("enterprises", "एंटरप्राइजेज");
		word("software", "सॉफ्टवेयर");
		word("infotech", "इन्फोटेक");
		word("bank", "बैंक");
		word("trust", "ट्रस्ट");
		word("government", "सरकार");
		word("govt", "सरकार");
		word("national", "राष्ट्रीय");
		word("ministry", "मंत्रालय");
		word("electronics", "इलेक्ट्रॉनिक्स");
		word("information", "सूचना");
		word("sample", "सैंपल");
		word("singh", "सिंह");
		word("kumar", "कुमार");
		word("yadav", "यादव");
		word("verma", "वर्मा");
		word("varma", "वर्मा");
		word("khan", "खान");
		word("patel", "पटेल");
		word("reddy", "रेड्डी");
		word("rao", "राव");
		word("das", "दास");
		word("devi", "देवी");
		word("kaur", "कौर");
		word("nath", "नाथ");
		word("prasad", "प्रसाद");
		word("chandra", "चंद्र");
		word("lal", "लाल");
		word("jain", "जैन");
		word("shah", "शाह");
		word("agarwal", "अग्रवाल");
		word("aggarwal", "अग्रवाल");
		word("bansal", "बंसल");
		word("mittal", "मित्तल");
		word("khanna", "खन्ना");
		word("kapoor", "कपूर");
		word("kapur", "कपूर");
		word("mehra", "मेहरा");
		word("saxena", "सक्सेना");
		word("srivastava", "श्रीवास्तव");
		word("choudhary", "चौधरी");
		word("chaudhary", "चौधरी");
		word("choudhury", "चौधरी");
		word("chowdhury", "चौधरी");
		word("mishra", "मिश्रा");
		word("pandey", "पांडे");
		word("tiwari", "तिवारी");
		word("dubey", "दुबे");
		word("shukla", "शुक्ला");
		word("trivedi", "त्रिवेदी");
		word("joshi", "जोशी");
		word("kulkarni", "कुलकर्णी");
		word("deshmukh", "देशमुख");
		word("patil", "पाटिल");
		word("naik", "नाईक");
		word("shetty", "शेट्टी");
		word("nair", "नायर");
		word("menon", "मेनन");
		word("iyer", "अय्यर");
		word("pillai", "पिल्लै");
		word("sinha", "सिन्हा");
		word("jha", "झा");
		word("thakur", "ठाकुर");
		word("chauhan", "चौहान");
		word("rathore", "राठौड़");
		word("rahul", "राहुल");
		word("rajesh", "राजेश");
		word("sanjay", "संजय");
		word("ajay", "अजय");
		word("vijay", "विजय");
		word("deepak", "दीपक");
		word("prakash", "प्रकाश");
		word("ashok", "अशोक");
		word("anil", "अनिल");
		word("sunil", "सुनील");
		word("krishna", "कृष्ण");
		word("sharma", "शर्मा");
		word("gupta", "गुप्ता");
	}

	private HindiScript() {
	}

	static String toHindi(String value) {
		if (value == null || value.isBlank()) {
			return "";
		}
		String working = value.trim();
		for (String[] phrase : PHRASES) {
			working = working.replaceAll("(?i)(?<![A-Za-z])" + Pattern.quote(phrase[0]) + "(?![A-Za-z])",
					Matcher.quoteReplacement(phrase[1]));
		}
		Matcher matcher = TOKEN.matcher(working);
		StringBuilder out = new StringBuilder();
		while (matcher.find()) {
			String token = matcher.group();
			if (token.chars().allMatch(ch -> (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))) {
				out.append(wordToHindi(token.toLowerCase(Locale.ROOT)));
			} else {
				out.append(token);
			}
		}
		return out.toString().replaceAll("[ ]{2,}", " ").replace(" ,", ",").trim();
	}

	private static String wordToHindi(String word) {
		if (WORDS.containsKey(word)) {
			return WORDS.get(word);
		}
		if (word.length() == 1) {
			return letter(word.charAt(0));
		}
		return transliterate(word);
	}

	private static String letter(char ch) {
		return switch (ch) {
			case 'a' -> "ए";
			case 'b' -> "बी";
			case 'c' -> "सी";
			case 'd' -> "डी";
			case 'e' -> "ई";
			case 'f' -> "एफ";
			case 'g' -> "जी";
			case 'h' -> "एच";
			case 'i' -> "आई";
			case 'j' -> "जे";
			case 'k' -> "के";
			case 'l' -> "एल";
			case 'm' -> "एम";
			case 'n' -> "एन";
			case 'o' -> "ओ";
			case 'p' -> "पी";
			case 'q' -> "क्यू";
			case 'r' -> "आर";
			case 's' -> "एस";
			case 't' -> "टी";
			case 'u' -> "यू";
			case 'v' -> "वी";
			case 'w' -> "डब्ल्यू";
			case 'x' -> "एक्स";
			case 'y' -> "वाई";
			case 'z' -> "जेड";
			default -> String.valueOf(ch);
		};
	}

	private static String transliterate(String word) {
		StringBuilder out = new StringBuilder();
		boolean pending = false;
		int i = 0;
		while (i < word.length()) {
			String vowel = matchVowel(word, i);
			if (vowel != null) {
				boolean last = i + vowel.length() == word.length();
				if (pending) {
					if ("a".equals(vowel)) {
						if (last) {
							out.append('ा');
						}
					} else {
						out.append(matra(vowel));
					}
					pending = false;
				} else if ("a".equals(vowel) && last && out.length() > 0) {
					out.append('ा');
				} else {
					out.append(independent(vowel));
				}
				i += vowel.length();
				continue;
			}
			if (word.charAt(i) == 'n' && anusvara(word, i)) {
				pending = false;
				out.append('ं');
				i++;
				continue;
			}
			String consonant = matchConsonant(word, i);
			if (consonant != null) {
				if (pending) {
					out.append('्');
				}
				out.append(glyph(consonant, word, i));
				pending = true;
				i += consonant.length();
				continue;
			}
			pending = false;
			out.append(word.charAt(i));
			i++;
		}
		return out.toString();
	}

	private static String matchVowel(String word, int index) {
		if (index >= word.length()) {
			return null;
		}
		if (index + 1 < word.length()) {
			String two = word.substring(index, index + 2);
			switch (two) {
				case "aa", "ai", "au", "ee", "ii", "oo", "uu", "ou", "oa" -> {
					return two;
				}
				case "ew" -> {
					if (index + 2 == word.length() || !isVowel(word.charAt(index + 2))) {
						return two;
					}
				}
				default -> {
				}
			}
		}
		char ch = word.charAt(index);
		if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
			return String.valueOf(ch);
		}
		return null;
	}

	private static boolean isVowel(char ch) {
		return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
	}

	private static String matchConsonant(String word, int index) {
		String[] keys = {
				"ksh", "chh", "sh", "kh", "gh", "jh", "th", "dh", "ph", "bh", "ck", "ch"
		};
		for (String key : keys) {
			if (word.startsWith(key, index)) {
				return key;
			}
		}
		char ch = word.charAt(index);
		if (ch >= 'b' && ch <= 'z' && ch != 'e' && ch != 'i' && ch != 'o' && ch != 'u') {
			return String.valueOf(ch);
		}
		return null;
	}

	private static boolean anusvara(String word, int index) {
		if (index + 1 >= word.length()) {
			return false;
		}
		char next = word.charAt(index + 1);
		if (!Character.isLetter(next) || matchVowel(word, index + 1) != null) {
			return false;
		}
		return next != 'y' && next != 'r' && next != 'l' && next != 'w' && next != 'h';
	}

	private static String glyph(String key, String word, int index) {
		if ("c".equals(key)) {
			int next = index + 1;
			if (next < word.length()) {
				char ch = word.charAt(next);
				if (ch == 'e' || ch == 'i' || ch == 'y') {
					return "स";
				}
			}
			return "क";
		}
		return switch (key) {
			case "ksh" -> "क्ष";
			case "chh" -> "छ";
			case "sh" -> "श";
			case "kh" -> "ख";
			case "gh" -> "घ";
			case "jh" -> "झ";
			case "th" -> "थ";
			case "dh" -> "ध";
			case "ph" -> "फ";
			case "bh" -> "भ";
			case "ck", "k", "q" -> "क";
			case "ch" -> "च";
			case "b" -> "ब";
			case "d" -> "द";
			case "f" -> "फ";
			case "g" -> "ग";
			case "h" -> "ह";
			case "j" -> "ज";
			case "l" -> "ल";
			case "m" -> "म";
			case "n" -> "न";
			case "p" -> "प";
			case "r" -> "र";
			case "s" -> "स";
			case "t" -> "त";
			case "v", "w" -> "व";
			case "x" -> "क्स";
			case "y" -> "य";
			case "z" -> "ज़";
			default -> key;
		};
	}

	private static String matra(String vowel) {
		return switch (vowel) {
			case "aa" -> "ा";
			case "i" -> "ि";
			case "ee", "ii" -> "ी";
			case "u" -> "ु";
			case "oo", "uu" -> "ू";
			case "e" -> "े";
			case "ai" -> "ै";
			case "o", "oa" -> "ो";
			case "au", "ou" -> "ौ";
			case "ew" -> "्यू";
			default -> "";
		};
	}

	private static String independent(String vowel) {
		return switch (vowel) {
			case "a" -> "अ";
			case "aa" -> "आ";
			case "i" -> "इ";
			case "ee", "ii" -> "ई";
			case "u" -> "उ";
			case "oo", "uu" -> "ऊ";
			case "e" -> "ए";
			case "ai" -> "ऐ";
			case "o", "oa" -> "ओ";
			case "au", "ou" -> "औ";
			case "ew" -> "यू";
			default -> "";
		};
	}

	private static void phrase(String english, String hindi) {
		PHRASES.add(new String[] { english, hindi });
	}

	private static void word(String english, String hindi) {
		WORDS.put(english, hindi);
	}
}
