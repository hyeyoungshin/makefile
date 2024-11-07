#  A MakefileRep is...
#    List[Tuple(Target, List[Dependency], Command)]
##   List[Tuple(String, List[String], String)]
###  List[Map(String -> (List[String], String))]

# Target is a String
# Command is a String

#  MakeFileRep Target -> List[Command]
def generate_command_sequence(makefile, target):
    # look for target in the list
    # if not there, return empty list
    # else, get its dependencies
    # generate command sequence for each dependency as target
    def gcs_rec(makefile, target, visited):
        if target not in makefile:
            return []    
        else: 
            (dependencies, command) = makefile[target]

            commands = []

            for dependency in dependencies:
                if dependency not in visited:
                    visited.append(dependency)
                    commands += gcs_rec(makefile, dependency, visited)

            return commands + [command]
    
    return gcs_rec(makefile, target, [])

        

        
# usage example
makefile0 = {
    "minesweeper": (["board.o", "referee.o", "ui.o"], "gcc board.c referee.c ui.c -o minesweeper"),
    "board.o": (["board.c"], "gcc -c board.c -o board.o"),
    "referee.o" : (["referee.c", "board.o"], "gcc -c referee.c -o referee.o"),
    "ui.o": (["ui.c"], "gcc -c ui.c -o ui.o") 
}

print(generate_command_sequence(makefile0, "minesweeper"))

assert(generate_command_sequence(makefile0, "minesweeper") == 
    [    
        "gcc -c board.c -o board.o", 
        "gcc -c referee.c -o referee.o", 
        "gcc -c ui.c -o ui.o", 
        "gcc board.c referee.c ui.c -o minesweeper",
    ]
)

assert(generate_command_sequence(makefile0, "ui.o") == 
    ["gcc -c ui.c -o ui.o"]
)

assert(generate_command_sequence(makefile0, "referee.o") == 
    ['gcc -c board.c -o board.o', 'gcc -c referee.c -o referee.o']
)


# TODO: Write this in pure functional way in Scala
# TODO: Write this in python using a global variable which each recursive call mutates 
# TODO: Makefile structure can be a graph! A directed acyclic graph. Topological sort on the graph. 
# visited nodes? to avoid duplicates
# is command already in commands?
