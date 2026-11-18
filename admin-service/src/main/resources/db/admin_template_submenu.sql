-- Restores the admin submenu pages created from Menu Management:
-- Bank Guarantee Proof, Agreement, Cross Certification Arrangement, and Audit Acceptance.
-- Safe to run more than once. Rows are added only when missing, under the
-- active ADMIN role menu named Application Management (role path admin).

UPDATE admin_user_management.sub_menu_master sm
SET submenu_path = 'auditacceptance',
    submenu_name = 'Audit Acceptance',
    tracker_heading = 'Audit Acceptance',
    updated = NOW()
FROM admin_user_management.menu_master m
JOIN admin_user_management.role_master r ON r.role_id = m.role_id
WHERE sm.menu_id = m.menu_id
  AND sm.submenu_path IN ('/auditacceptance', 'auditacceptance')
  AND m.menu_name = 'Application Management'
  AND LOWER(TRIM(COALESCE(r.path, ''))) = 'admin';

INSERT INTO admin_user_management.sub_menu_master (
    menu_id,
    submenu_name,
    submenu_path,
    sub_menu_order,
    tracker_heading,
    created_by,
    updated_by,
    status,
    created,
    updated
)
SELECT
    m.menu_id,
    seed.submenu_name,
    seed.submenu_path,
    COALESCE((
        SELECT MAX(s.sub_menu_order)
        FROM admin_user_management.sub_menu_master s
        WHERE s.menu_id = m.menu_id
    ), 0) + seed.order_offset,
    seed.tracker_heading,
    'system',
    'system',
    'Active',
    NOW(),
    NOW()
FROM admin_user_management.menu_master m
JOIN admin_user_management.role_master r ON r.role_id = m.role_id
JOIN (
    VALUES
        ('Bank Guarantee Proof', 'bankguaranteeproof', 'Bank Guarantee Proof', 1),
        ('Agreement', 'agreement', 'Agreement', 2),
        ('Cross Certification Arrangement', 'crosscertificationarrangement', 'Cross Certification Arrangement', 3),
        ('Audit Acceptance', 'auditacceptance', 'Audit Acceptance', 4)
) AS seed(submenu_name, submenu_path, tracker_heading, order_offset) ON TRUE
WHERE m.status = 'Active'
  AND r.status = 'Active'
  AND m.menu_name = 'Application Management'
  AND LOWER(TRIM(COALESCE(r.path, ''))) = 'admin'
  AND NOT EXISTS (
        SELECT 1
        FROM admin_user_management.sub_menu_master already
        WHERE already.menu_id = m.menu_id
          AND already.submenu_path = seed.submenu_path
  );

INSERT INTO admin_user_management.sub_menu_internal_master (
    sub_menu_id,
    submenu_internal_name,
    submenu_internal_path,
    tracker_heading,
    created_by,
    updated_by,
    status,
    created,
    updated
)
SELECT
    sm.sub_menu_id,
    seed.internal_name,
    seed.internal_path,
    seed.tracker_heading,
    'system',
    'system',
    'Active',
    NOW(),
    NOW()
FROM admin_user_management.sub_menu_master sm
JOIN admin_user_management.menu_master m ON m.menu_id = sm.menu_id
JOIN admin_user_management.role_master r ON r.role_id = m.role_id
JOIN (
    VALUES
        ('bankguaranteeproof', 'Add Bank Guarantee Template', 'addbankguaranteetemplate', 'Add Bank Guarantee Template'),
        ('bankguaranteeproof', 'Edit Bank Guarantee Template', 'editbankguaranteetemplate', 'Edit Bank Guarantee Template'),
        ('agreement', 'Add Agreement Template', 'addagreementtemplate', 'Add Agreement Template'),
        ('agreement', 'Edit Agreement Template', 'editagreementtemplate', 'Edit Agreement Template'),
        ('crosscertificationarrangement', 'Add Cross Certification', 'addcrosscertificationtemplate', 'Add Cross Certification'),
        ('crosscertificationarrangement', 'Edit Cross Certification', 'editcrosscertificationtemplate', 'Edit Cross Certification'),
        ('auditacceptance', 'Add Audit Acceptance', 'addauditacceptance', 'Add Audit Acceptance'),
        ('auditacceptance', 'Edit Audit Acceptance', 'editauditacceptance', 'Edit Audit Acceptance')
) AS seed(submenu_path, internal_name, internal_path, tracker_heading)
    ON sm.submenu_path = seed.submenu_path
WHERE m.status = 'Active'
  AND r.status = 'Active'
  AND m.menu_name = 'Application Management'
  AND LOWER(TRIM(COALESCE(r.path, ''))) = 'admin'
  AND NOT EXISTS (
        SELECT 1
        FROM admin_user_management.sub_menu_internal_master already
        WHERE already.sub_menu_id = sm.sub_menu_id
          AND already.submenu_internal_path = seed.internal_path
  );
