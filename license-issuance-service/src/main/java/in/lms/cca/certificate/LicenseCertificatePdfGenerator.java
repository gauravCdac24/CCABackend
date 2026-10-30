package in.lms.cca.certificate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

@Component
public class LicenseCertificatePdfGenerator {

	private static final float IMAGE_WIDTH = 1654f;
	private static final float IMAGE_HEIGHT = 2339f;
	private static final String[] ENGLISH_MONTHS = {
			"January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"
	};
	private static final float HINDI_NAME_X = 435f;
	private static final float HINDI_NAME_BASELINE = 809f;
	private static final float HINDI_NAME_WIDTH = 1095f;
	private static final float HINDI_LOCATION_X = 285f;
	private static final float HINDI_LOCATION_BASELINE = 863f;
	private static final float HINDI_LOCATION_WIDTH = 1245f;
	private static final float HINDI_FONT_PX = 32f;

	private volatile Font hindiBase;

	public byte[] render(LicenseCertificateContent content) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(output);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.getDocumentInfo().setTitle("Licence to act as a Certifying Authority");
		PdfPage page = pdf.addNewPage(PageSize.A4);
		PdfCanvas canvas = new PdfCanvas(page);
		float pageWidth = page.getPageSize().getWidth();
		float pageHeight = page.getPageSize().getHeight();

		canvas.addImageFittedIntoRectangle(image("form-blank.png"),
				new Rectangle(0, 0, pageWidth, pageHeight), false);

		PdfFont valueFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
		PdfFont keyFont = PdfFontFactory.createFont(StandardFonts.COURIER);

		String name = latin(content.getAuthorityName());
		String location = latin(content.getLocation());
		LocalDate issued = toLocalDate(content.getIssueDate());

		drawHindiLine(canvas, content.getAuthorityName(), HINDI_NAME_X, HINDI_NAME_BASELINE, HINDI_NAME_WIDTH,
				pageWidth, pageHeight);
		drawHindiLine(canvas, content.getLocation(), HINDI_LOCATION_X, HINDI_LOCATION_BASELINE, HINDI_LOCATION_WIDTH,
				pageWidth, pageHeight);
		drawFitted(canvas, valueFont, name, 388, 1144, 1142, 12f, pageWidth, pageHeight);
		drawFitted(canvas, valueFont, location, 258, 1194, 1272, 12f, pageWidth, pageHeight);

		if (issued != null) {
			drawFitted(canvas, valueFont, String.valueOf(issued.getDayOfMonth()), 813, 1009, 60, 12f, pageWidth,
					pageHeight);
			drawHindiMonth(canvas, valueFont, issued, 953, 1009, 208, pageWidth, pageHeight);
			drawFitted(canvas, valueFont, ordinal(issued.getDayOfMonth()), 1153, 1330, 72, 11f, pageWidth, pageHeight);
			String englishMonth = ENGLISH_MONTHS[issued.getMonthValue() - 1] + ", " + issued.getYear();
			drawFitted(canvas, valueFont, englishMonth, 1327, 1330, 172, 11f, pageWidth, pageHeight);
		}

		drawPublicKey(canvas, keyFont, content.getPublicKey(), 120, 1696, 1414, 2189, pageWidth, pageHeight);
		pdf.close();
		return output.toByteArray();
	}

	private void drawHindiMonth(PdfCanvas canvas, PdfFont font, LocalDate issued, float imageX, float baseline,
			float maxWidth, float pageWidth, float pageHeight) throws IOException {
		ImageData month = image(String.format("month-%02d.png", issued.getMonthValue()));
		float targetHeight = toPdf(34f, pageHeight, false);
		float targetWidth = targetHeight * month.getWidth() / month.getHeight();
		float maxPdf = toPdf(maxWidth, pageWidth, true);
		if (targetWidth > maxPdf * 0.62f) {
			targetWidth = maxPdf * 0.62f;
			targetHeight = targetWidth * month.getHeight() / month.getWidth();
		}
		float pdfX = toPdf(imageX, pageWidth, true);
		float pdfBaseline = pageHeight - toPdf(baseline, pageHeight, false);
		canvas.addImageFittedIntoRectangle(month,
				new Rectangle(pdfX, pdfBaseline + 1.5f, targetWidth, targetHeight), false);
		drawFitted(canvas, font, String.valueOf(issued.getYear()),
				imageX + (targetWidth / pageWidth * IMAGE_WIDTH) + 8, baseline, maxWidth * 0.4f, 12f, pageWidth,
				pageHeight);
	}

	void paintHindiOn(BufferedImage form, String name, String location) throws IOException {
		Graphics2D graphics = form.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			drawStamp(graphics, name, HINDI_NAME_X, HINDI_NAME_BASELINE, HINDI_NAME_WIDTH);
			drawStamp(graphics, location, HINDI_LOCATION_X, HINDI_LOCATION_BASELINE, HINDI_LOCATION_WIDTH);
		} finally {
			graphics.dispose();
		}
	}

	private void drawHindiLine(PdfCanvas canvas, String value, float imageX, float baseline, float maxWidth,
			float pageWidth, float pageHeight) throws IOException {
		Placed placed = place(value, imageX, baseline, maxWidth);
		if (placed == null) {
			return;
		}
		ByteArrayOutputStream png = new ByteArrayOutputStream();
		ImageIO.write(placed.raster, "png", png);
		float pdfX = toPdf(placed.x, pageWidth, true);
		float pdfW = toPdf(placed.width, pageWidth, true);
		float pdfH = toPdf(placed.height, pageHeight, false);
		float pdfY = pageHeight - toPdf(placed.y + placed.height, pageHeight, false);
		canvas.addImageFittedIntoRectangle(ImageDataFactory.create(png.toByteArray()),
				new Rectangle(pdfX, pdfY, pdfW, pdfH), false);
	}

	private void drawStamp(Graphics2D graphics, String value, float imageX, float baseline, float maxWidth)
			throws IOException {
		Placed placed = place(value, imageX, baseline, maxWidth);
		if (placed == null) {
			return;
		}
		graphics.drawImage(placed.raster, Math.round(placed.x), Math.round(placed.y), null);
	}

	private Placed place(String value, float imageX, float baseline, float maxWidth) throws IOException {
		String hindi = HindiScript.toHindi(value);
		if (hindi.isBlank()) {
			return null;
		}
		HindiRaster raster = rasterize(hindi, maxWidth);
		float nudge = 2f * IMAGE_HEIGHT / PageSize.A4.getHeight();
		float top = baseline - nudge - (raster.height - raster.bottomToBaseline);
		return new Placed(raster.image, imageX, top, raster.width, raster.height);
	}

	private HindiRaster rasterize(String text, float maxWidth) throws IOException {
		Font hindi = hindiTypeface();
		Font latin = new Font("Serif", Font.BOLD, Math.round(HINDI_FONT_PX));
		float size = HINDI_FONT_PX;
		TextLayout layout = layout(text, hindi, latin, size);
		float width = textWidth(layout);
		if (width > maxWidth && width > 0f) {
			size = Math.max(16f, size * maxWidth / width);
			layout = layout(text, hindi, latin, size);
			width = textWidth(layout);
		}
		Rectangle2D bounds = layout.getBounds();
		float padX = 4f;
		float padTop = 4f;
		float padBottom = 3f;
		float left = padX + (float) Math.max(0, -bounds.getX());
		int pixelWidth = Math.max(1, (int) Math.ceil(left + bounds.getWidth() + padX));
		int baselineY = (int) Math.ceil(padTop + layout.getAscent());
		int pixelHeight = Math.max(1, (int) Math.ceil(baselineY + layout.getDescent() + padBottom));
		BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics.setColor(Color.BLACK);
		layout.draw(graphics, left, baselineY);
		graphics.dispose();
		return new HindiRaster(image, pixelWidth, pixelHeight, pixelHeight - baselineY);
	}

	private TextLayout layout(String text, Font hindi, Font latin, float size) {
		Font hindiSized = hindi.deriveFont(size);
		Font latinSized = latin.deriveFont(Font.BOLD, size);
		AttributedString styled = new AttributedString(text);
		int index = 0;
		while (index < text.length()) {
			boolean useHindi = hindiSized.canDisplay(text.charAt(index));
			int next = index + 1;
			while (next < text.length() && hindiSized.canDisplay(text.charAt(next)) == useHindi) {
				next++;
			}
			styled.addAttribute(TextAttribute.FONT, useHindi ? hindiSized : latinSized, index, next);
			index = next;
		}
		FontRenderContext context = new FontRenderContext(null, true, true);
		return new TextLayout(styled.getIterator(), context);
	}

	private static float textWidth(TextLayout layout) {
		Rectangle2D bounds = layout.getBounds();
		float width = layout.getAdvance();
		if (bounds.getX() < 0) {
			width += (float) -bounds.getX();
		}
		return width;
	}

	private Font hindiTypeface() throws IOException {
		Font loaded = hindiBase;
		if (loaded != null) {
			return loaded;
		}
		synchronized (this) {
			if (hindiBase == null) {
				try (InputStream input = new ByteArrayInputStream(resource("NotoSansDevanagari-Bold.ttf"))) {
					hindiBase = Font.createFont(Font.TRUETYPE_FONT, input);
				} catch (FontFormatException ex) {
					throw new IOException("Devanagari font could not be loaded.", ex);
				}
			}
			return hindiBase;
		}
	}

	private static final class HindiRaster {
		private final BufferedImage image;
		private final int width;
		private final int height;
		private final int bottomToBaseline;

		private HindiRaster(BufferedImage image, int width, int height, int bottomToBaseline) {
			this.image = image;
			this.width = width;
			this.height = height;
			this.bottomToBaseline = bottomToBaseline;
		}
	}

	private static final class Placed {
		private final BufferedImage raster;
		private final float x;
		private final float y;
		private final float width;
		private final float height;

		private Placed(BufferedImage raster, float x, float y, float width, float height) {
			this.raster = raster;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	private void drawPublicKey(PdfCanvas canvas, PdfFont font, String publicKey, float imageX, float topBaseline,
			float maxWidth, float bottom, float pageWidth, float pageHeight) {
		String key = publicKey == null ? "" : publicKey.trim();
		if (key.isEmpty()) {
			return;
		}
		float size = 8f;
		float maxPdf = toPdf(maxWidth, pageWidth, true);
		List<String> lines = wrap(key, font, size, maxPdf);
		float lineHeight = size + 1.4f;
		float y = pageHeight - toPdf(topBaseline, pageHeight, false);
		float x = toPdf(imageX, pageWidth, true);
		float floor = pageHeight - toPdf(bottom, pageHeight, false);
		canvas.beginText();
		canvas.setFontAndSize(font, size);
		for (String line : lines) {
			if (y < floor) {
				break;
			}
			canvas.setTextMatrix(x, y);
			canvas.showText(line);
			y -= lineHeight;
		}
		canvas.endText();
	}

	private void drawFitted(PdfCanvas canvas, PdfFont font, String text, float imageX, float baseline, float maxWidth,
			float size, float pageWidth, float pageHeight) {
		if (text == null || text.isBlank()) {
			return;
		}
		float maxPdf = toPdf(maxWidth, pageWidth, true);
		float fitted = size;
		while (fitted > 6f && font.getWidth(text, fitted) > maxPdf) {
			fitted -= 0.25f;
		}
		float x = toPdf(imageX, pageWidth, true);
		float y = pageHeight - toPdf(baseline, pageHeight, false) + 2f;
		canvas.beginText();
		canvas.setFontAndSize(font, fitted);
		canvas.moveText(x, y);
		canvas.showText(text);
		canvas.endText();
	}

	private static List<String> wrap(String text, PdfFont font, float size, float maxWidth) {
		List<String> lines = new ArrayList<>();
		for (String rawLine : text.split("\\R")) {
			String pending = rawLine;
			if (pending.isEmpty()) {
				lines.add(" ");
				continue;
			}
			while (!pending.isEmpty()) {
				int fit = pending.length();
				while (fit > 1 && font.getWidth(pending.substring(0, fit), size) > maxWidth) {
					fit--;
				}
				lines.add(pending.substring(0, fit));
				pending = pending.substring(fit);
			}
		}
		return lines;
	}

	private static float toPdf(float imagePixels, float pageSize, boolean horizontal) {
		return imagePixels * pageSize / (horizontal ? IMAGE_WIDTH : IMAGE_HEIGHT);
	}

	private static String ordinal(int day) {
		int mod100 = day % 100;
		if (mod100 >= 11 && mod100 <= 13) {
			return day + "th";
		}
		return switch (day % 10) {
			case 1 -> day + "st";
			case 2 -> day + "nd";
			case 3 -> day + "rd";
			default -> day + "th";
		};
	}

	private static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private static String latin(String value) {
		if (value == null) {
			return "";
		}
		StringBuilder kept = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == '\t') {
				kept.append(' ');
			} else if (ch >= 32 && ch <= 255) {
				kept.append(ch);
			}
		}
		return kept.toString().trim();
	}

	private ImageData image(String name) throws IOException {
		return ImageDataFactory.create(resource(name));
	}

	private byte[] resource(String name) throws IOException {
		try (InputStream input = getClass().getResourceAsStream("/certificate/" + name)) {
			if (input == null) {
				throw new IOException("Certificate resource is missing: " + name);
			}
			return input.readAllBytes();
		}
	}
}
