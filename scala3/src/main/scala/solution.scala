type Target = String
type Command = String
type Dependency = String
type MakeFile = Map[Target, (List[Dependency], Command)]


// Takes: a makefile and target
// Returns: a sequence of commands

def generate_command_sequence(makefile: MakeFile, target: Target): List[Command] = 

    def gcs_rec(deps: List[Dependency], visited: List[Dependency], answer: List[Command], cmd: Command): List[Command] =
        deps.filterNot(visited.contains) match {
            case Nil => cmd :: answer
            case d :: ds =>
                makefile.get(d) match {
                    case None => gcs_rec(ds, d :: visited, answer, cmd)
                    case Some((ddeps, command)) => 
                        val newdeps = ddeps.filterNot(ds.contains)
                        gcs_rec(newdeps ++ ds, d ::visited, cmd :: answer, command)
                }
        }
    makefile.get(target) match {
        case None => Nil
        case Some((dependencies, command)) => 
            gcs_rec(dependencies, Nil, Nil, command)
    }


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