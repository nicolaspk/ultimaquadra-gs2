package br.com.fiap.dao;
import br.com.fiap.model.TrechoAcesso;
import br.com.fiap.exception.PersistenciaException;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
@Repository
public class TrechoAcessoDAO {

    private final DataSource dataSource;
    public TrechoAcessoDAO(DataSource dataSource) { this.dataSource = dataSource; }
    private RuntimeException falha(SQLException e) { return new PersistenciaException("Falha no acesso ao Oracle.", e); }
    private long proximoId(Connection connection, String sequence) throws SQLException {
        try (PreparedStatement p=connection.prepareStatement("SELECT " + sequence + ".NEXTVAL FROM DUAL");
             ResultSet r=p.executeQuery()) { if(!r.next()) throw new SQLException("Sequence sem resultado"); return r.getLong(1); }
    }

    private static final String SELECT = "SELECT * FROM tb_trecho_acesso";
    private TrechoAcesso mapear(ResultSet r) throws SQLException {
        TrechoAcesso m = new TrechoAcesso();
        m.setId(r.getLong("cd_trecho"));
        m.setUnidadeId(r.getLong("cd_unidade"));
        m.setNome(r.getString("nm_trecho"));
        m.setDescricao(r.getString("ds_trecho"));
        m.setDistanciaMediaEntrada(r.getString("ds_distancia_media_entrada"));
        m.setAtivo(r.getInt("st_ativo") == 1);
        return m;
    }
    public List<TrechoAcesso> listar() {
        List<TrechoAcesso> lista = new ArrayList<>();
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" ORDER BY cd_trecho"); ResultSet r=p.executeQuery()) {
            while(r.next()) lista.add(mapear(r)); return lista;
        } catch(SQLException e) { throw falha(e); }
    }
    public Optional<TrechoAcesso> buscar(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" WHERE cd_trecho=?")) {
            p.setLong(1,id); try(ResultSet r=p.executeQuery()) { return r.next() ? Optional.of(mapear(r)) : Optional.empty(); }
        } catch(SQLException e) { throw falha(e); }
    }
    public TrechoAcesso inserir(TrechoAcesso m) {
        String sql = "INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c=dataSource.getConnection()) {
            long id=proximoId(c,"sq_tb_trecho_acesso");
            try (PreparedStatement p=c.prepareStatement(sql)) {
                p.setLong(1,id);
            p.setLong(2, m.getUnidadeId());
            p.setString(3, m.getNome());
            p.setString(4, m.getDescricao());
            p.setString(5, m.getDistanciaMediaEntrada());
            p.setInt(6, Boolean.TRUE.equals(m.getAtivo()) ? 1 : 0);
                p.executeUpdate();
            }
            m.setId(id); return m;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean atualizar(TrechoAcesso m) {
        String sql="UPDATE tb_trecho_acesso SET cd_unidade=?, nm_trecho=?, ds_trecho=?, ds_distancia_media_entrada=?, st_ativo=? WHERE cd_trecho=?";
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setLong(1, m.getUnidadeId());
            p.setString(2, m.getNome());
            p.setString(3, m.getDescricao());
            p.setString(4, m.getDistanciaMediaEntrada());
            p.setInt(5, Boolean.TRUE.equals(m.getAtivo()) ? 1 : 0);
            p.setLong(6, m.getId());
            return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean remover(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement("DELETE FROM tb_trecho_acesso WHERE cd_trecho=?")) {
            p.setLong(1,id); return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }

    public List<TrechoAcesso> listarPorUnidade(Long unidadeId) {
        List<TrechoAcesso> lista=new ArrayList<>();
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(SELECT+" WHERE cd_unidade=? ORDER BY cd_trecho")) {
            p.setLong(1,unidadeId);try(ResultSet r=p.executeQuery()){while(r.next())lista.add(mapear(r));}return lista;
        }catch(SQLException e){throw falha(e);}
    }
    public int contarOcorrencias(Long trechoId) {
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM tb_ocorrencia WHERE cd_trecho=?")) {
            p.setLong(1,trechoId);try(ResultSet r=p.executeQuery()){r.next();return r.getInt(1);}
        }catch(SQLException e){throw falha(e);}
    }
}
