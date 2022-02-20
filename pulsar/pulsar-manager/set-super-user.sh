#!/bin/sh
# See https://github.com/apache/pulsar-helm-chart/issues/108#issuecomment-1046001952
#URL=http://pulsar-pulsar-manager-backend:7750
#USERNAME=pulsar
#PASSWORD=pulsar
echo Setting user=$USERNAME, pwd=$PASSWORD using url=$URL
CSRF_TOKEN=$(curl -sS $URL/pulsar-manager/csrf-token)
echo Got token: $CSRF_TOKEN
curl -sS \
  -H "X-XSRF-TOKEN: $CSRF_TOKEN" \
  -H "Cookie: XSRF-TOKEN=$CSRF_TOKEN;" \
  -H "Content-Type: application/json" \
  -X PUT $URL/pulsar-manager/users/superuser \
  -d "{\"name\": \"$USERNAME\", \"password\": \"$PASSWORD\", \"description\": \"test\", \"email\": \"username@test.org\"}"
