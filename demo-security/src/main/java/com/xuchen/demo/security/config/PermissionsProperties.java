package com.xuchen.demo.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "demo.security")
public class PermissionsProperties {

    private List<String> patternAndPermissions;
}
