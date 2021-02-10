package main

import "fmt"

func main() {
	var acc float64
	fmt.Print("Acceleration:")
	fmt.Scanln(&acc)
	var velocity float64
	fmt.Print("Initial velocity:")
	fmt.Scanln(&velocity)
	var displacement float64
	fmt.Print("Initial displacement:")
	fmt.Scanln(&displacement)
	var time float64
	fmt.Print("Time:")
	fmt.Scanln(&time)
	res := GenDisplaceFn(acc, velocity, displacement)
	fmt.Println(res(3))
	fmt.Println(res(5))
}

func GenDisplaceFn(acc, velocity, displacement float64) func(float64) float64 {
	return func(time float64) float64 {
		return 1/2*acc*time*time + velocity*time + displacement
	}
}
