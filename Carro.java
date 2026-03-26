
import java.io.*;
import java.util.*;

public class Carro {

    // ======================================================
    // ATRIBUTOS
    // ======================================================
    private String modelo;
    private String marca;
    private String ano;
    private double preco;

    public Carro(String modelo, String marca, String ano, double preco) {
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.preco = preco;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public String getAno() {
        return ano;
    }

    public double getPreco() {
        return preco;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void exibirInfo(int numero) {
        System.out.printf("[%d] %s %s (%s) - R$ %.2f%n", numero, marca, modelo, ano, preco);
    }

    // Converte o carro para uma linha CSV para salvar no arquivo
    public String paraCsv() {
        return modelo + "," + marca + "," + ano + "," + preco;
    }

    // Lê uma linha CSV e cria um objeto Carro
    public static Carro doCsv(String linha) {
        String[] partes = linha.split(",");
        return new Carro(partes[0], partes[1], partes[2], Double.parseDouble(partes[3]));
    }

    // ======================================================
    // USUÁRIOS — armazenados em users.txt (usuario:senha:perfil)
    // ======================================================
    private static final String ARQUIVO_USUARIOS = "users.txt";
    private static final String ARQUIVO_CARROS = "carros.csv";

    private static String usuarioLogado = null;
    private static String perfilLogado = null; // "admin" ou "usuario"

    // Cria o arquivo com usuário padrão se não existir
    private static void inicializarUsuarios() {
        File f = new File(ARQUIVO_USUARIOS);
        if (!f.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("admin:1234:admin");
                pw.println("joao:abcd:usuario");
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de usuários.");
            }
        }
    }

    private static boolean login(Scanner scanner) {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║           LOGIN              ║");
        System.out.println("╚══════════════════════════════╝");

        for (int tentativas = 3; tentativas > 0; tentativas--) {
            System.out.print("Usuário: ");
            String usuario = scanner.nextLine().trim();
            System.out.print("Senha:   ");
            String senha = scanner.nextLine().trim();

            try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split(":");
                    if (partes[0].equals(usuario) && partes[1].equals(senha)) {
                        usuarioLogado = usuario;
                        perfilLogado = partes[2];
                        System.out.println(" Bem-vindo, " + usuario + "! [" + perfilLogado + "]");
                        return true;
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao ler usuários.");
                return false;
            }

            System.out.println(" Usuário ou senha incorretos. Tentativas restantes: " + (tentativas - 1));
        }

        System.out.println(" Acesso bloqueado após 3 tentativas.");
        return false;
    }

    // Somente admin pode registrar novos usuários
    private static void registrarUsuario(Scanner scanner) {
        if (!perfilLogado.equals("admin")) {
            System.out.println(" Apenas administradores podem criar usuários.");
            return;
        }

        System.out.println("\n--- Novo Usuário ---");
        String novoUsuario = lerTexto(scanner, "Nome de usuário: ");
        String novaSenha = lerTexto(scanner, "Senha: ");

        System.out.print("Perfil (admin/usuario): ");
        String perfil = scanner.nextLine().trim();
        if (!perfil.equals("admin") && !perfil.equals("usuario")) {
            System.out.println(" Perfil inválido! Use 'admin' ou 'usuario'.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS, true))) {
            pw.println(novoUsuario + ":" + novaSenha + ":" + perfil);
            System.out.println(" Usuário criado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuário.");
        }
    }

    // ======================================================
    // ARQUIVO — salvar e carregar carros
    // ======================================================
    private static void salvarCarros(ArrayList<Carro> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CARROS))) {
            for (Carro c : lista) {
                pw.println(c.paraCsv());
            }
            System.out.println(" " + lista.size() + " carro(s) salvo(s) em " + ARQUIVO_CARROS);
        } catch (IOException e) {
            System.out.println(" Erro ao salvar carros: " + e.getMessage());
        }
    }

    private static ArrayList<Carro> carregandofavoritos() {
        ArrayList<Carro> favoritos = new ArrayList<>();
        File b = new File(ARQUIVO_CARROS_FAVORITOS);
        if (!b.exists()) {
            return favoritos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(b))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) {
                    linha.add(Carro.doCsv(linha));
                }
            }
            System.out.println(" " + lista.size() + " carro(s) carregado(s) do arquivo.");
        } catch (IOException | NumberFormatException e) {
            System.out.println(" Erro ao carregar carros: " + e.getMessage());
        }
    }

    

    private static ArrayList<Carro> carregarCarros() {
        ArrayList<Carro> lista = new ArrayList<>();
        File f = new File(ARQUIVO_CARROS);
        if (!f.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) {
                    lista.add(Carro.doCsv(linha));
                }
            }
            System.out.println(" " + lista.size() + " carro(s) carregado(s) do arquivo.");
        } catch (IOException | NumberFormatException e) {
            System.out.println(" Erro ao carregar carros: " + e.getMessage());
        }
        return lista;
    }

    // ======================================================
    // MENU
    // ======================================================
    private static void exibirMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.printf("║     CONCESSIONÁRIA  [%s]%s║%n",
                perfilLogado, perfilLogado.equals("admin") ? "  " : " ");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1 - Adicionar carro         ║");
        System.out.println("║  2 - Remover carro           ║");
        System.out.println("║  3 - Editar carro            ║");
        System.out.println("║  4 - Buscar por modelo       ║");
        System.out.println("║  5 - Listar todos            ║");
        System.out.println("║  6 - Ordenar por preço       ║");
        System.out.println("║  7 - Salvar em arquivo       ║");
        System.out.println("║  8 - Filtrando por preço     ║");
        if (perfilLogado.equals("admin")) {
            System.out.println("║  9 - Criar novo usuário      ║");
        }
        System.out.println("║  10-Contagem de carros na lista  ║");
        System.out.println("║  11 -o carro mais barato e o mais caro  ║");
        System.out.println("║  12 -Filtrar carro por marca ║");
        System.out.println("║  13 - media de preços        ║");
        System.out.println("║  14 - ordenar por ano        ║");
        System.out.println("║  15 - Alternar senha         ║");
        System.out.println("║  16 - Exibir Usuarios        ║");
        System.out.println("║  17 -  exportacao limpa      ║");
        System.out.println("║  0 - Sair                    ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Opção: ");
    }

    // ======================================================
    // MAIN
    // ======================================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        inicializarUsuarios();

        if (!login(scanner)) {
            return;
        }
        ArrayList<Carro> lista = carregarCarros();
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();

            if (!scanner.hasNextInt()) {
                System.out.println(" Digite apenas números!");
                scanner.nextLine();
                continue;
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 ->
                    adicionarCarro(lista, scanner);
                case 2 ->
                    removerCarro(lista, scanner);
                case 3 ->
                    editarCarro(lista, scanner);
                case 4 ->
                    buscarCarro(lista, scanner);
                case 5 ->
                    listarCarros(lista);
                case 6 ->
                    ordenarPorPreco(lista);
                case 7 ->
                    salvarCarros(lista);
                case 8 ->
                    filtrarporpreco(lista, scanner);
                case 9 ->
                    registrarUsuario(scanner);
                case 10 ->
                    ContarCarros(lista);
                case 11 ->
                    MaisBaratoeMaisCaro(lista);
                case 12 ->
                    filtrarMarca(lista, scanner);
                case 13 ->
                    caclularmediadepreco(lista);
                case 14 ->
                    ordenarPorAno(lista);
                case 15 ->
                    alternarsenha(scanner);
                case 16 ->
                    exibirUsuarios(scanner);
                case 17 ->
                    exportacaolimpa(scanner);
                case 0 -> {
                    salvarCarros(lista); // salva automaticamente ao sair
                    System.out.println("Até logo, " + usuarioLogado);
                }
                default ->
                    System.out.println(" Opção inválida.");
            }
        }

        scanner.close();
    }

    // ======================================================
    // FUNCIONALIDADES
    // ======================================================
    private static void sistemadefavoritos(ArrayList<Carro> lista) {

    }

    private static void adicionarCarro(ArrayList<Carro> lista, Scanner scanner) {
        // Somente admin pode adicionar
        if (!perfilLogado.equals("admin")) {
            System.out.println(" Apenas administradores podem adicionar carros.");
            return;
        }
        System.out.println("\n--- Adicionar Carro ---");
        lista.add(new Carro(
                lerTexto(scanner, "Modelo: "),
                lerTexto(scanner, "Marca: "),
                lerAno(scanner),
                lerPreco(scanner)));
        System.out.println(" Carro adicionado!");
    }

    private static void removerCarro(ArrayList<Carro> lista, Scanner scanner) {
        if (!perfilLogado.equals("admin")) {
            System.out.println(" Apenas administradores podem remover carros.");
            return;
        }
        if (listaVazia(lista)) {
            return;
        }
        listarCarros(lista);
        System.out.print("Número do carro para remover: ");
        int i = lerIndice(scanner, lista.size());
        if (i == -1) {
            return;
        }
        System.out.println(" \"" + lista.get(i).getModelo() + "\" removido.");
        lista.remove(i);
    }

    public static void ContarCarros(ArrayList<Carro> lista) {

        System.out.println("a quantidade de carros dentro da lista é: " + lista.size());
    }

    public static void MaisBaratoeMaisCaro(ArrayList<Carro> lista) {

        Carro precoMaximo = Collections.max(lista, Comparator.comparingDouble(Carro::getPreco));
        Carro precoMinimo = Collections.min(lista, Comparator.comparingDouble(Carro::getPreco));

        System.out.print("o carro mais barato tem este valor");
        precoMinimo.exibirInfo(1);
        System.out.print("o carro mais caro tem este valor");
        precoMaximo.exibirInfo((1));
    }

    // EDITAR — permite alterar campo por campo
    private static void editarCarro(ArrayList<Carro> lista, Scanner scanner) {
        if (!perfilLogado.equals("admin")) {
            System.out.println(" Apenas administradores podem editar carros.");
            return;
        }
        if (listaVazia(lista)) {
            return;
        }

        listarCarros(lista);
        System.out.print("Número do carro para editar: ");
        int i = lerIndice(scanner, lista.size());
        if (i == -1) {
            return;
        }

        Carro c = lista.get(i);
        System.out.println("\nEditando: " + c.getMarca() + " " + c.getModelo());
        System.out.println("(Deixe em branco para manter o valor atual)\n");

        System.out.print("Novo modelo [" + c.getModelo() + "]: ");
        String v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            c.setModelo(v);
        }

        System.out.print("Nova marca [" + c.getMarca() + "]: ");
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            c.setMarca(v);
        }

        System.out.print("Novo ano [" + c.getAno() + "]: ");
        v = scanner.nextLine().trim();
        if (!v.isEmpty() && v.matches("\\d{4}")) {
            c.setAno(v);
        }

        System.out.print("Novo preço [" + c.getPreco() + "]: ");
        v = scanner.nextLine().trim();
        if (!v.isEmpty()) {
            try {
                c.setPreco(Double.parseDouble(v.replace(",", ".")));
            } catch (NumberFormatException e) {
                System.out.println(" Preço inválido, mantido o anterior.");
            }
        }

        System.out.println(" Carro atualizado!");
    }

    private static void ordenarPorAno(ArrayList<Carro> lista) {
        if (listaVazia(lista)) {
            return;
        }

        lista.sort((a, b) -> Integer.compare(
                Integer.parseInt(a.getAno()),
                Integer.parseInt(b.getAno())));

        System.out.println(" Lista ordenada por ano!");
        listarCarros(lista);
    }

    private static void buscarCarro(ArrayList<Carro> lista, Scanner scanner) {
        if (listaVazia(lista)) {
            return;
        }
        System.out.print("Modelo a buscar: ");
        String termo = scanner.nextLine().trim().toLowerCase();
        boolean achou = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getModelo().toLowerCase().contains(termo)) {
                lista.get(i).exibirInfo(i + 1);
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum resultado para \"" + termo + "\".");
        }
    }

    private static void filtrarMarca(ArrayList<Carro> lista, Scanner scanner) {
        if (listaVazia(lista)) {
            return;
        }

        System.out.print("Qual marca você quer para seu carro? ");
        String marca = scanner.nextLine().trim().toLowerCase();
        boolean achou = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getMarca().toLowerCase().contains(marca)) {
                lista.get(i).exibirInfo(i + 1);
                achou = true;
            }
        }
        if (!achou) {
            System.out.print("nenhum resultado para:  " + marca);
        }

    }

    private static void caclularmediadepreco(ArrayList<Carro> lista) {
        if (listaVazia(lista)) {
            return;
        }
        double soma = 0;

        for (int i = 0; i < lista.size(); i++) {

            double preco = lista.get(i).getPreco();
            soma += preco;

        }
        double resultado = soma / lista.size();
        System.out.printf("Média de preços: R$ %.2f%n", resultado);

    }

    public static void ordenarporano(ArrayList<Carro> lista, Scanner scanner) {

        lista.sort((a, b) -> Integer.compare(Integer.parseInt(a.getAno()), Integer.parseInt(b.getAno())));

    }

    private static void alternarsenha(Scanner scanner) {

        System.out.println("Alterando senha ");
        String novaSenha = lerTexto(scanner, "escolha uma nova senha: ");
        ArrayList<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println(" Erro ao ler usuários: " + e.getMessage());
            return;
        }

        for (int i = 0; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(":");
            if (partes[0].equals(usuarioLogado)) {
                linhas.set(i, usuarioLogado + ":" + novaSenha + ":" + partes[2]);
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (String linha : linhas) {
                pw.println(linha);
            }
        } catch (IOException e) {
            System.out.println(" Erro ao ler usuários: " + e.getMessage());
            return;
        }
        System.out.println(" Senha alterada com sucesso!");
    }

    private static void exibirUsuarios(Scanner scanner) {
        if (!perfilLogado.equals("admin")) {
            System.out.println(" Apenas administradores podem adicionar carros.");
            return;
        }

        ArrayList<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println(" Erro ao ler usuários: " + e.getMessage());
            return;
        }

        for (String linha : linhas) {
            String[] partes = linha.split(":");
            System.out.println("Usuario = " + partes[0] + "Tipo de usuario: " + partes[2]);
        }
    }

    public static void exportacaolimpa(Scanner scanner) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CARROS))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");

                System.out.println(
                        "Modelo: " + partes[0]
                        + " | Marca: " + partes[1]
                        + " | Ano: " + partes[2]
                        + " | Valor: R$ " + partes[3]);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler carros: " + e.getMessage());
        }
    }

    private static void filtrarporpreco(ArrayList<Carro> lista, Scanner scanner) {
        if (listaVazia(lista)) {
            return;
        }
        System.out.println("Preço mínimo: ");
        double min = lerPreco(scanner);
        System.out.println("Preço máximo: ");
        double max = lerPreco(scanner);
        boolean achou = false;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getPreco() >= min && lista.get(i).getPreco() <= max) {
                lista.get(i).exibirInfo(i + 1);
                achou = true;
            }
        }
        if (!achou) {
            System.out.print("nenhum resultado achado");
        }

    }

    private static void listarCarros(ArrayList<Carro> lista) {
        if (listaVazia(lista)) {
            return;
        }
        System.out.println("\n---  Carros disponíveis ---");
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).exibirInfo(i + 1);
        }
    }

    private static void ordenarPorPreco(ArrayList<Carro> lista) {
        if (listaVazia(lista)) {
            return;
        }
        lista.sort((a, b) -> Double.compare(a.getPreco(), b.getPreco()));
        System.out.println(" Lista ordenada por preço!");
        listarCarros(lista);
    }

    // ======================================================
    // HELPERS
    // ======================================================
    private static boolean listaVazia(ArrayList<Carro> lista) {
        if (lista.isEmpty()) {
            System.out.println(" Nenhum carro cadastrado.");
            return true;
        }
        return false;
    }

    private static String lerTexto(Scanner scanner, String msg) {
        String v;
        do {
            System.out.print(msg);
            v = scanner.nextLine().trim();
            if (v.isEmpty()) {
                System.out.println(" Campo obrigatório!");
            }
        } while (v.isEmpty());
        return v;
    }

    private static String lerAno(Scanner scanner) {
        String ano;
        do {
            System.out.print("Ano: ");
            ano = scanner.nextLine().trim();
            if (!ano.matches("\\d{4}") || Integer.parseInt(ano) < 1886 || Integer.parseInt(ano) > 2025) {
                System.out.println(" Ano inválido!");
                ano = "";
            }
        } while (ano.isEmpty());
        return ano;
    }

    private static double lerPreco(Scanner scanner) {
        double preco = -1;
        do {
            System.out.print("Preço: R$ ");
            if (scanner.hasNextDouble()) {
                preco = scanner.nextDouble();
                scanner.nextLine();
                if (preco <= 0) {
                    System.out.println(" Preço deve ser positivo!");
                    preco = -1;
                }
            } else {
                System.out.println(" Valor inválido!");
                scanner.nextLine();
            }
        } while (preco == -1);
        return preco;
    }

    private static int lerIndice(Scanner scanner, int tamanho) {
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida!");
            scanner.nextLine();
            return -1;
        }
        int i = scanner.nextInt() - 1;
        scanner.nextLine();
        if (i < 0 || i >= tamanho) {
            System.out.println(" Número fora da lista!");
            return -1;
        }
        return i;
    }
}
