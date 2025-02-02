package com.xuchen.demo.security.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.security.constant.SecurityConstant;
import com.xuchen.demo.security.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;

@Component
public class JwtTool {

    @Autowired
    private JwtProperties jwtProperties;

    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    public String createToken(Long userId) {
        return JWT.create()
                .setPayload(SecurityConstant.PAYLOAD_NAME, userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getTtl().toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    public Long parseToken(String token) {
        try {
            JWT jwt = JWT.of(token).setSigner(jwtSigner);
            JWTValidator.of(jwt).validateDate();
            NumberWithFormat payload = (NumberWithFormat) jwt.getPayload(SecurityConstant.PAYLOAD_NAME);
            return payload.longValue();
        } catch (Exception e) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }
    }
}
