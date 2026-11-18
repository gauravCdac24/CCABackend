-- Align annual application_action_taken with fresh audit_management schema
-- so per-control applicant remarks/files can be stored (Hibernate ddl-auto=update also adds these).

ALTER TABLE annual_audit_management.application_action_taken
    ALTER COLUMN action_report TYPE character varying(100);

ALTER TABLE annual_audit_management.application_action_taken
    ADD COLUMN IF NOT EXISTS user_name character varying(74);

ALTER TABLE annual_audit_management.application_action_taken
    ADD COLUMN IF NOT EXISTS criteria_id bigint;

ALTER TABLE annual_audit_management.application_action_taken
    ADD COLUMN IF NOT EXISTS auditor_remarks text;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'fk_application_action_taken_criteria'
    ) THEN
        ALTER TABLE annual_audit_management.application_action_taken
            ADD CONSTRAINT fk_application_action_taken_criteria
            FOREIGN KEY (criteria_id)
            REFERENCES annual_audit_management."audit_report-criteria"(criteria_id);
    END IF;
END $$;

ALTER TABLE annual_audit_management."audit_report-criteria"
    ADD COLUMN IF NOT EXISTS remarks text;
