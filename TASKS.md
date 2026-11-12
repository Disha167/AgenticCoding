# Tasks for stages 2 and 3

Write tests for everything you add. Keep all existing tests passing.

## Feature A (stage 2): retries and a dead-letter list

Channel providers fail sometimes. Change notification sending so that:

1. A failed send on one channel is retried up to 3 attempts in total.
2. A send that still fails after 3 attempts is recorded in a dead-letter list that can be
   inspected (for example, `getDeadLetters()`), with the reason.
3. A failure on one channel must not stop other channels or other buyers from being notified.
4. Retrying must never send a duplicate on a channel that already succeeded.
5. A notification that ended in the dead-letter list must not be marked as sent, so a later
   replay could deliver it.

You will need a test double that fails a configurable number of times.

Extra (if time): add a per-watch threshold, so a buyer can ask for 20% instead of the
default 10%.

## Task B (stage 3): concurrent watch requests

Run ./run-drill.sh a few times. 16 threads add watchers to one popular item at the same time.

1. Explain what goes wrong and why (run it several times; results vary).
2. Make WatchRepository safe for concurrent use.
3. Also explain the performance problem with `add` when an item has 100,000 watchers,
   and fix it.
4. Explain how you would solve the same problem if watches lived in a real database
   shared by several service instances.
