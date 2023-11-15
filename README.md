# Ordering-Cross-Spread-Gathers

This repository contains Java source code described in the paper

    Trickett, S., 2024, Ordering Cross-Spread Gathers, Geophysics, 89 (to appear).

Here's the abstract:

    Seismic processing on cross-spread gathers is a valuable but underexploited strategy. To
    do it properly, sources from a single source line and receivers from a single receiver line
    must be ordered in a physically sensible way, so that adjacent sources or receivers on a
    surface diagram are physically near each other. But determining such an ordering is a
    challenge on irregularly acquired land data. I propose novel automatic ordering algorithms
    using tools from graph theory that minimize large gaps and preserve the sequential patterns
    found even in highly irregular acquisition. Java source code is available.

The Java source code contains both classes and JUnit test files (files of the form *Test.java).
The file "read_junit_file" is a Linux script file that compiles the java files and runs a 
specified JUnit test. Instructions on how to use it is in the script file itself.

All files are copyrighted by Stewart Trickett and licensed under the MIT license.
