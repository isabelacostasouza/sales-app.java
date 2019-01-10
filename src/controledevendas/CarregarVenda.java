package controledevendas;

import controledeclientes.Cliente;
import controledeestoque.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarregarVenda {
    
    private ArrayList<Produto> estoque = new ArrayList();
    private int quantidadeLinhas;
    
    public ArrayList<Produto> getEstoque() {
        setEstoque();
        for(int i = 0; i < this.estoque.size(); i++)
            this.estoque.get(i).setValorLiquido((this.estoque.get(i).getQuantidadeDesejada()*this.estoque.get(i).getValor()));
        if(this.estoque.isEmpty())
            return null;
        else
            return this.estoque;
    }
    
    public int quantidadeProduto() {
        setEstoque();
        return this.estoque.size();
    }
    
    public void removerProduto(Produto produto) {
        limparArquivo();
        for(int i = 0; i < this.estoque.size(); i++) {
            if(this.estoque.get(i).getNome().equals(produto.getNome()) && this.estoque.get(i).getQuantidade() == produto.getQuantidade())
                this.estoque.remove(i);
            else
                adicionarProdutoArquivo(this.estoque.get(i));
        }
    }
    
    public ArrayList<Produto> procurarProduto(String procurado) {
        ArrayList<Produto> produtoProcurado = new ArrayList();
        setEstoque();
        
        for(int i = 0; i < this.estoque.size(); i++) {
            if(this.estoque.get(i).getNome().toLowerCase().indexOf(procurado.toLowerCase()) >= 0)
                produtoProcurado.add(this.estoque.get(i));
        }
        
        return produtoProcurado;
    }
    
    public boolean procurarProdutoBoolean(String procurado) {
        setEstoque();
        for(int i = 0; i < this.estoque.size(); i++) {
            if(this.estoque.get(i).getNome().toLowerCase().indexOf(procurado.toLowerCase()) >= 0)
                return true;
        }
        return false;
    }
    
    public int tamanho() {
        setEstoque();
        return quantidadeLinhas/6 - 1;
    }
    
    public void setEstoque() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\vendasAux.txt";
        
        FileReader f;
        try {
            f = new FileReader(caminhoArquivo);
            BufferedReader readerf = new BufferedReader(f);
            String linha;
            try {
                linha = readerf.readLine();
                ArrayList<String> linhas = new ArrayList();
                while (linha != null) {
                linhas.add(linha);
                linha = readerf.readLine();
            }    
            int contador = 0;

            for(int i = 0; i < linhas.size(); i++) {
                Produto e = new Produto();
                if(contador > 5) 
                    contador = 0;
                else if(contador == 1) {
                    e.setNome(linhas.get(i-1));
                    e.setValor(Double.parseDouble(linhas.get(i)));
                    e.setQuantidade(Integer.parseInt(linhas.get(i+1)));
                    if(linhas.get(i+2).equals("null")){}
                    else
                        e.setQuantidadeDesejada(Integer.parseInt(linhas.get(i+2)));
                    if(linhas.get(i+3).equals("null")){}
                    else
                    e.setValorLiquido(Double.parseDouble(linhas.get(i+3)));
                    this.estoque.add(e);
                }
                quantidadeLinhas++;
                contador++;
            }
            readerf.close();
            } catch (IOException ex) {
                Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void adicionarProdutoArquivo(Produto produto) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\vendasAux.txt";

        escreverArquivo(produto.getNome(), caminhoArquivo);
        escreverArquivo(String.valueOf(produto.getValor()), caminhoArquivo);
        escreverArquivo(String.valueOf(produto.getQuantidade()), caminhoArquivo);
        escreverArquivo(String.valueOf(produto.getQuantidadeDesejada()), caminhoArquivo);
        escreverArquivo(String.valueOf(produto.getValorLiquido()), caminhoArquivo);
        escreverArquivo("", caminhoArquivo);
    }
    
    public void escreverArquivo(String linha, String caminhoArquivo) {
        FileWriter fw;
        try {
            fw = new FileWriter(caminhoArquivo, true);
            BufferedWriter conexao = new BufferedWriter(fw);
            conexao.write(linha);
            conexao.newLine();
            conexao.close();
        } catch (IOException ex) {
            Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Cliente clienteSelecionado;
    
    public void setArquivoCliente() {  
        FileReader f;
        try {
            String nomeCliente = "novoCliente";
            Path currentRelativePath2 = Paths.get("");
            String s2 = currentRelativePath2.toAbsolutePath().toString();
            String caminhoArquivo2 = s2+"\\comprasEfetuadas\\clienteSelecionado.txt";
            f = new FileReader(caminhoArquivo2);
            BufferedReader readerf = new BufferedReader(f);
            String linha;
            try {
                linha = readerf.readLine();
                ArrayList<String> linhas = new ArrayList();
                while (linha != null) {
                    linhas.add(linha);
                    linha = readerf.readLine();
                }    

                for(int i = 0; i < linhas.size(); i++) {
                    Cliente e = new Cliente();
                    if(i == 0) {
                        e.setNome(linhas.get(i));
                        e.setTelefone(linhas.get(i+1));
                        this.clienteSelecionado = e;
                        nomeCliente = e.getNome();
                    }
                }
                readerf.close();
            } catch (IOException ex) {
                Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String caminhoArquivo = s+"\\comprasEfetuadas\\"+nomeCliente+".txt";

            File file = new File(caminhoArquivo);

            if(file.exists()){}

            else {
                try {
                    new File(caminhoArquivo).createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void adicionarCompraCliente(ArrayList<Produto> produtosComprados) {
        setArquivoCliente();
        
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\comprasEfetuadas\\"+this.clienteSelecionado.getNome()+".txt";
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        escreverArquivo(dateFormat.format(date), caminhoArquivo);
        for(int i = 0; i < produtosComprados.size()/2; i++) {
            escreverArquivo(produtosComprados.get(i).getNome() ,caminhoArquivo);
            escreverArquivo(String.valueOf(produtosComprados.get(i).getQuantidadeDesejada()) ,caminhoArquivo);
            escreverArquivo(String.valueOf(produtosComprados.get(i).getValor()) ,caminhoArquivo);
            escreverArquivo(String.valueOf(produtosComprados.get(i).getValorLiquido()) ,caminhoArquivo);
        }
        escreverArquivo("" ,caminhoArquivo);
    }
    
    public void limparArquivo(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\vendasAux.txt";
        
        File file = new File(caminhoArquivo);
        file.delete();
        File f = new File(caminhoArquivo);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
