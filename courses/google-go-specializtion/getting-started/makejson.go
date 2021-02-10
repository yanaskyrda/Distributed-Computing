package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
)

func main() {
	var mapVar map[string]string
	mapVar = make(map[string]string)
	fmt.Println("Name:")
	reader := bufio.NewReader(os.Stdin)
	line, _, _ := reader.ReadLine()
	name := string(line)
	mapVar["name"] = name
	fmt.Println("Address:")
	newReader := bufio.NewReader(os.Stdin)
	newLine, _, _ := newReader.ReadLine()
	address := string(newLine)
	mapVar["address"] = address
	b, err := json.Marshal(mapVar)
	if err != nil {
		fmt.Println("Encoding fail")
	} else {
		fmt.Println("Encoded data : ")
		fmt.Println(b)
		fmt.Println("Decoded data : ")
		fmt.Println(string(b))
	}
}
