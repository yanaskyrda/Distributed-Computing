package main

import (
	"fmt"
)

type Animal struct {
	food, motion, sound string
}

func (v Animal) Eat() {
	fmt.Println(v.food)
}

func (v Animal) Move() {
	fmt.Println(v.motion)
}

func (v Animal) Speak() {
	fmt.Println(v.sound)
}

func main() {
	m := map[string]Animal{
		"cat":   {"fish", "walk", "meow"},
		"bird":  {"worms", "fly", "chick"},
		"dog": {"meat", "jump", "bark"},
	}
	for {
		fmt.Print(">")
		an := "0"
		ac := "0"
		fmt.Scan(&an, &ac)
		if ac == "eat" {
			m[an].Eat()
		} else if ac == "move" {
			m[an].Move()
		} else if ac == "speak" {
			m[an].Speak()
		}
	}
}
