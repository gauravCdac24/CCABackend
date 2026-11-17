package in.lms.cca.util.global;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import in.lms.cca.entity.AspAuditReport;

public final class AspAuditReportFiles {

	public static final String FOLDER = "ASPAuditReport/Document";

	private AspAuditReportFiles() {
	}

	public static byte[] read(AspAuditReport report) throws IOException {
		if (report == null || report.getFileName() == null || report.getFileName().isBlank()) {
			throw new IOException("ASP audit report file is missing.");
		}
		Path path = Paths.get(RealPath.REAL_PATH, FOLDER, report.getFileName());
		if (!Files.exists(path)) {
			throw new IOException("ASP audit report file was not found.");
		}
		return Files.readAllBytes(path);
	}

	public static boolean isPdf(byte[] bytes) {
		return bytes != null && bytes.length > 4
				&& bytes[0] == '%' && bytes[1] == 'P' && bytes[2] == 'D' && bytes[3] == 'F';
	}

	public static String extension(String fileName) {
		if (fileName == null) {
			return "";
		}
		int dot = fileName.lastIndexOf('.');
		return dot < 0 ? "" : fileName.substring(dot).toLowerCase();
	}

	public static String contentType(String fileName, byte[] bytes) {
		if (isPdf(bytes)) {
			return "application/pdf";
		}
		if (isDocx(bytes) || ".docx".equals(extension(fileName))) {
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		if (".doc".equals(extension(fileName))) {
			return "application/msword";
		}
		if (".pdf".equals(extension(fileName))) {
			return "application/pdf";
		}
		return "application/octet-stream";
	}

	public static String downloadName(String fileName, byte[] bytes) {
		String name = fileName == null || fileName.isBlank() ? "ASPReports" : fileName;
		if (isPdf(bytes) && !name.toLowerCase().endsWith(".pdf")) {
			return name + ".pdf";
		}
		if (isDocx(bytes) && name.toLowerCase().endsWith(".pdf")) {
			return name.substring(0, name.length() - 4) + ".docx";
		}
		return name;
	}

	/** One stored file is returned as uploaded. Several files become one PDF, oldest audit date first. */
	public static byte[] combinedPdf(List<AspAuditReport> reports) throws Exception {
		List<byte[]> parts = new ArrayList<>();
		for (AspAuditReport report : reports) {
			byte[] stored = read(report);
			parts.add(isPdf(stored) ? stored : textAsPdf(plainText(stored, report.getFileName())));
		}
		if (parts.size() == 1) {
			return parts.get(0);
		}
		ByteArrayOutputStream merged = new ByteArrayOutputStream();
		try (PdfDocument destination = new PdfDocument(new PdfWriter(merged))) {
			PdfMerger merger = new PdfMerger(destination);
			for (byte[] part : parts) {
				PdfReader reader = new PdfReader(new ByteArrayInputStream(part));
				reader.setUnethicalReading(true);
				try (PdfDocument source = new PdfDocument(reader)) {
					if (source.getNumberOfPages() > 0) {
						merger.merge(source, 1, source.getNumberOfPages());
					}
				}
			}
		}
		return merged.toByteArray();
	}

	private static byte[] textAsPdf(String text) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (PdfDocument pdf = new PdfDocument(new PdfWriter(out));
				Document document = new Document(pdf)) {
			String body = text == null || text.isBlank() ? "ASP audit report" : text;
			document.add(new Paragraph(body));
		}
		return out.toByteArray();
	}

	private static boolean isDocx(byte[] bytes) {
		return bytes != null && bytes.length > 4 && bytes[0] == 'P' && bytes[1] == 'K';
	}

	private static String plainText(byte[] bytes, String fileName) throws IOException {
		String ext = extension(fileName);
		if (isDocx(bytes) || ".docx".equals(ext)) {
			return docxText(bytes);
		}
		String raw = new String(bytes, StandardCharsets.ISO_8859_1);
		StringBuilder readable = new StringBuilder();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < raw.length(); i++) {
			char ch = raw.charAt(i);
			if (ch >= 32 && ch < 127) {
				word.append(ch);
			} else if (word.length() >= 4) {
				readable.append(word).append('\n');
				word.setLength(0);
			} else {
				word.setLength(0);
			}
		}
		if (word.length() >= 4) {
			readable.append(word);
		}
		return readable.toString();
	}

	private static String docxText(byte[] bytes) throws IOException {
		try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes))) {
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null) {
				if (!"word/document.xml".equals(entry.getName())) {
					continue;
				}
				String xml = new String(zip.readAllBytes(), StandardCharsets.UTF_8);
				return xml.replaceAll("</w:p>", "\n")
						.replaceAll("<[^>]+>", "")
						.replace("&amp;", "&")
						.replace("&lt;", "<")
						.replace("&gt;", ">")
						.replace("&quot;", "\"")
						.trim();
			}
		}
		return "";
	}
}
