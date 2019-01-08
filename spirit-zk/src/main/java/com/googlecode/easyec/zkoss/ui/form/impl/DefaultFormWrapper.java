package com.googlecode.easyec.zkoss.ui.form.impl;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.zkoss.ui.form.FormWrapper;

import java.io.Serializable;

public class DefaultFormWrapper<T extends GenericPersistentDomainModel<PK>, PK extends Serializable> implements FormWrapper<T, PK> {

    private static final long serialVersionUID = 4884248369867010992L;
    private T _model;

    public DefaultFormWrapper(T _model) {
        this._model = _model;
    }

    @Override
    public T getDomainModel() {
        return this._model;
    }

    @Override
    public boolean isNew() {
        return _model == null || _model.getUidPk() == null;
    }
}
