Executors API:
submit() vs invokeAll()
submit(task)	Submit one task and get its Future
invokeAll(tasks)	Submit a batch of tasks and wait for all of them

Future
→ mainly represents a result that will be available later.

CompletableFuture
→ represents a result + lets you build asynchronous workflows around it.

CompletableFuture
├── supplyAsync() → has result
├── runAsync()    → no result
├── thenApply()   → transform result
├── thenCompose() → chain async tasks
├── thenCombine() → combine tasks
└── allOf()       → wait for multiple tasks
└── exceptionally()      → handle error