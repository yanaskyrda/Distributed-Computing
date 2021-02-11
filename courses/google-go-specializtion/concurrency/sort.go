package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
	"sync"
)

func sort_part(wg *sync.WaitGroup, arr []int) {
	fmt.Println(arr)
	sort.Ints(arr)
	wg.Done()
}

func main() {
	fmt.Println("Enter numbers: ")
	reader := bufio.NewReader(os.Stdin)
	line, _, _ := reader.ReadLine()
	splitted := strings.Split(string(line), " ")
	var numbers []int
	for _, s := range splitted {
		n, _ := strconv.Atoi(s)
		numbers = append(numbers, n)
	}
	size := len(numbers) / 4
	var wg sync.WaitGroup
	for c := 0; c < 4; c++ {
		wg.Add(1)
		if c != 3 {
			go sort_part(&wg, numbers[c*size:(c+1)*size])
		} else {
			go sort_part(&wg, numbers[c*size:])
		}
	}
	wg.Wait()

	var sorted []int
	c1 := numbers[:1*size]
	c2 := numbers[1*size : 2*size]
	c3 := numbers[2*size : 3*size]
	c4 := numbers[3*size:]
	for k := 0; k < len(numbers); k++ {
		i := 0
		j := 0
		if len(c1) != 0 {
			i = c1[0]
			j = 1
		}
		if len(c2) != 0 {
			if j == 0 {
				i = c2[0]
				j = 2
			} else {
				if c2[0] < i {
					i = c2[0]
					j = 2
				}
			}
		}
		if len(c3) != 0 {
			if j == 0 {
				i = c3[0]
				j = 3
			} else {
				if c3[0] < i {
					i = c3[0]
					j = 3
				}
			}
		}
		if len(c4) != 0 {
			if j == 0 {
				i = c4[0]
				j = 4
			} else {
				if c4[0] < i {
					i = c4[0]
					j = 4
				}
			}
		}
		sorted = append(sorted, i)
		switch j {
		case 1:
			c1 = c1[1:]
		case 2:
			c2 = c2[1:]
		case 3:
			c3 = c3[1:]
		case 4:
			c4 = c4[1:]
		}
	}
	fmt.Println(sorted)
}
