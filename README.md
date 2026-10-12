# October Chess Engine

A "baby's first" multi-threaded chess engine in Java I developed in
October 2010. It plays a weak game of both classic chess and [gothic
chess](https://en.wikipedia.org/wiki/Capablanca_chess). Some of the more
complicated rules are probably wrong/missing (draws, castling), and it
doesn't support underpromotion.

## Getting started (quick and dirty)

Obtain a JDK, then compile and run:

    $ javac com/nullprogram/chess/Chess.java
    $ java com.nullprogram.chess.Chess

## Playing strength

In October 2026 the engine was given a UCI front end and rated by
playing gauntlets with [fastchess][fc] against Stockfish limited by
`UCI_Elo` (calibrated to CCRL 40/4, at 60s+0.6s). Openings came from
`8moves_v3.pgn`, and each opening was played with both colors. October
searches to a fixed depth and ignores the clock.

| Depth       | Elo  | 95% CI | Games                            |
|-------------|------|--------|----------------------------------|
| 4 (default) | 1235 | ±50    | 200 vs. Stockfish 1320           |
| 5           | 1351 | ±38    | 400 vs. Stockfish 1320 and 1600  |
| 6           | 1490 | ±70    | 120 vs. Stockfish 1600           |

Each ply is worth roughly 115–140 Elo, but also costs ~10x the search
time (about 0.3s, 3.5s, and 40s per move on one thread) since moves are
not ordered for alpha-beta.

To play it from a UCI GUI or match runner, compile the UCI front end
and run it from the top-level directory, so it can find its AI
configuration:

    $ javac com/nullprogram/chess/Uci.java
    $ java -Doctober.threads=1 com.nullprogram.chess.Uci

It accepts `setoption name Depth value N` and only `position startpos
[moves ...]`, not FEN.

[fc]: https://github.com/Disservin/fastchess

## See also

* [A Modest Chess Engine](http://nullprogram.com/blog/2010/10/17/)
* [October's Chess Engine Gets Some Attention](http://nullprogram.com/blog/2011/04/09/)
