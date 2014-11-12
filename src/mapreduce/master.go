package mapreduce

import "container/list"
import "fmt"
import "log"

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

func (mr *MapReduce) getJobArgs(operation JobType, number, numOther int) DoJobArgs {
	return DoJobArgs{
		File: mr.file,
		Operation: operation,
		JobNumber: number,
		NumOtherPhase: numOther,
	}
}

func (mr *MapReduce) RunMaster() *list.List {
	logchan := make(chan string, 1)
	mapping, reducing := 0,0
	for i := 0; i < mr.nMap; i++ {
		go func() {
			var reply RegisterReply
			select {
			case addr := <- mr.registerChannel:
						call(addr, "Worker.DoJob", mr.getJobArgs(Map, i, reducing), &reply)
						mapping++
						// logchan <- fmt.Sprintf("Called dojob for %v\n", addr)
			}
			}()
	}

	for i := 0; i < mr.nReduce; i++ {
		go func() {
			var reply RegisterReply
			select {
			case addr := <- mr.registerChannel:
						call(addr, "Worker.DoJob", mr.getJobArgs(Reduce, i, mapping), &reply)
						reducing++
						// logchan <- fmt.Sprintf("Called dojob for %v\n", addr)
			}
			}()
	}

	for {
		select {
			case str := <- logchan:
				log.Println(str)
		}
	}
	// for {
	// 	select {
	// 	case e := <-mr.registerChannel:
	// 		mr.Run()
	// 	case <-mr.DoneChannel:
	// 		break
	// 	}
	// }

	return mr.KillWorkers()
}
