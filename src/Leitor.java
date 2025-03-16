import java.io.*;
import java.util.*;

public class Leitor {
    private String filePath;
    private int bloco = 0;
    private int temp = 0;

    public Leitor(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Caminho do arquivo não pode ser nulo ou vazio.");
        }
        this.filePath = filePath;
    }

    public void lerArquivo() throws IOException {
        long start = System.currentTimeMillis();
        int cont = 0;
        List<Double> registros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                registros.add(Double.parseDouble(line));
                cont++;

                if (cont == 1000) {
                    Double[] aux = registros.toArray(new Double[0]);
                    heapSort(aux);
                    escrever(aux);
                    cont = 0;
                    registros.clear();
                }
            }
        }

        int index = 0;
        while (bloco > 0 && index < bloco) {
            intercalar(index);
            index += 10;
        }

        deletarArquivos();

        long end = System.currentTimeMillis();
        long mili = end - start;
        long segs = mili / 1000;
        String tempo = String.format("%02d:%02d", segs / 60, segs % 60);
        System.out.println("Tempo de execução: " + tempo);
    }

    private void intercalar(int numFile) throws IOException {
        List<Double> nums = new ArrayList<>();
        Double[] comparator = new Double[10];
        List<BufferedReader> bfs = new ArrayList<>();
        int leftFiles = bloco - numFile;

        if (leftFiles > 0) {
            for (int i = 0; i < 10; i++) {
                try {
                    BufferedReader b = new BufferedReader(new FileReader("" + numFile));
                    bfs.add(b);
                    numFile++;
                } catch (Exception e) {
                    break;
                }
            }
        } else {
            return;
        }

        for (int i = 0; i < bfs.size(); i++) {
            String line = bfs.get(i).readLine();
            if (line != null)
                comparator[i] = Double.parseDouble(line);
            else
                comparator[i] = null;
        }

        while (true) {
            int index = -1;
            Double min = Double.MAX_VALUE;
            for (int i = 0; i < 10; i++) {
                if (comparator[i] != null && comparator[i] < min) {
                    min = comparator[i];
                    index = i;
                }
            }

            if (index < 0) {
                break;
            }

            nums.add(min);

            String aux;
            if ((aux = bfs.get(index).readLine()) != null)
                comparator[index] = Double.parseDouble(aux);
            else
                comparator[index] = null;
        }

        for (BufferedReader b : bfs)
            b.close();

        Double[] output = nums.toArray(new Double[0]);
        escrever(output);
    }

    private void escrever(Double[] registros) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("" + temp))) {
            temp++;
            bloco++;
            for (Double f : registros) {
                bw.write(f.toString());
                bw.newLine();
            }
        }
    }

    private void deletarArquivos() {
        for (int i = 0; i < bloco - 1; i++) {
            File arquivo = new File("" + i);
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

        if (largest != i) {
            Double swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }

    public List<Double> carregarDadosOrdenados() throws IOException {
        List<Double> dados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("" + (bloco - 1)))) {
            String line;
            while ((line = br.readLine()) != null) {
                dados.add(Double.parseDouble(line));
            }
        }
        return dados;
    }
}