package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConexaoPostgresDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/Biblioteca1";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "root";

    public static Connection conectar() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o bd: " + e.getMessage());
        }
        return conexao;
    }

    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o bd fechada!");

            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o bd: " + e.getMessage());
            }
        }
    }

    public static void setAluno(String nome, int idade) throws SQLException {
        String sql = "INSERT INTO aluno (nome, idade) VALUES (?, ?)";
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, nome);
                stmt.setInt(2, idade);

                int linhasAfetadas = stmt.executeUpdate(); // Executa o INSERT
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno " + nome + " inserido no BD com sucesso!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir aluno no Postgresql: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após inserção: " + e.getMessage());
            }
        }
    }

    public static void getAlunos() {
        String sql = "SELECT id_aluno, nome, idade FROM aluno ORDER BY id_aluno";
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                rs = stmt.executeQuery();
                System.out.println("\n--- Alunos Cadastrados no DB ---");
                boolean encontrouAluno = false;
                while (rs.next()) {
                    encontrouAluno = true;
                    int id = rs.getInt("id_aluno");
                    String nome = rs.getString("nome");
                    Number idade = rs.getInt("idade");
                    System.out.println("ID: " + id + ", Nome: " + nome + ", Idade: " + idade);
                }
                if (!encontrouAluno) {
                    System.out.println("Nenhum aluno encontrado.");
                }
                System.out.println("---------------------------\n");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar alunos no DB: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após consulta: " + e.getMessage());
            }

        }
    }

    public static void atualizarAluno(int id_aluno, String novoNome, int novaIdade) {
        String sql = "UPDATE aluno SET nome = ?, idade = ? WHERE id_aluno = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, novoNome);
                stmt.setInt(2, novaIdade);
                stmt.setInt(3, id_aluno); //O ID DO ALUNO QUE QUEREMOS ATUALIZAR
                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno com ID " + id_aluno + " atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum aluno encontrado com ID " + id_aluno + " para atualização.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluno no PostgreSQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após atualização: " + e.getMessage());
            }
        }
    }

    public static void removerAluno(int id_aluno) {
        String sql = "DELETE FROM aluno WHERE id_aluno = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id_aluno);

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno com ID " + id_aluno + " removido com sucesso!");
                } else {
                    System.out.println("Nenhum aluno encontrado com ID " + id_aluno + " para remoção.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover aluno no PostgreSQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após remoção: " + e.getMessage());
            }
        }

    }

    public static void main(String[] args) throws SQLException {
    Connection testeConexao = conectar();
    if (testeConexao != null) {
        ConexaoPostgresDB.fecharConexao(testeConexao);
    }
    ConexaoPostgresDB.setAluno("Larissa", 29);
        ConexaoPostgresDB.getAlunos();
        ConexaoPostgresDB.atualizarAluno(2,"Everton", 18);
        ConexaoPostgresDB.removerAluno(4);
}
}