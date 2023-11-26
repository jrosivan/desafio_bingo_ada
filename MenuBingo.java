import java.util.Scanner;

public class MenuBingo {

    private static final String INVALID_OPTION_MSG = "Opção inválida. Tente novamente!";

    public static String[] menuCartelas = {"1 - CARTELAS AUTOMATICAS", "2 - CARTELAS MANUAIS", "X - SAIR"};
    public static String[] menuSorteios = {"3 - SORTEIOS AUTOMATICOS", "4 - SORTEIOS MANUAIS", "X - VOLTAR"};

    public static void menuInicial() {
        menu(menuCartelas, "Selecione como gerar as cartelas:");
    }


    public static void menu(String[] aOpcoes, String mensagemMenu ) {
        while (true) {
            montarTela(mensagemMenu);
            mostrarOpcoes(aOpcoes);
            String opcao = getOptionMenu(aOpcoes);
            if ("SAIR#VOLTAR".contains(opcao)) {break;}
            executar(opcao);
        }
        System.out.println("-".repeat(78));
        System.out.println("Agradecemos sua participação. Até a próxima!");
        System.out.println(" « OBS: Na saída, solicite seu bônus de 10% para a próxima visita »");
    }

    private static void montarTela(String mensagem) {
        System.out.println("\n\n");
        System.out.println("┌  ┬ ┌┐┌┌─┐┌─┐               ┌───────────────────────────────────────────────┐");
        System.out.println("├─┐│ ││││ ┬│ │  ┌─  ┌-┐      |         Sejam muito bem-vindos(as)!           |");
        System.out.println("└─┘┴ ┘└┘└─┘└─┘   ¯) └-┘ ─┼─  |                  BINGO 50+                    |");
        System.out.println("─────────────────────────────┴───────────────────────────────────────────────┘");
        System.out.printf("[ %s ]\n", mensagem);
    }

    public static void mostrarOpcoes(String[] opcoes) {
        for (String s : opcoes) {
            System.out.println(s);
        }
    }

    private static String getOptionMenu(String[] opcoes) {
        do {
            String opcao = getUserInput(">> Entre com uma opção");
            String selecao = selecionado(opcao, opcoes);
            if (!selecao.equals("")) {
                return selecao;
            } else {
                System.out.printf("[%s] ", INVALID_OPTION_MSG);
            }
        } while (true);
    }

    private static String selecionado(String opcao, String[] opcoes) {
        if (!opcao.trim().isEmpty()) {
            for (String s : opcoes) {
                if (s.startsWith(opcao)) {
                    s = s.replace(opcao.concat(" - "), "").trim();
                    return s;
                }
            }
        }
        return "";
    }

    public static String getUserInput(String question) {
        System.out.printf(question.concat(" # : "));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim().toUpperCase();
    }


    private static void executar(String opcao) {
        switch (opcao) {
            case "CARTELAS AUTOMATICAS" -> {
                ExecutarBingo.executarCartelaAutomatica();
            }
            case "CARTELAS MANUAIS" -> {
                menu(menuSorteios, "Selecione como efetuar o sorteio:");
            }
            case "SORTEIOS AUTOMATICOS" -> {
                ExecutarBingo.executarSorteio(true);
            }
            case "SORTEIOS MANUAIS" -> {
                 ExecutarBingo.executarSorteio(false);
            }
        }

    }


}
