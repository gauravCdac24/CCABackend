-- ============================================
-- STEP 1: Diagnose the data linkage
-- ============================================

-- Check annexure_main table for the active annexure
SELECT annexure_main_id, username, status, completed, auditor_tracker
FROM audit_management.annexure_main
WHERE status = 'Active';

-- Check location_main table
SELECT * FROM audit_management.location_main;

-- Check ca_services_main table
SELECT * FROM audit_management.ca_services_main;

-- Check orphaned location_details (location_main_id is NULL)
SELECT location_details_id, location_main_id, description, location
FROM audit_management.location_details;

-- Check ca_services_details linkage
SELECT ca_services_details_id, ca_services_main_id, description
FROM audit_management.ca_services_details;


-- ============================================
-- STEP 2: Fix orphaned location_details
-- Run this ONLY after verifying the data above
-- ============================================

-- This links orphaned location_details (with NULL location_main_id) 
-- to the location_main entry.
-- Adjust the location_main_id value based on your STEP 1 results.

-- Example: If location_main has ID = 1:
-- UPDATE audit_management.location_details 
-- SET location_main_id = 1 
-- WHERE location_main_id IS NULL;
