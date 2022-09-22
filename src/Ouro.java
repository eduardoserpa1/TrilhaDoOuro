import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

public class Ouro {

    public static HashMap<String,Integer> memoria = new HashMap<>();
    public static HashMap<String,String> memoria_path = new HashMap<>();
    public static String[][] memoria_path_nao_recursivo;
    static int menor_int = Integer.MIN_VALUE/20;

    public static void main(String[] args) throws IOException{


        String[][] mapa = arq("teste_enunciado.txt");

        long tempoFinal = System.currentTimeMillis();
        long tempoInicial = System.currentTimeMillis();


        int r1 = procura_ouro_mem(mapa);
        System.out.println(r1);

        System.out.println();
        int r2 = procura_ouro_nao_recursivo(mapa);
        System.out.println(r2);



    }
    public static String monta_caminho_nao_recursivo(String[][] m){
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

            int index = strMaxS(sul,sudoeste,oeste);

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
    public static int procura_ouro_nao_recursivo(String[][] m){
        int response = toInt(ouro_nao_recursivo(m));
        System.out.println("melhor caminho: "+monta_caminho_nao_recursivo(memoria_path_nao_recursivo));
        return response;
    }

    public static int procura_ouro_mem(String[][] m){
        int response = ouro_mem(m,m.length-1,0);
        System.out.println("melhor caminho: "+monta_caminho(memoria_path,m.length-1));
        return response;
    }
    public static int procura_ouro(String[][] m){
        return ouro(m,m.length-1,0);
    }


    public static String ouro_nao_recursivo(String[][] grid_str) {

        int N = grid_str.length;

        String [][]sum = new String[N + 1][N + 1];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= N; j++) {
                if (j==0)
                    sum[i][j] = Integer.toString(menor_int);
                else
                    sum[i][j] = "0";
            }
        }
        for (int i = 0; i <= N; i++) {
            sum[N][i] = Integer.toString(menor_int);
        }

        for (int i = N-1; i >= 0; i--) {
            for (int j = 1; j <= N; j++) {
                if(i==N-1 && j == 1)
                    sum[i][j] = grid_str[i][j-1];
                else
                    sum[i][j] = Integer.toString( strMax(Integer.toString(strMax(sum[i + 1][j], sum[i][j - 1])), sum[i + 1][j - 1]) + toInt(grid_str[i][j - 1]));
            }
        }

        memoria_path_nao_recursivo = sum;

        return sum[0][N];
    }


    public static int ouro_mem(String[][] matriz, int linha, int coluna){
        String label = linha+","+coluna;


        int n = matriz.length - 1;

        int atual = 0;

        atual = toInt(matriz[linha][coluna]);

        if(linha == 0 && coluna == n){
            return atual;
        }

        if (memoria.containsKey(label)){
            return memoria.get(label);
        }


        if(coluna == n){
            memoria_path.put(label,"N");
            return atual + ouro_mem(matriz,linha-1,coluna);
        }

        if(linha == 0){
            memoria_path.put(label,"E");
            return atual + ouro_mem(matriz,linha,coluna+1);
        }

        String norte = String.valueOf(ouro_mem(matriz,linha-1,coluna));
        String leste = String.valueOf(ouro_mem(matriz,linha,coluna+1));
        String nordeste = String.valueOf(ouro_mem(matriz,linha-1, coluna+1));

        int index = strMaxS ( norte, nordeste, leste);
        int melhor_escolha = 0;

        if (index == 1){
            melhor_escolha = toInt(norte);
            memoria_path.put(label,"N");
        }else
        if (index == 2){
            melhor_escolha = toInt(nordeste);
            memoria_path.put(label,"NE");
        }else
        if (index == 3){
            melhor_escolha = toInt(leste);
            memoria_path.put(label,"E");
        }

        memoria.put(label,atual + melhor_escolha);


        return atual + melhor_escolha;
    }

    public static String monta_caminho(HashMap<String,String> coord, int n){
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

    public static int ouro(String[][] matriz, int linha, int coluna){
        int n = matriz.length - 1;

        int atual = 0;

        atual = toInt(matriz[linha][coluna]);

        if(linha == 0 && coluna == n)
            return atual;
        if(coluna == n)
            return atual + ouro(matriz,linha-1,coluna);
        if(linha == 0)
            return atual + ouro(matriz,linha,coluna+1);

        String norte = String.valueOf(ouro(matriz,linha-1,coluna));
        String leste = String.valueOf(ouro(matriz,linha,coluna+1));
        String nordeste = String.valueOf(ouro(matriz,linha-1, coluna+1));

        int melhor_escolha = strMax ( String.valueOf(strMax( norte, leste)) , nordeste);

        //  verificar qual a melhor escolha entre (norte, nordeste e leste), e determinar o passo que
        //  foi dado antes da recursão

        return atual + melhor_escolha;
    }

    public static int toInt(String x){
        if (x.length()>1)
            if ( !(x.charAt(0) > 47 && x.charAt(0) < 58) )
                return Integer.parseInt(x.substring(1)) * -1;

        if (x.equals("x"))
            return menor_int;
        else
            return Integer.parseInt(x);
    }

    public static int strMaxS(String a, String b, String c){
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
    public static int strMax(String a, String b){
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

    public static String[][] arq(String file_name) throws IOException {

        BufferedReader buffer = new BufferedReader(new FileReader("./src/"+file_name));
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
