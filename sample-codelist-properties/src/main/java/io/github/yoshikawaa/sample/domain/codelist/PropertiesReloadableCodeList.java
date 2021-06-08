package io.github.yoshikawaa.sample.domain.codelist;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.terasoluna.gfw.common.codelist.AbstractReloadableCodeList;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesReloadableCodeList extends AbstractReloadableCodeList {
    
    @Inject
    private ResourceLoader resourceLoader;
    
    @Setter
    private String location;

    @Override
    protected Map<String, String> retrieveMap() {

        Resource resource = resourceLoader.getResource(location);

        // can find property file or not?
        if (!resource.exists()) {
            log.warn("could not find property file from [{}]", location);
            return Collections.emptyMap();
        }
        
        // can read property file or not?
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error("could not read property file from [{}]", location);
            return Collections.emptyMap();
        }

        return (Map<String, String>) properties.entrySet().stream().collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }
}
