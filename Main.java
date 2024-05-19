import java.util.*;

abstract class Funcionario {
    String nome;
    String cargo;
    int anoContratacao;
    int mesContratacao;

    public Funcionario(String nome, String cargo, int mesContratacao, int anoContratacao) {
        this.nome = nome;
        this.cargo = cargo;
        this.mesContratacao = mesContratacao;
        this.anoContratacao = anoContratacao;
    }

    public abstract double getSalarioBase();

    public abstract double getBeneficio(double valorVendas, int anosServico);
    
    public double getTotalPago(double valorVendas, int anosServico) {
        return getSalarioBase() + getBeneficio(valorVendas, anosServico);
    }

    public int getAnosServico(int mes, int ano) {
        return (ano - this.anoContratacao) * 12 + (mes - this.mesContratacao);
    }
}

class Secretario extends Funcionario {

    public Secretario(String nome, int mesContratacao, int anoContratacao) {
        super(nome, "Secretário", mesContratacao, anoContratacao);
    }

    @Override
    public double getSalarioBase() {
        return 7000;
    }

    @Override
    public double getBeneficio(double valorVendas, int anosServico) {
        return 1000 * anosServico + 0.2 * getSalarioBase();
    }
}

class Vendedor extends Funcionario {
    public Vendedor(String nome, int mesContratacao, int anoContratacao) {
        super(nome, "Vendedor", mesContratacao, anoContratacao);
    }

    @Override
    public double getSalarioBase() {
        return 12000;
    }

    @Override
    public double getBeneficio(double valorVendas, int anosServico) {
        return 1800 * anosServico + 0.3 * valorVendas;
    }
}

class Gerente extends Funcionario {

    public Gerente(String nome, int mesContratacao, int anoContratacao) {
        super(nome, "Gerente", mesContratacao, anoContratacao);
    }

    @Override
    public double getSalarioBase() {
        return 20000;
    }

    @Override
    public double getBeneficio(double valorVendas, int anosServico) {
        return 0;
    }
}

public class Main {

    public static double getTotalPago(List<Funcionario> funcionarios, int mes, int ano, Map<String, Double> vendas) {
        double total = 0;
        for (Funcionario f : funcionarios) {
            double valorVendas = vendas.getOrDefault(f.nome, 0.0);
            int anosServico = f.getAnosServico(mes, ano);
            total += f.getTotalPago(valorVendas, anosServico);
        }
        return total;
    }

    public static double getTotalSalarios(List<Funcionario> funcionarios, int mes, int ano) {
        double total = 0;
        for (Funcionario f : funcionarios) {
            total += f.getSalarioBase();
        }
        return total;
    }

    public static double getTotalBeneficios(List<Funcionario> funcionarios, int mes, int ano, Map<String, Double> vendas) {
        double total = 0;
        for (Funcionario f : funcionarios) {
            if (f instanceof Secretario || f instanceof Vendedor) {
                double valorVendas = vendas.getOrDefault(f.nome, 0.0);
                int anosServico = f.getAnosServico(mes, ano);
                total += f.getBeneficio(valorVendas, anosServico);
            }
        }
        return total;
    }

    public static Funcionario getMaiorRecebimento(List<Funcionario> funcionarios, int mes, int ano, Map<String, Double> vendas) {
        Funcionario maiorRecebimento = null;
        double maiorValor = 0;
        for (Funcionario f : funcionarios) {
            double valorVendas = vendas.getOrDefault(f.nome, 0.0);
            int anosServico = f.getAnosServico(mes, ano);
            double total = f.getTotalPago(valorVendas, anosServico);
            if (total > maiorValor) {
                maiorValor = total;
                maiorRecebimento = f;
            }
        }
        return maiorRecebimento;
    }

    public static Funcionario getMaiorBeneficio(List<Funcionario> funcionarios, int mes, int ano, Map<String, Double> vendas) {
        Funcionario maiorBeneficio = null;
        double maiorValor = 0;
        for (Funcionario f : funcionarios) {
            if (f instanceof Secretario || f instanceof Vendedor) {
                double valorVendas = vendas.getOrDefault(f.nome, 0.0);
                int anosServico = f.getAnosServico(mes, ano);
                double beneficio = f.getBeneficio(valorVendas, anosServico);
                if (beneficio > maiorValor) {
                    maiorValor = beneficio;
                    maiorBeneficio = f;
                }
            }
        }
        return maiorBeneficio;
    }

    public static Vendedor getMaiorVendas(List<Vendedor> vendedores, int mes, int ano, Map<String, Double> vendas) {
        Vendedor maiorVendedor = null;
        double maiorVenda = 0;
        for (Vendedor v : vendedores) {
            double venda = vendas.getOrDefault(v.nome, 0.0);
            if (venda > maiorVenda) {
                maiorVenda = venda;
                maiorVendedor = v;
            }
        }
        return maiorVendedor;
    }

    public static void main(String[] args) {
        List<Funcionario> funcionarios = Arrays.asList(
                new Secretario("Jorge Carvalho", 1, 2018),
                new Secretario("Maria Souza", 12, 2015),
                new Vendedor("Ana Silva", 12, 2021),
                new Vendedor("João Mendes", 12, 2021),
                new Gerente("Juliana Alves", 7, 2017),
                new Gerente("Bento Albino", 3, 2014)
        );

        Map<String, Double> vendasDez2021 = new HashMap<>();
        vendasDez2021.put("Ana Silva", 5200.0);
        vendasDez2021.put("João Mendes", 3400.0);

        int mes = 12;
        int ano = 2021;

        System.out.println("Total pago: " + getTotalPago(funcionarios, mes, ano, vendasDez2021));
        System.out.println("Total salários: " + getTotalSalarios(funcionarios, mes, ano));
        System.out.println("Total benefícios: " + getTotalBeneficios(funcionarios, mes, ano, vendasDez2021));
        System.out.println("Maior recebimento: " + getMaiorRecebimento(funcionarios, mes, ano, vendasDez2021).nome);
        System.out.println("Maior benefício: " + getMaiorBeneficio(funcionarios, mes, ano, vendasDez2021).nome);

        List<Vendedor> vendedores = Arrays.asList(
                new Vendedor("Ana Silva", 12, 2021),
                new Vendedor("João Mendes", 12, 2021)
        );

        System.out.println("Maior vendedor: " + getMaiorVendas(vendedores, mes, ano, vendasDez2021).nome);
    }
}