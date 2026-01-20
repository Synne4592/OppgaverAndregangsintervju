package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
	"time"
)

type normalizedLog struct {
	Timestamp time.Time `json:"timestamp"`
	RequestId string    `json:"requestId"`
	UserId    string    `json:"userId"`
	Log       string    `json:"log"`
}

func main() {
	file, err := os.Open("logs.log")

	if err != nil {
		log.Fatal(err)
	}

	scanner := bufio.NewScanner(file)

	logs := []normalizedLog{}

	for scanner.Scan() {
		logs = append(logs, parseLine(scanner.Text()))
	}

	jsonLogs, _ := json.Marshal(logs)
	fmt.Println(string(jsonLogs))
}

func parseLine(s string) normalizedLog {
	l := normalizedLog{}

	if len(s) < 11 {
		return l
	}

	//get the timestamp
	tsInt, _ := strconv.Atoi(string(s[:9]))

	date := time.Unix(int64(tsInt), 0)
	l.Timestamp = date

	splits := strings.Split(s, " ")

	if len(splits) < 4 {
		return l
	}

	//get request id
	requestId := extractField(splits[2])

	l.RequestId = requestId

	//get user id
	userId := extractField(splits[3])

	l.UserId = userId

	//get log text
	i := strings.LastIndex(s, "=")
	logStart := strings.Index(string(s[i+1:]), " ")
	l.Log = string(s[i+1+logStart+1:])

	return l
}

func extractField(s string) string {
	index := strings.Index(s, "=")

	if index == -1 {
		return ""
	}

	return string(s[index+1:])
}
