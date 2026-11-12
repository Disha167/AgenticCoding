# Unlimited practice: let an AI plant fresh bugs

Use any repository: this one after you've fixed it, Spring PetClinic, or your own projects.
The trick is to keep the bug-planting session separate from the fixing session, so you
don't see the answers.

1. Create a practice branch: `git checkout -b practice-1`
2. In a separate AI session (not the one you'll debug with), paste this prompt:

> Introduce 3 subtle, realistic bugs into this codebase that a mid-level backend engineer
> might write. Use a mix of: off-by-one or boundary errors, wrong equality or hashing,
> null handling, missing validation, transaction or concurrency mistakes, incorrect
> caching or invalidation, wrong HTTP status codes, and N+1 queries. Spread them across
> different files. Don't add comments that hint at them. Make sure at least one existing
> test fails, and that one bug is not caught by any test. Commit with the message
> "refactor: minor cleanup". List the bugs in a file called ANSWERS.md and do not show
> them to me in the chat.

3. Don't open ANSWERS.md. Start a fresh session with your interview tool, set a 45-minute
   timer and find all three, including the one no test catches.
4. Compare with ANSWERS.md, then `git checkout main` and repeat with a new branch.

For feature practice, ask the same separate session for a realistic feature request in
ticket form (requirements and acceptance criteria only, no implementation hints).
