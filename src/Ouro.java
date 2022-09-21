import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

public class Ouro {


    public static HashMap<String,String> caminhos_iterate = new HashMap<>();
    public static HashMap<String,String> caminhos = new HashMap<>();
    public static HashMap<String,Integer> memoria = new HashMap<>();
    public static void main(String[] args) throws IOException{


        String[][] mapa = arq("test.txt");

        int[][] teste = {   {1,2,3},
                            {-1,5,6},
                            {7,-200,9},
        };

        int mat[][] = { { 10, 10, 2, 0, 20, 4 },
                { 1, 0, 0, 30, 2, 5 },
                { 0, 10, 4, 0, 2, 0 },
                { 1, 0, 2, 20, 0, 4 }
        };

        HashMap<String,Integer> mem = new HashMap<>();
        /*
        System.out.println("\nAlgoritmo sem memorização retornando apenas quantidade de ouro:");
        long tempoInicial = System.currentTimeMillis();
        System.out.println("Ouro encontrado: "+procura_ouro(mapa));
        long tempoFinal = System.currentTimeMillis();
        System.out.printf("%.3f ms%n", (tempoFinal - tempoInicial) / 1000d);


        System.out.println("\nAlgoritmo com memorização retornando apenas quantidade de ouro:");
        tempoInicial = System.currentTimeMillis();
        System.out.println("Ouro encontrado: "+procura_ouro_mem(mapa,mem));
        tempoFinal = System.currentTimeMillis();
        System.out.printf("%.3f ms%n", (tempoFinal - tempoInicial) / 1000d);


        System.out.println(monta_caminho(caminhos,mapa.length-1));
        System.out.println("EENNENENNENENNEEEN");

        */

        ouro_nao_recursivo(mapa);
    }



    public static int ouro_nao_recursivo(String[][] matriz){
        int r = 0;
        int len = matriz.length-1;
        ArrayList<Cell> fila = new ArrayList<>();

        Cell origem = new Cell(len,0, 0,"");

        fila.add(origem);

        HashMap<String,Cell> caminhos_it = new HashMap<>();

        int tamanho = 1;

        for (int i = 0; i < tamanho; i++) {
            Cell c = fila.get(i);

            if (caminhos_it.containsKey(c.caminho)){

            }

            if(matriz[c.linha][c.coluna].equals("x")){
                continue;
            }else{
                c.amount += toInt(matriz[c.linha][c.coluna]);
            }

            if(c.coluna == len && c.linha == 0){
                c.is_final = true;
            } else if (c.coluna == len) {
                Cell cell = new Cell(c.linha-1, c.coluna,c.amount, c.caminho.concat("N"));
                fila.add(cell);
                caminhos_it.put(cell.caminho,cell);
            } else if (c.linha == 0) {
                Cell cell = new Cell(c.linha, c.coluna+1, c.amount, c.caminho.concat("E"));
                fila.add(cell);
                caminhos_it.put(cell.caminho,cell);
            } else{
                Cell cell_norte = new Cell(c.linha-1, c.coluna, c.amount, c.caminho.concat("N"));
                Cell cell_leste = new Cell(c.linha, c.coluna+1, c.amount, c.caminho.concat("E"));
                Cell cell_nordeste = new Cell(c.linha-1, c.coluna+1, c.amount, c.caminho.concat("NE"));

                fila.add(cell_nordeste);
                fila.add(cell_norte);
                fila.add(cell_leste);
                caminhos_it.put(cell_norte.caminho,cell_norte);
                caminhos_it.put(cell_leste.caminho,cell_leste);
                caminhos_it.put(cell_nordeste.caminho,cell_nordeste);
            }

            tamanho = fila.size();
        }


        Cell response = new Cell(0,0, 0,"");
        response.amount = Integer.MIN_VALUE;

        for (Cell c:fila)
            System.out.println(c.caminho);

        for (Cell c:fila){
            if (c.is_final)
                if (c.amount > response.amount)
                    response = c;
        }


        System.out.println("r:"+r);

        System.out.println("\n\nMELHOR CAMINHO E SUA QUANTIDADE EM OURO:");
        System.out.println(response.caminho);
        System.out.println(response.amount);

        return r;
    }

    public static int procura_ouro_mem(String[][] m, HashMap<String,Integer> mem){
        return ouro_mem(m,m.length-1,0);
    }
    public static int procura_ouro(String[][] m){
        return ouro(m,m.length-1,0);
    }

    public static int ouro_mem(String[][] matriz, int linha, int coluna){
        if (memoria.containsKey(linha+""+coluna))
            return memoria.get(linha+""+coluna);

        int n = matriz.length - 1;

        int atual = 0;

        atual = toInt(matriz[linha][coluna]);

        if(linha == 0 && coluna == n){
            return atual;
        }

        if(coluna == n){
            caminhos.put(linha+""+coluna,"N");
            return atual + ouro_mem(matriz,linha-1,coluna);
        }

        if(linha == 0){
            caminhos.put(linha+""+coluna,"E");
            return atual + ouro_mem(matriz,linha,coluna+1);
        }


        String norte = String.valueOf(ouro_mem(matriz,linha-1,coluna));
        String leste = String.valueOf(ouro_mem(matriz,linha,coluna+1));
        String nordeste = String.valueOf(ouro_mem(matriz,linha-1, coluna+1));

        int melhor_escolha = strMax ( String.valueOf(strMax( norte, leste)) , nordeste);

        if (String.valueOf(melhor_escolha).equals(norte)){
            caminhos.put(linha+""+coluna,"N");
        }
        if (String.valueOf(melhor_escolha).equals(nordeste)){
            caminhos.put(linha+""+coluna,"NE");
        }
        if (String.valueOf(melhor_escolha).equals(leste)){
            caminhos.put(linha+""+coluna,"E");
        }

        memoria.put(linha+""+coluna,atual + melhor_escolha);

        return atual + melhor_escolha;
    }

    public static String monta_caminho(HashMap<String,String> h, int n){
        String r = "";

        String index = n+"0";

        while(h.containsKey(index)){
            String direction = h.get(index);

            int x = 0;
            int y = 0;

            switch (direction){
                case "N":
                    x = Integer.parseInt(index.substring(0,1));
                    y = Integer.parseInt(index.substring(1,2));
                    x--;
                    index = Integer.toString(x) + Integer.toString(y);
                    r = r.concat("N");
                break;
                case "E":
                    x = Integer.parseInt(index.substring(0,1));
                    y = Integer.parseInt(index.substring(1,2));
                    y++;
                    index = Integer.toString(x) + Integer.toString(y);
                    r = r.concat("E");
                break;
                case "NE":
                    x = Integer.parseInt(index.substring(0,1));
                    y = Integer.parseInt(index.substring(1,2));
                    x--;
                    y++;
                    index = Integer.toString(x) + Integer.toString(y);
                    r = r.concat("NE");
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

}
