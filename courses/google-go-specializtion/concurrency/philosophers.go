package main

import (
	"fmt"
	"sync"
	"time"
)

type MutexSync struct {
	sync.Mutex
}
type Philosophers struct {
	num, count              int
	left_mutex, right_mutex *MutexSync
}

func (p Philosophers) eat(c chan *Philosophers, waitGroup *sync.WaitGroup) {
	for i := 0; i < 3; i++ {
		c <- &p
		if p.count < 3 {
			p.left_mutex.Lock()
			p.right_mutex.Lock()

			fmt.Println("starting ", p.num)
			p.count = p.count + 1
			fmt.Println("finished ", p.num)
			p.right_mutex.Unlock()
			p.left_mutex.Unlock()
			waitGroup.Done()
		}

	}
}

func host(c chan *Philosophers) {
	for {
		if len(c) == 2 {
			<-c
			<-c
			time.Sleep(20 * time.Millisecond)
		}
	}
}

func main() {
	var i int
	var waitGroup sync.WaitGroup
	c := make(chan *Philosophers, 2)

	waitGroup.Add(15)

	fork := make([]*MutexSync, 5)
	for i = 0; i < 5; i++ {
		fork[i] = new(MutexSync)
	}

	philosophers := make([]*Philosophers, 5)
	for i = 0; i < 5; i++ {
		philosophers[i] = &Philosophers{i + 1, 0, fork[i], fork[(i+1)%5]}
	}

	go host(c)
	for i = 0; i < 5; i++ {
		go philosophers[i].eat(c, &waitGroup)
	}
	waitGroup.Wait()
}
