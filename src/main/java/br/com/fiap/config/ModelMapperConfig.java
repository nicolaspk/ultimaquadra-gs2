package br.com.fiap.config;

import br.com.fiap.dto.OcorrenciaAtualizacaoEntrada;
import br.com.fiap.dto.OcorrenciaEntrada;
import br.com.fiap.dto.TrechoEntrada;
import br.com.fiap.dto.UnidadeEntrada;
import br.com.fiap.model.Ocorrencia;
import br.com.fiap.model.TrechoAcesso;
import br.com.fiap.model.UnidadeSaude;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.createTypeMap(UnidadeEntrada.class, UnidadeSaude.class)
                .addMappings(mapeamento -> mapeamento.skip(UnidadeSaude::setId));

        mapper.createTypeMap(TrechoEntrada.class, TrechoAcesso.class)
                .addMappings(mapeamento -> mapeamento.skip(TrechoAcesso::setId));

        mapper.createTypeMap(OcorrenciaEntrada.class, Ocorrencia.class)
                .addMappings(mapeamento -> mapeamento.skip(Ocorrencia::setId));

        mapper.createTypeMap(OcorrenciaAtualizacaoEntrada.class, Ocorrencia.class)
                .addMappings(mapeamento -> mapeamento.skip(Ocorrencia::setId));

        return mapper;
    }
}
