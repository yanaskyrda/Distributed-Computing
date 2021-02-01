package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

var random = rand.New(rand.NewSource(time.Now().UnixNano()))

const (
	ACTIONS_ALLOWED_PER_THREAD = 20
)

func btos(b bool) string {
	if b {
		return "1"
	}
	return "0"
}

func gardener(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	for i := 0; i < ACTIONS_ALLOWED_PER_THREAD; i++ {
		rwMutex.Lock()
		for i := 0; i < len(garden); i++ {
			for j := 0; j < len(garden[0]); j++ {
				if garden[i][j] == false {
					garden[i][j] = true
				}
			}
		}
		rwMutex.Unlock()
		time.Sleep(500 * time.Millisecond)
	}
	exitChan <- 1
}

func nature(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	for i := 0; i < ACTIONS_ALLOWED_PER_THREAD; i++ {
		rwMutex.Lock()
		for i := 0; i < len(garden)*2; i++ {
			index1 := random.Intn(len(garden))
			index2 := random.Intn(len(garden[0]))
			garden[index1][index2] = !garden[index1][index2]
		}
		rwMutex.Unlock()
		time.Sleep(500 * time.Millisecond)
	}
	exitChan <- 1
}

func statusToFile(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	file, err := os.Create("gardenStatus.txt")

	if err != nil {
		fmt.Println("Error while creating file:", err)
		return
	}

	defer file.Close()

	for i := 0; i < ACTIONS_ALLOWED_PER_THREAD; i++ {
		rwMutex.RLock()
		for i := 0; i < len(garden); i++ {
			var line string
			for j := 0; j < len(garden[0]); j++ {
				line += btos(garden[i][j]) + " "
			}
			_, _ = file.WriteString(line + "\n")
		}
		rwMutex.RUnlock()
		_, _ = file.WriteString("\n\n")
		time.Sleep(500 * time.Millisecond)
	}
	exitChan <- 1
}

func statusToConsole(garden [][]bool, rwMutex *sync.RWMutex, exitChan chan int) {
	for i := 0; i < ACTIONS_ALLOWED_PER_THREAD; i++ {
		rwMutex.RLock()
		for i := 0; i < len(garden); i++ {
			var line string
			for j := 0; j < len(garden[0]); j++ {
				line += btos(garden[i][j]) + " "
			}
			fmt.Println(line)
		}
		rwMutex.RUnlock()
		fmt.Println()
		fmt.Println()
		time.Sleep(500 * time.Millisecond)
	}
	exitChan <- 1
}

func main() {
	var garden [][]bool
	var rwMutex sync.RWMutex
	exitChan := make(chan int, 4)

	for i := 0; i < 10; i++ {
		var row []bool
		for j := 0; j < 10; j++ {
			random = rand.New(rand.NewSource(time.Now().UnixNano()))
			row = append(row, random.Intn(2) != 0)
		}
		garden = append(garden, row)
	}

	go statusToConsole(garden, &rwMutex, exitChan)
	go statusToFile(garden, &rwMutex, exitChan)
	go nature(garden, &rwMutex, exitChan)
	go gardener(garden, &rwMutex, exitChan)

	for i := 0; i < 4; i++ {
		<-exitChan
	}
}
