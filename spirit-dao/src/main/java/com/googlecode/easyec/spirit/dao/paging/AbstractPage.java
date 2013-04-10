package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import org.springframework.util.Assert;

/**
 * 分页对象默认的抽象类。
 * <p>
 * 此类实现了分页的默认公共方法。
 * </p>
 *
 * @author JunJie
 */
public abstract class AbstractPage implements Page, PageComputable, PageWritable {

    private int pageSize;
    private int totalSize;
    private int currentPage;
    private int totalRecordsCount;
    private boolean nextPageAvailable;
    private boolean prevPageAvailable;

    private PageDialect pageDialect;

    protected AbstractPage(PageDialect pageDialect) {
        Assert.notNull(pageDialect, "PageDialect is null.");
        this.pageDialect = pageDialect;
    }

    protected AbstractPage(PageDialect pageDialect, int currentPage, int pageSize) {
        this(pageDialect);

        setCurrentPage(currentPage);
        setPageSize(pageSize);
    }

    protected AbstractPage(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        this(pageDialect);

        setCurrentPage(currentPage);
        setPageSize(pageSize);
        setTotalRecordsCount(totalRecordsCount);

        // compute this class
        compute();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRecordsCount() {
        return totalRecordsCount;
    }

    public boolean getNextPageAvailable() {
        return nextPageAvailable;
    }

    public boolean getPrevPageAvailable() {
        return prevPageAvailable;
    }

    public void setTotalRecordsCount(int totalRecordsCount) {
        this.totalRecordsCount = totalRecordsCount;
    }

    public void setNextPageAvailable(boolean nextPageAvailable) {
        this.nextPageAvailable = nextPageAvailable;
    }

    public void setPrevPageAvailable(boolean prevPageAvailable) {
        this.prevPageAvailable = prevPageAvailable;
    }

    public PageDialect getPageDialect() {
        return pageDialect;
    }

    public PageLineIterator<Integer> getPageLineIterator() {
        return new PageLineIteratorImpl();
    }

    public void compute() {
        int thisPageSize = getPageSize();
        int thisCurrentPage = getCurrentPage();
        int thisTotalRecordsCount = getTotalRecordsCount();
        setTotalSize((int) Math.ceil((thisTotalRecordsCount + thisPageSize - 1) / thisPageSize));
        setPrevPageAvailable(thisCurrentPage > 1);
        setNextPageAvailable(thisCurrentPage < getTotalSize());
    }

    /**
     * 默认分页行迭代器类的实现。
     * <p>
     * 迭代器用于返回当前页的每行的行号。
     * </p>
     */
    private class PageLineIteratorImpl implements PageLineIterator<Integer> {

        private int firstLineNumber;
        private int lastLineNumber;
        private int nextLineNumber;

        PageLineIteratorImpl() {
            this.firstLineNumber = this.nextLineNumber = (currentPage - 1) * pageSize + 1;
            this.lastLineNumber = (!nextPageAvailable) ? totalRecordsCount : currentPage * pageSize;
        }

        public Integer getFirstLineNumber() {
            return firstLineNumber;
        }

        public Integer getLastLineNumber() {
            return lastLineNumber;
        }

        public Integer getNextLineNumber() {
            return hasNext() ? next() : null;
        }

        public boolean hasNext() {
            return (nextLineNumber <= lastLineNumber);
        }

        public Integer next() {
            return nextLineNumber++;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
