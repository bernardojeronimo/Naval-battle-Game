import kotlin.math.sqrt
import java.io.File
import java.io.PrintWriter

var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()


fun tamanhoTabuleiroValido(numLinhas : Int, numColunas : Int) : Boolean
{
    when
    {
        numLinhas == numColunas && (numLinhas == 4 || numLinhas == 5 || numLinhas == 7 || numLinhas == 8 || numLinhas == 10)-> return true
    }
    return false
}
fun criaLegendaHorizontal(numColunas: Int) : String
{
    var count = 0
    var horizontal = ""
    while(count <= numColunas - 2)
    {
        horizontal += (65 + count).toChar() + " | "
        count++
    }
    horizontal += (65 + count).toChar()
    return horizontal
}
fun criaTerreno(numLinhas: Int,numColunas: Int) : String
{
    var count1 = 1
    var vertical = "|"
    var count = 0
    var tabuleiro = "| " + criaLegendaHorizontal(numColunas) + " |"
    while (count1 <= numLinhas)
    {
        while (count <=numColunas - 1)
        {
            vertical += "   |"
            count++
        }
        tabuleiro += "\n$vertical $count1"
        count1++
    }
    return "\n$tabuleiro\n"
}
fun menuDefinirTabuleiro()
{
    var linhas : Int? = 0
    var colunas : Int? = 0
    println("""
        |
        |> > Batalha Naval < <
        |
        |Defina o tamanho do tabuleiro:
        """.trimMargin())
    do {
        println("Quantas linhas?")
        linhas = readln().toIntOrNull()
        when (linhas) {
            null -> println("!!! Número de linhas invalidas, tente novamente")
            -1 -> menuPrincipal()
        }
    }
    while (linhas == null)
    if (linhas != -1) {
        do {
            println("Quantas colunas?")
            colunas = readln().toIntOrNull()
            when (colunas) {
                null -> println("!!! Número de linhas invalidas, tente novamente")
                -1 -> menuPrincipal()
            }
        } while (colunas == null)
        if (tamanhoTabuleiroValido(linhas, colunas)) {
            numColunas = colunas!!
            numLinhas = linhas!!
            tabuleiroHumano = criaTabuleiroVazio(linhas,colunas)
            for (l in obtemMapa(tabuleiroHumano,true)) {
                println(l)
            }
            menuDefinirNavios()
        }
    }
}
fun processaCoordenadas(coordenadas : String, linhas : Int , colunas : Int) : Pair<Int,Int>?{
    var count = 0
    var numeros = ""
    var count2 = 0
    var count3 = 1
    when{
        coordenadas == "" || coordenadas.length < 3 -> return null
    }
    while (coordenadas[count] != ','){
        count++
        if (coordenadas.length - 1 <= count){
            return null
        }
    }
    while (count2 != count){
        if (coordenadas[count2].isDigit()) {
            numeros += coordenadas[count2]
            count2++
        }
        else{
            return null
        }
    }
    count++
    while (coordenadas[count] != (count3 + 64).toChar())
    {
        count3++
    }
    return when{
        count != coordenadas.length - 1 -> null
        numeros.toInt() in 1..linhas && count3 in (1..colunas) -> return Pair(numeros.toInt(),count3)
        else -> null
    }
}
fun menuDefinirNavios() {
    val numNaviosTipo = calculaNumNavios(tabuleiroHumano.size,tabuleiroHumano[0].size)
    var dimensao = 0
    var nome = ""
    for (i in 0 until numNaviosTipo.size) {
        when(i)
        {
            0 -> { dimensao = 1
                nome = "submarino"}
            1 -> { dimensao = 2
                nome = "contra-torpedeiro"}
            2 -> { dimensao = 3
                nome = "navio-tanque"}
            3 -> { dimensao = 4
                nome = "porta-avioes"}
        }
        if (numNaviosTipo[i]>0){
            for (j in 1..numNaviosTipo[i]){
                println("Insira as coordenadas de um $nome:\nCoordenadas? (ex: 6,G)")
                var coordenadas = readln()
                if (coordenadas == "-1") {
                    return main()
                }
                var coordenadasNavio = processaCoordenadas(coordenadas, numLinhas, numColunas)
                if (dimensao == 1){
                    while (coordenadasNavio == null || coordenadas == "" ||
                        !insereNavioSimples(tabuleiroHumano,coordenadasNavio.first,coordenadasNavio.second,dimensao)) {
                        println("!!! Posicionamento invalido, tente novamente\nInsira as coordenadas de um $nome:\nCoordenadas? (ex: 6,G)")
                        coordenadas = readln()
                        if (coordenadas == "-1") {
                            return main()
                        }
                        coordenadasNavio = processaCoordenadas(coordenadas, numLinhas, numColunas)
                    }
                }else{
                    println("Insira a orientacao do navio:\nOrientacao? (N, S, E, O)")
                    var orientacao = readln()
                    if (orientacao == "-1"){
                        return main()
                    }
                    while (coordenadasNavio == null || coordenadas == "" ||
                        !insereNavio(tabuleiroHumano,coordenadasNavio.first,coordenadasNavio.second,orientacao,dimensao)) {
                        println("!!! Posicionamento invalido, tente novamente\nInsira as coordenadas de um $nome:\nCoordenadas? (ex: 6,G)")
                        coordenadas = readln()
                        if (coordenadas == "-1") {
                            return main()
                        }
                        coordenadasNavio = processaCoordenadas(coordenadas, numLinhas, numColunas)
                        println("Insira a orientacao do navio:\nOrientacao? (N, S, E, O)")
                        orientacao = readln()
                        if (orientacao == "-1"){
                            return main()
                        }
                    }
                }
                val tabueleiro = obtemMapa(tabuleiroHumano,true)
                for (linha in 0 until tabueleiro.size){
                    println(tabueleiro[linha])
                }
            }
        }
    }
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
    tabuleiroComputador()
}
fun tabuleiroComputador()
{
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas,numColunas)
    tabuleiroComputador = criaTabuleiroVazio(numLinhas,numColunas)
    println("Pretende ver o mapa gerado para o Computador? (S/N)")
    val resposta = readln()
    when (resposta) {
        "S" ->{ preencheTabuleiroComputador(tabuleiroComputador,calculaNumNavios(numLinhas,numColunas))
            for (l in obtemMapa(tabuleiroComputador,true))
            {
                println(l)
            }
            return menuPrincipal()
        }
        "N" -> menuPrincipal()
        else -> print("!!! Opcao invalida, tente novamente")
    }
}

fun menuPrincipal(){
    var escolha: Int? = 0
    println("""
            |
            |> > Batalha Naval < <
            |
            |1 - Definir Tabuleiro e Navios
            |2 - Jogar
            |3 - Gravar
            |4 - Ler
            |0 - Sair
            |
        """.trimMargin())
    do {
        escolha = readln().toIntOrNull()
        when{
            escolha == 1 -> menuDefinirTabuleiro()
            escolha == 2 -> menuJogar()
            escolha == 3 -> { println("Introduza o nome do ficheiro (ex: jogo.txt)")
                val ficheiro = readln()
                gravarJogo(ficheiro,tabuleiroHumano,tabuleiroPalpitesDoHumano,tabuleiroComputador,tabuleiroPalpitesDoComputador)
                println("Tabuleiro ${tabuleiroHumano.size}x${tabuleiroHumano.size} gravado com sucesso")
                return menuPrincipal()
            }
            escolha == 4 -> {
                println("Introduza o nome do ficheiro (ex: jogo.txt)")
                val ficheiro = readln()
                tabuleiroHumano = lerJogo(ficheiro,1)
                tabuleiroPalpitesDoHumano = lerJogo(ficheiro,2)
                tabuleiroComputador = lerJogo(ficheiro,3)
                tabuleiroPalpitesDoComputador = lerJogo(ficheiro,4)
                println("Tabuleiro ${numLinhas}x${numColunas} lido com sucesso")
                for (l in obtemMapa(tabuleiroHumano,true))
                {
                    println(l)
                }
                return menuPrincipal()
            }
            escolha != 0 -> println("!!! Opcao invalida, tente novamente")
        }

    } while (escolha !in 0..4)

}


fun calculaNumNavios(numLinhas: Int, numColunas: Int) : Array<Int>
{
    var dimensao = emptyArray<Int>()
    if(numLinhas == numColunas) {
        dimensao =  when (numLinhas)
        {
            4 -> arrayOf(2,0,0,0)
            5 -> arrayOf(1,1,1,0)
            7 -> arrayOf(2,1,1,1)
            8 -> arrayOf(2,2,1,1)
            10 -> arrayOf(3,2,1,1)
            else -> dimensao
        }
    }
    return dimensao
}

fun criaTabuleiroVazio(numLinhas: Int,numColunas: Int) : Array<Array<Char?>>
{
    return Array(numLinhas){Array(numColunas){ null }}
}
fun coordenadaContida(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean
{
    when
    {
        tabuleiro.size >= numLinhas && tabuleiro.size >= numColunas && numLinhas >= 1 && numColunas >= 1 -> return true
    }
    return false
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int,Int>>
{
    var count = 0
    var count2 = 0
    for (num1 in 0..coordenadas.size - 1) {
        when {
            coordenadas[num1] == Pair(0, 0) -> {
                count++
            }
        }
    }
    val retorno = Array<Pair<Int,Int>>(coordenadas.size - count){Pair(0,0)}
    for (num1 in 0..coordenadas.size - 1) {
        when {
            coordenadas[num1] != Pair(0, 0) -> {
                retorno[count2] = coordenadas[num1]
                count2++
            }
        }
    }
    return retorno
}

fun juntarCoordenadas(coordenadas1: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    return coordenadas1 + coordenadas2
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int,orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    if (dimensao > 0) {
        val retorno = Array<Pair<Int, Int>>(dimensao) { Pair(0, 0) }
        for (count in 0..dimensao - 1) {
            when (orientacao) {
                "E" ->
                    when {
                        coordenadaContida(tabuleiro, numLinhas, numColunas + count) ->
                            retorno[count] = Pair(numLinhas, numColunas + count)

                        else -> return emptyArray()
                    }

                "O" ->
                    when {
                        coordenadaContida(tabuleiro, numLinhas, numColunas - count) ->
                            retorno[count] = Pair(numLinhas, numColunas - count)

                        else -> return emptyArray()
                    }

                "N" ->
                    when {
                        coordenadaContida(tabuleiro, numLinhas - count, numColunas) ->
                            retorno[count] = Pair(numLinhas - count, numColunas)

                        else -> return emptyArray()
                    }

                "S" ->
                    when {
                        coordenadaContida(tabuleiro, numLinhas + count, numColunas) ->
                            retorno[count] = Pair(numLinhas + count, numColunas)

                        else -> return emptyArray()
                    }
            }
        }
        return retorno
    }
    return emptyArray()
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, numLinhas: Int,
                              numColunas: Int, orientacao: String, dimensao: Int ): Array<Pair<Int, Int>>
{
    var resultado = emptyArray<Pair<Int,Int>>()
    for (count in -1 ..1) {
        for (count1 in -1..dimensao) {
            val coordenada = when (orientacao) {
                "E" -> Pair(numLinhas + count, numColunas + count1)
                "O" -> Pair(numLinhas + count, numColunas - count1)
                "S" -> Pair(numLinhas + count1, numColunas + count)
                "N" -> Pair(numLinhas - count1, numColunas + count)
                else -> null
            }

            if (coordenada != null && coordenadaContida(tabuleiro, coordenada.first, coordenada.second)) {
                resultado = juntarCoordenadas(resultado, arrayOf(coordenada))
            }
        }
    }
    val navio = gerarCoordenadasNavio(tabuleiro,numLinhas,numColunas,orientacao,dimensao)
    if (navio.size > 0) {
        for (count in 0..navio.size - 1) {
            for (count1 in 0..resultado.size - 1) {
                when {
                    resultado[count1] == navio[count] -> resultado[count1] = Pair(0, 0)
                }
            }
        }
        val borracha = limparCoordenadasVazias(resultado)
        return borracha
    }
    return emptyArray()
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean
{
    for (coords in 0.. coordenadas.size - 1) {
        for (linha in 0..tabuleiro.size -1) {
            for (coluna in 0..tabuleiro.size -1) {
                when
                {
                    !coordenadaContida(tabuleiro,coordenadas[coords].first,coordenadas[coords].second) -> return false
                    coordenadas[coords] == Pair(1+linha,1+coluna) -> when{tabuleiro[linha][coluna] != null -> return false}
                }
            }
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, dimensao: Int): Boolean {
    val navio = juntarCoordenadas(gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "E", dimensao),
        gerarCoordenadasFronteira(tabuleiro, numLinhas, numColunas, "E", dimensao))
    if (navio.size > 0) {
        when {
            estaLivre(tabuleiro, navio) -> {
                for (count in 0..dimensao - 1) {
                    tabuleiro[numLinhas - 1][numColunas + count - 1] = (dimensao + 48).toChar()
                }
                return true
            }

        }
    }
    return false
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String,
                dimensao: Int ): Boolean
{
    val navio = juntarCoordenadas(gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao),
        gerarCoordenadasFronteira(tabuleiro, numLinhas, numColunas, orientacao, dimensao))
    if (navio.size > 0)
    {
        when
        {
            estaLivre(tabuleiro,navio) == true ->
            {
                for (count in 0..dimensao-1) {
                    when(orientacao)
                    {
                        "E" -> tabuleiro[numLinhas - 1][numColunas + count - 1] = (dimensao + 48).toChar()
                        "O" -> tabuleiro[numLinhas - 1][numColunas - count - 1] = (dimensao + 48).toChar()
                        "S" -> tabuleiro[numLinhas + count - 1][numColunas - 1] = (dimensao + 48).toChar()
                        "N" -> tabuleiro[numLinhas - count - 1][numColunas - 1] = (dimensao + 48).toChar()
                    }

                }
                return true
            }

        }
        return false
    }
    return false
}
fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, numNavios: Array<Int>) {
    for (dimensao in 4 downTo 1) {
        for (i in 0 until numNavios[dimensao - 1]) {
            var sucesso = false
            while (!sucesso) {
                val numLinhas = (1..tabuleiro.size).random()
                val numColunas = (1..tabuleiro[numLinhas - 1].size).random()
                val orientacao = when ((0..3).random()) {
                    0 -> "E"
                    1 -> "O"
                    2 -> "S"
                    else -> "N"
                }

                sucesso = coordenadaContida(tabuleiro, numLinhas, numColunas) &&
                        insereNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
            }
        }
    }
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
    var completo = 1
    val tamanho = arrayOf('1','2','3','4')
    var dimensao = 0
    var count = 1
    when{
        tabuleiro[numLinhas -1][numColunas -1] == '4' -> dimensao = 4
        tabuleiro[numLinhas -1][numColunas -1] == '3' -> dimensao = 3
        tabuleiro[numLinhas -1][numColunas -1] == '2' -> dimensao = 2
        tabuleiro[numLinhas -1][numColunas -1] == '1' -> return true
        else -> return false
    }
    while (count != dimensao){
        when {numLinhas + count in 1..tabuleiro.size -> if(tabuleiro[numLinhas + count -1][numColunas -1] == tamanho[dimensao - 1]) { completo++}}
        when {numLinhas - count in 1..tabuleiro.size -> if(tabuleiro[numLinhas - count -1][numColunas -1] == tamanho[dimensao - 1]) { completo++}}
        when {numColunas + count in 1..tabuleiro.size -> if(tabuleiro[numLinhas -1][numColunas + count -1] == tamanho[dimensao - 1]) { completo++}}
        when {numColunas - count in 1..tabuleiro.size -> if(tabuleiro[numLinhas -1][numColunas - count -1] == tamanho[dimensao - 1]) { completo++}}
        count++

    }
    if(completo == dimensao){
        return true
    }
    else{
        return false
    }
}


fun obtemMapa(tabuleiro: Array<Array<Char?>>, verifica: Boolean): Array<String> {

    val tamanhoMapa =  tabuleiro.size + 1
    var mapa = Array(tamanhoMapa) { "" }
    var numLinhas = 0
    mapa[numLinhas++] = "| " + criaLegendaHorizontal(tabuleiro.size) + " |"
    for (numLinha in 0 until tabuleiro.size) {
        var linha = "|"
        for (numColunas in 0 until tabuleiro[numLinha].size) {
            val conteudo = if(verifica){tabuleiro[numLinha][numColunas]?: '~'}
            else {
                val navioCompleto = navioCompleto(tabuleiro, numLinha + 1, numColunas + 1)
                when(tabuleiro[numLinha][numColunas]) {
                    '4' ->  if(!navioCompleto) '\u2084' else '4'
                    '3' ->  if(!navioCompleto) '\u2083' else '3'
                    '2' ->  if(!navioCompleto) '\u2082' else '2'
                    else -> tabuleiro[numLinha][numColunas] ?: '?'
                }
            }
            linha += " $conteudo |"
        }
        mapa[numLinhas++] = "$linha ${numLinha + 1}"
    }
    return mapa
}


fun lancarTiro(tabuleiro: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>): String {
    when (tabuleiro[coordenadas.first - 1][coordenadas.second - 1]) {
        '1' -> {
            tabuleiroPalpites[coordenadas.first - 1][coordenadas.second - 1] = '1'
            return "Tiro num submarino."
        }
        '2' -> {
            tabuleiroPalpites[coordenadas.first - 1][coordenadas.second - 1] = '2'
            return "Tiro num contra-torpedeiro."
        }
        '3' -> {
            tabuleiroPalpites[coordenadas.first - 1][coordenadas.second - 1] = '3'
            return "Tiro num navio-tanque."
        }

        '4' -> {
            tabuleiroPalpites[coordenadas.first - 1][coordenadas.second - 1] = '4'
            return "Tiro num porta-avioes."
        }
        else -> {
            tabuleiroPalpites[coordenadas.first - 1][coordenadas.second - 1] = 'X'
            return "Agua."}
    }
}

fun geraTiroComputador(tabuleiro: Array<Array<Char?>>): Pair<Int, Int> {
    var numLinhas: Int
    var numColunas: Int
    do {
        numLinhas = (1..tabuleiro.size).random()
        numColunas = (1..tabuleiro.size).random()
    } while (tabuleiro[numLinhas - 1][numColunas - 1] != null)

    lancarTiro(tabuleiro, criaTabuleiroVazio(tabuleiro.size, tabuleiro.size), Pair(numLinhas, numColunas))
    return Pair(numLinhas, numColunas)
}


fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    var submarino = 0
    var contraTorpedeiro = 0
    var navioTanque = 0
    var portaAvioes = 0

    for (numLinha in 0 until tabuleiro.size) {
        for (numColuna in 0 until tabuleiro.size) {
            if (navioCompleto(tabuleiro, numLinha + 1, numColuna + 1)) {
                when (tabuleiro[numLinha][numColuna]) {
                    '4' -> if (dimensao == 4) portaAvioes++
                    '3' -> if (dimensao == 3) navioTanque++
                    '2' -> if (dimensao == 2) contraTorpedeiro++
                    '1' -> if (dimensao == 1) submarino++
                }
            }
        }
    }
    return when (dimensao) {
        4 -> portaAvioes / 4
        3 -> navioTanque / 3
        2 -> contraTorpedeiro / 2
        1 -> submarino
        else -> 0
    }
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    for (dimensao in 1..4) {
        if (contarNaviosDeDimensao(tabuleiro, dimensao) != calculaNumNavios(tabuleiro.size, tabuleiro.size)[dimensao - 1]) {
            return false
        }
    }
    return true
}


fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): Array<Array<Char?>> {
    val ficheiro = File(nomeDoFicheiro).readLines()

    numLinhas = ficheiro[0][0].digitToInt()
    numColunas = ficheiro[0][2].digitToInt()
    val linhaMin = 3 * tipoDeTabuleiro + 1 + numLinhas * (tipoDeTabuleiro - 1)
    var linha = linhaMin
    val tabuleiro = criaTabuleiroVazio(numLinhas, numColunas)
    while (linha in (3 * tipoDeTabuleiro + 1 + numLinhas * (tipoDeTabuleiro - 1) until 3 * tipoDeTabuleiro + 1 + numLinhas * tipoDeTabuleiro)) {
        var coluna = 0
        var count = 0
        while (count < numColunas && coluna < ficheiro[linha].length) {
            when (ficheiro[linha][coluna]) {
                ',' -> count++
                in '1'..'4', 'X' -> tabuleiro[linha - linhaMin][count] = ficheiro[linha][coluna]
            }
            coluna++
        }
        linha++
    }
    return tabuleiro
}

fun gravarJogo(
    nomeDoFicheiro: String,
    tabuleiroRealHumano: Array<Array<Char?>>,
    tabuleiroPalpitesHumano: Array<Array<Char?>>,
    tabuleiroRealComputador: Array<Array<Char?>>,
    tabuleiroPalpitesComputador: Array<Array<Char?>>
) {
    val filePrinter = File(nomeDoFicheiro).printWriter()
    filePrinter.println("${tabuleiroRealHumano.size},${tabuleiroRealHumano.size}\n")

    estruturaTabuleiro(filePrinter, "Jogador\nReal", tabuleiroRealHumano)
    estruturaTabuleiro(filePrinter, "\nJogador\nPalpites", tabuleiroPalpitesHumano)
    estruturaTabuleiro(filePrinter, "\nComputador\nReal", tabuleiroRealComputador)
    estruturaTabuleiro(filePrinter, "\nComputador\nPalpites", tabuleiroPalpitesComputador)

    filePrinter.close()
}

fun estruturaTabuleiro(filePrinter: PrintWriter, titulo: String, tabuleiro: Array<Array<Char?>>) {
    filePrinter.println(titulo)
    for (linha in 0 until tabuleiro.size) {
        for (coluna in 0 until tabuleiro[linha].size) {
            filePrinter.print("${tabuleiro[linha][coluna] ?: ""}")
            if (coluna < tabuleiro[linha].size - 1) {
                filePrinter.print(",")
            }
        }
        filePrinter.println()
    }
}

fun menuJogar() {
    when {
        numLinhas == -1 -> {
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            return menuPrincipal() } }
    val tiroComputador = geraTiroComputador(tabuleiroPalpitesDoHumano)
    while (!venceu(tabuleiroPalpitesDoComputador) || !venceu(tabuleiroPalpitesDoHumano)) {
        for (l in obtemMapa(tabuleiroPalpitesDoHumano, false)) { println(l) }
        println("""Indique a posição que pretende atingir
            |Coordenadas? (ex: 6,G)
        """.trimMargin())
        var coordenadasNavio: Pair<Int, Int>? = null
        while (coordenadasNavio == null) {
            val input = readln()
            if (input == "?") {
                val naviosFaltando = calculaNaviosFaltaAfundar(tabuleiroPalpitesDoHumano)
                var naviosFaltandoString = ""
                for (i in 0..3) {
                    val count = naviosFaltando[i]
                    val nome = when (i) {
                        0 -> "porta-avioes(s)"
                        1 -> "navio-tanque(s)"
                        2 -> "contra-torpedeiro(s)"
                        3 -> "submarino(s)"
                        else -> ""
                    }
                    if (count > 0) { naviosFaltandoString = if (naviosFaltandoString.length == 0) { "$count $nome" }
                    else { "$naviosFaltandoString; $count $nome" } } }
                if (naviosFaltandoString.length > 0) { println("Falta afundar: $naviosFaltandoString") }
                println("""Indique a posição que pretende atingir
                |Coordenadas? (ex: 6,G)""".trimMargin()) }
            if (input == "-1") { return menuPrincipal() }
            if(input == "" ) { println("Coordenadas invalidas! tente novamente") }
            coordenadasNavio= processaCoordenadas(input, numLinhas, numColunas)
            if (coordenadasNavio != null) { lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasNavio) }
            lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, tiroComputador) }
        if (navioCompleto(tabuleiroPalpitesDoHumano, coordenadasNavio.first, coordenadasNavio.second) ) {
            println(">>> HUMANO >>>${lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasNavio)} Navio ao fundo!") }
        else { println(">>> HUMANO >>>${lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasNavio)}") }
        if(!venceu(tabuleiroPalpitesDoHumano)) { println("Computador lancou tiro para a posicao $tiroComputador")
            if (navioCompleto(tabuleiroPalpitesDoComputador, tiroComputador.first, tiroComputador.second)) {
                println(">>> COMPUTADOR >>>${lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, tiroComputador)} Navio ao fundo!")
            }
            else { println(">>> COMPUTADOR >>>${lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, tiroComputador)}") } }
        obtemMapa(tabuleiroPalpitesDoComputador, false)
        obtemMapa(tabuleiroPalpitesDoHumano, false)
        when {
            venceu(tabuleiroPalpitesDoHumano) -> {
                println("PARABENS! Venceu o jogo!")
                println("Prima enter para voltar ao menu principal")
                readln()
                return menuPrincipal() }
            venceu(tabuleiroPalpitesDoComputador) -> {
                println("OPS! O computador venceu o jogo!")
                println("Prima enter para voltar ao menu principal")
                readln()
                return menuPrincipal() }
            else -> {
                println("Prima enter para continuar")
                readln() }
        }
    }
}
fun calculaEstatisticas(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {
    val retorno = arrayOf(0, 0, 0)

    for (numLinha in 0 until tabuleiroPalpites.size) {
        for (numColuna in 0 until tabuleiroPalpites[numLinha].size) {
            if (tabuleiroPalpites[numLinha][numColuna] != null) {
                val navioCompleto = navioCompleto(tabuleiroPalpites, numLinha + 1, numColuna + 1)
                retorno[0]++
                when (tabuleiroPalpites[numLinha][numColuna]) {
                    '1' -> {
                        retorno[1]++
                        retorno[2]++
                    }
                    in '2'..'4' -> {
                        if (navioCompleto) {
                            retorno[1]++
                            if (tabuleiroPalpites[numLinha][numColuna] == '4' && retorno[2] < 4) { retorno[2]++ }
                            else if (tabuleiroPalpites[numLinha][numColuna] == '3' && retorno[2] < 3) { retorno[2]++ }
                            else if (tabuleiroPalpites[numLinha][numColuna] == '2' && retorno[2] < 2) { retorno[2]++ }
                        }
                        else if (!navioCompleto) { retorno[1]++ }
                    }
                }
            }
        }
    }
    return retorno
}

fun calculaNaviosFaltaAfundar(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {
    val retorno = arrayOf(0, 0, 0, 0)

    for (dimensao in 1..4) {
        val numNavios = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)[dimensao - 1]
        val count = numNavios - contarNaviosDeDimensao(tabuleiroPalpites, dimensao)

        when (dimensao) {
            1 -> retorno[3] = count
            2 -> retorno[2] = count
            3 -> retorno[1] = count
            4 -> retorno[0] = count
        }
    }

    return retorno
}

fun main() {
    menuPrincipal()
}
