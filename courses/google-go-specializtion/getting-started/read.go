package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strings"
)

type Name struct {
	firstName string
	lastName  string
}

func main() {
	path := ""
	fmt.Println("Enter file full path")
	fmt.Scanln(&path)
	var name []Name
	f, err := os.Open(path)
	if err != nil {
		panic(err)
	}
	defer f.Close()

	reader := bufio.NewReader(f)
	for {
		line, _, err := reader.ReadLine()

		if err != nil || io.EOF == err {
			break
		}
		splitted := strings.Split(string(line), " ")
		fullName := Name{
			fixLongName(splitted[0]),
			fixLongName(splitted[1]),
		}
		name = append(name, fullName)
	}
	for i := 0; i < len(name); i++ {
		fmt.Println(i + 1)
		fmt.Println("First Name:" + name[i].firstName)
		fmt.Println("Last Name:" + name[i].lastName)
	}
}

func fixLongName(buffer string) string {
	if len(buffer) > 20 {
		return buffer[0:20]
	} else {
		return buffer
	}
}
