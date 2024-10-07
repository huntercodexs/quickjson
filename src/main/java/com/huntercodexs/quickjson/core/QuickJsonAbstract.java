package com.huntercodexs.quickjson.core;

public abstract class QuickJsonAbstract {

    protected static final String FIELD = "[_a-zA-Z][_0-9a-zA-Z]+";

    protected static final String VALUE = "[0-9a-zA-Z .\\]\\[)(@#!&*|/$%_+-]+";

    protected static final String STRINGED = "[_a-zA-Z][0-9a-zA-Z .\\]\\[)(@#!&*|/$%_+-]+";

    protected static final String TARGET = "@::field::@";

    protected static final String[] JSON_FIELD_REGEXP = new String[] {
            "(\\{\""+ FIELD +"\"):\\{(\""+ FIELD +"\":\""+ VALUE +"\",)*(\""+ TARGET +"\":\""+ VALUE +"\",?)(\""+ FIELD +"\":\""+ VALUE +"\",?)*}",
            "$1:{}"
    };

    protected static final String[] SUB_ARRAY1_REGEXP = new String[]{
            "(.*)(\""+ TARGET +"\": ?)\\[([\"0-9a-zA-Z:}{, _+.-]+)(\"])(.*)",
            "[$1][$2]{@EXTRACT}[$3$4{@EXTRACT}[$5]"
    };

    protected static final String[] SUB_ARRAY2_REGEXP = new String[]{
            "(.*)(\""+ TARGET + "\": ?)\\[([\"0-9a-zA-Z:}{\\]\\[, _+.-]+)(]])(.*)",
            "[$1][$2]{@EXTRACT}[$3$4{@EXTRACT}[$5]"
    };

    protected static final String[] ARRAY_REGEXP = new String[]{
            "(.*)(\""+ TARGET +"\": ?)\\[([\"0-9a-zA-Z, _+.-]+)](.*)",
            "[$1][$2]{@EXTRACT}[$3]{@EXTRACT}[$4]"
    };

    protected static final String[] JSON_REGEXP = new String[]{
            "(.*)(\""+ TARGET +"\": ?)\\{([\"0-9a-zA-Z:}{\\]\\[, _+.-]+)}(,\"[a-zA-Z][0-9a-zA-Z-_]\":)?(.*)",
            "[$1][$2]{@EXTRACT}{$3}{@EXTRACT}[$4][$5]"
    };

    protected static final String[] STR_REGEXP = new String[]{
            "(,\""+ TARGET +"\"): ?\"?([0-9a-zA-Z .}{\\]\\[)(@#!&*|/$%_+-]+)\"?,? ?",
            "$1{@EXTRACT}$2"
    };

    protected static final String[] INT_REGEXP = new String[]{};

    protected static final String HASHMAP_JSON_REGEXP =
            "(:\\{((\"[_a-zA-Z][_0-9a-zA-Z]+\")(:)(\"?[0-9a-zA-Z .\\]\\[)(@#!&*|/$%_+-]+\"?(,?)))+})";

    protected static final String HASHMAP_ARRAY_REGEXP = "(:\\[((\"?[0-9a-zA-Z .\\)\\(@#!&*|/$%_+-]+\"?(,?)))+])";

}
