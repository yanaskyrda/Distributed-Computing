package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	fmt.Println("Enter numbers:")
	reader := bufio.NewReader(os.Stdin)
	line, _, _ := reader.ReadLine()
	splitted := strings.Split(string(line), " ")
	var values []int
	for _, s := range splitted {
		n, _ := strconv.Atoi(s)
		values = append(values, n)
	}
	bubble_sort(values)
	fmt.Println(values)
}

func bubble_sort(a []int) {
	for i := 0; i < len(a); i++ {
		for j := 0; j < len(a) - 1 - i; j++ {
			if a[j + 1] < a[j] {
				swap(a, j)
			}
		}
	}
}

func swap(a []int, j int) {
	var temp int
	temp = a[j]
	a[j] = a[j+1]
	a[j+1] = temp
}
