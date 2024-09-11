//Importa as classes necessárias
//Importa as classes necessárias
import java.util.ArrayList; // Manipulação de listas dinâmicas
import java.util.Scanner; // Leitura de entradas do usuário
import java.text.DecimalFormat; // Formatação de números
import java.text.DecimalFormatSymbols; // Símbolos de formatação
import java.util.Locale; // Definir a localidade

public class Main {
    // Lista estática que armazena todos os clientes do sistema
    private static ArrayList<Cliente> clientes = new ArrayList<>();

    // Scanner para ler entradas do usuário
    private static Scanner scanner = new Scanner(System.in);

    // Criação de um DecimalFormat para formatar valores monetários em português do Brasil
    private static final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public static void main(String[] args) {
        // Cria um cliente padrão ao iniciar o programa
        criarClienteDefault();

        boolean executando = true;

        // Loop principal do programa que exibe o menu e processa as opções escolhidas pelo usuário
        while (executando) {
            System.out.println();
            System.out.println("---  Bem-vindo(a) ao Ufbank  ---");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar nova conta");
            System.out.println("2 - Autenticar cliente");
            System.out.println("3 - Autenticar cliente padrão");
            System.out.println("0 - Sair");
            String opcao = scanner.nextLine();

            // Processa a opção escolhida pelo usuário
            switch (opcao) {
                case "1":
                    criarConta(); // Chama o método para criar uma nova conta
                    break;
                case "2":
                    autenticarCliente(); // Chama o método para autenticar um cliente existente
                    break;
                case "3":
                    autenticarClienteDefault(); // Chama o método para autenticar o cliente padrão
                    break;
                case "0":
                    System.out.println("Saindo...");
                    executando = false; // Encerra o loop e sai do programa
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
        scanner.close(); // Fechar o scanner ao sair do programa
    }

    // Cria um cliente padrão para inicializar o sistema
    private static void criarClienteDefault() {
        String id = "C000";
        String nome = "Cliente Padrão";
        String cpf = "12345678900";
        String endereco = "Endereço Padrão";
        String telefone = "000000000";
        String numeroConta = "C000";
        double saldo = 1000.00;
        String senha = "0000";

        Cliente clienteDefault = new Cliente(id, nome, cpf, endereco, telefone, numeroConta, saldo, senha);
        clientes.add(clienteDefault); // Adiciona o cliente padrão à lista de clientes
    }

    // Método para criar uma nova conta de cliente
    private static void criarConta() {
        System.out.print("Digite o seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o seu CPF (Apenas números): ");
        String cpf = scanner.nextLine();
        System.out.print("Digite seu endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Digite seu telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();

        // Verifica se o número da conta já existe
        if (buscarClientePorNumeroConta(numeroConta) != null) {
            System.out.print("Número da conta já existente. Tente novamente.");
            return;
        }

        System.out.print("Digite o saldo inicial: ");
        double saldo = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        // Cria um novo cliente e adiciona à lista
        String id = "C" + (clientes.size() + 1);
        Cliente cliente = new Cliente(id, nome, cpf, endereco, telefone, numeroConta, saldo, senha);
        clientes.add(cliente);

        System.out.println("Conta criada com sucesso!");
    }

    // Busca um cliente na lista pelo número da conta
    private static Cliente buscarClientePorNumeroConta(String numeroConta) {
        for (Cliente cliente : clientes) {
            if (cliente.getNumeroConta().equals(numeroConta)) {
                return cliente; // Retorna o cliente encontrado
            }
        }
        return null; // Retorna null se o cliente não for encontrado
    }

    // Método para autenticar um cliente
    private static void autenticarCliente() {
        System.out.print("Digite o número da conta: ");
        String numeroConta = scanner.nextLine();
        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        Cliente cliente = buscarClientePorNumeroConta(numeroConta);
        if (cliente != null && cliente.getSenha().equals(senha)) {
            System.out.println("Login realizado com sucesso!");
            System.out.println();
            menuOperacoes(cliente); // Chama o método de operações após autenticação bem-sucedida
        } else {
            System.out.println("Falha de autenticação"); // Mensagem de falha de autenticação
        }
    }

    // Método para autenticar o cliente padrão
    private static void autenticarClienteDefault() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente padrão encontrado.");
            return;
        }
        menuOperacoes(clientes.get(0)); // Chama o método de operações para o cliente padrão
    }

    // Exibe o menu de operações para o cliente autenticado
    private static void menuOperacoes(Cliente cliente) {
        boolean continuar = true;

        // Loop para exibir as opções de operações disponíveis ao cliente
        while (continuar) {
            System.out.println();
            System.out.println();
            System.out.println("Escolha uma operação:");
            System.out.println("1 - Ver dados do cliente");
            System.out.println("2 - Realizar empréstimo");
            System.out.println("3 - Realizar investimento");
            System.out.println("4 - Realizar transferência");
            System.out.println("5 - Realizar depósito");
            System.out.println("6 - Realizar saque");
            System.out.println("7 - Pagar parcela de empréstimo");
            System.out.println("8 - Resgatar investimento");
            System.out.println("9 - Exibir extrato");
            System.out.println("0 - Sair");

            String opcao = scanner.nextLine();

            // Processa a opção escolhida pelo cliente
            switch (opcao) {
                case "1":
                    cliente.dados(cliente); // Exibe os dados do cliente
                    break;
                case "2":
                    // Realiza um empréstimo
                    System.out.print("Digite o tipo de empréstimo (Ex: Emprestimo Pessoal, Financiamento): ");
                    String tipoEmprestimo = scanner.nextLine();
                    System.out.print("Digite o valor do empréstimo: ");
                    double valorEmprestimo = scanner.nextDouble();
                    System.out.print("Digite a quantidade de parcelas: ");
                    int parcelas = scanner.nextInt();
                    scanner.nextLine();
                    if (cliente.podeRealizarEmprestimo(valorEmprestimo)) {
                        Emprestimo novoEmprestimo = new Emprestimo("E" + (int) (Math.random() * 1000), tipoEmprestimo, valorEmprestimo, cliente, parcelas);
                        cliente.setSaldo(cliente.getSaldo() + valorEmprestimo); // Atualiza o saldo do cliente após empréstimo
                        System.out.println("Empréstimo adicionado com sucesso!");
                    } else {
                        System.out.println("O valor do empréstimo excede o limite de crédito.");
                    }
                    break;
                case "3":
                    // Realiza um investimento
                    System.out.print("Digite o valor do investimento: ");
                    double valorInvestimento = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Digite o tipo de investimento (Ex: Poupança, Ações): ");
                    String tipoInvestimento = scanner.nextLine();
                    if (cliente.getSaldo() >= valorInvestimento) {
                        cliente.setSaldo(cliente.getSaldo() - valorInvestimento); // Atualiza o saldo do cliente após investimento
                        new Investimento(valorInvestimento, tipoInvestimento, cliente); // Cria um novo investimento
                        System.out.println("Investimento realizado com sucesso!");
                        cliente.getExtrato().add("Investimento realizado: Tipo: " + tipoInvestimento + ", Valor: R$ " + df.format(valorInvestimento)); // Adiciona ao extrato
                    } else {
                        System.out.println("Saldo insuficiente para realizar o investimento.");
                    }
                    break;
                case "4":
                    // Realiza uma transferência
                    System.out.print("Digite o valor da transferência: ");
                    double valorTransferencia = scanner.nextDouble();
                    scanner.nextLine(); // Limpar o buffer do scanner

                    System.out.print("Digite o número da conta do destinatário: ");
                    String numeroContaDestino = scanner.nextLine();
                    Cliente destinatario = buscarClientePorNumeroConta(numeroContaDestino);

                    if (destinatario != null) {
                        // Mostrar informações do destinatário para confirmação
                        System.out.println();
                        System.out.println("--- Confirmar transferência ---");
                        System.out.println("Nome do destinatário: " + destinatario.getNome());
                        System.out.println("Número da conta: " + destinatario.getNumeroConta());
                        System.out.println("Valor da transferência: R$ " + valorTransferencia);
                        System.out.print("Digite 'sim' para confirmar ou qualquer outra coisa para cancelar: ");
                        String confirmacao = scanner.nextLine();

                        if (confirmacao.equalsIgnoreCase("sim")) {
                            cliente.transferir(valorTransferencia, destinatario); // Realiza a transferência
                        } else {
                            System.out.println("Transferência cancelada.");
                        }
                    } else {
                        System.out.println("Cliente destinatário não encontrado.");
                    }
                    break;
                case "5":
                    // Realiza um depósito
                    System.out.print("Digite o valor a ser depositado: ");
                    double valorDeposito = scanner.nextDouble();
                    scanner.nextLine();
                    cliente.depositar(valorDeposito); // Atualiza o saldo do cliente após depósito
                    break;
                case "6":
                    // Realiza um saque
                    System.out.print("Digite o valor a ser sacado: ");
                    double valorSaque = scanner.nextDouble();
                    scanner.nextLine();
                    cliente.sacar(valorSaque); // Atualiza o saldo do cliente após saque
                    break;
                case "7":
                    // Paga uma parcela de um empréstimo
                    System.out.print("Digite o ID do empréstimo que deseja pagar a parcela: ");
                    String idEmprestimo = scanner.nextLine();
                    Emprestimo emprestimo = cliente.getEmprestimos().stream()
                            .filter(e -> e.getId().equals(idEmprestimo))
                            .findFirst()
                            .orElse(null);
                    if (emprestimo != null) {
                        emprestimo.pagarParcela(); // Paga a parcela do empréstimo
                    } else {
                        System.out.print("Empréstimo não encontrado.");
                    }
                    break;
                case "8":
                    // Resgata um investimento
                    System.out.print("Digite o tipo de investimento para resgatar: ");
                    String tipoResgate = scanner.nextLine();
                    System.out.print("Digite o valor para resgatar ou digite 'todo' para resgatar todo o valor: ");
                    String valorResgateStr = scanner.nextLine();
                    double valorResgate = 0;

                    if (valorResgateStr.equalsIgnoreCase("todo")) {
                        valorResgate = 0; // Passar zero para indicar resgate total
                    } else {
                        try {
                            valorResgate = Double.parseDouble(valorResgateStr);
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido.");
                            break;
                        }
                    }
                    Investimento.resgatarInvestimento(tipoResgate, valorResgate, cliente); // Realiza o resgate do investimento
                    break;
                case "9":
                    // Exibe o extrato do cliente
                    cliente.exibirExtrato();
                    break;
                case "0":
                    continuar = false; // Encerra o loop de operações
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente."); // Mensagem de opção inválida
                    break;
            }
        }
    }
}
