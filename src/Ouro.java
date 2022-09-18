import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Ouro {

    public static void main(String[] args) throws IOException{


        String[][] mapa = arq("teste20.txt");
        strdump(mapa);

        HashMap<String,Integer> mem = new HashMap<>();

        //System.out.println("Ouro encontrado: "+procura_ouro_mem(mapa,mem));
        System.out.println("Ouro encontrado: "+procura_ouro(mapa));

    }

    public static int procura_ouro_mem(String[][] m, HashMap<String,Integer> mem){
        return ouro_mem(m,m.length-1,0);
    }
    public static int procura_ouro(String[][] m){
        return ouro(m,m.length-1,0);
    }

    public static int ouro_mem(String[][] matriz, int linha, int coluna){
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
            return Integer.MIN_VALUE;
        else
            return Integer.parseInt(x);
    }

    public static int strMax(String a, String b){
        if(a.equals("x") && b.equals("x"))
            return Integer.MIN_VALUE;
        if (a.equals("x"))
            return Integer.parseInt(b);
        if (b.equals("x"))
            return Integer.parseInt(a);

        int aa = Integer.parseInt(a);
        int bb = Integer.parseInt(b);

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

    public static void chardump(char[][] m){
        int len = m.length;

        for (int i = 0; i < len; i++) {
            System.out.print("|");
            for (int j = 0; j < len; j++) {
                System.out.print(m[i][j] + "|");
            }
            System.out.println();
        }
    }

    public static void intdump(int[][] m){
        int len = m.length;

        for (int i = 0; i < len; i++) {
            System.out.print("|");
            for (int j = 0; j < len; j++) {
                System.out.print(m[i][j] + "|");
            }
            System.out.println();
        }
    }

    public static void strdump(String[][] m){
        int len = m.length;

        for (int i = 0; i < len; i++) {
            System.out.print("|");
            for (int j = 0; j < len; j++) {
                System.out.print(m[i][j] + "|");
            }
            System.out.println();
        }
    }
}
