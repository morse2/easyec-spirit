package com.googlecode.easyec.zkoss.ui.form.functions;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

import java.io.Serializable;

@FunctionalInterface
public interface NewOperator<T extends GenericPersistentDomainModel<PK>, PK extends Serializable> {

    T perform();
}
