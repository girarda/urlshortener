import httplib, urllib
params = urllib.urlencode({
    'isbn' : '9780131185838',
    'catalogId' : '10001',
    'schoolStoreId' : '15828',
    'search' : 'Search'
    })
headers = {"Content-type": "application/x-www-form-urlencoded",
           "Accept": "text/plain"}
conn = httplib.HTTPConnection("localhost:3000")
conn.request("POST", "/",
             params, headers)
response = conn.getresponse()
print response.status, response.reason
data = response.read()
conn.close()