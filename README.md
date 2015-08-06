## Programming exercise - Routing of telephone calls

### Brief description of implementation:

For every given phone number we read files with prices without loading them in the memory, line by line, every file in dedicated thread.
All matched prefixes stored in List and then we found max length prefix with minimal price. To reduce hdd scratching in-memory cache is used.
If route not found in cache we finally try to find it in the files. If there's no matched prefix in the files we save "poison pill"  in the cache for that phone
to designate that there's no route at all.

### Questions:

It's not fully clear statement in exercise:
"... You can assume that each price list can have thousands of entries but they will all fit together in memory. ..."
and exactly: "... they will all fit in memory ..."" - will it all prices or all entries of particular price fit in memory?
I assumed worst case when only one price can fit in memory and decided not to load prices in memory at all but read them line by line.