import java.util.ArrayList;

public class Cell{
    int linha;
    int coluna;
    String caminho = "";
    int amount = 0;
    boolean is_final = false;
    Cell(int linha, int coluna, int amount, String caminho){
        this.linha = linha;
        this.coluna = coluna;
        this.amount += amount;
        this.caminho = caminho;
    }

}