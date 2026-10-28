import psycopg2
from psycopg2.extras import RealDictCursor

conn = psycopg2.connect(
    host="localhost",
    port=5432,
    dbname="cca_db",
    user="postgres",
    password="root",
    connect_timeout=5,
)
cur = conn.cursor(cursor_factory=RealDictCursor)

cur.execute(
    """
    SELECT table_schema, table_name
    FROM information_schema.tables
    WHERE table_name ILIKE '%auditor%'
       OR table_name ILIKE '%certificate%'
    ORDER BY 1, 2
    """
)
print("tables:")
for r in cur.fetchall():
    print(dict(r))

cur.execute(
    """
    SELECT column_name, data_type
    FROM information_schema.columns
    WHERE table_schema='admin_user_management' AND table_name='auditors'
    ORDER BY ordinal_position
    """
)
print("auditors columns:")
for r in cur.fetchall():
    print(dict(r))

# Sample CDAC auditors and related certificate data
cur.execute(
    """
    SELECT a.auditors_id, a.first_name, a.middle_name, a.last_name, a.status, a.audit_agency_id
    FROM admin_user_management.auditors a
    WHERE a.audit_agency_id = 5
    ORDER BY a.auditors_id
    """
)
print("CDAC auditors:")
for r in cur.fetchall():
    print(dict(r))

# Check certificate type master
for table in [
    "auditor_certificate_type",
    "auditor_certification",
    "auditor_certificates",
    "certificate_type",
]:
    cur.execute(
        """
        SELECT table_schema, table_name
        FROM information_schema.tables
        WHERE table_name = %s
        """,
        (table,),
    )
    rows = cur.fetchall()
    if rows:
        print("found", rows)

cur.execute(
    """
    SELECT table_schema, table_name
    FROM information_schema.tables
    WHERE table_name ILIKE '%cert%'
    ORDER BY 1,2
    """
)
print("cert tables:")
for r in cur.fetchall():
    print(dict(r))

cur.close()
conn.close()
