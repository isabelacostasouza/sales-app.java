package controledevendas;

import controledeestoque.Produto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class FXMLDocumentController3 implements Initializable {
    
    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Label quantidadeDisponivel;

    @FXML
    void cancelar(ActionEvent event) {
        limparArquivo();
        Parent root;
        try {
             root = FXMLLoader.load(getClass().getResource("FXMLDocument2.fxml"));
             Scene scene = new Scene(root);
             Stage stage = new Stage();
             stage.setResizable(false);
             Image icone = new Image(getClass().getResourceAsStream("/imagens/icon-clientes.png"));
             stage.getIcons().add(icone);
             stage.setTitle("Meu carrinho");
             stage.setScene(scene);
             stage.setOnCloseRequest( e -> {
                Parent root2;
                try {
                    root2 = FXMLLoader.load(getClass().getResource("/controlegeral/FXMLDocument.fxml"));
                    Scene scene2 = new Scene(root2);
                    Stage stage2 = new Stage();
                    stage2.setResizable(false);
                    stage2.getIcons().add(icone);
                    stage2.setTitle("Oficinas de vendas");
                    stage2.setScene(scene2);
                    stage2.show();
                    stage.setScene(scene2);
                    stage2.show();
                } catch (IOException ex) {
                     Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            stage.show();
        } catch (IOException ex) {
             Logger.getLogger(controledeestoque.FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage2 = (Stage) spinner.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void salvar(ActionEvent event) {
        if(Integer.parseInt(spinner.getValue().toString()) < 1) {
            JOptionPane.showMessageDialog(null, "Quantidade invalida!");
        }
        else {
            CarregarVenda arquivoVenda = new CarregarVenda();
            this.produto.setQuantidadeDesejada(Integer.parseInt(spinner.getValue().toString()));
            arquivoVenda.adicionarProdutoArquivo(produto);
            limparArquivo();
            Parent root2;
            try {
                 root2 = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                 Scene scene2 = new Scene(root2);
                 Stage stage2 = new Stage();
                 stage2.setResizable(false);
                 Image icone2 = new Image(getClass().getResourceAsStream("/imagens/icon-clientes.png"));
                 stage2.getIcons().add(icone2);
                 stage2.setTitle("Oficina de vendas");
                 stage2.setScene(scene2);
                 stage2.show();
            } catch (IOException ex) {
                 Logger.getLogger(controledeestoque.FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stage stage2 = (Stage) spinner.getScene().getWindow();
            stage2.close();
        }
    }
    
    private static Produto produto = new Produto();
    
    public void limparArquivo(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\produtoSelecionado.txt";
        
        File file = new File(caminhoArquivo);
        file.delete();
        File f = new File(caminhoArquivo);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CarregarVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setProduto() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String caminhoArquivo = s+"\\produtoSelecionado.txt";
        
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
                if(contador > 5) 
                    contador = 0;
                else if(contador == 1) {
                    this.produto.setNome(linhas.get(i-1));
                    this.produto.setValor(Double.parseDouble(linhas.get(i)));
                    this.produto.setQuantidade(Integer.parseInt(linhas.get(i+1)));
                    if(linhas.get(i+2).equals("null")){}
                    else
                        this.produto.setQuantidadeDesejada(Integer.parseInt(linhas.get(i+2)));
                    if(linhas.get(i+3).equals("null")){}
                    else
                    this.produto.setValorLiquido(Double.parseDouble(linhas.get(i+3)));
                }
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProduto();
        final int valorMaximo = this.produto.getQuantidade();
        SpinnerValueFactory<Integer> gradesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, valorMaximo, 1);
        this.spinner.setValueFactory(gradesValueFactory);
        spinner.setEditable(false);
        quantidadeDisponivel.setText(String.valueOf(valorMaximo));
    }
}
