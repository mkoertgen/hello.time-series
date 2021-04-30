package main

import (
	"context"
	"fmt"
	"github.com/apache/pulsar/pulsar-function-go/pf"
)

func HandleRequest(ctx context.Context, input []byte) error {
	fmt.Println(string(input) + "!")
	return nil
}

func main() {
	pf.Start(HandleRequest)
}
