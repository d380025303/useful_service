package com.daixinmini.dbBase.util.curd.query;

/**
 * Simple operators - equals, greater than, less than etc.
 */
public enum Operation {
    /**
     * Equal to
     */
    EQ(" = ? "),

    /**
     * Not equal to.
     */
    NOT_EQ(" <> ? "),

    /**
     *
     * Less than.
     */

    LT(" < ? "),

    /**
     * Less than or equal to.
     */
    LT_EQ(" <= ? "),

    /**
     * Greater than.
     */
    GT(" > ? "),

    /**
     * Greater than or equal to.
     */
    GT_EQ(" >= ? ");

    final String exp;

    Operation(String exp) {
        this.exp = exp;
    }

    /**
     * Return the bind expression include JDBC ? bind placeholder.
     */
    public String bind() {
        return exp;
    }
}
