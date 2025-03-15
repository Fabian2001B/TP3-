import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Leitor {
    private String filePath;
    private int bloco = 0;
    private int temp = 0;

    public Leitor(String filepath) {
        this.filePath = filepath;
    }

    public void lerArquivo() throws IOException {
        long start = System.currentTimeMillis();
        int cont = 0;
        List<Double> registros = new LinkedList<>();
        
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // ler arquivo e separar
        while ((line=br.readLine()) != null) {
            registros.add(Double.parseDouble(line));
            cont++;
            
            // apos ler 100 linhas, cria um arquivo
            if  (cont == 100) {
                Double[] aux = new Double[registros.size()];
                for (int i = 0; i < aux.length; i++)
                    aux[i] = registros.get(i);
    
                heapSort(aux);
                escrever(aux);
                cont = 0; // reseta o contado de linhas
                registros.clear(); // limpa a lista que salva os arquivos
            }
        }

        br.close();
        int index = 0;

        // apos criar todos os arquivos, intercala eles ate restar apenas 1
        while (bloco > 0 && index < bloco) {
            System.out.println("tchau");
            intercalar(index);
            index += 10;
        }

        // apaga todos os arquivos restantes
        deletarArquivos();

        long end = System.currentTimeMillis();
        long mili = end - start;
        long segs = mili / 1000;

        String tempo = String.format("%02d:%02d", segs/60, segs%60);
        System.out.println("Tempo de execução: " + tempo);
    }

    private void intercalar (int numFile) throws IOException {
        List<Double> nums = new ArrayList<>();
        Double[] comparator = new Double[10];
        List<BufferedReader> bfs = new ArrayList<>();
        int leftFiles = bloco - numFile; // verificar se ainda há arquivos pra serem lidos

        if(leftFiles > 0) { 
            for (int i = 0; i < 10; i++) {
                try {
                    BufferedReader b = new BufferedReader(new FileReader(""+numFile));
                    bfs.add(b);
                    numFile++;
                } catch (Exception e) { // caso o numero de arquivos seja menor que 10, conclui a criação do array de bufferedReaders
                    break;
                }
            }
        } else {
            return; //caso não tenha mais arquivos
        }

        // preencher o comparador de 10 numeros
        for (int i = 0; i < bfs.size(); i++) {
            String line = bfs.get(i).readLine();
            if( line != null) 
                comparator[i] = Double.parseDouble(line);
            else 
                comparator[i] = null;
            
        }
        while (true) {

            int index = -1; // salva o numero do arquivo onde o menor numero foi encontrado

            // min maxzada pra achar o menor
            Double min = Double.MAX_VALUE;
            for (int i = 0; i < 10; i++) 
                // se achar um numero menor, salva o index
                // caso não tenha, os numeros acabaram
                if(comparator[i] != null && comparator[i] < min) {
                    min = comparator[i];
                    index = i; // muda o index aqui
                }
            
            
            // para quando os numeros acabam
            if (index < 0) {break;}

            nums.add(min);

            String aux;
            // pega o proximo numero do arquivo onde foi encontrado o menor na iteração atual
            if((aux = bfs.get(index).readLine()) != null) 
                comparator[index] = Double.parseDouble(aux);
            else 
                comparator[index] = null; // caso tenha acabado o arquivo
        }

        // fecha os buffered readers
        for (BufferedReader b : bfs)
            b.close();

        // novo arquivo com os numeros intercalados
        Double[] output = new Double[nums.size()];
        escrever(nums.toArray(output));
    }


    // escreve um arquivo e incrementa o numero que define o proximo
    private void escrever (Double[] registros) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter (""+temp));
        temp++;
        bloco++;
        for (Double f : registros) {
            bw.write(f.toString());
            bw.newLine();
        }
        bw.close();
    }

    // apaga arquivos
    private void deletarArquivos (){
        for (int i = 0; i < bloco - 1; i++) {
            File arquivo = new File (""+i);
            if (arquivo.exists())
                arquivo.delete();
        }
    }

    public void heapSort(Double[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);        

        for (int i = n - 1; i > 0; i--) {

            Double temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    private void heapify(Double[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) 
            largest = left;

        if (right < n && arr[right] > arr[largest]) 
            largest = right;
        

        // Se o maior não é a raiz
        if (largest != i) {
            Double swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }


    private void salvarArquivoFinal() {
        if (temp > 0) {
            File tempFile = new File(String.valueOf(temp - 1)); // Obtém o último arquivo gerado
            File finalFile = new File("saida_ordenacao.txt");
    
            if (!tempFile.exists()) {
                return;
            }
    
            // Remover o arquivo final caso já exista
            if (finalFile.exists()) {
                finalFile.delete();
            }
    
            tempFile.renameTo(finalFile);
        }
    }
    
    
    
    private void deletarArquivosTemporarios() {
        System.out.println("Número de arquivos temporários: " + temp);
        for (int i = 0; i < temp - 1; i++) {
            new File("" + i).delete();
        }
    }

    public List<Double> carregarDadosOrdenados() throws IOException {
        salvarArquivoFinal();
       // System.out.println("Tentando abrir: " + new File("saida_ordenacao.txt").getAbsolutePath());
//System.out.println("Existe? " + new File("saida_ordenacao.txt").exists());
        List<Double> dados = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("saida_ordenacao.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            dados.add(Double.parseDouble(line));
        }
        br.close();
        return dados;
        
    }
}

