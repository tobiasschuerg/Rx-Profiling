# Rx-Profiling

Profile your RX steams and find the bottleneck (of complex steams):
 - count emitted items
 - measure time to first item emitted
 
 ```
 receiveMessages()
        .emitCounter { Log.d(TAG, "message count $it") }
        .measureFirst("message receiver") { Log.d(TAG, it) }
        .subscribe(...)
```

results in
>11:09:32.434 D: RX-TIMER: subscribed(message received)  
>11:09:33.772 D: RX-COUNTER: message count 1  
>11:09:33.773 D: RX-TIMER: ############# message received emitted first item after 1339 millis  
>11:09:33.773 D: RX-COUNTER: message count 2  
>11:09:33.774 D: RX-TIMER: `message received` emitted 2 times  
