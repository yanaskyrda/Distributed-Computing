package main

import (
    "fmt"
    "sort"
    "strconv"
)

func main() {
    var arr = make([]int, 3)
    var in string
    fmt.Println("Enter number (q to exit)")
    for true {
        fmt.Scanln(&in) 
        if in == "q"{
            break
        }
        ap,err:=strconv.Atoi(in)
        if err != nil {
            fmt.Println("Error")
            continue
        }
        arr = append(arr, ap)
        sort.Ints(arr[:])
        fmt.Println(arr)
        fmt.Println("Enter number (q to exit)")
	}
}

