package com.googlecode.easyec.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

@JsonIgnoreProperties(
    value = "uidPk",
    ignoreUnknown = true
)
public interface ElasticsearchId extends GenericPersistentDomainModel<String> {
}
