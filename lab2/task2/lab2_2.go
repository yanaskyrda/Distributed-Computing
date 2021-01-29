package main

import (
	"fmt"
	"time"
)

type Ivanov struct {
}

type Petrov struct {
}

type Nechyporuk struct {
}

type PropertyItem struct {
	cost int
}

type WareHouse struct {
	items []PropertyItem
}

func (Ivanov) takeOut(wh WareHouse, whToTruck chan PropertyItem) {
	for i := 0; i < len(wh.items); i++ {
		whToTruck <- wh.items[i]
		fmt.Println("Ivanov take out from ware house")
		time.Sleep(1 * time.Microsecond)
	}
}

func (Petrov) load(whToTruck chan PropertyItem, truckToCount chan PropertyItem) {
	for i := 0; i < cap(truckToCount); i++ {
		propertyItem := <-whToTruck
		fmt.Println("Petrov take out from Ivanov")
		truckToCount <- propertyItem
		fmt.Println("Petrov give an item to Nechyporuk")
		time.Sleep(1 * time.Microsecond)
	}

}

func (Nechyporuk) count(truckToCount chan PropertyItem, output chan int) {
	var totalCost = 0
	for i := 0; i < cap(truckToCount); i++ {
		propertyItem := <-truckToCount
		fmt.Println("Nechyporuk take out from Petrov")
		totalCost += propertyItem.cost
		time.Sleep(1 * time.Microsecond)
	}

	output <- totalCost
}

func main() {
	var wh = WareHouse{
		[]PropertyItem{{10}, {3}, {13},
			{1}, {4}, {16},
			{18}, {19}, {3},
			{7}, {7}, {13}}}
	var output = make(chan int)
	var whToTruck = make(chan PropertyItem, len(wh.items))
	var truckToCount = make(chan PropertyItem, cap(whToTruck))
	ivanov := Ivanov{}
	petrov := Petrov{}
	nechyporuk := Nechyporuk{}

	go ivanov.takeOut(wh, whToTruck)
	go petrov.load(whToTruck, truckToCount)
	go nechyporuk.count(truckToCount, output)
	fmt.Println("total cost: ", <-output)
}
