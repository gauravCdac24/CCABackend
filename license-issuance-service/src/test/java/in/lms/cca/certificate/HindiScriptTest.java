package in.lms.cca.certificate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HindiScriptTest {

	@Test
	void hindiParagraphUsesHindiForNameAndAddress() {
		assertEquals("गौरव सिंह", HindiScript.toHindi("Gaurav Singh"));
		assertEquals("दिल्ली, दिल्ली, दिल्ली, दिल्ली, जसोला, दिल्ली, 110063, भारत",
				HindiScript.toHindi("Delhi, Delhi, Delhi, Delhi, Jasola, Delhi, 110063, India"));
	}

	@Test
	void keepsEnglishParagraphWordsTranslatableWithoutChangingDigitsOrHindi() {
		assertEquals("सैंपल प्रमाणन प्राधिकारी प्राइवेट. लिमिटेड.",
				HindiScript.toHindi("Sample Certifying Authority Pvt. Ltd."));
		assertEquals("ब्लॉक 12, कनॉट प्लेस, नई दिल्ली, 110001",
				HindiScript.toHindi("Block 12, Connaught Place, New Delhi, 110001"));
		assertEquals("गौरव सिंह", HindiScript.toHindi("गौरव सिंह"));
		assertEquals("सुरेश", HindiScript.toHindi("Suresh"));
		assertEquals("अमित", HindiScript.toHindi("Amit"));
		assertEquals("रवि", HindiScript.toHindi("Ravi"));
		assertEquals("अंकित", HindiScript.toHindi("Ankit"));
		assertEquals("", HindiScript.toHindi("  "));
	}
}
