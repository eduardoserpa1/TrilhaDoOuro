import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Ouro {
    /*
    *   José Eduardo Rodrigues Serpa - 20200311-7
    *   Turma 031 - 2022/2
    */
    static HashMap<String,Integer> otimizacao = new HashMap<>();
    static HashMap<String,String> caminho_recursivo = new HashMap<>();
    static String[][] caminho_nao_recursivo;
    static int menor_int = Integer.MIN_VALUE/100;

    public static void main(String[] args) throws IOException{
        if(args.length == 1){
            String[][] mapa = arq(args[0]);
            determina_menor_inteiro(mapa);

            long tempoFinal = 0;
            long tempoInicial = 0;

            tempoInicial = System.currentTimeMillis();
            System.out.println("Resultado do algoritmo recursivo com otimização exibindo a quantidade de ouro e melhor rota:\n");
            algoritmo_recursivo_otimizado(mapa);
            tempoFinal = System.currentTimeMillis();
            System.out.println("\nt: " + (tempoFinal-tempoInicial) + " ms\n");


            tempoInicial = System.currentTimeMillis();
            System.out.println("Resultado do algoritmo NÃO recursivo com otimização exibindo a quantidade de ouro e melhor rota:\n");
            algoritmo_nao_recursivo_otimizado(mapa);
            tempoFinal = System.currentTimeMillis();
            System.out.println("\nt: " + (tempoFinal-tempoInicial) + " ms\n");

            tempoInicial = System.currentTimeMillis();
            System.out.println("Resultado do algoritmo recursivo SEM otimização exibindo a quantidade de ouro apenas:\n");
            algoritmo_recursivo_nao_otimizado(mapa);
            tempoFinal = System.currentTimeMillis();
            System.out.println("\nt: " + (tempoFinal-tempoInicial) + " ms\n");

        }
    }

    /*
    *   Chamadas das funções desenvolvidas
    */

    static void algoritmo_recursivo_nao_otimizado(String[][] m){
        System.out.println(ouro_recursivo_nao_otimizado(m,m.length-1,0));
    }

    static void algoritmo_recursivo_otimizado(String[][] m){
        System.out.println(ouro_recursivo_otimizado(m,m.length-1,0));
        System.out.println(monta_caminho_algoritmo_recursivo(caminho_recursivo,m.length-1));
    }

    static void algoritmo_nao_recursivo_otimizado(String[][] m){
        System.out.println(ouro_nao_recursivo(m));
        System.out.println(monta_caminho_nao_recursivo(caminho_nao_recursivo));
    }


    /*
    *   Algoritmos desenvolvidos
    */

    static int ouro_recursivo_nao_otimizado(String[][] matriz, int linha, int coluna){
        int n = matriz.length - 1;

        int atual = 0;

        atual = toInt(matriz[linha][coluna]);

        if(linha == 0 && coluna == n)
            return atual;
        if(coluna == n)
            return atual + ouro_recursivo_nao_otimizado(matriz,linha-1,coluna);
        if(linha == 0)
            return atual + ouro_recursivo_nao_otimizado(matriz,linha,coluna+1);

        String norte = String.valueOf(ouro_recursivo_nao_otimizado(matriz,linha-1,coluna));
        String leste = String.valueOf(ouro_recursivo_nao_otimizado(matriz,linha,coluna+1));
        String nordeste = String.valueOf(ouro_recursivo_nao_otimizado(matriz,linha-1, coluna+1));

        int melhor_escolha = strMax ( String.valueOf(strMax( norte, leste)) , nordeste);

        return atual + melhor_escolha;
    }

    static int ouro_recursivo_otimizado(String[][] matriz, int linha, int coluna){
        String label = linha+","+coluna;


        int n = matriz.length - 1;

        int atual = 0;

        atual = toInt(matriz[linha][coluna]);

        if(linha == 0 && coluna == n){
            return atual;
        }

        if (otimizacao.containsKey(label))
            return otimizacao.get(label);


        if(coluna == n){
            caminho_recursivo.put(label,"N");
            return atual + ouro_recursivo_otimizado(matriz,linha-1,coluna);
        }

        if(linha == 0){
            caminho_recursivo.put(label,"E");
            return atual + ouro_recursivo_otimizado(matriz,linha,coluna+1);
        }

        String norte = String.valueOf(ouro_recursivo_otimizado(matriz,linha-1,coluna));
        String leste = String.valueOf(ouro_recursivo_otimizado(matriz,linha,coluna+1));
        String nordeste = String.valueOf(ouro_recursivo_otimizado(matriz,linha-1, coluna+1));

        int index = retorna_index_maior ( norte, nordeste, leste);
        int melhor_escolha = 0;

        if (index == 1){
            melhor_escolha = toInt(norte);
            caminho_recursivo.put(label,"N");
        }else
        if (index == 2){
            melhor_escolha = toInt(nordeste);
            caminho_recursivo.put(label,"NE");
        }else
        if (index == 3){
            melhor_escolha = toInt(leste);
            caminho_recursivo.put(label,"E");
        }

        otimizacao.put(label,atual + melhor_escolha);


        return atual + melhor_escolha;
    }

    static String ouro_nao_recursivo(String[][] matriz) {

        int mlen = matriz.length;

        String [][]clone = new String[mlen + 1][mlen + 1];

        for (int i = 0; i < mlen; i++)
            for (int j = 0; j <= mlen; j++)
                if (j==0)
                    clone[i][j] = Integer.toString(menor_int);
                else
                    clone[i][j] = "0";

        for (int i = 0; i <= mlen; i++)
            clone[mlen][i] = Integer.toString(menor_int);

        for (int i = mlen-1; i >= 0; i--)
            for (int j = 1; j <= mlen; j++)
                if(i==mlen-1 && j == 1)
                    clone[i][j] = matriz[i][j-1];
                else
                    clone[i][j] = Integer.toString( strMax(Integer.toString(strMax(clone[i + 1][j], clone[i][j - 1])), clone[i + 1][j - 1]) + toInt(matriz[i][j - 1]));

        caminho_nao_recursivo = clone;

        return clone[0][mlen];
    }


    /*
    * Funções utilitarias :)
    */

    static void determina_menor_inteiro(String[][] m){
        int menor = Integer.MAX_VALUE;

        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m.length; j++)
                if (!m[i][j].equals("x"))
                    if (toInt(m[i][j]) < menor)
                        menor = toInt(m[i][j]);

        menor_int = menor-1;
    }

    static String monta_caminho_nao_recursivo(String[][] m){
        String r = "";

        int linha_origem = m.length-2;
        int coluna_origem = 1;

        int linha = 0;
        int coluna = m.length-1;

        while ( !(linha == linha_origem && coluna == coluna_origem) ){

            String oeste = "x";
            String sul = "x";
            String sudoeste = "x";


            if(coluna > coluna_origem && linha < linha_origem){
                oeste = m[linha][coluna-1];
                sul = m[linha+1][coluna];
                sudoeste = m[linha+1][coluna-1];
            }else if(coluna > coluna_origem){
                oeste = m[linha][coluna-1];
            }else if(linha < linha_origem){
                sul = m[linha+1][coluna];
            }

            int index = retorna_index_maior(sul,sudoeste,oeste);

            switch (index){
                case 1:
                    r = r.concat(" ").concat("N");
                    linha++;
                    break;
                case 2:
                    r = r.concat(" ").concat("NE");
                    linha++;
                    coluna--;
                    break;
                case 3:
                    r = r.concat(" ").concat("E");
                    coluna--;
                    break;

                default:
                    break;
            }

        }

        String r_reverse = "";

        for (int i = r.length()-1; i >= 0; i--) {
            r_reverse += r.charAt(i);
        }

        return r_reverse;
    }

    static String monta_caminho_algoritmo_recursivo(HashMap<String,String> coord, int n){
        String r = "";

        int linha = n;
        int coluna = 0;

        while ( !(linha == 0 && coluna == n)){
            String label = linha+","+coluna;

            String coord_atual = coord.get(label);

            r = r.concat(coord_atual).concat(" ");

            switch (coord_atual){
                case "N":
                    linha--;
                    break;
                case "NE":
                    linha--;
                    coluna++;
                    break;
                case "E":
                    coluna++;
                    break;

                default:
                    break;
            }
        }
        return r;
    }

    static int toInt(String x){
        if (x.length()>1)
            if ( !(x.charAt(0) > 47 && x.charAt(0) < 58) )
                return Integer.parseInt(x.substring(1)) * -1;

        if (x.equals("x"))
            return menor_int;
        else
            return Integer.parseInt(x);
    }

    static int retorna_index_maior(String a, String b, String c){
        if(a.equals("x") && b.equals("x") && c.equals("x"))
            return 2;
        if (a.equals("x") && b.equals("x"))
            return 3;
        if (a.equals("x") && c.equals("x"))
            return 2;
        if (b.equals("x") && c.equals("x"))
            return 1;

        int aa = toInt(a);
        int bb = toInt(b);
        int cc = toInt(c);

        if (aa >= bb && aa >= cc)
            return 1;
        else if (bb >= aa && bb >= cc)
            return 2;
        else if (cc >= aa && cc >= bb)
            return 3;

        return 0;
    }

    static int strMax(String a, String b){
        if(a.equals("x") && b.equals("x"))
            return menor_int;
        if (a.equals("x"))
            return toInt(b);
        if (b.equals("x"))
            return toInt(a);

        int aa = toInt(a);
        int bb = toInt(b);

        if (aa > bb)
            return aa;
        else
            return bb;
    }

    static String[][] arq(String file_name) throws IOException {

        BufferedReader buffer = new BufferedReader(new FileReader(file_name));
        String linha = "";
        int len = Integer.parseInt(buffer.readLine());
        String[][] matriz = new String[len][len];

        try {
            for (int i = 0; i < len; i++) {
                linha = buffer.readLine();
                linha = linha.replace("   "," ");
                linha = linha.replace("  "," ");
                linha = (linha.charAt(0) == ' ') ? linha.substring(1) : linha;
                String[] s = linha.split(" ");
                for (int j = 0; j < len; j++)
                    matriz[i][j] = s[j];
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matriz;
    }

}
