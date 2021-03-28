package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var arraySize = 4
var array1 = []int{3, 6, 3, 2}
var array2 = []int{2, 9, 4, 1}
var array3 = []int{8, 3, 2, 0}

type CyclicBarrier struct {
	phase   int
	count   int
	parties int
	trigger *sync.Cond
}

func (b *CyclicBarrier) nextGeneration() {
	b.trigger.Broadcast()
	b.count = b.parties
	b.phase++
}

func (b *CyclicBarrier) await() {
	b.trigger.L.Lock()
	defer b.trigger.L.Unlock()
	phase := b.phase
	b.count--

	if b.count == 0 {
		b.nextGeneration()
	} else {
		for phase == b.phase {
			b.trigger.Wait()
		}
	}
}

func calculateSums() (int, int, int) {
	var sum1, sum2, sum3 = 0, 0, 0
	for i := 0; i < arraySize; i++ {
		sum1 += array1[i]
		sum2 += array2[i]
		sum3 += array3[i]
	}
	return sum1, sum2, sum3
}

func start(arr []int, b *CyclicBarrier) {
	for {
		rand.Seed(time.Now().UnixNano())
		if rand.Intn(2) == 0 {
			arr[rand.Intn(arraySize)] += -1
		} else {
			arr[rand.Intn(arraySize)] += 1
		}

		b.await()
		var sum1, sum2, sum3 = calculateSums()

		if sum1 == sum2 && sum2 == sum3 {
			fmt.Printf("Finished, sum = %d\n", sum1)
			break
		} else {
			fmt.Printf("Current sums: %d,%d,%d\n", sum1, sum2, sum3)
		}
	}
}

func main() {
	cyclicBarrier := CyclicBarrier{}
	cyclicBarrier.count = 3
	cyclicBarrier.parties = 3
	cyclicBarrier.trigger = sync.NewCond(&sync.Mutex{})

	go start(array1, &cyclicBarrier)
	go start(array2, &cyclicBarrier)
	go start(array3, &cyclicBarrier)

	_, _ = fmt.Scanln()
}
