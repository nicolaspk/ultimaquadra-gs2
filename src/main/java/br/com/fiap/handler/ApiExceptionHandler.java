package br.com.fiap.handler;
import br.com.fiap.dto.ApiErro;
import br.com.fiap.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private ResponseEntity<ApiErro> resposta(HttpStatus status, String mensagem, HttpServletRequest req, List<String> detalhes) {
        return ResponseEntity.status(status).body(new ApiErro(Instant.now(), status.value(), status.getReasonPhrase(), mensagem, req.getRequestURI(), detalhes));
    }
    @ExceptionHandler(EntradaInvalidaException.class)
    ResponseEntity<ApiErro> regraInvalida(EntradaInvalidaException e, HttpServletRequest r) { return resposta(HttpStatus.BAD_REQUEST, e.getMessage(), r, List.of()); }
    @ExceptionHandler(NaoEncontradoException.class)
    ResponseEntity<ApiErro> naoEncontrado(NaoEncontradoException e, HttpServletRequest r) { return resposta(HttpStatus.NOT_FOUND, e.getMessage(), r, List.of()); }
    @ExceptionHandler(ConflitoException.class)
    ResponseEntity<ApiErro> conflito(ConflitoException e, HttpServletRequest r) { return resposta(HttpStatus.CONFLICT, e.getMessage(), r, List.of()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErro> validacao(MethodArgumentNotValidException e, HttpServletRequest r) {
        List<String> erros=e.getBindingResult().getFieldErrors().stream().map(f -> f.getField()+": "+f.getDefaultMessage()).distinct().toList();
        return resposta(HttpStatus.BAD_REQUEST,"Dados invalidos.",r,erros);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, BindException.class, IllegalArgumentException.class})
    ResponseEntity<ApiErro> entradaInvalida(Exception e, HttpServletRequest r) { return resposta(HttpStatus.BAD_REQUEST,"Requisicao invalida; verifique JSON, IDs e campos.",r,List.of()); }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiErro> metodo(HttpRequestMethodNotSupportedException e, HttpServletRequest r) { return resposta(HttpStatus.METHOD_NOT_ALLOWED,"Metodo HTTP nao permitido.",r,List.of()); }
    @ExceptionHandler(PersistenciaException.class)
    ResponseEntity<ApiErro> banco(PersistenciaException e, HttpServletRequest r) {
        log.error("Falha de persistencia na rota {}", r.getRequestURI(), e);
        Throwable raiz=e; while (raiz.getCause()!=null && raiz.getCause()!=raiz) raiz=raiz.getCause();
        if (raiz instanceof SQLException sql && (sql.getErrorCode()==1 || sql.getErrorCode()==2291 || sql.getErrorCode()==2292))
            return resposta(HttpStatus.CONFLICT,"Operacao conflita com um registro existente ou relacionamento.",r,List.of());
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,"Nao foi possivel acessar os dados.",r,List.of());
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErro> inesperado(Exception e, HttpServletRequest r) {
        log.error("Erro inesperado na rota {}", r.getRequestURI(), e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno. Tente novamente mais tarde.",r,List.of());
    }
}
