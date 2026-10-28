-- Run once on ccadb to align audit sub-criteria flags with Admin UI expectations.
-- Inactive rows must not appear in auditor Detailed Audit Controls.

UPDATE admin_user_management.audit_subcriteria
SET status = TRIM(status)
WHERE status IS NOT NULL;

UPDATE admin_user_management.audit_subcriteria
SET isvisible = false
WHERE LOWER(TRIM(status)) = 'inactive';

UPDATE admin_user_management.audit_subcriteria
SET isvisible = false
WHERE LOWER(TRIM(status)) <> 'active';

-- Verify disabled sections (expect 0 rows):
-- SELECT audit_subcriteria_id, audit_subcriteria_title, status, isvisible
-- FROM admin_user_management.audit_subcriteria
-- WHERE audit_subcriteria_id IN (59, 60, 61, 62);
