package br.com.fiap.dao;
import br.com.fiap.model.Ocorrencia;
import br.com.fiap.exception.PersistenciaException;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
@Repository
public class OcorrenciaDAO {

    private final DataSource dataSource;
    public OcorrenciaDAO(DataSource dataSource) { this.dataSource = dataSource; }
    private RuntimeException falha(SQLException e) { return new PersistenciaException("Falha no acesso ao Oracle.", e); }
    private long proximoId(Connection connection, String sequence) throws SQLException {
        try (PreparedStatement p=connection.prepareStatement("SELECT " + sequence + ".NEXTVAL FROM DUAL");
             ResultSet r=p.executeQuery()) { if(!r.next()) throw new SQLException("Sequence sem resultado"); return r.getLong(1); }
    }

    private static final String SELECT = "SELECT o.* , t.cd_unidade AS cd_unidade_associada FROM tb_ocorrencia o JOIN tb_trecho_acesso t ON t.cd_trecho=o.cd_trecho";
    private Ocorrencia mapear(ResultSet r) throws SQLException {
        Ocorrencia m = new Ocorrencia();
        m.setId(r.getLong("cd_ocorrencia"));
        m.setTrechoId(r.getLong("cd_trecho"));
        m.setCategoria(r.getString("nm_categoria"));
        m.setDescricao(r.getString("ds_ocorrencia"));
        m.setReferenciaLocal(r.getString("ds_referencia_local"));
        m.setPassagem(r.getString("nm_passagem"));
        m.setSituacao(r.getString("nm_situacao"));
        m.setDataRelato(r.getDate("dt_relato").toLocalDate());
        m.setLarguraLivre(r.getString("ds_largura_livre"));
        m.setAlturaDesnivel(r.getString("ds_altura_desnivel"));
        m.setDistanciaEntrada(r.getString("ds_distancia_entrada"));
        m.setImpedimento(r.getString("nm_impedimento"));
        m.setUnidadeId(r.getLong("cd_unidade_associada"));
        return m;
    }
    public List<Ocorrencia> listar() {
        List<Ocorrencia> lista = new ArrayList<>();
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" ORDER BY o.cd_ocorrencia"); ResultSet r=p.executeQuery()) {
            while(r.next()) lista.add(mapear(r)); return lista;
        } catch(SQLException e) { throw falha(e); }
    }
    public Optional<Ocorrencia> buscar(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(SELECT+" WHERE o.cd_ocorrencia=?")) {
            p.setLong(1,id); try(ResultSet r=p.executeQuery()) { return r.next() ? Optional.of(mapear(r)) : Optional.empty(); }
        } catch(SQLException e) { throw falha(e); }
    }
    public Ocorrencia inserir(Ocorrencia m) {
        String sql = "INSERT INTO tb_ocorrencia (cd_ocorrencia, cd_trecho, nm_categoria, ds_ocorrencia, ds_referencia_local, nm_passagem, nm_situacao, dt_relato, ds_largura_livre, ds_altura_desnivel, ds_distancia_entrada, nm_impedimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c=dataSource.getConnection()) {
            long id=proximoId(c,"sq_tb_ocorrencia");
            try (PreparedStatement p=c.prepareStatement(sql)) {
                p.setLong(1,id);
            p.setLong(2, m.getTrechoId());
            p.setString(3, m.getCategoria());
            p.setString(4, m.getDescricao());
            p.setString(5, m.getReferenciaLocal());
            p.setString(6, m.getPassagem());
            p.setString(7, m.getSituacao());
            p.setDate(8, java.sql.Date.valueOf(m.getDataRelato()));
            p.setString(9, m.getLarguraLivre());
            p.setString(10, m.getAlturaDesnivel());
            p.setString(11, m.getDistanciaEntrada());
            p.setString(12, m.getImpedimento());
                p.executeUpdate();
            }
            m.setId(id); return m;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean atualizar(Ocorrencia m) {
        String sql="UPDATE tb_ocorrencia SET cd_trecho=?, nm_categoria=?, ds_ocorrencia=?, ds_referencia_local=?, nm_passagem=?, nm_situacao=?, dt_relato=?, ds_largura_livre=?, ds_altura_desnivel=?, ds_distancia_entrada=?, nm_impedimento=? WHERE cd_ocorrencia=?";
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setLong(1, m.getTrechoId());
            p.setString(2, m.getCategoria());
            p.setString(3, m.getDescricao());
            p.setString(4, m.getReferenciaLocal());
            p.setString(5, m.getPassagem());
            p.setString(6, m.getSituacao());
            p.setDate(7, java.sql.Date.valueOf(m.getDataRelato()));
            p.setString(8, m.getLarguraLivre());
            p.setString(9, m.getAlturaDesnivel());
            p.setString(10, m.getDistanciaEntrada());
            p.setString(11, m.getImpedimento());
            p.setLong(12, m.getId());
            return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }
    public boolean remover(Long id) {
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement("DELETE FROM tb_ocorrencia WHERE cd_ocorrencia=?")) {
            p.setLong(1,id); return p.executeUpdate()>0;
        } catch(SQLException e) { throw falha(e); }
    }

    public List<Ocorrencia> listarPorTrecho(Long trechoId) {
        List<Ocorrencia> lista=new ArrayList<>();
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(SELECT+" WHERE o.cd_trecho=? ORDER BY o.cd_ocorrencia")) {
            p.setLong(1,trechoId);try(ResultSet r=p.executeQuery()){while(r.next())lista.add(mapear(r));}return lista;
        }catch(SQLException e){throw falha(e);}
    }
}
