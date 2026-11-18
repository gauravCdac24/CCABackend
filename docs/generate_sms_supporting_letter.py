"""Generate SMS supporting letter (.docx) for CCALMS project."""

from datetime import date
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

OUTPUT_PATH = r"c:\Users\hp\Documents\CCABackend\docs\CCALMS_SMS_Service_Supporting_Letter.docx"


def set_cell_shading(cell, fill_hex: str):
    shading = OxmlElement("w:shd")
    shading.set(qn("w:fill"), fill_hex)
    cell._tc.get_or_add_tcPr().append(shading)


def add_heading(doc, text, level=1):
    p = doc.add_heading(text, level=level)
    return p


def build_document():
    doc = Document()

    # Page margins
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)

    # Letterhead
    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = title.add_run("SUPPORTING LETTER")
    run.bold = True
    run.font.size = Pt(16)
    run.font.color.rgb = RGBColor(0x1F, 0x4E, 0x79)

    sub = doc.add_paragraph()
    sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = sub.add_run(
        "Request for Allocation / Reimbursement of SMS Gateway Charges\n"
        "Certifying Authority License Management System (CCALMS)"
    )
    r.font.size = Pt(11)
    r.bold = True

    doc.add_paragraph()

    meta = doc.add_paragraph()
    meta.add_run(f"Date: {date.today().strftime('%d-%m-%Y')}\n").bold = True
    meta.add_run("Reference: CCALMS/SMS/DLT/2026\n").bold = True
    meta.add_run("Subject: ").bold = True
    meta.add_run(
        "Justification for use of SMS services and TRAI DLT-compliant transactional messaging "
        "under User ID "
    )
    meta.add_run("ccalmsdelhi").bold = True
    meta.add_run(" (Sender ID: ")
    meta.add_run("CCALMS").bold = True
    meta.add_run(")")

    doc.add_paragraph()
    doc.add_paragraph("To,")
    doc.add_paragraph("The Competent Financial / Administrative Authority")
    doc.add_paragraph("Controller of Certifying Authorities (CCA)")
    doc.add_paragraph("Ministry of Electronics and Information Technology (MeitY)")
    doc.add_paragraph("Government of India")
    doc.add_paragraph()

    doc.add_paragraph("Respected Sir/Madam,")
    doc.add_paragraph()

    intro = (
        "This letter is submitted in support of the budgetary requirement for SMS services "
        "used in the Certifying Authority License Management System (CCALMS), developed and "
        "operated for the Controller of Certifying Authorities (CCA). The application is an "
        "end-to-end digital platform for intent registration, license application, review, audit, "
        "issuance, renewal, and lifecycle management of Certifying Authorities (CAs) in India."
    )
    doc.add_paragraph(intro)

    add_heading(doc, "1. Purpose of This Letter", level=2)
    doc.add_paragraph(
        "To explain why SMS services are an essential and mandatory component of CCALMS; to "
        "document the approved TRAI DLT (Distributed Ledger Technology) configuration; and to "
        "support release of funds for prepaid SMS credits on the Government of India approved "
        "SMS gateway used by the project."
    )

    add_heading(doc, "2. Why SMS Services Are Used in CCALMS", level=2)

    reasons = [
        (
            "Secure authentication (OTP delivery)",
            "CCALMS uses SMS to deliver One-Time Passwords (OTPs) during user login and "
            "sensitive operations via the auth-service. OTP over SMS provides a second factor "
            "alongside email and is critical for applicants, licensees, CCA officers, review "
            "committee members, and audit agencies accessing the system."
        ),
        (
            "Intent registration and onboarding",
            "When a prospective Certifying Authority registers intent, a unique verification code "
            "is sent through SMS (and email) so that only the legitimate applicant with access "
            "to the registered mobile number can proceed with the license application."
        ),
        (
            "Transactional status alerts",
            "The notification-service dispatches SMS for application milestones—submission, "
            "review outcomes, audit scheduling, license issuance, and renewal reminders—ensuring "
            "timely communication even when users are not actively checking email or the dashboard."
        ),
        (
            "Regulatory and audit trail requirements",
            "SMS delivery logs, along with email and in-app dashboard notifications, form part of "
            "the auditable communication record required for a government licensing system handling "
            "sensitive PKI / digital certificate ecosystem data."
        ),
        (
            "Inclusivity and reach",
            "Many stakeholders rely primarily on mobile phones. SMS ensures that critical alerts "
            "reach users in rural and semi-urban areas with limited email access, supporting "
            "MeitY’s objective of accessible digital governance."
        ),
    ]

    for i, (heading, body) in enumerate(reasons, 1):
        p = doc.add_paragraph()
        p.add_run(f"2.{i} {heading}").bold = True
        doc.add_paragraph(body)

    add_heading(doc, "3. Technical Architecture of SMS Integration", level=2)

    tech_points = [
        "Microservice: notification-service (Spring Boot), registered on Eureka service discovery.",
        "SMS Gateway: Government of India MSDG DLT platform — https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT",
        "Integration class: SingleSignSMS.java — sends SHA-512 hashed, DLT-compliant single messages.",
        "Triggering services: auth-service (OTP), admin-service (registration codes), license-issuance-service, and other modules via REST notification API.",
        "Notification types supported: SMS, Email, Dashboard, Email & SMS, SMS & Dashboard, and All.",
    ]
    for point in tech_points:
        doc.add_paragraph(point, style="List Bullet")

    add_heading(doc, "4. SMS Gateway Credentials & Configuration", level=2)

    config_table = doc.add_table(rows=1, cols=2)
    config_table.style = "Table Grid"
    config_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    hdr = config_table.rows[0].cells
    hdr[0].text = "Parameter"
    hdr[1].text = "Value"
    for cell in hdr:
        for p in cell.paragraphs:
            for r in p.runs:
                r.bold = True
        set_cell_shading(cell, "D9E2F3")

    config_rows = [
        ("SMS User ID / Username", "ccalmsdelhi"),
        ("Sender ID (Header)", "CCALMS"),
        ("SMS Service Type", "singlemsg (Transactional)"),
        ("DLT Template ID (Primary)", "1407174244996769199"),
        ("API Endpoint", "https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT"),
        ("Hash Algorithm", "SHA-512 (DLT key generation)"),
        ("Password Encryption", "SHA-1 before API submission"),
        ("Principal Application", "CCALMS – CCA License Management System"),
    ]

    for param, value in config_rows:
        row = config_table.add_row().cells
        row[0].text = param
        row[1].text = value
        if param == "SMS User ID / Username":
            for p in row[1].paragraphs:
                for r in p.runs:
                    r.bold = True

    doc.add_paragraph()

    add_heading(doc, "5. TRAI DLT Compliance Matrix", level=2)
    doc.add_paragraph(
        "The following matrix documents the TRAI Telecommunications Commercial Communications "
        "Customer Preference Regulations (TCCCPR) compliant DLT registration details for SMS "
        "messages transmitted through CCALMS. All templates are registered under the Principal "
        "Entity for CCA/CDAC and mapped to User ID "
    )
    p = doc.add_paragraph()
    p.add_run("ccalmsdelhi").bold = True
    p.add_run(".")

    matrix_headers = [
        "S.No.",
        "Module / Use Case",
        "Message Category (TRAI)",
        "Sender ID",
        "DLT Template ID",
        "Template Content (Registered)",
        "Trigger Event",
        "User ID",
    ]

    matrix_data = [
        (
            "1",
            "User Login – OTP",
            "Service-Implicit (Transactional)",
            "CCALMS",
            "1407174244996769199",
            "Please do not share your OTP with anyone, your OTP is {#var#}",
            "User authentication / login",
            "ccalmsdelhi",
        ),
        (
            "2",
            "Intent Registration – Unique Code",
            "Service-Implicit (Transactional)",
            "CCALMS",
            "1407174244996769199",
            "Please do not share your code with anyone, your code is {#var#}",
            "New applicant intent registration",
            "ccalmsdelhi",
        ),
        (
            "3",
            "Application Submission Confirmation",
            "Service-Implicit (Transactional)",
            "CCALMS",
            "1407174244996769199",
            "Dear {#var#}, your intent to become a Certifying Authority submitted successfully. You can login & submit the License Application using the login credentials sent to your registered email address. CCA-MeitY",
            "Successful intent / application submission",
            "ccalmsdelhi",
        ),
        (
            "4",
            "Workflow Notifications (Review / Audit / Issuance)",
            "Service-Implicit (Transactional)",
            "CCALMS",
            "1407174244996769199",
            "{#var#} – dynamic system-generated status message",
            "Application status change across CCALMS modules",
            "ccalmsdelhi",
        ),
        (
            "5",
            "Password / Credential Alerts",
            "Service-Implicit (Transactional)",
            "CCALMS",
            "1407174244996769199",
            "{#var#} – credential and account-related alerts",
            "Account creation, reset, and security notifications",
            "ccalmsdelhi",
        ),
    ]

    matrix_table = doc.add_table(rows=1, cols=len(matrix_headers))
    matrix_table.style = "Table Grid"
    matrix_table.alignment = WD_TABLE_ALIGNMENT.CENTER

    header_row = matrix_table.rows[0].cells
    for i, h in enumerate(matrix_headers):
        header_row[i].text = h
        set_cell_shading(header_row[i], "1F4E79")
        for para in header_row[i].paragraphs:
            for r in para.runs:
                r.bold = True
                r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)
                r.font.size = Pt(9)

    for row_data in matrix_data:
        row = matrix_table.add_row().cells
        for i, val in enumerate(row_data):
            row[i].text = val
            for para in row[i].paragraphs:
                for r in para.runs:
                    r.font.size = Pt(8)

    doc.add_paragraph()
    note = doc.add_paragraph()
    note.add_run("Note: ").bold = True
    note.add_run(
        "{#var#} denotes DLT-approved variable placeholders. No promotional or marketing SMS "
        "is sent from CCALMS. All messages are transactional/service-implicit in nature, "
        "initiated by user action or system workflow events."
    )

    add_heading(doc, "6. Estimated SMS Volume & Fund Requirement", level=2)
    doc.add_paragraph(
        "SMS charges are incurred on a per-message basis through the prepaid MSDG DLT account "
        "linked to User ID ccalmsdelhi. Estimated annual consumption depends on:"
    )
    volume_items = [
        "Number of registered applicants, licensees, and government users",
        "OTP generation during peak login periods",
        "Application lifecycle events (review, audit, issuance, renewal)",
        "System-generated reminders and compliance notifications",
    ]
    for item in volume_items:
        doc.add_paragraph(item, style="List Bullet")

    doc.add_paragraph(
        "It is requested that adequate budgetary provision be made for annual SMS credits "
        "top-up on the ccalmsdelhi account to ensure uninterrupted delivery of mandatory "
        "transactional messages. Interruption of SMS services would directly impact user "
        "authentication (OTP), applicant onboarding, and statutory communication obligations "
        "under the CCA licensing framework."
    )

    add_heading(doc, "7. Compliance Statement", level=2)
    compliance = [
        "All SMS traffic is routed exclusively through the Government of India approved DLT platform.",
        "Sender ID CCALMS and User ID ccalmsdelhi are used only for registered transactional templates.",
        "No unsolicited commercial communication (UCC) or promotional messaging is undertaken.",
        "Message content adheres to registered DLT templates with variable substitution only.",
        "SMS is used as part of a multi-channel strategy alongside email and in-app dashboard notifications.",
    ]
    for item in compliance:
        doc.add_paragraph(item, style="List Bullet")

    add_heading(doc, "8. Conclusion & Request", level=2)
    doc.add_paragraph(
        "SMS services are not optional for CCALMS—they are integral to secure access, applicant "
        "verification, and timely dissemination of licensing workflow information to stakeholders "
        "of the Controller of Certifying Authorities. We request approval and release of funds "
        "for SMS gateway charges under User ID ccalmsdelhi, Sender ID CCALMS, in accordance "
        "with the TRAI DLT matrix documented above."
    )

    doc.add_paragraph()
    doc.add_paragraph("Thanking you,")
    doc.add_paragraph()
    doc.add_paragraph("Yours faithfully,")
    doc.add_paragraph()
    doc.add_paragraph("_" * 40)
    sig = doc.add_paragraph()
    sig.add_run("(Authorized Signatory)\n").bold = True
    sig.add_run("Project In-Charge – CCALMS\n")
    sig.add_run("Centre for Development of Advanced Computing (CDAC), Delhi\n")
    sig.add_run("On behalf of Controller of Certifying Authorities (CCA)\n")
    sig.add_run("Ministry of Electronics and Information Technology (MeitY)")

    doc.add_paragraph()
    enclosure = doc.add_paragraph()
    enclosure.add_run("Enclosures:\n").bold = True
    enclosure.add_run("1. TRAI DLT Template Registration Screenshot / Certificate (if available)\n")
    enclosure.add_run("2. SMS API integration architecture diagram\n")
    enclosure.add_run("3. Sample transactional SMS delivery logs from notification-service")

    doc.save(OUTPUT_PATH)
    print(f"Created: {OUTPUT_PATH}")


if __name__ == "__main__":
    build_document()
