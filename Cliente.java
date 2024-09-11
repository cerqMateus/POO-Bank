// Importa as classes necessárias
import java.text.DecimalFormat; // Formatação de números
import java.text.DecimalFormatSymbols; // Símbolos de formatação
import java.util.ArrayList; // Manipulação de listas dinâmicas
import java.util.List; // Interface para listas
import java.util.Locale; // Definir a localidade

public class Cliente {
    private String id; // Identificador único do cliente
    private String nome; // Nome do cliente
    private String cpf; // CPF do cliente
    private String endereco; // Endereço do cliente
    private String telefone; // Telefone do cliente
    private String numeroConta; // Número da conta do cliente
    private ArrayList<Emprestimo> emprestimos; // Lista de empréstimos do cliente
    private ArrayList<Investimento> investimentos; // Lista de investimentos do cliente
    private double saldo; // Saldo atual da conta
    private String senha; // Senha do cliente
    private double limiteCredito; // Limite de crédito do cliente
    private List<String> extrato; // Extrato de transações realizadas

    // Criação de um DecimalFormat para formatar valores monetários em português do Brasil
    private static final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    // Método Construtor
    public Cliente(String id, String nome, String cpf, String endereco, String telefone, String numeroConta, double saldo, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.numeroConta = numeroConta;
        this.emprestimos = new ArrayList<>();
        this.investimentos = new ArrayList<>(); 
        this.saldo = saldo;
        this.senha = senha;
        this.extrato = new ArrayList<>();
        atualizarLimiteCredito(); 
    }

    // Getters e Setters para acessar e modificar os atributos da classe
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public ArrayList<Emprestimo> getEmprestimos() { return emprestimos; }
    public void setEmprestimos(ArrayList<Emprestimo> emprestimos) { this.emprestimos = emprestimos; }
    public ArrayList<Investimento> getInvestimentos() { return investimentos; }
    public void setInvestimentos(ArrayList<Investimento> investimentos) { this.investimentos = investimentos; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public double getLimiteCredito() { return limiteCredito; }
    public List<String> getExtrato() { return extrato; }

    // Atualiza o limite de crédito com base no saldo, total de investimentos e total de empréstimos
    private void atualizarLimiteCredito() {
        double totalInvestimentos = investimentos.stream()
            .mapToDouble(Investimento::getValor)
            .sum();
        double totalEmprestimos = emprestimos.stream()
            .mapToDouble(Emprestimo::getValor) 
            .sum();
        this.limiteCredito = (saldo + totalInvestimentos) - totalEmprestimos;
    }

    // Verifica se é possível realizar um empréstimo com o valor solicitado
    public boolean podeRealizarEmprestimo(double valor) {
        return valor <= limiteCredito;
    }

    // Adiciona um empréstimo à lista e atualiza o limite de crédito
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
        atualizarLimiteCredito();
        extrato.add("Empréstimo realizado: ID " + emprestimo.getId() + ", Tipo: " + emprestimo.getTipo() + ", Valor: R$ " + df.format(emprestimo.getValor()));
    }

    // Remove um empréstimo da lista e atualiza o limite de crédito
    public void removerEmprestimo(Emprestimo emprestimo) {
        emprestimos.remove(emprestimo);
        atualizarLimiteCredito();
    }

    // Adiciona um investimento à lista e atualiza o limite de crédito
    public void adicionarInvestimento(Investimento investimento) {
        investimentos.add(investimento);
        atualizarLimiteCredito();
    }

    // Remove um investimento da lista e atualiza o limite de crédito
    public void removerInvestimento(Investimento investimento) {
        investimentos.remove(investimento); 
        atualizarLimiteCredito();
    }

    // Exibe os dados básicos do cliente
    public void dados(Cliente cliente) {
        System.out.println();
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Endereço: " + endereco);
        System.out.println("Telefone: " + telefone);
        System.out.println("Número da conta: " + numeroConta);
        System.out.println("Saldo: R$ " + df.format(saldo)); 
        System.out.println("Limite: R$ " + df.format(limiteCredito));

        System.out.println("Lista de Empréstimos:");
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getValor() > 0) {
                System.out.println("  - ID: " + emprestimo.getId() + ", Tipo: " + emprestimo.getTipo() + ", Valor: R$ " + df.format(emprestimo.getValor()) + ", Parcelas: " + emprestimo.getParcelas());
            }
        }

        Investimento.exibirInvestimentos(cliente);
    }

    // Realiza uma transferência de valor para outro cliente
    public void transferir(double valor, Cliente destinatario) {
        if (this.saldo >= valor) { 
            this.saldo -= valor; 
            destinatario.saldo += valor; 
            atualizarLimiteCredito(); 
            destinatario.atualizarLimiteCredito(); 
            extrato.add("Transferência: R$ " + df.format(valor) + " para " + destinatario.getNome()); 
            destinatario.extrato.add("Recebido: R$ " + df.format(valor) + " de " + this.getNome()); 
            System.out.println("Transferência de R$ " + df.format(valor) + " realizada com sucesso!");
            System.out.println("Novo saldo: R$ " + df.format(this.saldo));
        } else {
            System.out.println("Saldo insuficiente para realizar a transferência.");
        }
    }

    // Realiza um depósito na conta
    public void depositar(double valor) {
        this.saldo += valor;
        atualizarLimiteCredito(); 
        extrato.add("Depósito: R$ " + df.format(valor));
        System.out.println("Depósito realizado com sucesso!");
        System.out.println("Novo saldo: R$ " + df.format(this.saldo));
    }

    // Realiza um saque da conta
    public void sacar(double valor) {
        if (this.saldo >= valor) { 
            this.saldo -= valor; 
            atualizarLimiteCredito(); 
            extrato.add("Saque: R$ " + df.format(valor)); 
            System.out.println("Saque de R$ " + df.format(valor) + " realizado com sucesso!");
            System.out.println("Novo saldo: R$ " + df.format(this.saldo));
        } else {
            System.out.println("Saldo insuficiente para realizar o saque."); 
        }
    }

    // Exibe o extrato das transações realizadas
    public void exibirExtrato() {
        System.out.println();
        System.out.println("--- Extrato de Conta ---");
        for (String transacao : extrato) {
            System.out.println(transacao);
        }
        System.out.println();
    }
}
