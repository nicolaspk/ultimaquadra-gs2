package br.com.fiap.dao;
import br.com.fiap.model.UnidadeSaude;
import br.com.fiap.exception.PersistenciaException;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
@Repository
public class UnidadeSaudeDAO {

    private final DataSource dataSource;
    public UnidadeSaudeDAO(DataSource dataSource) { this.dataSource = dataSource; }
    private RuntimeException falha(SQLException e) { return new PersistenciaException("Falha no acesso ao Oracle.", e); }
    private long proximoId(Connection connection, String sequence) throws SQLException {
        try (PreparedStatement p=connection.prepareStatement("SELECT " + sequence + ".NEXTVAL FROM DUAL");
             ResultSet r=p.executeQuery()) { if(!r.next()) throw new SQLException("Sequence sem resultado"); return r.getLong(1); }
    }

    private static final String SELECT = "SELECT * FROM tb_unidade_saude";
    private UnidadeSaude mapear(ResultSet r) throws SQLException {
        UnidadeSaude m = new UnidadeSaude();
        m.setId(r.getLong("cd_unidade"));
        m.setCnes(r.getString("cd_cnes"));
        m.setNome(r.getString("nm_unidade"));
        m.setBairro(r.getString("nm_bairro"));
        m.setEndereco(r.getString("ds_endereco"));
        m.setReferencia(r.getString("ds_referencia"));
        m.setDescricao(r.getString("ds_unidade"));
        m.setAtiva(r.getInt("st_ativa") == 1);
        return m;
    }
    public List<UnidadeSaude> listar() {
        List<UnidadeSaude> lista = new ArrayList<>();
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" ORDER BY cd_unidade"); ResultSet r=p.executeQuery()) {
            while(r.next()) lista.add(mapear(r)); return lista;
        } catch(SQLException e) { throw falha(e); }
    }
    public Optional<UnidadeSaude> buscar(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" WHERE cd_unidade=?")) {
            p.setLong(1,id); try(ResultSet r=p.executeQuery()) { return r.next() ? Optional.of(mapear(r)) : Optional.empty(); }
        } catch(SQLException e) { throw falha(e); }
    }
    public UnidadeSaude inserir(UnidadeSaude m) {
        String sql = "INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c=dataSource.getConnection()) {
            long id=proximoId(c,"sq_tb_unidade_saude");
            try (PreparedStatement p=c.prepareStatement(sql)) {
                p.setLong(1,id);
            p.setString(2, m.getCnes());
            p.setString(3, m.getNome());
            p.setString(4, m.getBairro());
            p.setString(5, m.getEndereco());
            p.setString(6, m.getReferencia());
            p.setString(7, m.getDescricao());
            p.setInt(8, Boolean.TRUE.equals(m.getAtiva()) ? 1 : 0);
                p.executeUpdate();
            }
            m.setId(id); return m;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean atualizar(UnidadeSaude m) {
        String sql="UPDATE tb_unidade_saude SET cd_cnes=?, nm_unidade=?, nm_bairro=?, ds_endereco=?, ds_referencia=?, ds_unidade=?, st_ativa=? WHERE cd_unidade=?";
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1, m.getCnes());
            p.setString(2, m.getNome());
            p.setString(3, m.getBairro());
            p.setString(4, m.getEndereco());
            p.setString(5, m.getReferencia());
            p.setString(6, m.getDescricao());
            p.setInt(7, Boolean.TRUE.equals(m.getAtiva()) ? 1 : 0);
            p.setLong(8, m.getId());
            return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean remover(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement("DELETE FROM tb_unidade_saude WHERE cd_unidade=?")) {
            p.setLong(1,id); return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }

    public boolean existeCnes(String cnes, Long ignorarId) {
        String sql = ignorarId == null
            ? "SELECT COUNT(*) FROM tb_unidade_saude WHERE cd_cnes=?"
            : "SELECT COUNT(*) FROM tb_unidade_saude WHERE cd_cnes=? AND cd_unidade<>?";
        try(Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,cnes);
            if(ignorarId!=null) p.setLong(2,ignorarId);
            try(ResultSet r=p.executeQuery()) {r.next();return r.getLong(1)>0;}
        }catch(SQLException e){throw falha(e);}
    }
    public int contarTrechos(Long unidadeId) { return contar("SELECT COUNT(*) FROM tb_trecho_acesso WHERE cd_unidade=?", unidadeId); }
    private int contar(String sql,Long id) {
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)) {
            p.setLong(1,id);try(ResultSet r=p.executeQuery()){r.next();return r.getInt(1);}
        }catch(SQLException e){throw falha(e);}
    }
}
