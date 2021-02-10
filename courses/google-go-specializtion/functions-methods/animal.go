package main

import (
	"fmt"
)

type animal struct {
	food   string
	motion string
	sound  string
}

type animalInterface interface {
	Eat()
	Move()
	Speak()
}

func (ani animal) Eat() {
	fmt.Println(ani.food)
	return
}

func (ani animal) Move() {
	fmt.Println(ani.motion)
	return
}

func (ani animal) Speak() {
	fmt.Println(ani.sound)
	return
}

func main() {
	animalMap := make(map[string]animal)
	animalMap["cat"] = animal{"fish", "walk", "meow"}
	animalMap["bird"] = animal{"worms", "fly", "chick"}
	animalMap["dog"] = animal{"meat", "jump", "bark"}
	var animalInter animalInterface
	for {
		var command, requestAni, requestType string
		fmt.Print(">")
		fmt.Scan(&command, &requestAni, &requestType)
		if command == "query" {
			animalInter = animalMap[requestAni]
			switch requestType {
			case "eat":
				animalInter.Eat()
			case "move":
				animalInter.Move()
			case "speak":
				animalInter.Speak()
			}
		}
		if command == "newanimal" {
			animalMap[requestAni] = animalMap[requestType]
			fmt.Println("Created!")
		}
	}
}
