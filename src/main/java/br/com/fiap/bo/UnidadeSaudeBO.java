package br.com.fiap.bo;
import br.com.fiap.dao.UnidadeSaudeDAO;
import br.com.fiap.dto.*;
import br.com.fiap.exception.*;
import br.com.fiap.model.UnidadeSaude;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UnidadeSaudeBO {
    private final UnidadeSaudeDAO dao;
    private final ModelMapper mapper;
    public UnidadeSaudeBO(UnidadeSaudeDAO dao, ModelMapper mapper) { this.dao=dao;this.mapper=mapper; }
    public UnidadeSaude buscarModelo(Long id) { return dao.buscar(id).orElseThrow(()->new NaoEncontradoException("UBS nao encontrada.")); }
    private UnidadeSaida saida(UnidadeSaude m) { return mapper.map(m,UnidadeSaida.class); }
    public List<UnidadeSaida> listar() { return dao.listar().stream().map(this::saida).toList(); }
    public UnidadeSaida buscar(Long id) { return saida(buscarModelo(id)); }
    public UnidadeSaida cadastrar(UnidadeEntrada dto) {
        if(dao.existeCnes(dto.getCnes(),null)) throw new ConflitoException("CNES ja cadastrado.");
        return saida(dao.inserir(mapper.map(dto, UnidadeSaude.class)));
    }
    public UnidadeSaida atualizar(Long id,UnidadeEntrada dto) {
        buscarModelo(id);
        if(dao.existeCnes(dto.getCnes(),id)) throw new ConflitoException("CNES ja cadastrado para outra UBS.");
        UnidadeSaude m=mapper.map(dto,UnidadeSaude.class);m.setId(id);
        if(!dao.atualizar(m)) throw new NaoEncontradoException("UBS nao encontrada.");
        return saida(m);
    }
    public void remover(Long id) {
        buscarModelo(id);
        if(dao.contarTrechos(id)>0) throw new ConflitoException("Nao e permitido remover UBS com trechos cadastrados.");
        if(!dao.remover(id)) throw new NaoEncontradoException("UBS nao encontrada.");
    }
}
