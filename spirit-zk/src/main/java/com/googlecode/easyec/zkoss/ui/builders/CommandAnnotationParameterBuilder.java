package com.googlecode.easyec.zkoss.ui.builders;

/**
 * 基于ZK command名称注解的参数构建类
 *
 * @author junjie
 */
public class CommandAnnotationParameterBuilder extends AbstractAnnotationParameterBuilder {

    protected CommandAnnotationParameterBuilder() {}

    public static CommandAnnotationParameterBuilder create() {
        return new CommandAnnotationParameterBuilder();
    }

    @Override
    public void setAnnotateName(String annotateName) {
        // no need to set.
    }

    @Override
    protected String getAnnotateName() {
        return "command";
    }

    @Override
    public AnnotationParameter build() {
        DefaultAnnotationParameter impl
            = new DefaultAnnotationParameter(
            getProperty(), getAnnotateName()
        );

        impl.setArgs(getArgs());

        return impl;
    }
}
