from pathlib import Path
import re

path = Path(r"c:\Users\hp\Documents\CCABackend\CCABackend\audit-service\src\main\java\in\lms\cca\controller\ApplicationAuditController.java")
text = path.read_text(encoding="utf-8")

patterns = [
    (
        r'InternalAuditMain mainObj = internalMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*if \(mainObj == null\) \{\s*return "<p>No internal audit details available\.</p>";\s*\}',
        'InternalAuditMain mainObj = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}',
    ),
    (
        r'EKYCMonthMain mainObj = monthMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*if \(mainObj == null\) \{\s*return "<p>No eKYC account audit details available\.</p>";\s*\}',
        'EKYCMonthMain mainObj = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}',
    ),
    (
        r'EKYCAccountBasedMain mainObj = accountBasedServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*if \(mainObj == null\) \{\s*return "<p>No eKYC account-based verification details available\.</p>";\s*\}',
        'EKYCAccountBasedMain mainObj = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}',
    ),
    (
        r'ConnectivityMain mainObj = connectivityMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*if \(mainObj == null\) \{\s*return "<p>No CA Software and external connectivity available\.</p>";\s*\}',
        'ConnectivityMain mainObj = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}',
    ),
    (
        r'CATrustedPersonMain mainObj = personMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*if \(mainObj == null\) \{\s*return "<p>No CA Trusted Persons available\.</p>";\s*\}',
        'CATrustedPersonMain mainObj = personMainServ.getByAnnexureId(amain.getAnnexureMainId());\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}',
    ),
]

# Variable-named main objects
named = [
    ("RaAuditMain", "mainObj4", "raAuditServ"),
    ("CourtCasesMain", "mainObj5", "courtCasesServ"),
    ("RevocationMain", "mainObj6", "revocationServ"),
    # crypto / caSw / location / caServices / asp / publicInfo often use mainObj
]

count = 0
for pat, repl in patterns:
    text2, n = re.subn(pat, repl, text, count=1, flags=re.S)
    print(("OK" if n else "MISS"), pat[0:50])
    text = text2
    count += n

# Patch methods that assign then continue without null return, by inserting check after assignment
insert_checks = [
    (r'(RaAuditMain mainObj4 = raAuditServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj4 == null || !shouldIncludeAnnexureSection(mainObj4.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(CourtCasesMain mainObj5 = courtCasesServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj5 == null || !shouldIncludeAnnexureSection(mainObj5.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(RevocationMain mainObj6 = revocationServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj6 == null || !shouldIncludeAnnexureSection(mainObj6.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(CryptoTokenMain mainObj = cryptoTokenMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(CaSwWebMain mainObj = caSwWebMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(LocationMain mainObj = locationMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(CAServicesMain mainObj = caServicesMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(ASPDetails mainObj11 = aspDetailsServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\tif (mainObj11 == null || !shouldIncludeAnnexureSection(mainObj11.getAuditorVerification())) {\n\t\t\treturn "";\n\t\t}\n'),
    (r'(PublicInfoMain mainObj = publicInfoMainServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj == null || !shouldIncludeAnnexureSection(mainObj.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(CertificateCost mainObj13 = certificateCostServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\tif (mainObj13 == null || !shouldIncludeAnnexureSection(mainObj13.getAuditorVerification())) {\n\t\t\treturn "";\n\t\t}\n'),
    (r'(SelfAssessmentMain mainObj14 = selfServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj14 == null || !shouldIncludeAnnexureSection(mainObj14.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
    (r'(DownTime mainObj16 = downTimeServ\.getByAnnexureId\(amain\.getAnnexureMainId\(\)\);\s*)',
     r'\1\n\t\t\tif (mainObj16 == null || !shouldIncludeAnnexureSection(mainObj16.getAuditorVerification())) {\n\t\t\t\treturn "";\n\t\t\t}\n'),
]

for pat, repl in insert_checks:
    # skip if already patched
    marker = "shouldIncludeAnnexureSection"
    # find assignment line context
    m = re.search(pat, text, flags=re.S)
    if not m:
        print("MISS insert", pat[:60])
        continue
    start = m.start()
    window = text[start:start + 250]
    if "shouldIncludeAnnexureSection" in window:
        print("SKIP already", pat[:40])
        continue
    text, n = re.subn(pat, repl, text, count=1, flags=re.S)
    print("INSERT", n, pat[:50])
    count += n

# Replace empty-shell amain==null returns with return "" for annexure helpers only:
# Match blocks that start with resolveAnnexureForReport and immediately build empty tables.
# Safer targeted: if (amain == null) { ... return contentBuilder.toString(); } before else { for known section titles

section_titles = [
    "2. Internal Audit Details",
    "3. eKYC Account Audit Details of last one year - Month-wise",
    "4. eKYC Account-based Verification Enabled by CA - Details of the Audit Period",
    "5. RA Audit Details",
    "6. No of Court Cases",
    "7. No of revocation",
    "8. Empanelment",
    "9. Details of CA software",
    "10. Details of",
    "11. Details of CA Services",
    "12. Details of ASP",
    "13. Public Information",
    "14. Cost of Certificates",
    "15. CA Self-assessment",
    "16. CA Software and external connectivity",
    "18. List of CA Trusted Persons",
]

for title in section_titles:
    # find title occurrence in empty shell and replace whole if(amain==null) block return
    pass

# Generic replace: for methods still returning empty shells when amain==null,
# convert first branch to return "" when the next else uses shouldIncludeAnnexureSection already.
# Regex: if (amain == null) { <anything without shouldInclude> return contentBuilder.toString(); } else {
# Limit to private String annexure-like methods by only applying where else branch already has shouldInclude.

def collapse_empty_amain(src: str) -> str:
    pattern = re.compile(
        r'AnnexureMain amain = resolveAnnexureForReport\(username\);\s*'
        r'if \(amain == null\) \{.*?\n\t\t\treturn contentBuilder\.toString\(\);\s*'
        r'\} else \{',
        re.S,
    )
    def repl(m):
        block = m.group(0)
        # only collapse if this is clearly an empty shell (contains blank cells loop or empty spaces)
        if 'shouldIncludeAnnexureSection' in block:
            return block
        return 'AnnexureMain amain = resolveAnnexureForReport(username);\n\t\tif (amain == null) {\n\t\t\treturn "";\n\t\t} else {'
    new_src, n = pattern.subn(repl, src)
    print("collapsed amain null shells", n)
    return new_src

text = collapse_empty_amain(text)

path.write_text(text, encoding="utf-8")
print("done total ops", count)
