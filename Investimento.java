// Importa as classes necessárias
import java.text.DecimalFormat; // Formatação de números
import java.text.DecimalFormatSymbols; // Símbolos de formatação
import java.util.ArrayList; // Manipulação de listas dinâmicas
import java.util.List; // Interface para listas
import java.util.Locale; // Definir a localidade
import java.util.Random; // Definir números aleatórios

public class Investimento {
    // Lista para armazenar todos os investimentos
    private static List<Investimento> investimentos = new ArrayList<>();

    private double valor; 
    private String tipo; 
    private Cliente cliente;

    // Formatação para exibir valores monetários em formato brasileiro
    private static final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    // Construtor da classe Investimento
    public Investimento(double valor, String tipo, Cliente cliente) {
        this.valor = valor;
        this.tipo = tipo;
        this.cliente = cliente;
        adicionarOuAtualizarInvestimento(this); // Adiciona ou atualiza o investimento na lista
        if (tipo.equalsIgnoreCase("Poupança")) {
            iniciarIncrementoPoupanca();
        } else if (tipo.equalsIgnoreCase("Ações")) {
            iniciarIncrementoAcoes();
        }
    }

    // Métodos getter e setter para atributos
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // Adiciona ou atualiza o investimento na lista de investimentos
    private static void adicionarOuAtualizarInvestimento(Investimento novoInvestimento) {
        for (Investimento investimento : investimentos) {
            if (investimento.getTipo().equalsIgnoreCase(novoInvestimento.getTipo())) {
                investimento.setValor(investimento.getValor() + novoInvestimento.getValor());
                return;
            }
        }
        investimentos.add(novoInvestimento); // Se não encontrar, adiciona um novo investimento
    }

    // Exibe todos os investimentos de um cliente específico
    public static void exibirInvestimentos(Cliente cliente) {
        System.out.println("Lista de Investimentos: ");
        boolean encontrouInvestimento = false;
        for (Investimento investimento : investimentos) {
            if (investimento.getCliente().equals(cliente)) {
                double valor = investimento.getValor();
                if (valor > 0) {
                    System.out.println("  - Tipo: " + investimento.getTipo() + ", Valor: R$ " + df.format(valor));
                    encontrouInvestimento = true;
                }
            }
        }
    }

    // Resgata um investimento do cliente
    public static void resgatarInvestimento(String tipo, double valor, Cliente cliente) {
        if (valor == 0) { // Resgate total
            for (Investimento investimento : investimentos) {
                if (investimento.getTipo().equalsIgnoreCase(tipo) && investimento.getCliente().equals(cliente)) {
                    double valorInvestimento = investimento.getValor();
                    cliente.setSaldo(cliente.getSaldo() + valorInvestimento);
                    cliente.removerInvestimento(investimento);
                    investimentos.remove(investimento);
                    cliente.getExtrato().add("Resgate total de investimento: Tipo " + tipo + ", Valor: R$ " + df.format(valorInvestimento));
                    System.out.println("Resgate total de R$ " + df.format(valorInvestimento) + " realizado com sucesso!");
                    System.out.println("Novo saldo do cliente: R$ " + df.format(cliente.getSaldo()));
                    return;
                }
            }
            System.out.println("Investimento não encontrado.");
        } else { // Resgate parcial
            for (Investimento investimento : investimentos) {
                if (investimento.getTipo().equalsIgnoreCase(tipo) && investimento.getCliente().equals(cliente)) {
                    double valorInvestimento = investimento.getValor();
                    if (valorInvestimento >= valor) {
                        investimento.setValor(valorInvestimento - valor);
                        cliente.setSaldo(cliente.getSaldo() + valor);
                        cliente.removerInvestimento(new Investimento(valor, tipo, cliente));
                        cliente.getExtrato().add("Resgate de investimento: Tipo " + tipo + ", Valor: R$ " + df.format(valor));
                        System.out.println("Resgate de R$ " + df.format(valor) + " realizado com sucesso!");
                        System.out.println("Novo saldo do cliente: R$ " + df.format(cliente.getSaldo()));
                        if (investimento.getValor() == 0) {
                            investimentos.remove(investimento);
                        }
                        return;
                    } else {
                        System.out.println("Valor insuficiente para resgate.");
                        return;
                    }
                }
            }
            System.out.println("Investimento não encontrado.");
        }
    }

    // Inicia o incremento periódico para o tipo "Poupança"
    private void iniciarIncrementoPoupanca() {
        Runnable incrementoPoupanca = () -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    synchronized (Investimento.class) {
                        for (Investimento investimento : investimentos) {
                            if (investimento.getTipo().equalsIgnoreCase(tipo)) {
                                double valorAtual = investimento.getValor();
                                investimento.setValor(valorAtual + 0.001 * valorAtual); // Incremento de 0.1%
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        Thread thread = new Thread(incrementoPoupanca);
        thread.setDaemon(true);
        thread.start(); // Inicia a thread de incremento
    }

    // Inicia o incremento periódico para o tipo "Ações"
    private void iniciarIncrementoAcoes() {
        Runnable incrementoAcoes = () -> {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(1000);
                    synchronized (Investimento.class) {
                        for (Investimento investimento : investimentos) {
                            if (investimento.getTipo().equalsIgnoreCase(tipo)) {
                                double valorAtual = investimento.getValor();
                                double valorAleatorio = -0.001 + (0.003) * random.nextDouble(); // Incremento aleatório entre -0.1% e 0.3%
                                investimento.setValor(valorAtual + (valorAleatorio * valorAtual));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        Thread thread = new Thread(incrementoAcoes);
        thread.setDaemon(true);
        thread.start(); // Inicia a thread de incremento
    }
}
