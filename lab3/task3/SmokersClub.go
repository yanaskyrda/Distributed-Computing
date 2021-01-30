package main

import (
	"fmt"
	"math/rand"
	"time"
)

var NUM_OF_CIGARETTES_ALLOWED = 20

type Ingredient int

const (
	TOBACCO Ingredient = iota
	PAPER
	MATCHES
)

func (ingredient Ingredient) String() string {
	ingredients := []string{
		"Tobacco",
		"Paper",
		"Matches",
	}

	if ingredient < TOBACCO || ingredient > MATCHES {
		return "Unknown"
	}

	return ingredients[ingredient]
}

type Smoker struct {
	name       string
	ingredient Ingredient
}

type Moderator struct {
}

func (smoker Smoker) tryToSmoke(partsChan chan Ingredient,
	smokerSemaphore chan int, moderatorSemaphore chan bool) {
	for {
		if <-smokerSemaphore == -1 {
			return
		}
		firstIngredient := <-partsChan
		secondIngredient := <-partsChan
		if firstIngredient != smoker.ingredient && secondIngredient != smoker.ingredient {
			fmt.Println("Smoker "+smoker.name+" got ingredients: ",
				firstIngredient, ", ", secondIngredient)
			fmt.Println("Smoker "+smoker.name+" add ingredient: ",
				smoker.ingredient)
			time.Sleep(10 * time.Millisecond)
			fmt.Println("Smoker " + smoker.name + " smoked a cigarette")
			fmt.Println()
			moderatorSemaphore <- true
		} else {
			partsChan <- firstIngredient
			partsChan <- secondIngredient
			smokerSemaphore <- 1
		}
	}
}

func (Moderator) giveItems(partsChan chan Ingredient,
	smokerSemaphore chan int, moderatorSemaphore chan bool,
	endChan chan bool) {
	for i := 0; i < NUM_OF_CIGARETTES_ALLOWED; i++ {
		if i != 0 {
			<-moderatorSemaphore
		}
		random := rand.New(rand.NewSource(time.Now().UnixNano()))
		var firstIngredient = random.Intn(3)
		var secondIngredient = random.Intn(3)
		for secondIngredient == firstIngredient {
			secondIngredient = random.Intn(3)
		}
		fmt.Println("Moderator sends ", Ingredient(firstIngredient), " and ", Ingredient(secondIngredient))
		partsChan <- Ingredient(firstIngredient)
		partsChan <- Ingredient(secondIngredient)
		smokerSemaphore <- 1
	}
	<-moderatorSemaphore
	smokerSemaphore <- -1
	smokerSemaphore <- -1
	smokerSemaphore <- -1
	endChan <- true
}

func main() {
	smoker1 := Smoker{"Chi", Ingredient(0)}
	smoker2 := Smoker{"Cha", Ingredient(1)}
	smoker3 := Smoker{"Chu", Ingredient(2)}

	partsChan := make(chan Ingredient, 2)
	smokerSemaphore := make(chan int, 1)
	moderatorSemaphore := make(chan bool, 1)
	endChan := make(chan bool, 1)
	go Moderator{}.giveItems(partsChan, smokerSemaphore, moderatorSemaphore, endChan)
	go smoker1.tryToSmoke(partsChan, smokerSemaphore, moderatorSemaphore)
	go smoker2.tryToSmoke(partsChan, smokerSemaphore, moderatorSemaphore)
	go smoker3.tryToSmoke(partsChan, smokerSemaphore, moderatorSemaphore)
	<-endChan
}
