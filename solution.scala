type Target = String
type Command = String
type Dependency = String
type MakeFile = Map[Target, (List[Dependency], Command)]


// Takes: a makefile and target
// Returns: a sequence of commands
def generate_command_sequence(makefile: MakeFile, target: Target): List[Command] = 
    def gcs_rec(makefile: MakeFile, target: Target, visited: Set[Dependency]): List[Command] =
        makefile.get(target) match {
            case None => List()
            case Some((dependencies, command)) => 
                val commands = dependencies.foldRight(List())((dependency, acc) => 
                    visited.contains(dependency) match {
                        case true => acc
                        case false => acc ++ gcs_rec(makefile, dependency, visited + dependency)
                    }
                )
                commands :+ command
                
        }

    gcs_rec(makefile, target, Set())


@main def test: Unit = {
    val makefile0 = Map(
            "minesweeper" -> (List("board.o", "referee.o", "ui.o"), "gcc board.c referee.c ui.c -o minesweeper"),
            "board.o" -> (List("board.c"), "gcc -c board.c -o board.o"),
            "referee.o" -> (List("referee.c", "board.o"), "gcc -c referee.c -o referee.o"),
            "ui.o"-> (List("ui.c"), "gcc -c ui.c -o ui.o") 
    )

    // assert(generate_command_sequence(makefile0, "ui.o") ==
    //     List("gcc -c ui.c -o ui.o")) 
    
    // println(generate_command_sequence(makefile0, "ui.o"))   
    // println(generate_command_sequence(makefile0, "referee.o"))   
    println(generate_command_sequence(makefile0, "minesweeper"))   
}