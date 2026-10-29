import psycopg2

conn = psycopg2.connect(
    host="localhost", port=5432, dbname="cca_db", user="postgres", password="root", connect_timeout=5
)
cur = conn.cursor()
cur.execute(
    """
    SELECT dscesign_issued_id, stateid, dsc_issued, esign_issued, created
    FROM dashboard_management.dsc_esign_issued
    WHERE year = '2098' AND month = 'April' AND causername = 'TESTMULTI'
    ORDER BY dscesign_issued_id
    """
)
rows = cur.fetchall()
print("count", len(rows))
for row in rows:
    print(row)
conn.close()
