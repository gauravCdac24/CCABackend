-- Fix "Dashobard" typo in admin sidebar menu labels (Home > Dashboard, etc.)
-- Schema: admin_user_management

-- Preview rows that will be updated
SELECT menu_id, menu_name
FROM admin_user_management.menu_master
WHERE menu_name REGEXP 'dashobard';

SELECT sub_menu_id, submenu_name, submenu_path
FROM admin_user_management.sub_menu_master
WHERE submenu_name REGEXP 'dashobard'
   OR tracker_heading REGEXP 'dashobard';

-- Apply fixes
UPDATE admin_user_management.menu_master
SET menu_name = REGEXP_REPLACE(menu_name, 'dashobard', 'Dashboard', 1, 0, 'i')
WHERE menu_name REGEXP 'dashobard';

UPDATE admin_user_management.sub_menu_master
SET submenu_name = REGEXP_REPLACE(submenu_name, 'dashobard', 'Dashboard', 1, 0, 'i')
WHERE submenu_name REGEXP 'dashobard';

UPDATE admin_user_management.sub_menu_master
SET tracker_heading = REGEXP_REPLACE(tracker_heading, 'dashobard', 'Dashboard', 1, 0, 'i')
WHERE tracker_heading REGEXP 'dashobard';
