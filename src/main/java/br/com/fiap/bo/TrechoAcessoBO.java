package br.com.fiap.bo;
import br.com.fiap.dao.TrechoAcessoDAO;
import br.com.fiap.dto.*;
import br.com.fiap.exception.*;
import br.com.fiap.model.TrechoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TrechoAcessoBO {
    private final TrechoAcessoDAO dao;
    private final UnidadeSaudeBO unidades;
    private final ModelMapper mapper;
    public TrechoAcessoBO(TrechoAcessoDAO dao, UnidadeSaudeBO unidades, ModelMapper mapper) {this.dao=dao;this.unidades=unidades;this.mapper=mapper;}
    public TrechoAcesso buscarModelo(Long id) {return dao.buscar(id).orElseThrow(()->new NaoEncontradoException("Trecho nao encontrado."));}
    private TrechoSaida saida(TrechoAcesso m) {return mapper.map(m,TrechoSaida.class);}
    public List<TrechoSaida> listar() {return dao.listar().stream().map(this::saida).toList();}
    public TrechoSaida buscar(Long id) {return saida(buscarModelo(id));}
    public List<TrechoSaida> listarPorUnidade(Long unidadeId) {
        unidades.buscarModelo(unidadeId);
        return dao.listarPorUnidade(unidadeId).stream().map(this::saida).toList();
    }
    public TrechoSaida cadastrar(TrechoEntrada dto) {
        unidades.buscarModelo(dto.getUnidadeId());
        return saida(dao.inserir(mapper.map(dto,TrechoAcesso.class)));
    }
    public TrechoSaida cadastrarNaUnidade(Long unidadeId,TrechoEntrada dto) {
        if(!unidadeId.equals(dto.getUnidadeId()))throw new ConflitoException("Trecho informado pertence a outra UBS.");
        return cadastrar(dto);
    }
    public TrechoSaida atualizar(Long id,TrechoEntrada dto) {
        buscarModelo(id); unidades.buscarModelo(dto.getUnidadeId());
        TrechoAcesso m=mapper.map(dto,TrechoAcesso.class);m.setId(id);
        if(!dao.atualizar(m))throw new NaoEncontradoException("Trecho nao encontrado.");
        return saida(m);
    }
    public void remover(Long id) {
        buscarModelo(id);
        if(dao.contarOcorrencias(id)>0)throw new ConflitoException("Nao e permitido remover trecho com ocorrencias.");
        if(!dao.remover(id))throw new NaoEncontradoException("Trecho nao encontrado.");
    }
}
