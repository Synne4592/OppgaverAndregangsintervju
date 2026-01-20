package main

import (
	"fmt"
	"testing"
	"time"
)

func Test_parse_log(t *testing.T) {
	s := "1700000004490 debug requestId=req-1e2f3g userId=1111 thread=main logger=authService hostname=web-01 file=TokenValidator.java:22 Validating JWT token"

	log := parseLine(s)

	if log.Timestamp != time.Unix(int64(170000000), 0) {
		fmt.Println("timestamp is wrong")
		t.Fail()
	}

	if log.userId != "=1111" {
		fmt.Println("user id is wrong")
		t.Fail()
	}

	if log.RequestId != "=req-1e2f3g" {
		fmt.Println("requestId is wrong")
		t.Fail()
	}

	if log.Log != "Validating JWT token" {
		fmt.Println("log text is wrong")
		t.Fail()
	}
}
