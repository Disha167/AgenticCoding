# Agentic coding practice: price-drop watchlist

A small Java backend you have never seen, with failing tests, a feature to build,
a concurrency problem, and a pull request to review. It mirrors the format of
AI-assisted coding rounds: understand unfamiliar code, debug it, extend it, review it,
using an AI agent while staying in control.

No frameworks or downloads needed. Requires JDK 17+.

    ./run-tests.sh     # unit tests (6 fail at the start, on purpose)
    ./run-drill.sh     # stage 3 concurrency drill

Windows: run the two commands inside each script manually from Git Bash or WSL.

## What the code does

Sellers update item prices. When the price is at least 10% below the item's base price,
every buyer watching the item is notified on each of their channels (email, push, in-app),
once per price change. Price updates use the item version for optimistic locking.

## Rules for each session (this is what interviewers score)

1. Set a timer. Talk out loud the whole time, as if an interviewer is listening.
2. Spend the first 5-8 minutes reading: entry points, data flow, tests. Use the agent to
   explain specific files, but confirm what it says by reading the code yourself.
3. Follow the failing tests one at a time. Form a hypothesis before asking the agent.
4. Ask for small, specific changes. Review every diff before accepting it.
5. Re-run the tests after every change. Never trust "fixed" without a green run.
6. When the agent is wrong, say so and say why. Catching AI mistakes is a scored skill.
7. Do not open SOLUTIONS.md until the session is over.

## Session plan (about 2 hours in total, or split across days)

| Stage | Time | Task |
|---|---|---|
| 0 | 10 min | Understand: write a 5-line summary of the architecture and request flow |
| 1 | 30 min | Debug: make all tests pass. There are several separate bugs |
| 2 | 30 min | Build: see TASKS.md, feature A, including new tests |
| 3 | 20 min | Concurrency: see TASKS.md, task B |
| 4 | 20 min | Review: review/PR-142.md, write prioritized review comments |

Afterwards, compare with SOLUTIONS.md and note what you missed and how long each stage took.

For unlimited fresh practice, see NEW-BUGS.md.
