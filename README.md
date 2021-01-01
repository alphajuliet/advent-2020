# Advent of Code 2020

My attempts at the [Advent of Code 2020](https://adventofcode.com/2020/) challenge, 
using Clojure (1.10.x) as my power tool of
choice. I'm not doing it at the actual time the daily challenges were released, but I
promise I'm not peeking at others' solutions or the answers.

## Commentary

Some comments on the challenges.

### Day 1

In part 1, we look for two numbers from the sequence that sum to 2020. To cut down the work slightly, we note that each number must be on opposite sides of 1010, so we split the pile into two, and exhaustively add them together. The pair that gets through is multiplied to give the answer.

Part 2 scales it up to three, but the trick of splitting the pile doesn't work, but we can minimise the comparisons by ensuring that they are always less than each other. For the 200 numbers that means a maximum of 200 * 199 * 198 = 7880400 triplets need to be checked.

### Day 2

Regular expressions are the key to both parts of this. Part 1 is about counting the matches against the rule. Part 2 asks for "exactly one" match, which is logically an exclusive-or, so we code one up in `eor` to get the answer.

I could probably have pulled the line apart a bit more efficiently with `re-seq` but it's not much work.

### Day 3

We solve the toboggan problem by rotating vectors by given amounts, corresponding to the slope. For part 1 we rotate each row by a constant (x) times the row number (y), and count the number of trees as the first element. For part 2, we are given a collection of slopes, so we iterate through them all as for part 1, with the minor complication of handling a multiple of rows (y value). The `check-trees` function was refactored to handle this.

### Day 10

This challenge is about looking at the differences between consecutive elements in the sequence of adaptors. For part 1, we generate the vector of `deltas`, use the `frequencies` function to count the 1s and 3s, and then multiply the two counts to get the answer.

In part 2, because the number of possible sequences is huge (2^n), we need to be smarter. For the first test set, if you write out the number of possible sequences, you see that you get multiple paths through a region iff there is a sequence of at least three consecutive numbers. For example, 4 -> 5 -> 6 allows the two paths: 4 -> 5 -> 6 and 4 -> 6. Similarly for four number there are 4 possible paths, for five there are 7 paths. At that point the limit of jumping by 3 is reached, and any longer sequences are the overlap of two or more sequences of five consecutive numbers. 

When this translates to deltas, it means we need to look at the length of the sequences of 1s. Four consecutive numbers gives us deltas of `[1 1 1]`. Because the deltas only consist of 1s and 3s, we just partition the delta vector on the element 3. We then run each sequence through a case statement in `paths` to generate the number of paths.  We then multiply them all together. The result can only be a multiple of powers of 2 and 7. 

## License

Copyright © 2020 Andrew Joyner

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
