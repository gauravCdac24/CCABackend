-- Diagnose & fix payment verification visibility for UCCA20260057
-- Run against your PostgreSQL DB (schema: new_license_management)

-- 1) Current state
SELECT ia.intent_app_id,
       ia.user_name,
       ia.application_status,
       ia.created,
       pp.payment_proof_id,
       pp.status AS payment_proof_status,
       pp.created AS payment_proof_uploaded
FROM new_license_management.intent_application ia
LEFT JOIN new_license_management.payment_proof pp
  ON ia.intent_app_id = pp.intent_app_id
WHERE ia.user_name = 'UCCA20260057'
ORDER BY ia.intent_app_id DESC, pp.payment_proof_id DESC;

-- 2) Timeline (what applicant tracker shows)
SELECT atl.app_timeline_id,
       atl.intent_app_id,
       atl.application_status,
       atl.created
FROM new_license_management.application_timeline atl
INNER JOIN new_license_management.intent_application ia
  ON ia.intent_app_id = atl.intent_app_id
WHERE ia.user_name = 'UCCA20260057'
ORDER BY atl.created DESC;

-- 3) Fix: align intent status when active payment proof exists
UPDATE new_license_management.intent_application ia
SET application_status = 'PaymentProof Recieved',
    updated = NOW()
FROM new_license_management.payment_proof pp
WHERE ia.intent_app_id = pp.intent_app_id
  AND ia.user_name = 'UCCA20260057'
  AND pp.status = 'Active'
  AND (ia.application_status IS NULL
       OR ia.application_status NOT IN ('PaymentProof Recieved', 'Edit_Upon_Review'));

-- 4) Verify CCA officer list query (same logic as backend after fix)
SELECT DISTINCT ia.intent_app_id, ia.user_name, ia.application_status
FROM new_license_management.intent_application ia
INNER JOIN new_license_management.payment_proof pp
  ON ia.intent_app_id = pp.intent_app_id
WHERE pp.status = 'Active'
  AND (ia.application_status IS NULL OR ia.application_status NOT IN ('Edit_Upon_Review'))
  AND ia.user_name = 'UCCA20260057';
