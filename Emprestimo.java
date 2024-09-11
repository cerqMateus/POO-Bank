// Importa as classes necessárias
import java.text.DecimalFormat; // Formatação de números
import java.text.DecimalFormatSymbols; // Símbolos de formatação
import java.util.ArrayList; // Mnipulação de listas dinâmicas
import java.util.List; // Interface para listas
import java.util.Locale; // Definir a localidade

public class Emprestimo {
    private String id; // Identificador único do empréstimo
    private String tipo; // Tipo de empréstimo
    private double valor; // Valor total do empréstimo
    private int parcelas; // Número total de parcelas
    private int parcelasPagas; // Número de parcelas já pagas
    private Cliente cliente; // Cliente associado ao empréstimo

    // Lista estática para armazenar todos os empréstimos
    public static List<Emprestimo> emprestimos = new ArrayList<>();

    // Criação de um DecimalFormat para formatar valores monetários em português do Brasil
    private static final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    // Construtor da classe Emprestimo
    public Emprestimo(String id, String tipo, double valor, Cliente cliente, int parcelas) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.cliente = cliente;
        this.parcelas = parcelas;
        emprestimos.add(this);
        cliente.adicionarEmprestimo(this);
    }

    // Getters e Setters para acessar e modificar os atributos da classe
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public int getParcelas() { return parcelas; }
    public void setParcelas(int parcelas) { this.parcelas = parcelas; }

    // Calcula a taxa de juros com base no tipo de empréstimo
    public double calcularJuros() {
        if (this.tipo.equals("Emprestimo Pessoal")) {
            return 0.05; 
        } else if (this.tipo.equals("Financiamento")) {
            return 0.03; 
        } else {
            return 0.04;
        }
    }

    // Realiza o pagamento de uma parcela do empréstimo
    public void pagarParcela() {
        if (parcelas <= 0) {
            System.out.println("Todas as parcelas já foram pagas.");
            return;
        }

        double juros = calcularJuros(); 
        double valorParcela = (this.valor / this.parcelas) * (1 + juros);
        double novoSaldo = cliente.getSaldo() - valorParcela;

        if (novoSaldo >= 0) {
            cliente.setSaldo(novoSaldo);
            this.valor -= (this.valor / this.parcelas);
            this.parcelas--;
            parcelasPagas++;

            // Adiciona a informação da parcela paga ao extrato do cliente
            cliente.getExtrato().add("Parcela (" + parcelasPagas + "/" + (parcelasPagas + this.parcelas) + ") paga: ID " + this.id + ", Valor: R$ " + df.format(valorParcela));

            System.out.println("Parcela de R$ " + df.format(valorParcela) + " paga com sucesso!");
            System.out.println("Novo saldo do cliente: R$ " + df.format(novoSaldo));
            System.out.println("Valor restante do empréstimo: R$ " + df.format(this.valor));
            System.out.println("Parcelas restantes: " + this.parcelas);

            if (this.parcelas <= 0) {
                cliente.removerEmprestimo(this);
                System.out.println("Empréstimo removido porque todas as parcelas foram pagas.");
            }
        } else {
            System.out.println("Saldo insuficiente para pagar a parcela."); 
        }
    }
}
