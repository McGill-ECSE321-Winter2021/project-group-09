#!/bin/bash

ROOTDIR=http://localhost:8080

###################################

echo "Generating admin token..."
curl -s -N --request POST "$ROOTDIR/api/admin/register" --header "Content-Type: application/json" \
  --data-raw "{\"email\":\"adminfortoken@mail.ca\",\"phoneNumber\":\"1231231234\",\"name\":\"rick\",\"password\":\"hello\",\"address\":\"street\"}"

token=$(curl -s -N --request POST "$ROOTDIR/api/authentication/login" --header "Content-Type: application/json" \
  --data-raw "{\"email\":\"adminfortoken@mail.ca\",\"password\":\"hello\",\"userType\":\"Admin\"}")

echo $token

####################################

echo "admin controller tests..."
echo "/api/admin/register"
curl -s -N --request POST "$ROOTDIR/api/admin/register" --header "Content-Type: application/json" \
  --data-raw "{\"email\":\"admin@mail.ca\",\"phoneNumber\":\"1231231234\",\"name\":\"rick\",\"password\":\"hello\",\"address\":\"street\"}" \
  | grep -c admin@mail.ca

####################################

echo "timeslot controller tests..."

echo "/api/timeslot/create"
curl -s -N --request POST "$ROOTDIR/api/timeslot/create" --header "Content-Type: application/json" \
  --data-raw "{\"startDateTime\":\"2021-03-02T10:00:00\",\"endDateTime\":\"2021-03-02T11:00:01\"}" \
  | grep -c startDateTime


