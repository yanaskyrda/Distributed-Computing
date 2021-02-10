package main

import (
    "fmt"
)

func main() {
    var number int
    fmt.Print("Please enter float:\n")
    fmt.Scanln(&number)
    fmt.Print("\nConverted int:\n",number)
}