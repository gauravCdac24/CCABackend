import psycopg2

conn = psycopg2.connect(
    host="localhost", port=5432, dbname="cca_db", user="postgres", password="root", connect_timeout=5
)
cur = conn.cursor()
cur.execute(
    """
    SELECT column_name, data_type
    FROM information_schema.columns
    WHERE table_schema='admin_user_management' AND table_name='state'
    ORDER BY ordinal_position
    """
)
print("state cols", cur.fetchall())
cur.execute(
    """
    SELECT column_name, data_type
    FROM information_schema.columns
    WHERE table_schema='admin_user_management' AND table_name='country'
    ORDER BY ordinal_position
    """
)
print("country cols", cur.fetchall())
cur.execute("SELECT * FROM admin_user_management.country")
print("COUNTRIES")
for row in cur.fetchall():
    print(row)
cur.execute("SELECT count(*) FROM admin_user_management.state")
print("state count", cur.fetchone())
cur.execute("SELECT * FROM admin_user_management.state ORDER BY 1")
print("STATES")
for row in cur.fetchall():
    print(row)
conn.close()
