public class DadosBingo {

    static String [] dadosBingo;

    static final String SEP_DADOS = "|";
    static final String SEP_ITENS = ";";

    static final int TOTAL_NUMEROS_BINGO = 60;
    static final int QTD_NUMEROS_POR_CARTELA = 5;

    static private String numerosSorteados = "";
    static int[] numerosEmbaralhadoBingo = geraListaNumerica();

    /**
     * Cria a estrutura de dados do BINGO:<br>
     * ------------------------------------------------------------<br>
     * "JOGADOR" | "n1";n2";n3"n4"n5" |"s1"; "s2"...<br>
     * ------------------------------------------------------------<br>
     *  <b>dado1</b>: nome  | <b>dado2</b>: cartela  | <b>dado3</b>: acertos <br>
     *
     * @author  Jorge Rosivan
     * @version 0.1
     * @Note:  A estrutura de dados possui todos os campos necessários para
     * armazenamento das informações dos jogadores.
     * @// TODO: 20/11/2023: garantir quantidade dos números informados.
     **/
    public static void iniciarDados(String[] jogadores, String[] cartelas) {
        numerosSorteados = "";
        dadosBingo = new String[jogadores.length];
        for (int i = 0; i < jogadores.length ; i++) {
            dadosBingo[i] = String.format("%s%s%s%s%s", jogadores[i], SEP_DADOS, cartelas[i], SEP_DADOS, "" );
        }
    }
    public static void iniciarDados(String[] jogadores) {
        String[] cartelas = new String[jogadores.length];
        for (int i = 0; i < jogadores.length ; i++) {
            cartelas[i] = gerarCartela(numerosEmbaralhadoBingo);
        }
        iniciarDados(jogadores, cartelas);
    }

    private static String gerarCartela(int[] numerosBingo) {
        String result = "";
        for (int contador = 0; contador < QTD_NUMEROS_POR_CARTELA; ) {
            int numero = numerosBingo[ (int) (Math.random() *  TOTAL_NUMEROS_BINGO)];
            String s = String.valueOf(numero).concat(SEP_ITENS);
            if (!result.contains(s)) {
                result += s;
                contador++;
            }
        }
        return result;
    }

    private static boolean numeroJaSorteado(String numero) {
        return (numerosSorteados.contains("#".concat(numero).concat(SEP_ITENS)));
    }

    public static boolean addNumeroSorteado(String numero) {
        if (!numeroJaSorteado(numero)) {
            numerosSorteados += "#".concat(numero).concat(SEP_ITENS);
            return true;
        }
        return false;
    }

    public static String getJogador(String blocoDados) {
        return blocoDados.substring(0, blocoDados.indexOf(SEP_DADOS));
    }
    public static String getCartela(String blocoDados) {
        return blocoDados.substring(blocoDados.indexOf(SEP_DADOS)+1, blocoDados.lastIndexOf(SEP_DADOS));
    }
    public static String getAcertos(String blocoDados) {
        return blocoDados.substring(blocoDados.lastIndexOf(SEP_DADOS)+1);
    }
    public static int countAcertos(String blocoDados) {
        return getAcertos(blocoDados).split(SEP_ITENS).length;
    }

    public static void ordenarPorQuantidadeAcertos() {
        int n = dadosBingo.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                  if (countAcertos(dadosBingo[j]) < countAcertos(dadosBingo[j + 1])) {
                    String temp = dadosBingo[j];
                    dadosBingo[j] = dadosBingo[j + 1];
                    dadosBingo[j + 1] = temp;
                }
            }
        }
    }

    private static int[] geraListaNumerica() {
        int[] lista = new int[DadosBingo.TOTAL_NUMEROS_BINGO];
        for (int i = 0; i < DadosBingo.TOTAL_NUMEROS_BINGO; i++) {
            lista[i] = i + 1;
        }
        embaralha(lista);
        embaralha(lista);
        return lista;
    }
    private static int[] embaralha(int[] embaralhado) {  //ROCHA!
        for (int i = embaralhado.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = embaralhado[i];
            embaralhado[i] = embaralhado[j];
            embaralhado[j] = temp;
        }
        return embaralhado;
    }

}
