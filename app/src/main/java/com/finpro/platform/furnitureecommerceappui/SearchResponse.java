package com.finpro.platform.furnitureecommerceappui;

public class SearchResponse {
    private String symbol;
    private String name;
    private String exchange;

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getExchange() { return exchange; }

    @Override
    public String toString() {
        return symbol + " - " + name + " (" + exchange + ")";
    }
}
