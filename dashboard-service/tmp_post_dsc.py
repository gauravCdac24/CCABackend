import json
import urllib.request

with open("tmp_enc.json", encoding="utf-8") as f:
    enc = json.load(f)

boundary = "----DscTestBoundary7f3a"
lines = []

def add(name, value):
    lines.append(f"--{boundary}")
    lines.append(f'Content-Disposition: form-data; name="{name}"')
    lines.append("")
    lines.append(value)

add("caUsername", enc["user"])
add("month", "April")
add("year", "2098")
for i, state in enumerate(enc["states"]):
    add(f"dscesignList[{i}].countryId", enc["country"])
    add(f"dscesignList[{i}].stateId", state)
    add(f"dscesignList[{i}].dscIssued", str(10 + i))
    add(f"dscesignList[{i}].eSignIssued", str(20 + i))
lines.append(f"--{boundary}--")
lines.append("")
body = "\r\n".join(lines).encode("utf-8")

req = urllib.request.Request(
    "http://localhost:7790/dashboard-service/add-dsc-esign-issued",
    data=body,
    method="POST",
    headers={"Content-Type": f"multipart/form-data; boundary={boundary}"},
)
try:
    with urllib.request.urlopen(req, timeout=30) as resp:
        print("STATUS", resp.status)
        print(resp.read().decode("utf-8", errors="replace"))
except Exception as e:
    if hasattr(e, "read"):
        print("STATUS", getattr(e, "code", None))
        print(e.read().decode("utf-8", errors="replace"))
    else:
        print(type(e).__name__, e)
