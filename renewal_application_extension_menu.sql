-- Adds the CCA "Renewal application Extension" page under the CCA role only.
-- Safe to run more than once.
-- The screen route is /cca/renewal-application-extension.

UPDATE admin_user_management.sub_menu_master sm
SET status = 'Inactive',
    updated = NOW(),
    updated_by = 'system'
FROM admin_user_management.menu_master m
JOIN admin_user_management.role_master r ON r.role_id = m.role_id
WHERE sm.menu_id = m.menu_id
  AND sm.submenu_path = 'renewal-application-extension'
  AND LOWER(TRIM(COALESCE(r.path, ''))) <> 'cca';

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
    'Renewal application Extension',
    'renewal-application-extension',
    COALESCE((
        SELECT MAX(s.sub_menu_order)
        FROM admin_user_management.sub_menu_master s
        WHERE s.menu_id = m.menu_id
    ), 0) + 1,
    'Renewal application Extension',
    'system',
    'system',
    'Active',
    NOW(),
    NOW()
FROM admin_user_management.menu_master m
JOIN admin_user_management.role_master r ON r.role_id = m.role_id
WHERE m.status = 'Active'
  AND r.status = 'Active'
  AND (
        m.menu_name ILIKE '%renewal%'
        OR EXISTS (
            SELECT 1
            FROM admin_user_management.sub_menu_master existing
            WHERE existing.menu_id = m.menu_id
              AND (
                    existing.submenu_name ILIKE '%renewal%'
                    OR existing.submenu_path ILIKE '%renewal%'
                  )
        )
      )
  AND LOWER(TRIM(COALESCE(r.path, ''))) = 'cca'
  AND NOT EXISTS (
        SELECT 1
        FROM admin_user_management.sub_menu_master already
        WHERE already.menu_id = m.menu_id
          AND already.submenu_path = 'renewal-application-extension'
      );
