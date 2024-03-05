package com.manders.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
  
  @Autowired
  private EntityManager entityManager;
  
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
      CorsRegistry cors) {
    RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    expostIds(config);
  }

  private void expostIds(RepositoryRestConfiguration config) {
    // expose entity ids
    
    // get a list of all entity classes from the entity manager
    Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
    
    // create an array of the entity types
    List<Class<?>> entityClasses = new ArrayList<>();
    for (EntityType<?> entityType: entities) {
      entityClasses.add(entityType.getJavaType());
    }
    
    // expose the entity ids for the array of entity/domain types
    // 因為config.exposeIdsFor() 參數是Class<?>[] 所以才搞得這麼麻煩
    Class<?>[] domainTypes = entityClasses.toArray(new Class[0]);
    config.exposeIdsFor(domainTypes);
  }
}
