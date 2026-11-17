package br.com.fiap.bo;
import br.com.fiap.dao.OcorrenciaDAO;
import br.com.fiap.dto.*;
import br.com.fiap.exception.*;
import br.com.fiap.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.Normalizer;
import java.util.List;
import java.util.Set;
@Service
public class OcorrenciaBO {
    private static final String TRECHO_PROVISORIO="Entorno imediato da UBS — local a indicar";
    private static final Set<String> VAGAS=Set.of("na calcada", "na calcada da ubs", "na calcada perto da ubs", "perto da ubs", "proximo a ubs", "proximo da ubs", "ao lado da ubs", "em frente a ubs", "na frente da ubs", "na entrada da ubs", "perto da entrada", "perto da unidade", "na frente da unidade", "na frente do posto", "na calcada do posto", "em frente a unidade", "na rua", "na esquina", "no caminho", "no entorno da ubs", "na area da ubs", "nao sei", "nao sei informar", "nao lembro onde");
    private final OcorrenciaDAO dao;
    private final TrechoAcessoBO trechos;
    private final UnidadeSaudeBO unidades;
    private final ModelMapper mapper;
    public OcorrenciaBO(OcorrenciaDAO dao, TrechoAcessoBO trechos, UnidadeSaudeBO unidades, ModelMapper mapper) {
        this.dao=dao;this.trechos=trechos;this.unidades=unidades;this.mapper=mapper;
    }
    private OcorrenciaSaida saida(Ocorrencia m) {return mapper.map(m,OcorrenciaSaida.class);}
    public List<OcorrenciaSaida> listar() {return dao.listar().stream().map(this::saida).toList();}
    private Ocorrencia buscarModelo(Long id) {return dao.buscar(id).orElseThrow(()->new NaoEncontradoException("Ocorrencia nao encontrada."));}
    public OcorrenciaSaida buscar(Long id) {return saida(buscarModelo(id));}
    public List<OcorrenciaSaida> listarPorTrecho(Long trechoId) {
        trechos.buscarModelo(trechoId);
        return dao.listarPorTrecho(trechoId).stream().map(this::saida).toList();
    }
    private TrechoAcesso validarVinculo(Long unidadeId,Long trechoId,boolean exigirAtivo) {
        UnidadeSaude u=unidades.buscarModelo(unidadeId);
        TrechoAcesso t=trechos.buscarModelo(trechoId);
        if(!unidadeId.equals(t.getUnidadeId()))throw new ConflitoException("Trecho nao pertence a UBS selecionada.");
        if(exigirAtivo && (!Boolean.TRUE.equals(t.getAtivo()) || !Boolean.TRUE.equals(u.getAtiva())))
            throw new ConflitoException("UBS ou trecho indisponivel para novos relatos.");
        return t;
    }
    public static boolean referenciaClara(String referencia) {
        if(referencia==null) return false;
        String t=referencia.trim().replaceAll("\\s+"," ");
        if(t.length()<15)return false;
        String n=Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}+", "").toLowerCase(java.util.Locale.ROOT).replaceAll("[.,!?;:]", "").trim();
        return !VAGAS.contains(n);
    }
    private void validarReferenciaLocal(TrechoAcesso trecho, String referenciaLocal) {
        boolean provisoria = TRECHO_PROVISORIO.equals(trecho.getNome())
                || "Calçada de acesso à UBS".equals(trecho.getNome());
        if (provisoria && !referenciaClara(referenciaLocal)) {
            throw new EntradaInvalidaException("Informe uma referencia local reconhecivel com pelo menos 15 caracteres.");
        }
    }

    public OcorrenciaSaida cadastrar(OcorrenciaEntrada entrada) {
        TrechoAcesso trecho = validarVinculo(entrada.getUnidadeId(), entrada.getTrechoId(), true);
        validarReferenciaLocal(trecho, entrada.getReferenciaLocal());

        Ocorrencia model = mapper.map(entrada, Ocorrencia.class);
        model.setUnidadeId(trecho.getUnidadeId());
        model.setSituacao("Relatada");
        model.setDataRelato(LocalDate.now(ZoneId.of("America/Sao_Paulo")));

        Ocorrencia inserida = dao.inserir(model);
        return saida(buscarModelo(inserida.getId()));
    }
    public OcorrenciaSaida cadastrarNoTrecho(Long trechoId, OcorrenciaEntrada entrada) {
        if(!trechoId.equals(entrada.getTrechoId()))throw new ConflitoException("Trecho da URL e do cadastro sao diferentes.");
        return cadastrar(entrada);
    }
    public OcorrenciaSaida atualizar(Long id, OcorrenciaAtualizacaoEntrada entrada) {
        Ocorrencia anterior = buscarModelo(id);
        TrechoAcesso trecho = validarVinculo(entrada.getUnidadeId(), entrada.getTrechoId(), false);
        validarReferenciaLocal(trecho, entrada.getReferenciaLocal());

        Ocorrencia model = mapper.map(entrada, Ocorrencia.class);
        model.setId(id);
        model.setUnidadeId(trecho.getUnidadeId());
        model.setDataRelato(anterior.getDataRelato());

        if (!dao.atualizar(model)) {
            throw new NaoEncontradoException("Ocorrencia nao encontrada.");
        }
        return saida(buscarModelo(id));
    }
    public void remover(Long id) {
        buscarModelo(id);
        if(!dao.remover(id))throw new NaoEncontradoException("Ocorrencia nao encontrada.");
    }
}
