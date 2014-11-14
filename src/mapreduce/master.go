package mapreduce

import "container/list"
import "fmt"

type WorkerInfo struct {
	address string
	// You can add definitions here.
}

// Clean up all workers by sending a Shutdown RPC to each one of them Collect
// the number of jobs each work has performed.
func (mr *MapReduce) KillWorkers() *list.List {
	l := list.New()
	for _, w := range mr.Workers {
		DPrintf("DoWork: shutdown %s\n", w.address)
		args := &ShutdownArgs{}
		var reply ShutdownReply
		ok := call(w.address, "Worker.Shutdown", args, &reply)
		if ok == false {
			fmt.Printf("DoWork: RPC %s shutdown error\n", w.address)
		} else {
			l.PushBack(reply.Njobs)
		}
	}
	return l
}

func (mr *MapReduce) getJobArgs(operation JobType, number int) (result DoJobArgs) {
	result = DoJobArgs{
		File:      mr.file,
		Operation: operation,
		JobNumber: number,
	}

	switch operation {
	case Map:
		result.NumOtherPhase = mr.nReduce
	case Reduce:
		result.NumOtherPhase = mr.nMap
	}

	return
}

func (mr *MapReduce) sendJob(jobtype JobType, number int, results chan<- int) {
	var addr string
	var reply RegisterReply
	var ok bool
	select {
	case addr = <-mr.freeChannel:
		ok = call(addr, "Worker.DoJob", mr.getJobArgs(jobtype, number), &reply)
	case addr = <-mr.registerChannel:
		ok = call(addr, "Worker.DoJob", mr.getJobArgs(jobtype, number), &reply)
	}

	if ok {
		results <- number
		mr.freeChannel <- addr
	}
}

func (mr *MapReduce) sendMap(index int, results chan<- int) {
	mr.sendJob(Map, index, results)
}

func (mr *MapReduce) sendReduce(index int, results chan<- int) {
	mr.sendJob(Reduce, index, results)
}

func (mr *MapReduce) RunMaster() *list.List {
	fmt.Println(mr.nMap)
	mapped, reduced := make(chan int, mr.nMap), make(chan int, mr.nReduce)
	for i := 0; i < mr.nMap; i++ {
		go mr.sendMap(i, mapped)
	}

	for i := 0; i < mr.nMap; i++ {
		<-mapped
	}

	for i := 0; i < mr.nReduce; i++ {
		go mr.sendReduce(i, reduced)
	}

	for i := 0; i < mr.nReduce; i++ {
		<-reduced
	}

	return mr.KillWorkers()
}
