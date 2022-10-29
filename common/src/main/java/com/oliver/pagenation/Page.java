package com.oliver.pagenation;

import java.util.List;

public interface Page<T> {
    /**
     * Returns the total number of data of current page.
     *
     * @return {int} Returns the total number of data of current page.
     */
    int getTotal();

    /**
     * Returns a list of data of current page.
     *
     * @return Returns a list of data of current page.
     */
    List<T> getData();
}
