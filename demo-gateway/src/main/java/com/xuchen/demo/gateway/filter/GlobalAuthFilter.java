package com.xuchen.demo.gateway.filter;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xuchen.demo.api.security.UserClient;
import com.xuchen.demo.model.common.constant.ContextConstant;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.gateway.constant.GatewayConstant;
import com.xuchen.demo.model.security.dto.UserVerifyDto;
import com.xuchen.demo.model.security.vo.UserVerifyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    private final UserClient userClient;

    private Mono<Void> redirectException(ServerWebExchange exchange, String type, ResponseStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        URI uri = exchange.getRequest().getURI();

        String redirect = UriComponentsBuilder.newInstance()
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .path(GatewayConstant.EXCEPTION_PATH)
                .queryParam(GatewayConstant.EXCEPTION_TYPE, type)
                .queryParam(GatewayConstant.EXCEPTION_STATUS, status)
                .build()
                .toUriString();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(redirect));
        return response.setComplete();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("global filter: {}", exchange.getRequest().getURI());
        ServerHttpRequest request = exchange.getRequest();
        if (Objects.equals(request.getURI().getPath(), GatewayConstant.EXCEPTION_PATH)) {
            return chain.filter(exchange);
        }

        try {
            String token = null;
            List<String> headers = request.getHeaders().get(ContextConstant.OUTER_CONTEXT_NAME);
            if (headers != null && !headers.isEmpty()) {
                token = headers.get(0);
            }

            Boolean verify = null;
            ResponseResult<UserVerifyVo> result = userClient.verify(new UserVerifyDto(token, request.getURI().getPath()));
            if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
                verify = result.getData().getVerify();
            }

            if (Boolean.FALSE.equals(verify)) {
                throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
            }
            Long userId = result.getData().getUserId();
            if (userId == null) {
                return chain.filter(exchange);
            } else {
                exchange = exchange.mutate().request(builder -> builder.header(ContextConstant.INNER_CONTEXT_NAME, String.valueOf(userId))).build();
            }
        } catch (ClientException e) {
            log.info("client exception: {}", e.getStatus());
            return redirectException(exchange, e.getClass().getName(), e.getStatus());
        } catch (ServerException e) {
            log.info("server exception: {}", e.getStatus());
            return redirectException(exchange, e.getClass().getName(), e.getStatus());
        } catch (Exception e) {
            log.info("unknown exception: {}, {}", e, e.getMessage());
            return redirectException(exchange, e.getClass().getName(), null);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}