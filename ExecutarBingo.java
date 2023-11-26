import java.sql.SQLOutput;

public class ExecutarBingo {

    static String inputJogadores = "Player1-Player2-Player3-Player4-Player5";

    private static final String INVALID_INPUT = "Entrada inválida. Tente novamente!";
    private static final String INVALID_CARTELAS = "Cartelas inválidas. Tente novamente!";

    public static String[] inputUsuarios() {
        do {
            String input = MenuBingo.getUserInput("Entre com os jogadores separados por hifen(-). [X-sair]: ");
            if (input.equals("X")) {
                return null;
            } else if (input.equals("")) {
                System.out.printf("[%s] ", INVALID_INPUT);
                continue;
            }
            String[] jogadores = input.split("\\s*-\\s*");
            if (jogadores.length > 0) {
                return jogadores;
            } else {
                System.out.printf("[%s] ", INVALID_INPUT);
            }
        } while (true);
    }

    public static String[] inputCartelas(int qtdCartelas) {
        do {
            String input = MenuBingo.getUserInput(String.format("Entre com %s cartela(s) de %s números cada, separadas por %s. [X-Voltar]: ",
                    qtdCartelas,
                    DadosBingo.QTD_NUMEROS_POR_CARTELA,
                    (qtdCartelas == 1) ? "ponto e vírgula(;)" : "hifen(-)"));
            if (input.equals("X")) {
                return null;
            } else if (input.equals("")) {
                System.out.printf("[%s] ", INVALID_INPUT);
                continue;
            }
            String[] cartelas = input.split("\\s*-\\s*");
            if (cartelas.length == qtdCartelas) {
                return cartelas;
            } else if (cartelas.length != qtdCartelas) {
                System.out.printf("[%s] ", INVALID_CARTELAS);
                System.out.printf("\s*** Voce forneceu %s cartelas. Devem ser %s!\n", cartelas.length, DadosBingo.dadosBingo.length);
            } else {
                System.out.printf("[%s] ", INVALID_INPUT);
            }
        } while (true);
    }

    public static void executarCartelaAutomatica() {
        mensagemConsole("Gerando lista de jogadores e suas cartelas");
        DadosBingo.iniciarDados(inputJogadores.split("\\s*-\\s*"));
        mensagemConsole("Sorteando os números e verificando ganhador");
        sortear(DadosBingo.numerosEmbaralhadoBingo);
    }

    public static void executarSorteio(boolean executarSorteioAutomatico) {
        if (!inputDados()) return;
        if (executarSorteioAutomatico) {
            mensagemConsole("Sorteando os números e verificando ganhador");
            sortear(DadosBingo.numerosEmbaralhadoBingo);
        } else {
            System.out.println("**** FORNEÇA OS NÚMEROS PARA O SORTEIO:");
            String[] cartelas = inputCartelas(1);
            if (cartelas == null) return;
            sortear(converterParaArrayInt(cartelas[0].split(DadosBingo.SEP_ITENS)));
        }
    }

    private static boolean inputDados() {
        String[] jogadores = inputUsuarios();
        if (jogadores == null) return false;

        DadosBingo.iniciarDados(jogadores);

        String[] cartelas = inputCartelas(jogadores.length);
        if (cartelas == null) return false;

        DadosBingo.iniciarDados(jogadores, cartelas);
        return true;
    }

    private static void sortear(int[] numerosParaSortear) {
        boolean bingo = false;
        int rodadas = 0;
        while (!bingo) {
            bingo = false;
            int numero = numerosParaSortear[rodadas];
            if (DadosBingo.addNumeroSorteado(String.valueOf(numero))) {
                bingo = gerarGanhadores(String.valueOf(numero));
                if (bingo) {
                    printEstatisticas();
                }
            }
            rodadas++;
            if (rodadas >= DadosBingo.numerosEmbaralhadoBingo.length) {  // scape trigger...
                System.out.println("  *** PANIC *** Sorteio abortado por indefinição do ganhador!");
                return;
            }
        }
    }

    private static void printEstatisticas() {
        System.out.printf("\n[BINGO] - RESULTADO (lista dos %s primeiros colocados): ", Math.min(10, DadosBingo.dadosBingo.length));
        DadosBingo.ordenarPorQuantidadeAcertos();
        System.out.println("¨".repeat(80));
        for (int i = 0; i < Math.min(10, DadosBingo.dadosBingo.length); i++) {
            System.out.printf("%02d - %s \tCartela: %s \tAcertos: %s %s\n",
                    i + 1,
                    DadosBingo.getJogador(DadosBingo.dadosBingo[i]).concat(" ".repeat(8)).substring(0, 8),
                    DadosBingo.getCartela(DadosBingo.dadosBingo[i]).concat(" ".repeat(15)).substring(0, 15),
                    DadosBingo.getAcertos(DadosBingo.dadosBingo[i]),
                    i == 0 ? "[* BINGO *]" : "");
        }
        System.out.println("¨".repeat(80));
    }

    public static boolean gerarGanhadores(String numero) {
        for (int i = 0; i < DadosBingo.dadosBingo.length; i++) {
            String[] cartela = DadosBingo.getCartela(DadosBingo.dadosBingo[i]).split(DadosBingo.SEP_ITENS);
            String[] acertos = DadosBingo.getAcertos(DadosBingo.dadosBingo[i]).split(DadosBingo.SEP_ITENS);
            if (cartelaContemNumero(numero, cartela)) {
                if (!cartelaContemNumero(numero, acertos)) {
                    DadosBingo.dadosBingo[i] += numero.concat(DadosBingo.SEP_ITENS);
                }
                if (DadosBingo.countAcertos(DadosBingo.dadosBingo[i]) >= DadosBingo.QTD_NUMEROS_POR_CARTELA) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean cartelaContemNumero(String numero, String[] cartela) {
        for (String s : cartela) {
            if (numero.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static void mensagemConsole(String mensagem) {
        System.out.print("  >> ".concat(mensagem.concat(".".repeat(48)).substring(0, 45).concat(": ")));
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(250);
                System.out.print("■");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    private static int[] converterParaArrayInt(String[] aString) {
        int[] aInt = new int[aString.length];
        for (int i = 0; i < aString.length; i++) {
            aInt[i] = Integer.parseInt(aString[i]);
        }
        return aInt;
    }

}