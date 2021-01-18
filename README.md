# Advent of Code 2020

My attempts at the [Advent of Code 2020](https://adventofcode.com/2020/) challenge, 
using Clojure (1.10.x) as my power tool of choice, because it's both a nice and often elegant experience, and to sharpen my skills. 

I have clearly not attempted the daily challenges at the time they were released, but I promise I'm not peeking at others' solutions, discussions, or the answers. This is all my own work, or so far at least.
I also give myself more than a day to complete them, depending on the
difficulty of course, and my availability and motivation.

## Lines of code

Raw SLOC stats for each day's solution.

Day | Source lines
----|-------------
   1|  37
   2|  60
   3|  46
   4|  81
   5|  32
   6|  40
   7| 119
   8| 113
   9|  68
  10|  55
  11| 184
  12|  66
  13|  98
  14| 108
  15|  51

## Commentary

Some comments on my solutions.

### Day 1

In part 1, we look for two numbers from the sequence that sum to 2020. To cut down the work slightly, we note that each number must be on opposite sides of 1010, so we split the pile into two, and exhaustively add them together. The pair that gets through is multiplied to give the answer.

Part 2 scales it up to three, but the trick of splitting the pile doesn't work, but we can minimise the comparisons by ensuring that they are always less than each other. For the 200 numbers that means a maximum of 200 * 199 * 198 = 7880400 triplets need to be checked.

### Day 2

Regular expressions are the key to both parts of this. Part 1 is about counting the matches against the rule. Part 2 asks for "exactly one" match, which is logically an exclusive-or, so we code one up in `eor` to get the answer.

I could probably have pulled the line apart a bit more efficiently with `re-seq` but it's not much work.

### Day 3

We solve the toboggan problem by rotating vectors by given amounts, corresponding to the slope. For part 1 we rotate each row by a constant (x) times the row number (y), and count the number of trees as the first element. For part 2, we are given a collection of slopes, so we iterate through them all as for part 1, with the minor complication of handling a multiple of rows (y value). The `check-trees` function was refactored to handle this.

### Day 4

This challenge is elegantly solved in Clojure using the `spec` library. We parse everything into a Clojure map, and then validate the map against a spec. The parsing is the painful bit, and again, I could have maybe done it better with `re-seq` but I haven't used regexs much in Clojure up to now.

The important spec is `::passport`, which was refactored for part 2 to include the narrower constraints for each field.

### Day 5

Once you realise that the boarding pass code is simply a binary representation with letters instead of numbers, it becomes simple. The easy way to deal with this was to pre-parse the input data using the shell invocation `tr "BFRL" "01"` to convert the letters into the corresponding 0 or 1 value. 

In part 1 we read the binary numbers, convert them to decimal with `Integer/parseInt`, and simply look for the largest number in the list. For part 2, we generate all the seat numbers in the same range as our input data, and use the set `difference` function to identify the missing one.

### Day 6

Sets come to the rescue in this challenge. In part 1, we parse the input into lists of answers, and turn them into sets which removes the duplicates, ie. a union. We then count up the total of elements in each set using `reduce`.

For part 2, we need to look at the intersections with a bit more parsing, and then count up the numbers again for each set. 

### Day 7

Ok, for this one I couldn't resist using a graph network because (a) I like graphs, and (b) the `ubergraph` library in Clojure is nice and easy to use. The problem lends itself very well to a graph, so the first step was to parse each line into a relationship between source and target nodes, with the multiplicity captured as the edge weight. Each relationship is then added to the graph as a *directed* edge.

Once everything is in a directed graph then we can apply some graph algorithms, thanks again to `ubergraph`. In part 1 we look at all the nodes that are reachable from the given start node, thanks for the `alg/shortest-path` function that returns all paths from a given starting node.

Part 2 requires a bit more work because we need to recursively sum the multiplicities of all the upstream (i.e. incoming) edges to the given node. The `sum-incoming-weights` does this recursive traversal of the graph, using a slightly complicated reducing function to collect the sub-total. This is my favourite challenge so far.

### Day 8

Being an electrical engineering and computer science major gives me an handy headstart on analysing this challenge, which requires implementing a very simple microcontroller with just three instructions. The core of this is the `run-code` function that takes an initial state, and steps through the given code, fetching instructions and executing them via a reducing function that maintains the state. This is quite a compact and elegant way of implementing a finite state machine. 

The only quirk is that we need to maintain a list of visited states, so we can determine if we've entered an infinite loop. We break when we either terminally normally by the program counter (pc) running out of instructions, or because we're attempting to execute an instruction we've seen before.

Part 1 keeps track of the accumulator register and returns the result before we enter a loop. In part 2 we run through all the `nop` and `jmp` instructions and invert them to see if we avoid entering a loop in the resulting program.

### Day 9

For this one, we need to mess about with sequences and sums over a moving window. The core function for part 1 is `not-a sum`, which checks that a number is not a sum of the previous `n` numbers. 

For the second part, we need to exhaustively generate all the candidate sequences and filter the ones that add to the target, then add the min and max of that range for the result. A bit of fiddly work with sub-sequences and filters.

### Day 10

This challenge is about looking at the differences between consecutive elements in the sequence of adaptors. For part 1, we generate the vector of `deltas`, use the built-in `frequencies` function to count the 1s and 3s, and then multiply the two counts to get the answer.

In part 2, because the number of possible sequences is huge (2^n), we need to be smarter. For the first test set, if you write out the number of possible sequences, you see that you get multiple paths through a region iff there is a sequence of at least three consecutive numbers. For example, 4 -> 5 -> 6 allows the two paths: 4 -> 5 -> 6 and 4 -> 6. Similarly for four number there are 4 possible paths, for five there are 7 paths. At that point the limit of jumping by 3 is reached, and any longer sequences are the overlap of two or more sequences of five consecutive numbers. 

When this translates to deltas, it means we need to look at the length of the sequences of 1s. Four consecutive numbers gives us deltas of `[1 1 1]`. Because the deltas only consist of 1s and 3s, we just partition the delta vector on the element 3. We then run each sequence through a case statement in `paths` to generate the number of paths.  We then multiply them all together. The result can only be a multiple of powers of 2 and 7. 

### Day 11

This is very similar to Conway's Game of Life but with three states, not two. We read in the text file and convert the characters to 0 (floor), 1 (unoccupied), or 2 (occupied). We then take advantage of the `clojure.core.matrix` library to do the heavy lifting and the `neighbour` function to get the adjacent states.

In part 1, we iterate with a reducing function (yet again) that uses the given rules to evolve the matrix. When the input and output are the same, we exit the loop and count the 2s.

Part 2 requires some more calculation, where we look at the sequence of elements along a particular *ray* heading in a given direction from a starting point in the matrix. We return the first non-zero value we hit, or return zero if we overrun the boundaries of the matrix. The ray sequences are lazy and we take only what we need. This is the longest program so far but there is a fair amount of code duplication to account for the two parts. We could certainly shorten the code by refactoring, but it could impact readability.

### Day 12

This is another state machine, this time moving a ship around on a 2D plane. Both parts are pretty straightforward if you know your coordinate geometry. In part 1, we just need to relative to the heading of the ship by the number of units, and change the heading on left and right instructions.

In part 2, we only go forward relative to a waypoint, and otherwise we are only shifting the waypoint. The ship heading becomes irrelevant. The core of this the formula for rotating a point around the origin. I guess I could have worked that out but I looked it up with a quick web search. Geometry is not the point of the challenge, coding is. Btw, the code was refactored for part 2, so part 1 no longer works. Go back to the previous commit if you're interested.

### Day 13

Modular arithmetic comes to play in this challenge. In part 1, we need to find the minimum distance to the next multiple of the bus number from the given timestamp. I use what I call the complement modulo, which is the the divisor minus the remainder, and minimise for that over the collection. We need both the min and the argmin, which we multiply together. Nice and easy.

But then part 2 hits with a threat of very large numbers, making any linear search impractical. However, when we realise that this problem is equivalent to solving a system of linear modulo equations, then the [Chinese Remainder Theorem](https://en.wikipedia.org/wiki/Chinese_remainder_theorem) from number theory shows us the way ahead. For the test set, we effectively need to solve the system: `x = 0 (mod 19) = 1 (mod 31) = 3 (mod 59) = 6 (mod 13) = 0 (mod 7)`, giving us the final answer of `x-7`. The recommended approach (in quadratic time) is to solve the first two equations, then add the third, and so on, by iteratively computing the Bezout coefficients using the extended Euclidean algorithm, and multiplying them through. I found a nice little `xgcd` function, but the rest is manual. The final challenge was to make sure we don't encounter arithmetic overflow. Using `bigint` gets over that.

### Day 14

This challenge is about bit operations, but using "X" for "don't care" or floating to encompass multiple locations in an address space. For the easy first part, we use X as a mask to propagate values, otherwise we set the bit accordingly. We add up all the values in the address space. Because the address space is large (2^36 locations), we use a sparse representation (a simple map) to hold the values, plus the most recent mask.

For part 2, we introduce binary n-cubes. From a geometrical point of view, an X in a binary vector generates an "edge" in that dimension in the binary n-cube. Two Xs generates a plane, three a cube and so on. Consider the value "XX1"; this generates all values [001, 011, 101, 111], which corresponds to one of the faces of the binary 3-cube. My function `mask-gen` is the "clever" bit because it generates the corners of the n-cube from a given binary vector that includes X's. Then we need to do a different masking operation to generate multiple locations that we write values to. 

### Day 15

Part 1 is dead easy. Set up your rule for generating the next element and add it to the end of the vector. We use vector operations because we're dealing with the end of the sequence, and functions like `peek`, `pop`, and `subvec` are all well optimised for vectors. And of course, we use `reduce` to iterate, because that's being functional, kids.

Part 2 is, of course, a different story. We're now looking at the 30,000,000th element in the sequence, not some piddly number in the thousands. After running some quick timings of sequences of increasing length, and even with a few optimisations, it's clear that holding a sequence that long and doing a search over it every iteration is not going to fly. We then realise, of course, that we only need to know the *last* time we saw the given number, and that the highest number seen so far in the sequence increases only very weakly against the sequence length. Enter the hash (using a Clojure map of course), where the keys are the numbers we've seen, and the key's value contains the index of where we saw it last. We also keep a note in the hash of the number we're comparing, and just update the hash in each iteration and pass it on. The performance with this approach is good enough that on my modest electronic abacus, the answer pops out in about a minute. Job done.

## License

Copyright © 2021 Andrew Joyner

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
