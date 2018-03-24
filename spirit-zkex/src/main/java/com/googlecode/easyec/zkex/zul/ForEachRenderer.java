package com.googlecode.easyec.zkex.zul;

import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.sys.ShadowElementsCtrl;
import org.zkoss.zk.ui.util.ForEachStatus;
import org.zkoss.zk.ui.util.Template;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.googlecode.easyec.zkex.zul.TemplateBasedShadowElement.FOREACH_RENDERED_COMPONENTS;

public class ForEachRenderer implements Serializable {

    private static final long serialVersionUID = 3372839796556080811L;

    public void render(ForEach forEachComp, Component host, Template tm, List<?> list, boolean isUsingListModel) {
        int begin = forEachComp.getBegin();
        int end = forEachComp.getEnd();
        int step = forEachComp.getStep();
        render(forEachComp, host, tm, begin, end, step, list, isUsingListModel);
    }

    public void render(ForEach forEachComp, Component host, Template tm, int begin, int end, int step, List<?> list, boolean isUsingListModel) {
        Component insertBefore;
        Object shadowInfo = ShadowElementsCtrl.getCurrentInfo();
        String forEachRenderedCompAttr = FOREACH_RENDERED_COMPONENTS + forEachComp.getUuid();

        try {
            ShadowElementsCtrl.setCurrentInfo(forEachComp);
            List feCompList = (List) host.getAttribute(forEachRenderedCompAttr);
            if ((feCompList != null) && (feCompList.size() > begin)) {
                insertBefore = ((Component[]) feCompList.get(begin))[0];
            } else {
                insertBefore = forEachComp.getNextInsertionComponentIfAny();
            }

            int insertIndex = begin;
            ForEachIterator<Object> it = new ForEachIterator<>(begin, end, step, list);
            while (it.hasNext()) {
                VariableResolver variableResolver = initVariableResolver(forEachComp, it);
                Execution exec = Executions.getCurrent();
                exec.addVariableResolver(variableResolver);

                Component[] creates;
                forEachComp.setAttribute(forEachComp.getVar(), variableResolver.resolveVariable(forEachComp.getVar()));
                forEachComp.setAttribute(forEachComp.getVarStatus(), variableResolver.resolveVariable(forEachComp.getVarStatus()));

                creates = tm.create(host, insertBefore, variableResolver, null);
                forEachComp.removeAttribute(forEachComp.getVar());
                forEachComp.removeAttribute(forEachComp.getVarStatus());

                if (isUsingListModel) {
                    if (feCompList == null)
                        feCompList = new LinkedList();
                    if (feCompList.size() > insertIndex) {
                        feCompList.add(insertIndex, creates);
                    } else
                        feCompList.add(creates);
                    host.setAttribute(forEachRenderedCompAttr, feCompList);
                }

                for (Component comp : creates) {
                    comp.setAttribute(forEachComp.getVar(), variableResolver.resolveVariable(forEachComp.getVar()));
                    comp.setAttribute(forEachComp.getVarStatus(), variableResolver.resolveVariable(forEachComp.getVarStatus()));
                }

                insertIndex += step;
            }
        } finally {
            ShadowElementsCtrl.setCurrentInfo(shadowInfo);
        }
    }

    public VariableResolver initVariableResolver(final ForEach forEachComp, final ForEachIterator<Object> it) {
        final Object item = it.next();
        final int index = it.getIndex();
        return new VariableResolver() {

            private ForEachStatus prev = null;
            private ForEachStatus current = null;

            public Object resolveVariable(String name) {
                if (forEachComp.getVar().equals(name)) {
                    return item;
                }

                if (forEachComp.getVarStatus().equals(name)) {
                    if (this.current != null)
                        this.prev = this.current;
                    this.current = new ForEachStatus() {

                        public ForEachStatus getPrevious() {
                            return prev;
                        }

                        public Object getEach() {
                            return getCurrent();
                        }

                        public int getIndex() {
                            return index;
                        }

                        public Integer getBegin() {
                            return it.getBegin();
                        }

                        public Integer getEnd() {
                            return it.getEnd();
                        }

                        public Object getCurrent() {
                            return item;
                        }

                        public boolean isFirst() {
                            return getCount() == 1;
                        }

                        public boolean isLast() {
                            return getIndex() + 1 == getEnd();
                        }

                        public Integer getStep() {
                            return null;
                        }

                        public int getCount() {
                            return getIndex() + 1;
                        }
                    };

                    return this.current;
                }

                return null;
            }
        };
    }

    private static class ForEachIterator<E> {

        private final int startIndex;
        private final int endIndex;
        private final int step;
        private int index;
        private ListIterator<Object> iter;
        private List<?> items;

        ForEachIterator(int begin, int end, int step, List items) {
            this.startIndex = begin;
            this.endIndex = end;
            this.step = step;
            this.items = items;
            this.index = this.startIndex;
            if ((items != null) && (items.size() > this.startIndex)) {
                this.iter = (items.size() > this.startIndex ? items.listIterator(this.startIndex) : null);
            }
        }

        int getBegin() {
            return this.startIndex;
        }

        int getEnd() {
            return this.endIndex;
        }

        boolean hasPrevious() {
            return this.index - this.step > this.startIndex;
        }

        Object previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            Object item = null;
            if (this.iter != null) {
                int prevStep = this.step;
                while (prevStep-- > 0)
                    item = this.iter.previous();
                this.index -= this.step;
            } else {
                item = this.index;
                this.index -= this.step;
            }

            return item;
        }

        boolean hasNext() {
            if (this.index <= this.endIndex) {
                if (this.iter != null) {
                    return this.iter.hasNext();
                }

                return this.items == null;
            }

            return false;
        }

        int getIndex() {
            return this.index > this.startIndex ? this.index - 1 : this.index;
        }

        Object next() {
            Object item = null;
            if (this.iter != null) {
                int nextStep = this.step;
                while (nextStep-- > 0) {
                    item = this.iter.next();
                }

                this.index += this.step;
            } else {
                item = this.index;
                this.index += this.step;
            }

            return item;
        }
    }
}
